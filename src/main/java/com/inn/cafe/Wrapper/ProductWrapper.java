package com.inn.cafe.Wrapper;


import lombok.Data;

/**
 * in this class we will specify the columns and key in which we want to return the value
 */

@Data
public class ProductWrapper {
    Integer id;

    String name;

    String description;

    Integer price;

    String status;

    Integer categoryId;

    String categoryName;

    // default constructor
    public ProductWrapper(){

    }
    public ProductWrapper(Integer id, String name, String description, Integer price, String status, Integer categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
