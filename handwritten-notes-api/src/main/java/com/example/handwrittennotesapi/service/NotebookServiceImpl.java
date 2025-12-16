package com.example.handwrittennotesapi.service;

import com.example.handwrittennotesapi.dto.RedeemRequest;
import com.example.handwrittennotesapi.dto.RedeemResponse;
import com.example.handwrittennotesapi.model.*;
import com.example.handwrittennotesapi.repository.CoinLedgerRepository;
import com.example.handwrittennotesapi.repository.NotebookInstanceRepository;
import com.example.handwrittennotesapi.repository.NotebookSkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;


@Service
public class NotebookServiceImpl implements NotebookService {

    private final NotebookInstanceRepository notebookInstanceRepository;
    private final NotebookSkuRepository notebookSkuRepository;
    private final CoinLedgerRepository coinLedgerRepository;
    private final UserService userService;

    @Value("${qr.hmac.secret}")
    private String hmacSecret;

    @Autowired
    public NotebookServiceImpl(NotebookInstanceRepository notebookInstanceRepository,
                               NotebookSkuRepository notebookSkuRepository,
                               CoinLedgerRepository coinLedgerRepository,
                               UserService userService) {
        this.notebookInstanceRepository = notebookInstanceRepository;
        this.notebookSkuRepository = notebookSkuRepository;
        this.coinLedgerRepository = coinLedgerRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public RedeemResponse redeemNotebook(RedeemRequest redeemRequest) {
        // In a real application, we would validate the qrPayload signature
        // For now, we'll use a simple HMAC to simulate a tamper-proof hash.
        String qrHash = createHmac(redeemRequest.getQrPayload());

        notebookInstanceRepository.findByQrHash(qrHash).ifPresent(ni -> {
            throw new IllegalStateException("QR code already redeemed");
        });

        User currentUser = userService.getCurrentUser();
        // In a real application, the default SKU name would come from configuration
        NotebookSku sku = notebookSkuRepository.findByName("Default Notebook").orElseGet(() -> {
            NotebookSku newSku = new NotebookSku();
            newSku.setName("Default Notebook");
            newSku.setPageCount(30);
            newSku.setCreatedAt(LocalDateTime.now());
            return notebookSkuRepository.save(newSku);
        });

        NotebookInstance notebookInstance = new NotebookInstance();
        notebookInstance.setSku(sku);
        notebookInstance.setUser(currentUser);
        notebookInstance.setQrHash(qrHash);
        notebookInstance.setRedeemedAt(LocalDateTime.now());
        notebookInstance = notebookInstanceRepository.save(notebookInstance);

        CoinLedger credit = new CoinLedger();
        credit.setUser(currentUser);
        credit.setTransactionType(TransactionType.CREDIT);
        credit.setAmount(sku.getPageCount());
        credit.setReason("Notebook redemption");
        credit.setReferenceId(String.valueOf(notebookInstance.getId()));
        credit.setCreatedAt(LocalDateTime.now());
        coinLedgerRepository.save(credit);

        Integer currentBalance = coinLedgerRepository.getBalanceByUserId(currentUser.getId());

        RedeemResponse response = new RedeemResponse();
        response.setNotebookInstanceId(String.valueOf(notebookInstance.getId()));
        response.setCoinsCredited(sku.getPageCount());
        response.setCurrentBalance(currentBalance != null ? currentBalance : 0);
        return response;
    }

    private String createHmac(String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(hmacSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to create HMAC", e);
        }
    }
}
