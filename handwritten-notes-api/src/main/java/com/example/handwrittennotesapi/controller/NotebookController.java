package com.example.handwrittennotesapi.controller;

import com.example.handwrittennotesapi.dto.RedeemRequest;
import com.example.handwrittennotesapi.dto.RedeemResponse;
import com.example.handwrittennotesapi.service.NotebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notebooks")
public class NotebookController {

    private final NotebookService notebookService;

    @Autowired
    public NotebookController(NotebookService notebookService) {
        this.notebookService = notebookService;
    }

    @PostMapping("/redeem")
    public ResponseEntity<RedeemResponse> redeemNotebook(@RequestBody RedeemRequest redeemRequest) {
        return ResponseEntity.ok(notebookService.redeemNotebook(redeemRequest));
    }
}
