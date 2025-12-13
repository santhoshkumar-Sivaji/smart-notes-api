package com.example.handwrittennotesapi.controller;

import com.example.handwrittennotesapi.dto.RedeemRequest;
import com.example.handwrittennotesapi.dto.RedeemResponse;
import com.example.handwrittennotesapi.service.NotebookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notebooks")
@Tag(name = "Notebook Management", description = "APIs for managing notebooks")
public class NotebookController {

    private final NotebookService notebookService;

    @Autowired
    public NotebookController(NotebookService notebookService) {
        this.notebookService = notebookService;
    }

    @PostMapping("/redeem")
    @Operation(summary = "Redeem notebook", description = "Redeems a notebook using a QR code")
    @ApiResponse(responseCode = "200", description = "Successful redemption")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RedeemResponse> redeemNotebook(@RequestBody RedeemRequest redeemRequest) {
        return ResponseEntity.ok(notebookService.redeemNotebook(redeemRequest));
    }
}
