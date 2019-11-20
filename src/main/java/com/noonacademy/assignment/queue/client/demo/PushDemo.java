package com.noonacademy.assignment.queue.client.demo;

import com.noonacademy.assignment.queue.client.AsyncQueueClient;
import com.noonacademy.assignment.queue.client.FixedRetryQueueClient;
import com.noonacademy.assignment.queue.client.QueueClient;
import com.noonacademy.assignment.queue.client.exception.PushOperationFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class PushDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushDemo.class);

    /**
     * Start Queue Server
     * Start main method of PushDemo class
     * From swagger ui call dequeue method for testing
     */
    public static void main(String[] args) {

        QueueClient client = new QueueClient("http://localhost:3456");

        try {
            client.push("abc");
        } catch (PushOperationFailed | InterruptedException e) {
            LOGGER.error("Push operation failed");
        }

        AsyncQueueClient asyncQueueClient = new AsyncQueueClient("http://localhost:3456");

        try {
            asyncQueueClient.push("xyz");
        } catch (PushOperationFailed | InterruptedException e) {
            LOGGER.error("Push operation failed");
        }

        CompletableFuture<Void> future = asyncQueueClient.pushWithFuture("123");
        future.join();

        FixedRetryQueueClient retryQueueClient = new FixedRetryQueueClient("http://localhost:3456", 5);

        try {
            retryQueueClient.push("5retry");
        } catch (PushOperationFailed pushOperationFailed) {
            LOGGER.error("Failed after 5 retry");
        } catch (InterruptedException e) {
            LOGGER.error("Thread intrupt caught");
        }
    }
}
