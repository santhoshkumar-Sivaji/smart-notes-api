package com.example.handwrittennotesapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StubQueueServiceImpl implements QueueService {

    private static final Logger logger = LoggerFactory.getLogger(StubQueueServiceImpl.class);

    @Override
    public void sendMessage(String queueName, String message) {
        logger.info("Sending message to queue '{}': {}", queueName, message);
        // In a real implementation, this would send a message to a message broker.
    }
}
