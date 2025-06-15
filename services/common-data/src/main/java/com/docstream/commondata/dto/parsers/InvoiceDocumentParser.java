package com.docstream.commondata.dto.parsers;

import com.docstream.commondata.dto.Document;
import com.docstream.commondata.dto.InvoiceDocument;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InvoiceDocumentParser implements DocumentParserStrategy {

    @Override
    public boolean supports(String documentType) {
        return "INVOICE".equalsIgnoreCase(documentType);
    }

    @Override
    public Document parse(String text, UUID id) {
        InvoiceDocument doc = new InvoiceDocument();
        doc.setId(id);
        doc.setInvoiceNumber(extractInvoiceNumber(text));
        doc.setTotalAmount(extractAmount(text));
        return doc;
    }

    private String extractInvoiceNumber(String text) {
        return "INV-123456";
    }

    private Double extractAmount(String text) {
        return 99.99;
    }
}
