package com.example.AmezonPayment.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @GetMapping(value = "/payment")
    public String GetPayment(){
        return "Payment completed";
    }
}
