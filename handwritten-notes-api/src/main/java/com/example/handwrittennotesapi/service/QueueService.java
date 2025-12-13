package com.example.handwrittennotesapi.service;

public interface QueueService {
    void sendMessage(String queueName, String message);
}
