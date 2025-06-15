package com.docstream.commondata.dto;

import lombok.Data;

@Data
public class InvoiceItem {

    private String item;
    private Double amount;
    private Double price;
    private Double tax;
    private Double totalPrice;
}
