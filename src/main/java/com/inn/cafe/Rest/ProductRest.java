package com.inn.cafe.Rest;

import com.inn.cafe.Wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/product")
public interface ProductRest {

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewProduct(@RequestBody Map<String, String> requestMap);


    // API to get all the product present inside the particular category
    @GetMapping(path = "/get")
    ResponseEntity<List<ProductWrapper>> getAllProduct();

    // API to update the product
    @PostMapping(path = "/update")
    ResponseEntity<String> updateProduct(@RequestBody Map<String, String> requestMap);
}
