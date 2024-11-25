package tn.soa_ecommerce.order.model;

public enum OrderStatus {
    CREATED,
    RESERVED,
    PAID,
    SHIPPING_SCHEDULED,
    COMPLETED,
    CANCELED,
    FAILED
}