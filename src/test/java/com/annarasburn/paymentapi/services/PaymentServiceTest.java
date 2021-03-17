package com.annarasburn.paymentapi.services;

import com.annarasburn.paymentapi.dao.PaymentDao;
import com.annarasburn.paymentapi.dto.Currency;
import com.annarasburn.paymentapi.dto.Payment;
import com.annarasburn.paymentapi.dto.exception.DataMissingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class PaymentServiceTest {


    @Mock
    PaymentDao mockPaymentDao;

    @Mock
    PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        paymentService = new PaymentService(mockPaymentDao);
    }

    @Test
    void addPayments() throws Exception {
        List<Payment> expectedPayments = getListOfPayments();
        when(mockPaymentDao.saveAll(anyList())).thenReturn(getListOfPayments());
        List<Payment> actualPayments = paymentService.addPayments(expectedPayments);
        assertEquals(expectedPayments.size(), actualPayments.size());
        assertEquals(expectedPayments, actualPayments);
        verify(mockPaymentDao, times(1)).saveAll(anyList());
        verifyNoMoreInteractions(mockPaymentDao);
    }

    @Test
    void addEmptyPayments() throws Exception {
        List<Payment> expectedPayments = new ArrayList<>();
        when(mockPaymentDao.saveAll(anyList())).thenReturn(getListOfPayments());
        List<Payment> actualPayments = paymentService.addPayments(expectedPayments);
        assertEquals(0, actualPayments.size());
        verifyNoMoreInteractions(mockPaymentDao);
    }

    @Test
    void addPaymentsExceptionsProductNameNull() throws Exception {

        Payment missingAmountPayment = new Payment(new BigDecimal(200), Currency.USD, null);

        Exception dataMissingException = assertThrows(DataMissingException.class, () -> {
            paymentService.addPayments(Collections.singletonList(missingAmountPayment));
        });

        assertTrue(dataMissingException.getMessage().contains("Payment Product Name is missing"));

    }

    @Test
    void addPaymentsExceptionsCurrencyNull() throws Exception {

        Payment missingAmountPayment = new Payment(new BigDecimal(200), null, "Nintendo Switch");

        Exception dataMissingException = assertThrows(DataMissingException.class, () -> {
            paymentService.addPayments(Collections.singletonList(missingAmountPayment));
        });

        assertTrue(dataMissingException.getMessage().contains("Payment Currency is missing"));

    }

    @Test
    void addPaymentsExceptionsAllNull() throws Exception {
        Payment missingAmountPayment = new Payment(null, null, null);
        Exception dataMissingException = assertThrows(DataMissingException.class, () -> {
            paymentService.addPayments(Collections.singletonList(missingAmountPayment));
        });
        assertTrue(dataMissingException.getMessage().contains("Payment Amount is missing"));

    }

    @Test
    void updatePayment() throws Exception {
        Optional<Payment> updatedPayment = Optional.of(new Payment(1, new BigDecimal(200), Currency.USD, "Calender"));
        Optional<Payment> payment = Optional.of(getDummyPayment());
        when(mockPaymentDao.findById(1)).thenReturn(payment);

        when(mockPaymentDao.findById(1)).thenReturn(updatedPayment);

        Payment actualPayment = paymentService.updatePayment(1, updatedPayment.get());

        assertEquals(updatedPayment.get(), actualPayment);
        assertEquals(updatedPayment.get().getId(), actualPayment.getId());
    }


    @Test
    void updatePaymentEverythingNullButCurrency() throws Exception {
        Payment updatePayment = new Payment(1, null, Currency.USD, null);
        Optional<Payment> updatedPayment = Optional.of(new Payment(new BigDecimal(100), Currency.USD, "Telephone"));
        Optional<Payment> payment = Optional.of(getDummyPayment());
        when(mockPaymentDao.findById(1)).thenReturn(payment);
        when(mockPaymentDao.findById(1)).thenReturn(updatedPayment);
        Payment actualPayment = paymentService.updatePayment(1, updatePayment);
        assertEquals(updatedPayment.get(), actualPayment);
        assertEquals(updatedPayment.get().getId(), actualPayment.getId());
        assertEquals(updatedPayment.get().getCurrency(), actualPayment.getCurrency());
    }

    @Test
    void updatePaymentEverythingNullButProductName() throws Exception {
        Payment updatePayment = new Payment(1, null, null, "Hairbrush");
        Optional<Payment> updatedPayment = Optional.of(new Payment(new BigDecimal(100), Currency.AUD, "Hairbrush"));
        Optional<Payment> payment = Optional.of(getDummyPayment());
        when(mockPaymentDao.findById(1)).thenReturn(payment);
        when(mockPaymentDao.findById(1)).thenReturn(updatedPayment);
        Payment actualPayment = paymentService.updatePayment(1, updatePayment);
        assertEquals(updatedPayment.get(), actualPayment);
        assertEquals(updatedPayment.get().getId(), actualPayment.getId());
        assertEquals(updatedPayment.get().getProductName(), actualPayment.getProductName());
    }

    @Test
    void updatePaymentEverythingNullButAmount() throws Exception {
        Payment updatePayment = new Payment(1, new BigDecimal(200), null, null);
        Optional<Payment> updatedPayment = Optional.of(new Payment(new BigDecimal(200), Currency.AUD, "Hairbrush"));
        Optional<Payment> payment = Optional.of(getDummyPayment());
        when(mockPaymentDao.findById(1)).thenReturn(payment);
        when(mockPaymentDao.findById(1)).thenReturn(updatedPayment);
        Payment actualPayment = paymentService.updatePayment(1, updatePayment);
        assertEquals(updatedPayment.get(), actualPayment);
        assertEquals(updatedPayment.get().getId(), actualPayment.getId());
        assertEquals(updatedPayment.get().getAmount(), actualPayment.getAmount());
    }

    @Test
    void getAllPayments() {
        when(mockPaymentDao.findAll()).thenReturn(getListOfPayments());
        List<Payment> actualPayments = paymentService.getAllPayments();
        assertEquals(getListOfPayments().size(), actualPayments.size());
        assertEquals(getListOfPayments(), actualPayments);
    }

    @Test
    void getPaymentsById() throws Exception {
        Optional<Payment> expectedPayment = Optional.of(getDummyPayment());
        when(mockPaymentDao.findById(1)).thenReturn(expectedPayment);

        Payment actualPayment = paymentService.getPaymentById(1);
        assertEquals(expectedPayment.get(), actualPayment);
        verify(mockPaymentDao, times(1)).findById(1);
        verifyNoMoreInteractions(mockPaymentDao);

    }

    @Test
    void deletePayment() throws Exception {
        Optional<Payment> payment = Optional.of(getDummyPayment());
        when(mockPaymentDao.findById(1)).thenReturn(payment);
        paymentService.deletePayment(1);
        verify(mockPaymentDao, times(1)).deleteById(1);
    }

    @Test
    void deletePaymentException() {
        when(mockPaymentDao.findById(1)).thenReturn(null);
        assertThrows(DataMissingException.class, () -> paymentService.deletePayment(1));

    }

    private Payment getDummyPayment() {
        return new Payment(new BigDecimal(100), Currency.AUD, "Telephone");
    }

    private List<Payment> getListOfPayments() {
        List<Payment> paymentList = new ArrayList<>();

        Payment payment1 = new Payment();
        payment1.setProductName("Mobile Phone");
        payment1.setCurrency(Currency.GBP);
        payment1.setAmount(new BigDecimal(2000));
        paymentList.add(payment1);

        Payment payment2 = new Payment();
        payment2.setProductName("Television");
        payment2.setCurrency(Currency.JPY);
        payment2.setAmount(new BigDecimal(3000));
        paymentList.add(payment2);

        Payment payment3 = new Payment();
        payment3.setProductName("Wardrobe");
        payment3.setCurrency(Currency.EUR);
        payment3.setAmount(new BigDecimal(100));
        paymentList.add(payment3);

        return paymentList;
    }

}