package com.project.shopapp.mapper;

import com.project.shopapp.dtos.OrderDto;
import com.project.shopapp.models.Order;
import com.project.shopapp.responses.OrderResponse;

public class OrderMapper {

    public static OrderDto mapToOrderDto(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(order.getUser().getId());
        orderDto.setFullName(order.getFullName());
        orderDto.setEmail(order.getEmail());
        orderDto.setPhoneNumber(order.getPhoneNumber());
        orderDto.setAddress(order.getAddress());
        orderDto.setNote(order.getNote());
        orderDto.setTotalMoney(order.getTotalMoney());
        orderDto.setShippingMethod(order.getShippingMethod());
        orderDto.setShippingAddress(order.getSippingAddress());
        orderDto.setPaymentMethod(order.getPaymentMethod());
        return orderDto;
    }

    public static OrderResponse mapToOrderResponse(Order order){
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setUserId(order.getUser().getId());
        orderResponse.setEmail(order.getEmail());
        orderResponse.setFullName(order.getFullName());
        orderResponse.setPhoneNumber(order.getPhoneNumber());
        orderResponse.setAddress(order.getAddress());
        orderResponse.setNote(order.getNote());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setTotalMoney(order.getTotalMoney());
        orderResponse.setShippingMethod(order.getShippingMethod());
        orderResponse.setShippingAddress(order.getSippingAddress());
        orderResponse.setShippingDate(order.getShippingDate());
        orderResponse.setTrackingNumber(order.getTrackingNumber());
        orderResponse.setPaymentMethod(order.getPaymentMethod());
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setActive(order.getActive());
        return orderResponse;
    }

    public static Order mapToOrder(OrderDto orderDto){
        Order order = new Order();
        order.setFullName(orderDto.getFullName());
        order.setEmail(orderDto.getEmail());
        order.setPhoneNumber(orderDto.getPhoneNumber());
        order.setAddress(orderDto.getAddress());
        order.setNote(orderDto.getNote());
        order.setTotalMoney(orderDto.getTotalMoney());
        order.setShippingMethod(orderDto.getShippingMethod());
        order.setSippingAddress(orderDto.getShippingAddress());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        return order;
    }




}
