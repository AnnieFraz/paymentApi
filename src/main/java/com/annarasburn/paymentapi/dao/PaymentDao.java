package com.annarasburn.paymentapi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "payment", path = "payment")
public interface PaymentDao<Payment> extends JpaRepository<Payment, Long> {
}
