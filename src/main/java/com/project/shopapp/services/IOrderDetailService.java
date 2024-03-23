package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDto;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.responses.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {

    OrderDetailResponse createOrderDetail(OrderDetailDto orderDetailDto);

    OrderDetailResponse getOrderDetail(Long id);

    OrderDetailResponse updateOrderDetail(Long id, OrderDetailDto orderDetailDto);

    void deleteOrderDetail(Long id);

    List<OrderDetailResponse> getAllOrderDetails(Long orderId);

}
