package com.ferro.exchange.domain.orderbook;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.util.LinkedList;

@Data
public class PriceLevel {

    private final long price;
    private long volume;
//    @JsonIgnore
    private final LinkedList<Order> orders;

    protected PriceLevel(long price) {
        this.price = price;
        this.orders = new LinkedList();
    }

    public PriceLevel(Order order) {
        this.price = order.getPrice();
        this.volume = order.getVolume();
        this.orders = new LinkedList();
        this.orders.add(order);
    }

    public void add(Order order) {
        this.volume += order.getVolume();
        orders.addLast(order);
    }

    public long delete(Order order) {
        orders.remove(order);
        return this.volume -= order.getVolume();
    }

    public long make(long volume) {
        while (volume > 0l && orders.size() > 0) {
            Order firstOrder = orders.getFirst();
            if (volume > firstOrder.getVolume()) {
                volume -= firstOrder.getVolume();
                this.volume -= firstOrder.getVolume();
                orders.removeFirst();
            } else {
                this.volume -= volume;
                firstOrder.setVolume(firstOrder.getVolume() - volume);
                return 0l;
            }
        }
        return volume;
    }

}
