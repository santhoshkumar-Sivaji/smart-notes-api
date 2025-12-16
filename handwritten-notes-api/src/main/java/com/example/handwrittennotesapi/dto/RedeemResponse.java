package com.example.handwrittennotesapi.dto;

import lombok.Data;

@Data
public class RedeemResponse {
    private String notebookInstanceId;
    private int coinsCredited;
    private int currentBalance;
}
