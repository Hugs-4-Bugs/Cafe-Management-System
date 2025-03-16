package com.inn.cafe.ServiceImpl;

import com.inn.cafe.Constents.CafeConstants;
import com.inn.cafe.DAO.ProductDao;
import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.POJO.Category;
import com.inn.cafe.POJO.Product;
import com.inn.cafe.Service.ProductService;
import com.inn.cafe.Utils.CafeUtils;
import com.inn.cafe.Wrapper.ProductWrapper;
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
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private JwtFilter jwtFilter;


    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        log.info("Inside addNewProduct{}", requestMap);
        try {
            if (jwtFilter != null && jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap, false)) {
                    productDao.save(getProductFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Product Added Successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHOROZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * The `validateProductMap` method checks if the request has a `name` and, if needed, an `id` to make sure the data is correct before adding or updating a product* .
     */

    /**
     * // THIS METHOD WILL ONLY UPDATE THE COMPLETE PRODUCT BUT NOT ONLY PRODUCT STATUS
     * <p>
     * //    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
     * ////        if (requestMap.containsKey("name") &&
     * ////                requestMap.containsKey("description") &&
     * ////                requestMap.containsKey("price") &&
     * ////                requestMap.containsKey("status")) {
     * //
     * //       // Or we can also write like this in 1 line
     * ////        if (requestMap.containsKey("name") && requestMap.containsKey("description") && requestMap.containsKey("price") && requestMap.containsKey("status")) {
     * //        if (requestMap.containsKey("name") && requestMap.containsKey("description") && requestMap.containsKey("price")) { // avoid status update
     * //
     * //
     * //            if (validateId) {
     * //                return requestMap.containsKey("id") && requestMap.containsKey("status"); // Ensure "id" is present if validateId is true
     * //            }
     * //            return true; // If validateId is false, return true since all required fields are present
     * //        }
     * //        return false;
     * //    }
     */
    // THIS METHOD WILL WORK FOR BOTH UPDATE THE COMPLETE PRODUCT AS WELL TO UPDATE ONLY THE PRODUCT STATUS.
    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if (validateId) {
            // For update requests, ensure 'id' and 'status' exist
            return requestMap.containsKey("id") && requestMap.containsKey("status");
        } else {
            // For create requests, ensure 'name', 'description', and 'price' exist
            return requestMap.containsKey("name") && requestMap.containsKey("description") && requestMap.containsKey("price");
        }
    }


    /**
     * the getProductFromMap method will be used for both getting the product map or validating the product
     */
    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        /** To match the expected data type in the database, as `id`, `categoryId`, and `price` are stored as integers, but requestMap stores them as strings. */
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product = new Product();  // creating object of the Product
        if (isAdd && requestMap.containsKey("id")) {
            product.setId(Integer.parseInt(requestMap.get("id")));
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));
        product.setStatus(requestMap.get("status"));
        return product;

        /**
         We create a Category object inside `getProductFromMap()` along with the Product object because:

         1️⃣ Product belongs to a Category – A product must be linked to a category due to the `@ManyToOne` relationship between Product and Category.
         2️⃣ Setting the Foreign Key – The Category object helps set the `category_fk` column in the Product table, ensuring proper database relationships.
         3️⃣ Ensuring Data Consistency – Before saving a product, we must assign it a valid category to maintain data integrity.

         ### Example:
         If you're adding a new product like "Paneer", it should be assigned to the "Veg Dish" category.
         To do this, we:
         ✅ Create a Category object for "Veg Dish"
         ✅ Assign this Category to the Paneer product
         ✅ Save the Product (Paneer) with its linked Category (Veg Dish) in the database

         Simply put – A product needs a category, so we create a Category object to establish that link before storing the product! 🚀
         */
    }


    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                // method to check whether the product with id is present in the database or not
                if (validateProductMap(requestMap, true)) {
                    /** The server extracts the product ID from requestMap (sent by the client), converts it to an Integer (since findById() requires a number),
                     and checks if the product exists in productDao. And this will return the object of type Optional so, we will store it into the optional*/
//                    Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
                    Optional optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
                    if (!optional.isEmpty()) {
                        /** If everything is correct, saves the updated product in the database
                         after converting the request data into a proper product format using getProductFromMap(). */
                        productDao.save(getProductFromMap(requestMap, true));
                        return CafeUtils.getResponseEntity("Product Updated Successfully", HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity("Product id doesn't exists", HttpStatus.OK);
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


    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional optional = productDao.findById(id);
                if (!optional.isEmpty()) {
                    productDao.deleteById(id);
                    return CafeUtils.getResponseEntity("Product Deleted Successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Product id does not exist.", HttpStatus.OK);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHOROZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // Method to update the product status
    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap, true)) {
                    Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
                    if (!optional.isEmpty()) {  // or we can also use if (optional.isPresent()) {
                        // Initialize the updateProductStatus in ProductDao to accept 2 arguments 'status' and 'id' as a parameter and also write query in ProductPOJO class
                        productDao.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                        return CafeUtils.getResponseEntity("Product Status Updated Successfully", HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity("Product id doesn't exists", HttpStatus.OK);
                    }
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHOROZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try {
            // Initialize the getProductByCategory in ProductDao to accept and argument 'id' as a parameter and also write query in ProductPOJO class
            return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // method to get product by id
    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
        return new ResponseEntity<>(productDao.getProductById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
