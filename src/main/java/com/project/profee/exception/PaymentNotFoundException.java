package com.project.profee.exception;

import org.springframework.web.client.RestClientException;

public class PaymentNotFoundException extends RestClientException {

    public PaymentNotFoundException(String id) {
        super(getMessage(id));
    }

    private static String getMessage(String id) {
        return String.format("Payment not found with id - %s", id);
    }

}
