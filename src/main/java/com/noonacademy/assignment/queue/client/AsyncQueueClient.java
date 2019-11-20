package com.noonacademy.assignment.queue.client;

import com.noonacademy.assignment.queue.client.exception.EnqueueOperationFailed;
import com.noonacademy.assignment.queue.client.exception.PushOperationFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class AsyncQueueClient extends QueueClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncQueueClient.class);

    public AsyncQueueClient(String queueServerBaseUrl) {
        super(queueServerBaseUrl);
    }

    public AsyncQueueClient(String queueServerBaseUrl, long pollingInterval, TimeUnit pollingIntervalTimeUnit) {
        super(queueServerBaseUrl, pollingInterval, pollingIntervalTimeUnit);
    }

    @Override
    public void push(String entry) throws PushOperationFailed, InterruptedException {
        this.pushWithFuture(entry);
    }

    public CompletableFuture<Void> pushWithFuture(String entry) {
        return CompletableFuture.runAsync(() -> {
            try {
                this.enqueue(entry);
            } catch (EnqueueOperationFailed enqueueOperationFailed) {
                LOGGER.error("Failed to process push request", enqueueOperationFailed);
                throw new RuntimeException("Failed to process push request");
            }
        });
    }
}
