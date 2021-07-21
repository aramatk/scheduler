package com.scheduler.service;

import com.scheduler.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpService {
    private RestTemplate restTemplate;

    @Autowired
    public HttpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> run(Task task) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(task.getRequestBodyJson(), headers);
        ResponseEntity<String> response = restTemplate.exchange(task.getUrl(), task.getHttpMethod(), request, String.class);
        return response;
    }
}
