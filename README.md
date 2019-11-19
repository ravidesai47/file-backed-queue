# File Backed Queue Server

## Overview

This project provides a file backed queue server. Client's can enqueue data to the queue or they can dequeue/fetch data from the queue.

## Frameworks and Libraries

* Maven as a project and dependency management tool
* Spring Boot for configuring and running restful web service
* Logback for console and file based logging
* Spring Fox (Swagger 2) for API Documentation and manual testing

## Rest API Documentation

**Link:** http://localhost:3456/swagger-ui.html

* POST /queue/enqueue
   * Use this method to enqueue an element on queue server
   * Data can be passed as a message body
* GET /queue/dequeue
   * Use this method to dequeue an element from queue server

## How to start queue server

* Open pom.xml file in IDE (Eclipse/IntelliJ)
* Resolve maven dependencies
* Open QueueServerApplication class
* Run main method from this class to start queue server
* Go to http://localhost:3456/swagger-ui.html
* Click on queue-controller
* Perform relevant operations on queue from that screen to test the server