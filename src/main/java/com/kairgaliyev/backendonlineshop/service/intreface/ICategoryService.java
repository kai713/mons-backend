package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.model.Category;

public interface ICategoryService {

    Response getAllCategories();

    Response getCategoryById(Long id);

    Response createCategory(Category category);

    Response deleteCategory(Long id);

    Response addProductToCategory(Long productId, Long categoryId);
}
