package com.project.profee.service;

import com.project.profee.dto.payment.PaymentRequest;
import com.project.profee.dto.payment.PaymentResponse;

public interface IPaymentService {

    PaymentResponse create(String id, PaymentRequest paymentRequest);

    PaymentResponse update(String id, PaymentRequest paymentRequest);
}
