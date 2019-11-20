package com.noonacademy.assignment.queue.server.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueueManagerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueManagerService.class);

    private final String queuePersistenceDirectory;

    private final Integer queueUpperCapacityLimitAlert;

    private final FilePersistenceService filePersistenceService;

    private Integer startIndex;

    private Integer endIndex;

    public QueueManagerService(@Value("${queue.persistence.directory}") String queuePersistenceDirectory,
                               @Value("${queue.upper.capacity.limit.alert}") Integer queueUpperCapacityLimitAlert,
                               FilePersistenceService filePersistenceService) {
        this.queuePersistenceDirectory = queuePersistenceDirectory;
        this.filePersistenceService = filePersistenceService;
        this.queueUpperCapacityLimitAlert = queueUpperCapacityLimitAlert;
    }

    /**
     * If server had restarted due to any reason
     * Then this method will reload the previous state of the Queue
     */
    @PostConstruct
    public void init() {

        File directory = new File(queuePersistenceDirectory);
        try {
            if (!directory.exists()) {
                FileUtils.forceMkdir(directory);
            }

            String[] listOfFileNames = directory.list();
            if (listOfFileNames == null || listOfFileNames.length == 0) {
                startIndex = 0;
                endIndex = 0;
            } else {
                List<Integer> sortedList = Arrays.stream(listOfFileNames)
                        .map(Integer::parseInt)
                        .sorted(Integer::compare)
                        .collect(Collectors.toList());
                startIndex = sortedList.get(0) - 1;
                endIndex = sortedList.get(sortedList.size() - 1);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to create Queue Persistence Directory: " + queuePersistenceDirectory, e);
            throw new RuntimeException("Failed to create Queue Persistence Directory");
        }
    }

    public synchronized void enqueue(String entry) {

        LOGGER.debug("Enqueue operation. Entry: " + entry);

        endIndex++;
        String entryFileName = queuePersistenceDirectory + "/" + endIndex;
        try {
            filePersistenceService.saveEntryToFile(entryFileName, entry);
        } catch (IOException e) {
            LOGGER.error("Failed to persist entry at index: " + endIndex, e);
            throw new RuntimeException("Failed to persist entry");
        }

        if ((endIndex - startIndex) > queueUpperCapacityLimitAlert) {
            LOGGER.warn("Queue has breached upper capacity limit. There may be a production outage preventing consumption from queue.");
        }
    }

    public synchronized String dequeue() {

        if (startIndex.equals(endIndex)) {
            return null;
        }

        startIndex++;
        String entryFileName = queuePersistenceDirectory + "/" + startIndex;
        String entry;
        try {
            entry = filePersistenceService.readEntryAndDeleteFile(entryFileName);
        } catch (IOException e) {
            LOGGER.error("Failed to read entry from index: " + startIndex, e);
            throw new RuntimeException("Failed to read entry");
        }

        if (startIndex.equals(endIndex)) {
            startIndex = 0;
            endIndex = 0;
        }

        LOGGER.debug("Dequeue operation. Entry: " + entry);
        return entry;
    }
}
