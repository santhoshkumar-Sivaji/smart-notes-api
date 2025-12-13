package com.example.handwrittennotesapi.dto;

import com.example.handwrittennotesapi.model.OcrJobStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PageInfo {
    private String pageId;
    private LocalDateTime createdAt;
    private OcrJobStatus ocrStatus;
}
