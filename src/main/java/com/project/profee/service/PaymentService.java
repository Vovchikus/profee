package com.project.profee.service;

import com.project.profee.dto.payment.PaymentRequest;
import com.project.profee.dto.payment.PaymentResponse;
import com.project.profee.entity.Payment;
import com.project.profee.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService implements IPaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponse create(String id, PaymentRequest paymentRequest) {
        Payment payment = paymentRepository.create(
                id,
                paymentRequest.getFrom(),
                paymentRequest.getTo(),
                paymentRequest.getAmount()
        );
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPayment(payment);
        paymentResponse.setId(id);
        return paymentResponse;
    }

    @Override
    public PaymentResponse update(String id, PaymentRequest paymentRequest) {
        return create(id, paymentRequest);
    }
}
