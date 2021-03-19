package com.annarasburn.paymentapi.dto;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "`payment`")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String productName;


    public Payment(BigDecimal amount, Currency currency, String productName) {
        this.amount = amount;
        this.currency = currency;
        this.productName = productName;
    }

    public Payment(int id, BigDecimal amount, Currency currency, String productName) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.productName = productName;
    }

    public Payment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", amount=" + amount +
                ", currency=" + currency +
                ", productName='" + productName + '\'' +
                '}';
    }
}
