package com.inn.cafe.DAO;

import com.inn.cafe.POJO.Product;
import com.inn.cafe.Wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {

    List<ProductWrapper> getAllProduct();

}
