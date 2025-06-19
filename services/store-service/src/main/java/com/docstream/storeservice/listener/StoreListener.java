package com.docstream.storeservice.listener;

import com.docstream.commondata.dto.DocumentProcessingEvent;
import com.docstream.storeservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StoreListener {

    private final StoreService storeService;
    private final KafkaTemplate<String, DocumentProcessingEvent> kafkaTemplate;


    @KafkaListener(topics = "${docstream.topics.input}", groupId = "ocr-consumer-group")
    public void receive(DocumentProcessingEvent event) {
        DocumentProcessingEvent processedEvent = storeService.storeDocument(event);
    }

    @KafkaListener(topics = "${docstream.topics.input}.DLT", groupId = "dlt-processor")
    public void handleDLT(DocumentProcessingEvent  event) {
        log.error("Mensaje redirigido a DLT: {}", event);
    }


}
