package com.project.shopapp.services.impl;

import com.project.shopapp.dtos.OrderDetailDto;
import com.project.shopapp.exception.ResourceNotFoundException;
import com.project.shopapp.mapper.OrderDetailMapper;
import com.project.shopapp.mapper.OrderMapper;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.OrderDetailResponse;
import com.project.shopapp.services.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements IOrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailDto orderDetailDto) {
        Order order = orderRepository.findById(orderDetailDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order","ID",orderDetailDto.getOrderId().toString()));

        Product product = productRepository.findById(orderDetailDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product","ID",orderDetailDto.getProductId().toString()));

        OrderDetail orderDetail = OrderDetailMapper.mapToOrderDetail(orderDetailDto);
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);

        orderDetailRepository.save(orderDetail);

       return OrderDetailMapper.mapToOrderDetailResponse(orderDetail);
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long id) {
        OrderDetail orderDetail =
                orderDetailRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("OrderDetail","ID",id.toString()));

        return OrderDetailMapper.mapToOrderDetailResponse(orderDetail);
    }

    @Override
    public OrderDetailResponse updateOrderDetail(Long id, OrderDetailDto orderDetailDto)
    {

        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail", "ID",id.toString()));

        Order order = orderRepository.findById(orderDetailDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order","ID",orderDetailDto.getOrderId().toString()));

        Product product = productRepository.findById(orderDetailDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product","ID",orderDetailDto.getProductId().toString()));

        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setPrice(orderDetailDto.getPrice());
        orderDetail.setNumberOfProducts(orderDetailDto.getNumberOfProducts());
        orderDetail.setTotalMoney(orderDetailDto.getTotalMoney());
        orderDetail.setColor(orderDetailDto.getColor());

        OrderDetail updatedOrderDetail = orderDetailRepository.save(orderDetail);

        return OrderDetailMapper.mapToOrderDetailResponse(updatedOrderDetail);

    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail","ID",id.toString()));
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetailResponse> getAllOrderDetails(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        return orderDetails.stream().map(orderDetail -> OrderDetailMapper.mapToOrderDetailResponse(orderDetail))
                .collect(Collectors.toList());
    }
}
