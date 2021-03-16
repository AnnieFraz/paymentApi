package com.annarasburn.paymentapi.endpoints;


import com.annarasburn.paymentapi.dto.Payment;
import com.annarasburn.paymentapi.services.PaymentService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.util.List;

@RestController()
@RequestMapping("/payment")
@Api(tags = "Payment", description = "Payment CRUD APIs")
public class PaymentEndpoint {

    private final PaymentService paymentService;

    @Autowired
    public PaymentEndpoint(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/")
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
            @ApiParam(value = "Payment Id", required = true) @PathVariable("id") final long id
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
            @ApiParam(value = "Payment Id", required = true) @PathVariable("id") final Long id
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
            @NotNull @RequestBody List<Payment> paymentList
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
            @ApiParam(value = "Payment Id", required = true) @PathVariable("id") final Long id,
            @NotNull @RequestBody Payment payment
    ) throws Exception {
        return paymentService.updatePayment(id, payment);
    }

}
