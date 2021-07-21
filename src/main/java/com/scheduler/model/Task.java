package com.scheduler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "url", nullable = false)
    private String url;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "httpMethod", nullable = false)
    private HttpMethod httpMethod;

    @Column(name = "requestBodyJson", columnDefinition = "TEXT")
    private String requestBodyJson;

    @Column(name = "executeTime", nullable = false)
    private Date executeTime;

    @Column(name = "done")
    private boolean done;
}
