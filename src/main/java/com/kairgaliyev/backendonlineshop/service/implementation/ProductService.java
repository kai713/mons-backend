package com.kairgaliyev.backendonlineshop.service.implementation;

import com.kairgaliyev.backendonlineshop.dto.ProductDTO;
import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.exception.MyException;
import com.kairgaliyev.backendonlineshop.model.Product;
import com.kairgaliyev.backendonlineshop.repository.ProductRepository;
import com.kairgaliyev.backendonlineshop.service.intreface.IProductService;
import com.kairgaliyev.backendonlineshop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Response addProduct(ProductDTO productDTO) {

        Response response = new Response();

        try {
            Product newProduct = new Product();
            newProduct.setName(productDTO.getName());
            newProduct.setDescription(productDTO.getDescription());
            newProduct.setPrice(productDTO.getPrice());
            newProduct.setStockQuantity(productDTO.getStock());

            productRepository.save(newProduct);

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
    public Response updateProduct(ProductDTO productDTO) {
        Response response = new Response();

        try {

            //TODO findByName and do name attribute unique
            Product existingProduct = productRepository.findById(Long.parseLong(productDTO.getId().toString()))
                    .orElseThrow(() -> new MyException("product not found"));

            //TODO create method map method in util / ModelMapper
            existingProduct.setName(productDTO.getName());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setId(productDTO.getId());
            existingProduct.setStockQuantity(productDTO.getStock());

            productRepository.save(existingProduct);

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
            Product existingProduct = productRepository.findById(Long.valueOf(productId)).orElseThrow(() -> new MyException("product not found"));
            response.setProduct(Utils.mapProductEntityToProductDTO(existingProduct));

            productRepository.delete(existingProduct);

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
            List<Product> productList = productRepository.findAll();
            List<ProductDTO> productDTOListDTO = Utils.mapProductListEntityToProductListDTO(productList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setProductList(productDTOListDTO);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all products " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getProductById(String productId) {
        Response response = new Response();

        try {
            Product searchProduct = productRepository.findById(Long.parseLong(productId)).orElseThrow(() -> new MyException("product not found"));
            response.setProduct(Utils.mapProductEntityToProductDTO(searchProduct));

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
            List<Product> products = productRepository.findByNameContainingOrDescriptionContaining(input, input);

            if (products.isEmpty()) {
                throw new MyException("products not found");
            }

            List<ProductDTO> productDTOList = Utils.mapProductListEntityToProductListDTO(products);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setProductList(productDTOList);

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
