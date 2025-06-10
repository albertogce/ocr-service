package com.docstream.parserservice.service;

import com.docstream.parserservice.dto.DocumentProcessingEvent;
import com.docstream.parserservice.dto.ProcessingStage;
import com.docstream.parserservice.interfaces.DocumentParsingService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReactiveDocumentParsingService implements DocumentParsingService {

    @Override
    public Mono<DocumentProcessingEvent> parse(DocumentProcessingEvent event) {
        return Mono.fromCallable(() -> {
            String text = event.getExtractedText();
            Map<String, Object> parsedData = new HashMap<>();

            // Simular extracción de campos
            parsedData.put("invoiceNumber", extractInvoiceNumber(text));
            parsedData.put("amount", extractAmount(text));

            event.setParsedData(parsedData);
            event.setStage(ProcessingStage.PARSED);
            return event;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private String extractInvoiceNumber(String text) {
        // Regex o NLP aquí
        return "INV-123456";
    }

    private Double extractAmount(String text) {
        return 99.99;
    }

}
