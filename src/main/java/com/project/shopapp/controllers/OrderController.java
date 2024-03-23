package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDto;
import com.project.shopapp.responses.OrderResponse;
import com.project.shopapp.services.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderDto orderDto){
        return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
    }

    @GetMapping("{order_id}")
    public ResponseEntity<OrderResponse> getOrderById(@Valid @PathVariable("order_id") Long orderId){
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping("users/{user_id}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@Valid @PathVariable("user_id") Long userId){
        return ResponseEntity.ok(orderService.getAllOrders(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@Valid @PathVariable Long id,
                                         @Valid @RequestBody OrderDto orderDto){
        return ResponseEntity.ok(orderService.updateOrder(id, orderDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderById(@Valid @PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }




}
