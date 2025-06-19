package com.docstream.commondata.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContractDocument extends Document implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String typeContract;
    private String number;
    private LocalDate expirationDate;

}
