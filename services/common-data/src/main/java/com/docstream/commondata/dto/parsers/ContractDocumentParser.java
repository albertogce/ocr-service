package com.docstream.commondata.dto.parsers;

import com.docstream.commondata.dto.ContractDocument;
import com.docstream.commondata.dto.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class ContractDocumentParser implements DocumentParserStrategy {

    @Override
    public boolean supports(String documentType) {
        return "CONTRACT".equalsIgnoreCase(documentType);
    }

    @Override
    public Document parse(String text, UUID id) {
        ContractDocument doc = new ContractDocument();
        doc.setId(id);
        doc.setScannedDate(LocalDate.now());
        doc.setNumber("123456");
        doc.setExpirationDate(LocalDate.now());
        return doc;
    }
}
