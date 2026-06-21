package org.yearup.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "shipping_amount")
    private BigDecimal shippingAmount;

    @Column(name = "date")
    private LocalDateTime date;

    public Order() {
    }

    public int getOrderId() {
        return orderId;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }
}
