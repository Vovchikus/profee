package com.project.profee.controller;

import com.project.profee.dto.payment.PaymentResponse;
import com.project.profee.exception.PaymentNotFoundException;
import com.project.profee.dto.payment.PaymentRequest;
import com.project.profee.entity.Payment;
import com.project.profee.repository.PaymentRepository;
import com.project.profee.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PaymentResponse create(@Valid @RequestBody PaymentRequest paymentRequest) {
        return paymentService.create(UUID.randomUUID().toString(), paymentRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Payment getOne(@PathVariable("id") String id) throws PaymentNotFoundException {
        return Optional
                .ofNullable(paymentRepository.getById(id))
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public HashMap<String, Payment> getAll() {
        return paymentRepository.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public PaymentResponse updateOne(@Valid @RequestBody PaymentRequest paymentRequest, @PathVariable("id") String id) {
        Payment payment = Optional
                .ofNullable(paymentRepository.getById(id))
                .orElseThrow(() -> new PaymentNotFoundException(id));
        return paymentService.update(id, paymentRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable("id") String id) {
        Payment payment = Optional
                .ofNullable(paymentRepository.getById(id))
                .orElseThrow(() -> new PaymentNotFoundException(id));
        paymentRepository.deleteById(id);
    }
}
