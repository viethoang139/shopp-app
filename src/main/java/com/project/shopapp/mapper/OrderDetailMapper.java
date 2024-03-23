package com.project.shopapp.mapper;

import com.project.shopapp.dtos.OrderDetailDto;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.responses.OrderDetailResponse;

public class OrderDetailMapper {

    public static OrderDetail mapToOrderDetail(OrderDetailDto orderDetailDto){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setPrice(orderDetailDto.getPrice());
        orderDetail.setNumberOfProducts(orderDetailDto.getNumberOfProducts());
        orderDetail.setTotalMoney(orderDetailDto.getTotalMoney());
        orderDetail.setColor(orderDetailDto.getColor());
        return orderDetail;
    }

    public static OrderDetailResponse mapToOrderDetailResponse(OrderDetail orderDetail){
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        orderDetailResponse.setProductId(orderDetail.getProduct().getId());
        orderDetailResponse.setOrderId(orderDetail.getOrder().getId());
        orderDetailResponse.setPrice(orderDetail.getPrice());
        orderDetailResponse.setNumberOfProducts(orderDetail.getNumberOfProducts());
        orderDetailResponse.setTotalMoney(orderDetail.getTotalMoney());
        orderDetailResponse.setColor(orderDetail.getColor());
        return orderDetailResponse;
    }


    public static OrderDetailDto mapToOrderDetailDto(OrderDetail orderDetail){
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setPrice(orderDetail.getPrice());
        orderDetailDto.setNumberOfProducts(orderDetail.getNumberOfProducts());
        orderDetailDto.setTotalMoney(orderDetail.getTotalMoney());
        orderDetailDto.setColor(orderDetail.getColor());
        return orderDetailDto;
    }


}
