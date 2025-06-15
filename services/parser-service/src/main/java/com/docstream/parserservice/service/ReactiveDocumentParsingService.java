package com.docstream.parserservice.service;

import com.docstream.commondata.dto.Document;
import com.docstream.commondata.dto.DocumentProcessingEvent;
import com.docstream.commondata.dto.ProcessingStage;
import com.docstream.commondata.dto.parsers.DocumentParserFactory;
import com.docstream.commondata.dto.parsers.DocumentParserStrategy;
import com.docstream.parserservice.interfaces.DocumentParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ReactiveDocumentParsingService implements DocumentParsingService {

    @Autowired
    DocumentParserFactory parserFactory;

    @Override
    public Mono<DocumentProcessingEvent> parse(DocumentProcessingEvent event) {
        return Mono.fromCallable(() -> {
            String documentType = detectDocumentType(event.getExtractedText());
            DocumentParserStrategy parser = parserFactory.getParser(documentType);

            Document parsedDoc = parser.parse(event.getExtractedText(), event.getDocumentId());

            event.setParsedData(parsedDoc);
            event.setStage(ProcessingStage.PARSED);
            return event;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private String detectDocumentType(String text) {
        String lowerText = text.toLowerCase();

        if (lowerText.contains("invoice") || lowerText.contains("factura")) return "INVOICE";
        if (lowerText.contains("contract") || lowerText.contains("contrato")) return "DNI";
        return "UNKNOWN";
    }


}
