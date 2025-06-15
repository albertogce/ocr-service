package com.docstream.parserservice.listener;

import com.docstream.commondata.dto.DocumentProcessingEvent;
import com.docstream.parserservice.service.ReactiveDocumentParsingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParserListener {

    @Autowired
    ReactiveDocumentParsingService reactiveDocumentParsingService;
    private final KafkaTemplate<String, DocumentProcessingEvent> kafkaTemplate;

    @Value("${docstream.topics.output}")
    private String outputQueue;

    private final Executor executor = Executors.newVirtualThreadPerTaskExecutor();

    @KafkaListener(topics = "${docstream.topics.input}", groupId = "parser-consumer-group")
    public void receive(DocumentProcessingEvent event) {
        String summary = "Longitud del texto: " + event.getExtractedText().length();
        reactiveDocumentParsingService.parse(event)
                .flatMap(this::validateEvent)
                .doOnError(error -> log.error("Parse error", error))
                .subscribe();

    }

    public Mono<Void> validateEvent(DocumentProcessingEvent event) {
        return Mono.fromRunnable(() -> kafkaTemplate.send(outputQueue, event))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> log.error("Error enviando evento", e))
                .then();
    }

    @KafkaListener(topics = "${docstream.topics.input}.DLT", groupId = "dlt-processor")
    public void handleDLT(DocumentProcessingEvent  event) {
        log.error("Mensaje redirigido a DLT: {}", event);
    }



}
