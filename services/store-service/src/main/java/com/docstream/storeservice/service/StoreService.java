package com.docstream.storeservice.service;

import com.docstream.commondata.dto.DocumentProcessingEvent;
import com.docstream.commondata.dto.ProcessingStage;
import com.docstream.storeservice.mapper.StoredDocumentMapper;
import com.docstream.storeservice.repository.StoredDocumentRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class StoreService {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private StoredDocumentRepository storedDocumentRepository;


    public DocumentProcessingEvent storeDocument(DocumentProcessingEvent event) {
        long startTime = System.nanoTime();

        try {
            event.setStage(ProcessingStage.STORED);
            storedDocumentRepository.save(StoredDocumentMapper.fromEvent(event));

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