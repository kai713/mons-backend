package com.kairgaliyev.backendonlineshop.service.implementation;

import com.kairgaliyev.backendonlineshop.dto.ProductDTO;
import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.entity.ProductEntity;
import com.kairgaliyev.backendonlineshop.exception.MyException;
import com.kairgaliyev.backendonlineshop.repository.ProductRepository;
import com.kairgaliyev.backendonlineshop.service.aws.AwsS3Service;
import com.kairgaliyev.backendonlineshop.service.intreface.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AwsS3Service awsS3Service;

    private static final String PRODUCT_CACHE_PREFIX = "product:";
    private static final String PRODUCTS_CACHE_PREFIX = "products:";

    @Override
    public Response addProduct(ProductDTO productDTO, MultipartFile photo) {

        Response response = new Response();

        try {
            String imageUrl = awsS3Service.saveImageToS3(photo);

            ProductEntity newProduct = new ProductEntity();
            newProduct.setName(productDTO.getName());
            newProduct.setDescription(productDTO.getDescription());
            newProduct.setPrice(productDTO.getPrice());
            newProduct.setStockQuantity(productDTO.getStock());
            newProduct.setImageURL(imageUrl);

            productRepository.save(newProduct);

            redisTemplate.delete(PRODUCTS_CACHE_PREFIX);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setProduct(productDTO);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateProduct(ProductDTO productDTO, MultipartFile photo) {
        Response response = new Response();

        try {
            String imageUrl = awsS3Service.saveImageToS3(photo);

            //TODO findByName and do name attribute unique or get id from front
            ProductEntity existingProduct = productRepository.findByName(productDTO.getName())
                    .orElseThrow(() -> new MyException("product not found"));

            //TODO create method map method in util / ModelMapper
            existingProduct.setName(productDTO.getName());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setId(productDTO.getId());
            existingProduct.setStockQuantity(productDTO.getStock());
            existingProduct.setImageURL(imageUrl);

            productRepository.save(existingProduct);

            String cacheKey = PRODUCT_CACHE_PREFIX + productDTO.getId();
            redisTemplate.delete(cacheKey);
            redisTemplate.delete(PRODUCTS_CACHE_PREFIX);

            response.setMessage("successful");
            response.setStatusCode(200);
            response.setProduct(productDTO);

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating product" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteProduct(String productId) {

        Response response = new Response();

        try {
            ProductEntity existingProduct = productRepository.findById(Long.valueOf(productId)).orElseThrow(() -> new MyException("product not found"));
//            response.setProduct(Utils.mapProductEntityToProductDTO(existingProduct));

            productRepository.delete(existingProduct);

            String cacheKey = PRODUCT_CACHE_PREFIX + productId;
            redisTemplate.delete(cacheKey);
            redisTemplate.delete(PRODUCTS_CACHE_PREFIX);

            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage("error delete product " + e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting product" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllProducts() {
        Response response = new Response();

        try {
            List<ProductDTO> productDTOList = (List<ProductDTO>) redisTemplate.opsForValue().get(PRODUCTS_CACHE_PREFIX);

            if (productDTOList != null) {
                response.setStatusCode(200);
                response.setMessage("successful");
                response.setProductList(productDTOList);
            }

            List<ProductEntity> productList = productRepository.findAll();
//            productDTOList = Utils.mapProductListEntityToProductListDTO(productList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setProductList(productDTOList);

            redisTemplate.opsForValue().set(PRODUCT_CACHE_PREFIX, productDTOList, Duration.ofMinutes(20));

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all products " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getProductById(String productId) {
        Response response = new Response();
        String cacheKey = PRODUCT_CACHE_PREFIX + productId;

        try {
            ProductDTO cachedProduct = (ProductDTO) redisTemplate.opsForValue().get(cacheKey);

            if (cachedProduct != null) {
                response.setProduct(cachedProduct);
                response.setStatusCode(200);
                response.setMessage("successful (from cache)");
                return response;
            }

            ProductEntity searchProduct = productRepository.findById(Long.parseLong(productId)).orElseThrow(() -> new MyException("product not found"));

//            ProductDTO productDTO = Utils.mapProductEntityToProductDTO(searchProduct);

//            redisTemplate.opsForValue().set(cacheKey, productDTO, Duration.ofMinutes(10));
//            response.setProduct(productDTO);

            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error searching product " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response searchProduct(String input) {
        Response response = new Response();

        try {
            List<ProductEntity> products = productRepository.findByNameContainingOrDescriptionContaining(input, input);

            if (products.isEmpty()) {
                throw new MyException("products not found");
            }

//            List<ProductDTO> productDTOList = Utils.mapProductListEntityToProductListDTO(products);

            response.setStatusCode(200);
            response.setMessage("successful");
//            response.setProductList(productDTOList);

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
