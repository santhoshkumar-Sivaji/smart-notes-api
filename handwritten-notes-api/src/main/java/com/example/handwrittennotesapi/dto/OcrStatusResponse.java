package com.example.handwrittennotesapi.dto;

import lombok.Data;

@Data
public class OcrStatusResponse {
    private String status;
    private String extractedText;
}
