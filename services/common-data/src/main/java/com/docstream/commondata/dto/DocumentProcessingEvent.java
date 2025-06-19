package com.docstream.commondata.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class DocumentProcessingEvent {

    private UUID documentId;
    private String rawFilePath;
    private String extractedText;
    private Document parsedData;
    private List<String> validationErrors;
    private Map<String, Object> enrichedData;
    private Instant timestamp;
    private ProcessingStage stage;

}