package com.docstream.validateservice.service;

import com.docstream.commondata.dto.DocumentProcessingEvent;
import com.docstream.commondata.dto.ProcessingStage;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ValidateService {

    @Autowired
    private MeterRegistry meterRegistry;

    public DocumentProcessingEvent validateDocument(DocumentProcessingEvent event) {
        long startTime = System.nanoTime();
        event.setStage(ProcessingStage.RECEIVED); // Cambiar a VALIDATED

        try {
            meterRegistry.counter("ocr.documents.processed").increment();

        } catch (Exception e) {
            meterRegistry.counter("ocr.errors").increment();
            throw new RuntimeException("OCR processing failed", e);
        } finally {
            long endTime = System.nanoTime();
            meterRegistry.timer("ocr.processing.time").record(endTime - startTime, TimeUnit.NANOSECONDS);
        }

        return event;
    }

}