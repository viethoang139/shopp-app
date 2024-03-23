package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDto;
import com.project.shopapp.dtos.ProductImageDto;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    Product createProduct(ProductDto productDto);

    ProductResponse getProductById(Long id);

    ProductListResponse getAllProducts(int pageSize, int pageLimit, String sortBy, String sortDir);

    ProductResponse updateProduct(Long id, ProductDto productDto);

    void deleteProduct(long id);

    boolean existsByName(String name);
    ProductImage createProductImage(Long productId, ProductImageDto productImageDto);

    List<ProductImage> uploadImage(Long id, List<MultipartFile> files) throws IOException;
    String generateFakeProductData();


}
