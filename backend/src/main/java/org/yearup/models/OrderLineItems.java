package org.yearup.models;


import jakarta.persistence.*;

/**
 * Entity class that represents a customer's order after checkout
 */
@Entity
@Table(name = "order_line_items")
public class OrderLineItems {

    @Id
    @Column(name = "order_line_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderLineItemId;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "sales_price")
    private double salesPrice;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "discount")
    private double discount;

    public OrderLineItems(int orderId, int productId, double salesPrice, int quantity, double discount) {
        this.orderId = orderId;
        this.productId = productId;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public OrderLineItems() {

    }

    public int getOrder() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProduct() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
