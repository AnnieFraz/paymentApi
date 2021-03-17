package com.annarasburn.paymentapi.dao;

import com.annarasburn.paymentapi.dto.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDao extends JpaRepository<Payment, Integer> {
}
