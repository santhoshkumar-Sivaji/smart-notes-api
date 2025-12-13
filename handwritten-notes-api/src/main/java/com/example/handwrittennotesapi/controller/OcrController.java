package com.example.handwrittennotesapi.controller;

import com.example.handwrittennotesapi.dto.OcrRequest;
import com.example.handwrittennotesapi.dto.OcrRequestResponse;
import com.example.handwrittennotesapi.dto.OcrStatusResponse;
import com.example.handwrittennotesapi.service.OcrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "OCR Management", description = "APIs for OCR operations on pages")
public class OcrController {

    private final OcrService ocrService;

    @Autowired
    public OcrController(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    @PostMapping("/pages/{pageId}/ocr")
    @Operation(summary = "Request OCR", description = "Requests OCR processing for a specific page")
    @ApiResponse(responseCode = "200", description = "OCR request successful")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OcrRequestResponse> requestOcr(
            @Parameter(description = "ID of the page") @PathVariable("pageId") String pageId,
            @RequestBody OcrRequest ocrRequest
    ) {
        return ResponseEntity.ok(ocrService.requestOcr(pageId, ocrRequest));
    }

    @GetMapping("/ocr/jobs/{jobId}")
    @Operation(summary = "Get OCR status", description = "Retrieves the status of an OCR job")
    @ApiResponse(responseCode = "200", description = "OCR status retrieved")
    @ApiResponse(responseCode = "404", description = "Job not found")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OcrStatusResponse> getOcrStatus(@Parameter(description = "ID of the OCR job") @PathVariable("jobId") String jobId) {
        return ResponseEntity.ok(ocrService.getOcrStatus(jobId));
    }
}
