package com.example.handwrittennotesapi.service;

import com.example.handwrittennotesapi.dto.OcrRequest;
import com.example.handwrittennotesapi.dto.OcrRequestResponse;
import com.example.handwrittennotesapi.dto.OcrStatusResponse;

public interface OcrService {
    OcrRequestResponse requestOcr(String pageId, OcrRequest ocrRequest);
    OcrStatusResponse getOcrStatus(String jobId);
    void handleOcrFailure(String jobId);
}
