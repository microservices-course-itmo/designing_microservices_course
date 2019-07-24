package com.microservices.accounting.dto;

public enum PaymentStatus {
    ACCEPTED,
    DENIED,
    /**
     * Used to indicate request should be repeated
     */
    RETRY
}
