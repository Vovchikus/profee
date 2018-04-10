package com.project.profee.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment {

    private Long from;

    private Long to;

    private BigDecimal amount;

    private LocalDate created;

    public Payment(Long from, Long to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.setCreated(LocalDate.now());
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getCreated() {
        return created;
    }

    private void setCreated(LocalDate created) {
        this.created = created;
    }

}
