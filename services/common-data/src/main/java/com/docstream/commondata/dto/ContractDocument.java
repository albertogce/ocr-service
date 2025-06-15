package com.docstream.commondata.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContractDocument extends Document {

    private String typeContract;
    private String number;
    private LocalDate expirationDate;

}
