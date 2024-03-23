package com.project.shopapp.services.impl;

import com.project.shopapp.dtos.OrderDto;
import com.project.shopapp.exception.OrderException;
import com.project.shopapp.exception.ResourceNotFoundException;
import com.project.shopapp.mapper.OrderMapper;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.UserRepository;
import com.project.shopapp.responses.OrderResponse;
import com.project.shopapp.services.IOrderService;
import com.project.shopapp.utils.AppConstraints;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;
    @Override
    public OrderResponse createOrder(OrderDto orderDto) {
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User","userId",orderDto.getUserId().toString()));

        Order order = OrderMapper.mapToOrder(orderDto);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(AppConstraints.PENDING);

        LocalDate shippingDate = orderDto.getShippingDate() == null ? LocalDate.now() : orderDto.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())){
            throw new OrderException("Date must be at least today !");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);

        orderRepository.save(order);

        return OrderMapper.mapToOrderResponse(order);
    }

    @Override
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order","ID",id.toString()));
        return OrderMapper.mapToOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders(Long userId) {
       userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","ID",userId.toString()));

        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream().map(order -> OrderMapper.mapToOrderResponse(order))
                .collect(Collectors.toList());
    }
    @Override
    public OrderResponse updateOrder(Long id, OrderDto orderDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order","ID",id.toString()));
        order.setFullName(orderDto.getFullName());
        order.setEmail(orderDto.getEmail());
        order.setPhoneNumber(orderDto.getPhoneNumber());
        order.setAddress(orderDto.getAddress());
        order.setNote(orderDto.getNote());
        order.setTotalMoney(orderDto.getTotalMoney());
        order.setShippingMethod(orderDto.getShippingMethod());
        order.setSippingAddress(orderDto.getShippingAddress());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        Order updatedOrder = orderRepository.save(order);
        return OrderMapper.mapToOrderResponse(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id.toString()));

        order.setActive(false);
        orderRepository.save(order);

    }

}
