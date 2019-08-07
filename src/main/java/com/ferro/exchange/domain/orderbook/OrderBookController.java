package com.ferro.exchange.domain.orderbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderBookController {

    OrderBook orderBook;

    public OrderBookController() {
        this.orderBook = new OrderBook();
    }

    @RequestMapping("/order")
    public String newOrder(@RequestParam(value = "price") long price,
                             @RequestParam(value = "volume") long volume,
                             @RequestParam(value = "side") String side) {
        Order order = new Order(price, volume);
        if(side.equals("ask")){
            this.orderBook.addAsk(order);
        } else {
            this.orderBook.addBid(order);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = null;
        try {
            jsonResult = mapper.writeValueAsString(orderBook);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

}
