package com.docstream.grpcuploaderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${docstream.topics.output}")
    private String outputQueue;

    public void send(String filename, byte[] content) {
        String encodedContent = Base64.getEncoder().encodeToString(content);

        kafkaTemplate.send(outputQueue, filename, encodedContent);
    }
}
