package com.example.handwrittennotesapi.service;

import com.example.handwrittennotesapi.dto.RedeemRequest;
import com.example.handwrittennotesapi.dto.RedeemResponse;

public interface NotebookService {
    RedeemResponse redeemNotebook(RedeemRequest redeemRequest);
}
