package com.ferro.exchange.repository;

import com.ferro.exchange.domain.orderbook.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "orders", collectionResourceRel = "orders", itemResourceRel = "order")
public interface OrderRepository extends JpaRepository<Order, Long> {
}