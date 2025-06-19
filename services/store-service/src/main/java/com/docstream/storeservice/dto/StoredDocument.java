package com.docstream.storeservice.dto;

import com.docstream.commondata.dto.Document;
import com.docstream.commondata.dto.ProcessingStage;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Data
public class StoredDocument {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID documentId;

    @Column(nullable = false)
    private String rawFilePath;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String extractedText;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Document parsedData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> validationErrors;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> enrichedData;

    @Column(nullable = false)
    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessingStage stage;

}