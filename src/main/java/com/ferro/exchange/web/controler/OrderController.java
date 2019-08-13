package com.ferro.exchange.web.controler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferro.exchange.domain.orderbook.Order;
import com.ferro.exchange.domain.orderbook.OrderBook;
import com.ferro.exchange.domain.orderbook.OrderSide;
import com.ferro.exchange.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.ferro.exchange.domain.orderbook.OrderSide.ASK;
import static com.ferro.exchange.domain.orderbook.OrderSide.BID;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.badRequest;

@CrossOrigin
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    OrderBook orderBook;

    @Autowired
    private OrderRepository orders;

    public OrderController() {
        this.orderBook = new OrderBook();
    }

    @PostMapping("/create")
    public ResponseEntity newOrder(@RequestBody OrderForm orderForm) {
        if (orderForm.getSide().equals("ask")) {
            Order saved = this.orders.save(Order.builder()
                    .price(orderForm.getPrice())
                    .volume(orderForm.getVolume())
                    .side(ASK).build());
            this.orderBook.addAsk(saved);
        } else if(orderForm.getSide().equals("bid")){
            Order saved = this.orders.save(Order.builder()
                    .price(orderForm.getPrice())
                    .volume(orderForm.getVolume())
                    .side(BID).build());
            this.orderBook.addBid(saved);
        }else {
            return badRequest().build();
        }
        System.out.println(orderBook.toString());
        return ok(orderBook);
    }

    @GetMapping("/orderbook")
    public ResponseEntity orderBook(){
        return ok(orderBook);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class OrderForm {
        private long price;
        private long volume;
        private String side;
    }
}
