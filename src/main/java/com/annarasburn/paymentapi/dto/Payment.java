package com.annarasburn.paymentapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private Long id;

    private BigDecimal amount;

    private Currency currency;

    private String productName;

    public Payment(BigDecimal amount, Currency currency, String productName) {
        this.amount = amount;
        this.currency = currency;
        this.productName = productName;
    }

}
