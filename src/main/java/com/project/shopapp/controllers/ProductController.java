package com.project.shopapp.controllers;

import com.project.shopapp.dtos.ProductDto;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.services.IProductService;
import com.project.shopapp.utils.AppConstraints;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDto productDto) {
            Product product = productService.createProduct(productDto);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value ="uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ProductImage>> uploadImages( @PathVariable Long id,
            @ModelAttribute("files") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok(productService.uploadImage(id,files));
    }



    @GetMapping
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(value = "page_size", required = false, defaultValue = AppConstraints.PAGE_SIZE) int pageSize,
            @RequestParam(value = "page_limit", required = false, defaultValue = AppConstraints.PAGE_LIMIT) int pageLimit,
            @RequestParam(value = "sort_by", required = false, defaultValue = AppConstraints.SORT_BY) String sortBy,
            @RequestParam(value = "sort_dir",required = false, defaultValue = AppConstraints.SORT_DIR) String sortDir
    ) {

        return ResponseEntity.ok(productService.getAllProducts(pageSize, pageLimit, sortBy, sortDir));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable("id") Long productId, @Valid @RequestBody ProductDto productDto){
        return ResponseEntity.ok(productService.updateProduct(productId,productDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(String.format("Product with id %d deleted successfully",productId));
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts(){
        return ResponseEntity.ok(productService.generateFakeProductData());
    }


}
