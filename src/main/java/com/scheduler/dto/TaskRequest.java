package com.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scheduler.model.Task;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.Date;

@Data
public class TaskRequest {
    private String url;
    private HttpMethod httpMethod;
    private String requestBodyJson;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date executeTime;

    public Task toTask() {
        return Task.builder()
                .url(url)
                .httpMethod(httpMethod)
                .requestBodyJson(requestBodyJson)
                .executeTime(executeTime)
                .build();
    }
}
