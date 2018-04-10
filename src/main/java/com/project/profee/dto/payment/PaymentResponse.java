package com.project.profee.dto.payment;

import com.project.profee.entity.Payment;

public class PaymentResponse {

    private Payment payment;
    private String id;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
