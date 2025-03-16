package com.inn.cafe.DAO;

import com.inn.cafe.POJO.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category, Integer> {

    // query to fetch the data (which is get all products from category)
    List<Category> getAllCategory();

    boolean existsByName(String name); // This checks if a category with the given name exists (implemented by Prabhat)

    Category findByName(String name);





}
