package com.example.handwrittennotesapi.controller;

import com.example.handwrittennotesapi.dto.CoinBalanceResponse;
import com.example.handwrittennotesapi.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coins")
public class CoinController {

    private final CoinService coinService;

    @Autowired
    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping("/balance")
    public ResponseEntity<CoinBalanceResponse> getCoinBalance() {
        return ResponseEntity.ok(coinService.getCoinBalance());
    }
}
