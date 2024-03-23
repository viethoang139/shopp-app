package com.project.shopapp.services.impl;

import com.github.javafaker.Faker;
import com.project.shopapp.dtos.ProductDto;
import com.project.shopapp.dtos.ProductImageDto;
import com.project.shopapp.exception.ImageException;
import com.project.shopapp.exception.ResourceNotFoundException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.services.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDto productDto) {
        Category existingCategory = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category","ID",productDto.getCategoryId().toString()));

        Product product = Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .category(existingCategory)
                .build();
        return productRepository.save(product);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product =  productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product","ID",id.toString()));
        return ProductResponse.mapToProductResponse(product);
    }

    @Override
    public ProductListResponse getAllProducts(int pageSize, int pageLimit, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name())? Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageSize,pageLimit,sort);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<Product> products = productPage.getContent();

        List<ProductResponse> productResponse = products.stream().map(ProductResponse::mapToProductResponse)
                .collect(Collectors.toList());

        ProductListResponse productListResponse = new ProductListResponse();

        productListResponse.setProductResponseList(productResponse);

        productListResponse.setPageSize(pageSize);
        productListResponse.setPageLimit(pageLimit);
        productListResponse.setTotalPages(productPage.getTotalPages());
        productListResponse.setTotalElements(productPage.getTotalElements());
        productListResponse.setLast(productPage.isLast());

        return productListResponse;
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductDto productDto) {
        ProductResponse existingProduct = getProductById(id);
        Category existingCategory = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category","ID",productDto.getCategoryId().toString()));
        existingProduct.setName(productDto.getName());

        existingProduct.setCategoryId(existingCategory.getId());

        existingCategory.setId(existingProduct.getCategoryId());

        existingProduct.setPrice(productDto.getPrice());

        existingProduct.setDescription(productDto.getDescription());

        Product updatedProduct = new Product();
        updatedProduct.setId(existingProduct.getId());
        updatedProduct.setName(existingProduct.getName());
        updatedProduct.setPrice(existingProduct.getPrice());
        updatedProduct.setDescription(existingProduct.getDescription());
        updatedProduct.setCategory(existingCategory);
        updatedProduct.setCreatedAt(existingProduct.getCreatedAt());
        updatedProduct.setUpdatedAt(existingProduct.getUpdatedAt());
        productRepository.save(updatedProduct);
        return existingProduct;
    }

    @Override
    public void deleteProduct(long id) {
        getProductById(id);
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
    @Override
    public List<ProductImage> uploadImage(Long id, List<MultipartFile> files) throws IOException {
        ProductResponse existingProduct = getProductById(id);
        List<ProductImage> productImages = new ArrayList<>();
        files = files == null ? new ArrayList<>(): files;
        if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
           throw new ImageException("You can only upload maximum 5 images");
        }
        for(MultipartFile file : files){
            if(file.getSize() == 0){
                continue;
            }
            if(file.getSize() > 10 * 1024 * 1024){ // kick thuoc > 10 MB
                throw new ImageException("File is too large");
            }
            String contentType = file.getContentType();
            if(contentType == null || !contentType.startsWith("image/")){
                throw new ImageException("File must be image");
            }
            String fileName = storeFile(file);
            ProductImage productImage = createProductImage(existingProduct.getId(), ProductImageDto.builder()
                    .imageUrl(fileName).build());
            productImages.add(productImage);
        }
        return productImages;
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDto productImageDto){
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","ID",productId.toString()));
        ProductImage productImage = ProductImage
                .builder().product(existingProduct)
                .imageUrl(productImageDto.getImageUrl())
                .build();
        int size = productImageRepository.findByProductId(productId).size();
        if(size >=  ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new RuntimeException("Number of images must be <= 5");
        }
        return productImageRepository.save(productImage);
    }

    private String storeFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if(contentType == null || !contentType.startsWith("image/") || file.getOriginalFilename() == null){
            throw new IOException("Invalid image format");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Them UUID vao truoc ten file de dam bap ten file la duy nhat
        String uniqueFilename = UUID.randomUUID() + "_" + fileName;
        // Duong dan den thu muc ma ban muon luu file
        java.nio.file.Path uploadDir =  Paths.get("uploads");
        // Kiem tra va tao thu muc neu no khong ton tai
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        // Duong dan day du den file
        Path destination = Paths.get(uploadDir.toString(),uniqueFilename);
        // Sao chep file vao thu muc dich
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }


    @Override
    public String generateFakeProductData() {
        Faker faker = new Faker();
        for(int i =0; i < 1_000_000; i++){
            String productName = faker.commerce().productName();
            if(productRepository.existsByName(productName)){
                continue;
            }
            ProductDto productDto = ProductDto.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10,90_000_000))
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(2,5))
                    .build();
            createProduct(productDto);
        }
        return "Fake data successfully";
    }
}
