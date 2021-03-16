package com.annarasburn.paymentapi.services;

import com.annarasburn.paymentapi.dao.PaymentDao;
import com.annarasburn.paymentapi.dto.Payment;
import com.annarasburn.paymentapi.dto.exception.DataMissingException;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PaymentService {

    private final PaymentDao paymentDao;

    @Autowired
    public PaymentService(
            final PaymentDao paymentDao
    ) {
        this.paymentDao = paymentDao;
    }

    public List<Payment> addPayments(List<Payment> paymentList) throws Exception {
        if (!CollectionUtils.isEmpty(paymentList)) {
            validatePaymentList(paymentList);
            try {
                return paymentDao.saveAll(paymentList);
            } catch (Exception e) {
               // log.error("Exception while saving payments", e);
                throw new Exception("Exception while saving payments");
            }
        }
        return paymentList;
    }

    public List<Payment> getAllPayments() {
        return paymentDao.findAll();
    }

    public Payment getPaymentById(Long id) throws Exception {
        Optional<Payment> payment = paymentDao.findById(id);
        if (payment.isEmpty()) {
            throw new Exception("Payment was not found");
        }
        return payment.get();
    }

    public void deletePayment(Long id) throws Exception {
        Optional<Payment> payment = paymentDao.findById(id);

        if (null == payment || payment.isEmpty()) {
            throw new DataMissingException("Payment was not found");
        }
        try {
            paymentDao.deleteById(id);
        } catch (Exception e) {
            // log.error("Exception while deleting payments", e);
            throw new Exception("Exception while deleting payments");
        }
    }

    public Payment updatePayment(Long id, Payment payment) throws Exception {
        Optional<Payment> originalPayment = paymentDao.findById(id);

        if (originalPayment == null || originalPayment.isEmpty()){
            throw new DataMissingException("Payment was not found");
        }

        Payment updatePayment = getPayment(id, payment);
        paymentDao.save(updatePayment);

        Optional<Payment> updatedPayment = paymentDao.findById(id);

        if (updatedPayment.isEmpty()) {
            throw new Exception("Error when updating payment");
        }
        return updatedPayment.get();
    }

    @NotNull
    private Payment getPayment(Long id, Payment payment) {
        Payment updatePayment = new Payment();
        updatePayment.setId(id);
        if (null != payment.getCurrency()) {
            updatePayment.setCurrency(payment.getCurrency());
        }
        if (null != payment.getAmount()) {
            updatePayment.setAmount(payment.getAmount());
        }
        if (null != payment.getProductName()) {
            updatePayment.setProductName(payment.getProductName());
        }
        return updatePayment;
    }

    private void validatePaymentList(List<Payment> paymentList) {
        paymentList.forEach(payment -> {
            if (null == payment.getAmount()) {
                throw new DataMissingException("Payment Amount is missing");
            }
            if (null == payment.getProductName()) {
                throw new DataMissingException("Payment Product Name is missing");
            }
            if (null == payment.getCurrency()) {
                throw new DataMissingException("Payment Currency is missing");
            }
        });
    }


}
