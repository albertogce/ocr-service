package com.docstream.commondata.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceDocument extends Document implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String invoiceNumber;
    private LocalDate invoiceDate;
    private String supplierName;
    private Double totalAmount;
    private List<InvoiceItem> invoiceItemList;

}
