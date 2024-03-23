package com.project.shopapp.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponse {
    private List<ProductResponse> productResponseList;
    private int pageSize;
    private int pageLimit;
    private int totalPages;
    private long totalElements;
    private boolean isLast;
}
