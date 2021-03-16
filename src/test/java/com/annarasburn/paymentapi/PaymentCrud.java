package com.annarasburn.paymentapi;

import com.annarasburn.paymentapi.dto.Currency;
import com.annarasburn.paymentapi.dto.Payment;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentCrud {

    private List<Payment> givenPayments;

    private ResponseEntity<List<Payment>> response;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before("@PaymentCRUD")
    public void setup(){
        restTemplate = Mockito.mock(TestRestTemplate.class);
    }

    @Given("^Payments to be saved has following details$")
    public void paymentsToBeSavedHasFollowingDetails(DataTable dt) {
        List<List<String>> payments = dt.asLists(String.class);

        List<Payment> paymentList = new ArrayList<>();
        for (int i = 1; i < payments.size(); i++) {
                Payment createdPayment = new Payment();
                createdPayment.setId(Long.valueOf(payments.get(i).get(0)));
                createdPayment.setAmount(new BigDecimal(payments.get(i).get(1)));
                createdPayment.setCurrency(Currency.valueOf(payments.get(i).get(2)));
                createdPayment.setProductName(payments.get(i).get(3));
                paymentList.add(createdPayment);
        }
        givenPayments = paymentList;
    }

    @When("the endpoint to create new payments is hit with the following payment")
    public void theEndpointToCreateNewPaymentsIsHitWithTheFollowingPayment(DataTable dt) throws URISyntaxException {

        List<List<String>> paymentList = dt.asLists(String.class);

        List<Payment> payments = new ArrayList<>();
        for (int i = 1; i < paymentList.size(); i++) {
            Payment createdPayment = new Payment();
            createdPayment.setAmount(new BigDecimal(paymentList.get(i).get(0)));
            createdPayment.setCurrency(Currency.valueOf(paymentList.get(i).get(1)));
            createdPayment.setProductName(paymentList.get(i).get(2));
            payments.add(createdPayment);
        }
        String url = "http://localhost:8080/payment/create";
        RequestEntity<List<Payment>> request = RequestEntity
                .put(new URI(url))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payments);

        this.response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<List<Payment>>() {
                }
        );
    }

    @Then("the response code should be {int}")
    public void theResponseCodeShouldBe(int expectedStatusCode) {
        assertEquals(expectedStatusCode, this.response.getStatusCodeValue());
    }

    @And("the response should contain the following list of payments")
    public void theResponseShouldContainTheFollowingListOfPayments(DataTable dt) {

        List<List<String>> paymentList = dt.asLists(String.class);

        List<Payment> expectedPayments = new ArrayList<>();
        for (int i = 1; i < paymentList.size(); i++) {
            Payment createdPayment = new Payment();
            createdPayment.setId(Long.valueOf(paymentList.get(i).get(0)));
            createdPayment.setAmount(new BigDecimal(paymentList.get(i).get(1)));
            createdPayment.setCurrency(Currency.valueOf(paymentList.get(i).get(2)));
            createdPayment.setProductName(paymentList.get(i).get(3));
            expectedPayments.add(createdPayment);
        }

        List<Payment> actualPayments = this.response.getBody();

        if (CollectionUtils.isNotEmpty(expectedPayments)) {
            assertEquals(expectedPayments.size(), actualPayments.size());
            for (Payment expected : expectedPayments) {
                Payment actual = actualPayments.stream().filter(a -> expected.getId().equals(a.getId())).findFirst().orElse(null);
                assertEquals(expected.getAmount(), actual.getAmount());
                assertEquals(expected.getCurrency(), actual.getCurrency());
                assertEquals(expected.getProductName(), actual.getProductName());
            }
        }

    }

    @When("the endpoint to update payment with id {string} is hit with the following payment")
    public void theEndpointToUpdatePaymentWithIdIsHitWithTheFollowingPayment(String paymentId, DataTable dt) throws URISyntaxException {

        List<List<String>> paymentList = dt.asLists(String.class);

        List<Payment> payments = new ArrayList<>();
        for (int i = 1; i < paymentList.size(); i++) {
            Payment createdPayment = new Payment();
            createdPayment.setAmount(new BigDecimal(paymentList.get(i).get(0)));
            createdPayment.setCurrency(Currency.valueOf(paymentList.get(i).get(1)));
            createdPayment.setProductName(paymentList.get(i).get(2));
            payments.add(createdPayment);
        }

        String url = "http://localhost:8080/payment/update/" + paymentId;

        RequestEntity<List<Payment>> request = RequestEntity
                .post(new URI(url))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payments);

        this.response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<List<Payment>>() {
                }
        );
    }

    @When("the endpoint to delete payment with id {string} is hit")
    public void theEndpointToDeletePaymentWithIdIsHit(String paymentId) throws URISyntaxException {

        String url = "http://localhost:8080/payment/delete/" + paymentId;
        RequestEntity<Void> request = RequestEntity
                .delete(new URI(url))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        this.response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                request,
                new ParameterizedTypeReference<List<Payment>>() {
                }
        );
    }

    @When("the endpoint to get all payments is hit")
    public void theEndpointToGetAllPaymentsIsHit() throws URISyntaxException {
        String url = "http://localhost:8080/payment";
        RequestEntity<Void> request = RequestEntity
                .get(URI.create(url))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        this.response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<Payment>>() {
                }
        );
    }

    @When("the endpoint to get payment with id {string} is hit")
    public void theEndpointToGetPaymentWithIdIsHit(String paymentId) {
        String url = "http://localhost:8080/payment/" + paymentId;
        RequestEntity<Void> request = RequestEntity
                .get(URI.create(url))
                .accept(MediaType.APPLICATION_JSON)
                .build();

        this.response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<Payment>>() {
                }
        );
    }
}
