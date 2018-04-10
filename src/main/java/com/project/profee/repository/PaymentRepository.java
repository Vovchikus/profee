package com.project.profee.repository;

import com.project.profee.entity.Payment;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;

@Repository
public class PaymentRepository {

    private HashMap<String, Payment> temporaryPaymentStorage = new HashMap<>();

    public Payment create(String id, Long from, Long to, BigDecimal amount) {
        Payment payment = new Payment(from, to, amount);
        temporaryPaymentStorage.put(id, payment);
        return payment;
    }

    public Payment getById(String id) {
        return temporaryPaymentStorage.get(id);
    }

    public HashMap<String, Payment> getAll() {
        return temporaryPaymentStorage;
    }

    public void deleteById(String id) {
        temporaryPaymentStorage.remove(id);
    }

}
