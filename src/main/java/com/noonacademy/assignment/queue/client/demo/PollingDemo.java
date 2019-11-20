package com.noonacademy.assignment.queue.client.demo;

import com.noonacademy.assignment.queue.client.QueueClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PollingDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollingDemo.class);

    /**
     * Start Queue Server
     * Start main method of PollingDemo class
     * Push entry to queue at different interval through swagger for testing
     */
    public static void main(String[] args) {

        QueueClient client = new QueueClient("http://localhost:3456");

        try {
            LOGGER.info(client.poll());
        } catch (InterruptedException e) {
            LOGGER.error("Handling Interruption");
        }

        try {
            LOGGER.info(client.poll(10, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            LOGGER.error("Handling Interruption");
        } catch (TimeoutException e) {
            LOGGER.error("Handling Time Out");
        }
    }
}
