package com.docstream.storeservice.mapper;

import com.docstream.commondata.dto.DocumentProcessingEvent;
import com.docstream.storeservice.dto.StoredDocument;

import java.time.Instant;

public class StoredDocumentMapper {

    public static StoredDocument fromEvent(DocumentProcessingEvent event) {
        StoredDocument stored = new StoredDocument();
        stored.setDocumentId(event.getDocumentId());
        stored.setRawFilePath(event.getRawFilePath());
        stored.setExtractedText(event.getExtractedText());
        stored.setParsedData(event.getParsedData());
        stored.setValidationErrors(event.getValidationErrors());
        stored.setEnrichedData(event.getEnrichedData());
        stored.setTimestamp(Instant.now());
        stored.setStage(event.getStage());
        return stored;
    }
}
