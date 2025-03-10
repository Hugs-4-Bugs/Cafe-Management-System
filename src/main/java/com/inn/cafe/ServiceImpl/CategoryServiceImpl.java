package com.inn.cafe.ServiceImpl;

import com.google.common.base.Strings;
import com.inn.cafe.Constents.CafeConstants;
import com.inn.cafe.DAO.CategoryDao;
import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.POJO.Category;
import com.inn.cafe.Service.CategoryService;
import com.inn.cafe.Utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private JwtFilter jwtFilter;  // to check the current user is admin because we have method for admin in JwtFilter class, so we are creating the bean of JwtFilter class here.

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            // only admin can add the category, so first we will check whether the current user is admin
            if (jwtFilter.isAdmin()) {
                if (validateCategoryMap(requestMap, false)) {
                    // this is implemented by Prabhat
                    String categoryName = requestMap.get("name").trim();
                    if(categoryName.isEmpty()){
                    return CafeUtils.getResponseEntity("Item cannot be empty", HttpStatus.BAD_REQUEST);
                    }
                    if (categoryDao.existsByName(requestMap.get("name"))) {
                        return CafeUtils.getResponseEntity("Item Already Exists", HttpStatus.BAD_REQUEST);
                    }
                categoryDao.save(getCategoryFromMap(requestMap, false));
                return CafeUtils.getResponseEntity("Category Added Successfully", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHOROZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    /**
     * The `validateCategoryMap` method checks if the request has a `name` and, if needed, an `id` to make sure the data is correct before adding or updating a category.
     */
    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    /** This method takes data from the request and creates a Category object. If it's a new category, it just sets the name.
    If it's an update, it also sets the ID. This helps in saving or updating categories properly. */
    private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd) {
        Category category = new Category();  // object of Category
        if (isAdd) {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }


    /** API to get all the items by admin only if you use the url: http://localhost:8000/category/get?filterValue=true
     * and if u use http://localhost:8000/category/get it will list all the items by normal user
     */
    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            // this will return those value which contains one or more product in that (ie. the category list in which entry is not null)
            if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
                log.info("Inside if");
                return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {  // if user is admin
                // method to check whether the category with id is present in the database or not
                if (validateCategoryMap(requestMap, true)) {  // true means we have validated the id also (see the implementation of above validateCategoryMap method)
                    /** The server extracts the category ID from requestMap (sent by the client), converts it to an Integer (since findById() requires a number),
                     and checks if the category exists in categoryDao. And this will return the object of type Optional so, we will store it into the optional*/
                    Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                    if (!optional.isEmpty()) {
                        /** If everything is correct, saves the updated category in the database
                         after converting the request data into a proper category format using getCategoryFromMap(). */

                        categoryDao.save(getCategoryFromMap(requestMap, true));
                        return CafeUtils.getResponseEntity("Category Updated Successfully", HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity("Category id doesn't exists", HttpStatus.OK);
                    }
                }
                // and if validation is failed then return
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHOROZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
