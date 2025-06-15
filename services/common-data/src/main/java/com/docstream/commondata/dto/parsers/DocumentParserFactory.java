package com.docstream.commondata.dto.parsers;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentParserFactory {

    private final List<DocumentParserStrategy> strategies;

    public DocumentParserFactory(List<DocumentParserStrategy> strategies) {
        this.strategies = strategies;
    }

    public DocumentParserStrategy getParser(String documentType) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(documentType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported document type: " + documentType));
    }
}
