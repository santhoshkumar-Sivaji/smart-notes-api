package com.example.handwrittennotesapi.controller;

import com.example.handwrittennotesapi.dto.CoinBalanceResponse;
import com.example.handwrittennotesapi.service.CoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coins")
@Tag(name = "Coin Management", description = "APIs for managing user coins")
public class CoinController {

    private final CoinService coinService;

    @Autowired
    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping("/balance")
    @Operation(summary = "Get coin balance", description = "Retrieves the current coin balance for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of coin balance")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CoinBalanceResponse> getCoinBalance() {
        return ResponseEntity.ok(coinService.getCoinBalance());
    }
}
