package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.shopapp.models.Product;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends BaseResponse{

    private Long id;
    private String name;
    private Float price;
    private String description;
    @JsonProperty("category_id")
    private Long categoryId;


    public static ProductResponse mapToProductResponse(Product product){
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setPrice(product.getPrice());
        productResponse.setDescription(product.getDescription());
        productResponse.setCategoryId(product.getCategory().getId());
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }



}
