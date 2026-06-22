package org.yearup.models;


import jakarta.persistence.*;

@Entity
@Table(name = "order_line_items")
public class OrderLineItem {

    @Id
    @Column(name = "order_line_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderLineItemId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "sales_price")
    private double salesPrice;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "discount")
    private double discount;
}
