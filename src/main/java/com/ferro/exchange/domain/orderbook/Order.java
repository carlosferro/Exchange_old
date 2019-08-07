package com.ferro.exchange.domain.orderbook;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
//@Entity
public class Order {
    @Id
    @GeneratedValue
    private long id;
    private long price;
    private long volume;
    private OrderSide side;
//    private User user;

    protected Order() {
    }

    public Order(long price, long volume) {
        this.price = price;
        this.volume = volume;
//        this.user = user;
    }
}
