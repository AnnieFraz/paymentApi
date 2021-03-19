package com.annarasburn.paymentapi;

import com.annarasburn.paymentapi.dto.Currency;
import com.annarasburn.paymentapi.dto.Payment;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:Feature")
public class PaymentCrud {

    private DataTable expectedInitialPayments;

    private HttpResponse response;

    WireMockServer wireMockServer = new WireMockServer(options().port(9090));

    CloseableHttpClient httpClient = HttpClients.createDefault();

    @Before("@PaymentCRUD")
    public void setup() {

    }

    @Given("^Payments to be saved has following details$")
    public void paymentsToBeSavedHasFollowingDetails(DataTable dt) {
        List<Payment> originalPayments = getPayments(dt);
    }


    @When("the endpoint to create new payments is hit with the following payment")
    public void theEndpointToCreateNewPaymentsIsHitWithTheFollowingPayment(DataTable dt) throws URISyntaxException, IOException {

        List<Payment> payments = getPayments(dt);

        wireMockServer.start();
        configureFor("localhost", 9090);
        stubFor(put(urlEqualTo("/payment/create"))
                .willReturn(aResponse().withBody(getDummyAddedPayments().toString()).withStatus(200)));

        HttpPut request = new HttpPut("http://localhost:9090/payment/create");
        StringEntity entity = new StringEntity(payments.toString());
        request.addHeader("content-type", "application/json");
        request.setEntity(entity);
        this.response = httpClient.execute(request);

    }

    @Then("the response code should be {int}")
    public void theResponseCodeShouldBe(int expectedStatusCode) throws IOException {
        assertEquals(expectedStatusCode, this.response.getStatusLine().getStatusCode());
    }

    @And("the response should contain the following list of payments")
    public void theResponseShouldContainTheFollowingListOfPayments(DataTable dt) throws IOException {

        List<Payment> payments = getPayments(dt);

        String entityResponse = null;
        HttpEntity responseEntity = this.response.getEntity();
        if(responseEntity!=null) {
            entityResponse = EntityUtils.toString(responseEntity);
        }
        assertNotNull(entityResponse);
        assertTrue(entityResponse.contains("Payment"));
        assertEquals(payments.toString(), entityResponse);

        wireMockServer.stop();

    }

    @When("the endpoint to update payment with id {string} is hit with the following payment")
    public void theEndpointToUpdatePaymentWithIdIsHitWithTheFollowingPayment(String paymentId, DataTable dt) throws URISyntaxException, IOException {

        List<Payment> payments = getPayments(dt);

        wireMockServer.start();
        configureFor("localhost", 9090);
        stubFor(post(urlEqualTo("/payment/update" + paymentId))
                .willReturn(aResponse().withBody(getDummyUpdatedPayments().toString()).withStatus(200)));

        HttpPost request = new HttpPost("http://localhost:9090/payment/update" + paymentId);
        StringEntity entity = new StringEntity(payments.toString());
        request.addHeader("content-type", "application/json");
        request.setEntity(entity);
        this.response = httpClient.execute(request);
    }

    @When("the endpoint to delete payment with id {string} is hit")
    public void theEndpointToDeletePaymentWithIdIsHit(String paymentId) throws URISyntaxException, IOException {

        wireMockServer.start();
        configureFor("localhost", 9090);
        stubFor(delete(urlEqualTo("/payment/delete"))
                .willReturn(aResponse().withStatus(200)));

        HttpDelete request = new HttpDelete("http://localhost:9090/payment/delete");
        this.response = httpClient.execute(request);
    }

    @When("the endpoint to get all payments is hit")
    public void theEndpointToGetAllPaymentsIsHit() throws IOException {
        wireMockServer.start();
        configureFor("localhost", 9090);
        stubFor(get(urlEqualTo("/payment/all"))
                .willReturn(aResponse().withBody(getAllDummyPayments().toString()).withStatus(200)));

        HttpGet request = new HttpGet("http://localhost:9090/payment/all");
        this.response = httpClient.execute(request);
    }


    @When("the endpoint to get payment with id {string} is hit")
    public void theEndpointToGetPaymentWithIdIsHit(String paymentId) throws IOException {
        wireMockServer.start();
        configureFor("localhost", 9090);
        stubFor(get(urlEqualTo("/payment/" + paymentId))
                .willReturn(aResponse().withBody(getSingularDummyPayment().toString()).withStatus(200)));

        HttpGet request = new HttpGet("http://localhost:9090/payment/" + paymentId);
        this.response = httpClient.execute(request);
    }

    private List<Payment> getPayments(DataTable dt) {
        List<List<String>> paymentList = dt.asLists(String.class);


        List<Payment> payments = new ArrayList<>();
        for (int i = 1; i < paymentList.size(); i++) {
            Payment createdPayment = new Payment();
            if (0 != Integer.valueOf(paymentList.get(i).get(0))) {
                createdPayment.setId(new Integer(paymentList.get(i).get(0)));
            }
            createdPayment.setAmount(new BigDecimal(paymentList.get(i).get(1)));
            createdPayment.setCurrency(Currency.valueOf(paymentList.get(i).get(2)));
            createdPayment.setProductName(paymentList.get(i).get(3));
            payments.add(createdPayment);
        }
        return payments;
    }

    private List<Payment> getAllDummyPayments() {
        Payment payment1 = new Payment(1, new BigDecimal(2000), Currency.GBP, "Mobile");
        Payment payment2 = new Payment(2, new BigDecimal(3000), Currency.USD, "TV");

        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment1);
        paymentList.add(payment2);
        return paymentList;
    }

    private List<Payment> getSingularDummyPayment() {
        Payment payment1 = new Payment(1, new BigDecimal(2000), Currency.GBP, "Mobile");
        return Collections.singletonList(payment1);
    }

    private List<Payment> getDummyAddedPayments() {
        Payment payment1 = new Payment(1, new BigDecimal(2000), Currency.GBP, "Mobile");
        Payment payment2 = new Payment(2, new BigDecimal(3000), Currency.USD, "TV");
        Payment payment3 = new Payment(3, new BigDecimal(1000), Currency.EUR, "Laptop");

        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment1);
        paymentList.add(payment2);
        paymentList.add(payment3);
        return paymentList;
    }

    private List<Payment> getDummyUpdatedPayments() {
        Payment payment1 = new Payment(1, new BigDecimal(1000), Currency.EUR, "Laptop");
        Payment payment2 = new Payment(2, new BigDecimal(3000), Currency.USD, "TV");

        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(payment1);
        paymentList.add(payment2);
        return paymentList;
    }

}
