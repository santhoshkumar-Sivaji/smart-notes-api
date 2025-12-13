package com.example.handwrittennotesapi.controller;

import com.example.handwrittennotesapi.dto.OcrRequest;
import com.example.handwrittennotesapi.dto.OcrRequestResponse;
import com.example.handwrittennotesapi.dto.OcrStatusResponse;
import com.example.handwrittennotesapi.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OcrController {

    private final OcrService ocrService;

    @Autowired
    public OcrController(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    @PostMapping("/pages/{pageId}/ocr")
    public ResponseEntity<OcrRequestResponse> requestOcr(
            @PathVariable("pageId") String pageId,
            @RequestBody OcrRequest ocrRequest
    ) {
        return ResponseEntity.ok(ocrService.requestOcr(pageId, ocrRequest));
    }

    @GetMapping("/ocr/jobs/{jobId}")
    public ResponseEntity<OcrStatusResponse> getOcrStatus(@PathVariable("jobId") String jobId) {
        return ResponseEntity.ok(ocrService.getOcrStatus(jobId));
    }
}
