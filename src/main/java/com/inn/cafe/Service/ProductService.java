package com.inn.cafe.Service;

import com.inn.cafe.Wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {

    // to add the new product in to the list
    ResponseEntity<String> addNewProduct(Map<String, String> requestMap);


    ResponseEntity<List<ProductWrapper>> getAllProduct();

    ResponseEntity<String> updateProduct(Map<String, String> requestMap);
}
