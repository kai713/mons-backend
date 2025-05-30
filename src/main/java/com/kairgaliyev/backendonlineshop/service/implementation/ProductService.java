package com.kairgaliyev.backendonlineshop.service.implementation;

import com.kairgaliyev.backendonlineshop.dto.ProductDto;
import com.kairgaliyev.backendonlineshop.repository.ProductRepository;
import com.kairgaliyev.backendonlineshop.service.aws.AwsS3Service;
import com.kairgaliyev.backendonlineshop.service.intreface.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private static final String PRODUCT_CACHE_PREFIX = "product:";
    private static final String PRODUCTS_CACHE_PREFIX = "products:";
    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AwsS3Service awsS3Service;

    //TODO: реализовать хранения данных в локальной директории
    @Override
    public ProductDto addProduct(ProductDto productDTO, MultipartFile photo) {
        return null;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDTO, MultipartFile photo) {
        return null;
    }

    @Override
    public void deleteProduct(String productId) {
        return;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return null;
    }

    @Override
    public ProductDto getProductById(String productId) {
        return null;
    }

    @Override
    public ProductDto searchProduct(String input) {
        return null;
    }
}
