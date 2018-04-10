package com.project.profee.dto.payment;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PaymentRequest {

    @NotNull
    private Long from;

    @NotNull
    private Long to;

    @NotNull
    @Min(value = 0)
    @Digits(integer = 6, fraction = 2)
    private BigDecimal amount;

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
