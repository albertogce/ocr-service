package com.docstream.validateservice.listener;

import com.docstream.commondata.dto.DocumentProcessingEvent;
import com.docstream.validateservice.service.ValidateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ValidateListener {

    private final ValidateService validateService;
    private final KafkaTemplate<String, DocumentProcessingEvent> kafkaTemplate;

    @Value("${docstream.topics.output}")
    private String outputTopic;

    @KafkaListener(topics = "${docstream.topics.input}", groupId = "ocr-consumer-group")
    public void receive(DocumentProcessingEvent event) {
        DocumentProcessingEvent processedEvent = validateService.validateDocument(event);

        kafkaTemplate.send(outputTopic, processedEvent);
    }

    @KafkaListener(topics = "${docstream.topics.input}.DLT", groupId = "dlt-processor")
    public void handleDLT(DocumentProcessingEvent  event) {
        log.error("Mensaje redirigido a DLT: {}", event);
    }


}
