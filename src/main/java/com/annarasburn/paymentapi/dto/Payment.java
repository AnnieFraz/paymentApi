package com.annarasburn.paymentapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "`payment`")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    private int id;

    private BigDecimal amount;

    private Currency currency;

    private String productName;


    public Payment(BigDecimal amount, Currency currency, String productName) {
        this.amount = amount;
        this.currency = currency;
        this.productName = productName;
    }
}
