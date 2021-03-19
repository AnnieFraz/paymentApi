package com.annarasburn.paymentapi.endpoints;

import com.annarasburn.paymentapi.dao.PaymentDao;
import com.annarasburn.paymentapi.dto.Payment;
import com.annarasburn.paymentapi.services.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/payment")
@Api(tags = "Payment", description = "Payment CRUD APIs")
public class PaymentEndpoint {

    private final PaymentDao paymentDao;

    private final PaymentService paymentService;

    @Autowired
    public PaymentEndpoint(final PaymentDao paymentDao, final PaymentService paymentService
    ) {
        this.paymentDao = paymentDao;
        this.paymentService = paymentService;
    }

    @GetMapping("/all")
    @ApiOperation(
            value = "Get Payments",
            response = Payment.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched payments"),
            @ApiResponse(code = 400, message = "Unable to handle request"),
            @ApiResponse(code = 500, message = "Server Error")})
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    @ApiOperation(
            value = "Get Payment by Id",
            response = Payment.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched payments"),
            @ApiResponse(code = 400, message = "Unable to handle request"),
            @ApiResponse(code = 500, message = "Server Error")})
    public Payment getPaymentById(
            @PathVariable("id") final int id
    ) throws Exception {
        return paymentService.getPaymentById(id);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(
            value = "Get Given Payment"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete Payment"),
            @ApiResponse(code = 400, message = "Unable to handle request"),
            @ApiResponse(code = 500, message = "Server Error")})
    public void deletePayment(
             @PathVariable("id") final int id
    ) throws Exception {
        paymentService.deletePayment(id);
    }

    @PutMapping("/create")
    @ApiOperation(
            value = "Create Payment",
            response = Payment.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully create payments"),
            @ApiResponse(code = 400, message = "Unable to handle request"),
            @ApiResponse(code = 500, message = "Server Error")})
    public List<Payment> createPayment(
             @RequestBody List<Payment> paymentList
    ) throws Exception {
        return paymentService.addPayments(paymentList);

    }

    @PostMapping("/update/{id}")
    @ApiOperation(
            value = "Update Payment",
            response = Payment.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated payments"),
            @ApiResponse(code = 400, message = "Unable to handle request"),
            @ApiResponse(code = 500, message = "Server Error")})
    public Payment updatePayment(
            @PathVariable("id") final int id,
            @RequestBody Payment payment
    ) throws Exception {
        return paymentService.updatePayment(id, payment);
    }

}
