package com.example.handwrittennotesapi.dto;

import lombok.Data;

@Data
public class OcrRequestResponse {
    private String jobId;
    private String status;
}
