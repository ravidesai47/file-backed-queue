package com.noonacademy.assignment.queue.server.controller;

import com.noonacademy.assignment.queue.server.service.QueueManagerService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "queue")
public class QueueController {

    private final QueueManagerService queueManagerService;

    public QueueController(QueueManagerService queueManagerService) {
        this.queueManagerService = queueManagerService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "enqueue")
    public void enqueue(@RequestBody String entry) {
        queueManagerService.enqueue(entry);
    }

    @RequestMapping(method = RequestMethod.GET, path = "dequeue")
    public String dequeue() {
        return queueManagerService.dequeue();
    }
}
