package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDetailDto;
import com.project.shopapp.responses.OrderDetailResponse;
import com.project.shopapp.services.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<OrderDetailResponse> createOrderDetail(@Valid @RequestBody OrderDetailDto orderDetailDto){
        return new ResponseEntity<>(orderDetailService.createOrderDetail(orderDetailDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(@Valid @PathVariable Long id){
        return ResponseEntity.ok(orderDetailService.getOrderDetail(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailResponse>> getOrderDetails(@Valid @PathVariable Long orderId){
        return ResponseEntity.ok(orderDetailService.getAllOrderDetails(orderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable Long id,
                                               @RequestBody OrderDetailDto orderDetailDto){
        return ResponseEntity.ok(orderDetailService.updateOrderDetail(id, orderDetailDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetails(@Valid @PathVariable Long id){
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("Delete order detail successfully");
    }
}
