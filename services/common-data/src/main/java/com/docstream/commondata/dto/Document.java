package com.docstream.commondata.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"  // Indica el tipo de clase concreta
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ContractDocument.class, name = "contractDocument"),
        @JsonSubTypes.Type(value = InvoiceDocument.class, name = "invoiceDocument")
})
@Data
public abstract class Document {

    protected UUID id;
    protected LocalDate scannedDate;

}
