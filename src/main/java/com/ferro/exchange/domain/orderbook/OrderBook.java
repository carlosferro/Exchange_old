package com.ferro.exchange.domain.orderbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.LinkedList;

@Data
public class OrderBook {
    private LinkedList<PriceLevel> bids;
    private LinkedList<PriceLevel> asks;

    public OrderBook() {
        this.bids = new LinkedList();
        this.asks = new LinkedList();
    }

    public void addBid(Order order) {
        while (asks.size() > 0) {
            if(order.getPrice() < asks.getFirst().getPrice()){
                break;
            }
            order.setVolume(asks.getFirst().make(order.getVolume()));
            if (asks.getFirst().getVolume() == 0) {
                asks.removeFirst();
            }
            if (order.getVolume() == 0) {
                return;
            }
        }
        if (bids.size() == 0) {
            bids.addFirst(new PriceLevel(order));
            return;
        }
        if (order.getPrice() <= bids.getFirst().getPrice()) {
            for (int i = 0; i < bids.size(); i++) {
                if (bids.get(i).getPrice() == order.getPrice()) {
                    bids.get(i).add(order);
                    return;
                }
                if (bids.get(i).getPrice() < order.getPrice()) {
                    bids.add(i, new PriceLevel(order));
                    return;
                }
            }
            bids.addLast(new PriceLevel(order));
        }else {
            bids.addFirst(new PriceLevel(order));
        }
    }

    public void addAsk(Order order) {
        while (bids.size() > 0) {
            if(order.getPrice() > bids.getFirst().getPrice()){
                break;
            }
            order.setVolume(bids.getFirst().make(order.getVolume()));
            if (bids.getFirst().getVolume() == 0) {
                bids.removeFirst();
            }
            if (order.getVolume() == 0) {
                return;
            }
        }
        if (asks.size() == 0) {
            asks.addFirst(new PriceLevel(order));
            return;
        }
        if (order.getPrice() >= asks.getFirst().getPrice()) {
            for (int i = 0; i < asks.size(); i++) {
                if (asks.get(i).getPrice() == order.getPrice()) {
                    asks.get(i).add(order);
                    return;
                }
                if (asks.get(i).getPrice() > order.getPrice()) {
                    asks.add(i, new PriceLevel(order));
                    return;
                }
            }
            asks.addLast(new PriceLevel(order));
        }else {
            asks.addFirst(new PriceLevel(order));
        }
    }

    public boolean deleteBid(Order order) {
        for (int i = 0; i < bids.size(); i++) {
            if (bids.get(i).getPrice() == order.getPrice()) {
                if (bids.get(i).delete(order) == 0) {
                    bids.remove(i);
                }
                return true;
            }
        }
        return false;
    }

    public boolean deleteAsk(Order order) {
        for (int i = 0; i < asks.size(); i++) {
            if (asks.get(i).getPrice() == order.getPrice()) {
                if (asks.get(i).delete(order) == 0) {
                    asks.remove(i);
                }
                return true;
            }
        }
        return false;
    }

}
