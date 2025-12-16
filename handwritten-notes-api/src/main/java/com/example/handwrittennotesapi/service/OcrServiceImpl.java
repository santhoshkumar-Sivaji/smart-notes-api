package com.example.handwrittennotesapi.service;

import com.example.handwrittennotesapi.dto.OcrRequest;
import com.example.handwrittennotesapi.dto.OcrRequestResponse;
import com.example.handwrittennotesapi.dto.OcrStatusResponse;
import com.example.handwrittennotesapi.model.*;
import com.example.handwrittennotesapi.repository.CoinLedgerRepository;
import com.example.handwrittennotesapi.repository.OcrJobRepository;
import com.example.handwrittennotesapi.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OcrServiceImpl implements OcrService {

    private final OcrJobRepository ocrJobRepository;
    private final PageRepository pageRepository;
    private final CoinLedgerRepository coinLedgerRepository;
    private final QueueService queueService;
    private final UserService userService;

    @Autowired
    public OcrServiceImpl(OcrJobRepository ocrJobRepository,
                          PageRepository pageRepository,
                          CoinLedgerRepository coinLedgerRepository,
                          QueueService queueService,
                          UserService userService) {
        this.ocrJobRepository = ocrJobRepository;
        this.pageRepository = pageRepository;
        this.coinLedgerRepository = coinLedgerRepository;
        this.queueService = queueService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public OcrRequestResponse requestOcr(String pageId, OcrRequest ocrRequest) {
        User currentUser = userService.getCurrentUser();
        Page page = pageRepository.findById(Long.parseLong(pageId))
                .orElseThrow(() -> new IllegalArgumentException("Page not found"));

        if (!page.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("User does not have permission to access this page");
        }

        Integer currentBalance = coinLedgerRepository.getBalanceByUserId(currentUser.getId());
        if (currentBalance == null || currentBalance < 1) {
            throw new IllegalStateException("Insufficient coin balance");
        }

        CoinLedger reservation = new CoinLedger();
        reservation.setUser(currentUser);
        reservation.setTransactionType(TransactionType.RESERVE);
        reservation.setAmount(-1);
        reservation.setReason("OCR job reservation");
        reservation.setReferenceId(pageId);
        reservation.setCreatedAt(LocalDateTime.now());
        coinLedgerRepository.save(reservation);

        OcrJob ocrJob = new OcrJob();
        ocrJob.setPage(page);
        ocrJob.setUser(currentUser);
        ocrJob.setStatus(OcrJobStatus.QUEUED);
        ocrJob.setReservedCoins(1);
        ocrJob.setCreatedAt(LocalDateTime.now());
        ocrJob.setUpdatedAt(LocalDateTime.now());
        ocrJob = ocrJobRepository.save(ocrJob);

        queueService.sendMessage("ocr-jobs", String.valueOf(ocrJob.getId()));

        OcrRequestResponse response = new OcrRequestResponse();
        response.setJobId(String.valueOf(ocrJob.getId()));
        response.setStatus(ocrJob.getStatus().name());
        return response;
    }

    @Override
    public OcrStatusResponse getOcrStatus(String jobId) {
        OcrJob ocrJob = ocrJobRepository.findById(Long.parseLong(jobId))
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        OcrStatusResponse response = new OcrStatusResponse();
        response.setStatus(ocrJob.getStatus().name());

        if (ocrJob.getStatus() == OcrJobStatus.COMPLETED) {
            // In a real app, we would fetch the text from OcrResult
            response.setExtractedText("Meeting notes...");
        }

        return response;
    }

    @Override
    @Transactional
    public void handleOcrFailure(String jobId) {
        OcrJob ocrJob = ocrJobRepository.findById(Long.parseLong(jobId))
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        ocrJob.setStatus(OcrJobStatus.FAILED);
        ocrJob.setUpdatedAt(LocalDateTime.now());
        ocrJobRepository.save(ocrJob);

        CoinLedger refund = new CoinLedger();
        refund.setUser(ocrJob.getUser());
        refund.setTransactionType(TransactionType.REFUND);
        refund.setAmount(ocrJob.getReservedCoins());
        refund.setReason("OCR job failed");
        refund.setReferenceId(String.valueOf(ocrJob.getId()));
        refund.setCreatedAt(LocalDateTime.now());
        coinLedgerRepository.save(refund);
    }
}
