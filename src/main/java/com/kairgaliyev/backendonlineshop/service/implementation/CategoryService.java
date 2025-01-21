package com.kairgaliyev.backendonlineshop.service.implementation;

import com.kairgaliyev.backendonlineshop.dto.CategoryDTO;
import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.exception.MyException;
import com.kairgaliyev.backendonlineshop.model.Category;
import com.kairgaliyev.backendonlineshop.model.Product;
import com.kairgaliyev.backendonlineshop.repository.CategoryRepository;
import com.kairgaliyev.backendonlineshop.repository.ProductRepository;
import com.kairgaliyev.backendonlineshop.service.intreface.ICategoryService;
import com.kairgaliyev.backendonlineshop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CATEGORY_CACHE_KEY = "category:";

    public Response getAllCategories() {
        Response response = new Response();

        response.setMessage("successful");
        response.setStatusCode(200);

        response.setCategoryList(Utils.mapCategoryListEntityToCategoryListDTO(categoryRepository.findAll()));
        return response;
    }

    public Response getCategoryById(Long id) {
        Response response = new Response();

        try {

            CategoryDTO categoryDTO = (CategoryDTO) redisTemplate.opsForValue().get(CATEGORY_CACHE_KEY + id);

            if (categoryDTO != null) {
                response.setMessage("successful");
                response.setStatusCode(200);
                response.setCategory(categoryDTO);
            }
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new MyException("Категория не найдена"));


            categoryDTO = Utils.mapCategoryEntityToCategoryDTO(category);
            redisTemplate.opsForValue().set(CATEGORY_CACHE_KEY + id, categoryDTO, Duration.ofMinutes(10));

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setCategory(categoryDTO);

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage("error get category by id" + e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("error get category by id" + e.getMessage());
        }
        return response;
    }

    public Response createCategory(Category category) {
        Response response = new Response();

        Category category1 = categoryRepository.save(category);
        response.setStatusCode(200);
        response.setMessage("successful");
        response.setCategory(Utils.mapCategoryEntityToCategoryDTO(category1));

        return response;
    }

    public Response deleteCategory(Long id) {

        Response response = new Response();

        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new MyException("Категория не найдена"));
            categoryRepository.delete(category);
            response.setStatusCode(200);
            response.setMessage("successful");

            redisTemplate.delete(CATEGORY_CACHE_KEY + id);
        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage("error delete category by id" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response addProductToCategory(Long productId, Long categoryId) {
        Response response = new Response();

        try {
            Product product = productRepository.findById(productId).orElseThrow(() -> new MyException("product not found"));
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new MyException("category not found"));

            product.setCategory(category);
            productRepository.save(product);

            redisTemplate.delete(CATEGORY_CACHE_KEY + categoryId);
            redisTemplate.delete("product:" + productId);
            redisTemplate.delete("products");

            response.setProduct(Utils.mapProductEntityToProductDTO(product));
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage("error add product to category" + e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("error add product to category" + e.getMessage());
        }
        return response;
    }

}

