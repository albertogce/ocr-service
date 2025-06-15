package com.docstream.commondata.dto.parsers;

import com.docstream.commondata.dto.Document;

import java.util.UUID;

public interface DocumentParserStrategy {

    boolean supports(String documentType);
    Document parse(String text, UUID id);

}
