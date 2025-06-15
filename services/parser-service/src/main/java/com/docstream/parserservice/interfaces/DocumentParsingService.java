package com.docstream.parserservice.interfaces;

import com.docstream.commondata.dto.DocumentProcessingEvent;
import reactor.core.publisher.Mono;

public interface DocumentParsingService {

    Mono<DocumentProcessingEvent> parse(DocumentProcessingEvent event);

}