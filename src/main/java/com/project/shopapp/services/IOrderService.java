package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDto;
import com.project.shopapp.responses.OrderResponse;

import java.util.List;

public interface IOrderService {

    OrderResponse createOrder(OrderDto orderDto);

    OrderResponse getOrder(Long id);

    OrderResponse updateOrder(Long id, OrderDto orderDto);

    void deleteOrder(Long id);

    List<OrderResponse> getAllOrders(Long userId);



}
