package com.inn.cafe.RestImpl;

import com.inn.cafe.Constents.CafeConstants;
import com.inn.cafe.DAO.ProductDao;
import com.inn.cafe.Rest.ProductRest;
import com.inn.cafe.Service.ProductService;
import com.inn.cafe.Utils.CafeUtils;
import com.inn.cafe.Wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ProductRestImpl implements ProductRest {

    @Autowired
    private ProductService productService;


    @Override
    public ResponseEntity<String> addNewProduct(@RequestBody Map<String, String> requestMap) {
        try{
            return productService.addNewProduct(requestMap);
        }catch(Exception ex){
        ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return productService.getAllProduct();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try{
            return productService.updateProduct(requestMap);
        }catch (Exception ex){
        ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
