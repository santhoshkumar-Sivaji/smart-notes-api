package com.example.handwrittennotesapi.service;

import com.example.handwrittennotesapi.dto.CoinBalanceResponse;
import com.example.handwrittennotesapi.model.User;
import com.example.handwrittennotesapi.repository.CoinLedgerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinServiceImpl implements CoinService {

    private final CoinLedgerRepository coinLedgerRepository;
    private final UserService userService;

    @Autowired
    public CoinServiceImpl(CoinLedgerRepository coinLedgerRepository, UserService userService) {
        this.coinLedgerRepository = coinLedgerRepository;
        this.userService = userService;
    }

    @Override
    public CoinBalanceResponse getCoinBalance() {
        User currentUser = userService.getCurrentUser();
        Integer balance = coinLedgerRepository.getBalanceByUserId(currentUser.getId());
        CoinBalanceResponse response = new CoinBalanceResponse();
        response.setBalance(balance != null ? balance : 0);
        return response;
    }
}
