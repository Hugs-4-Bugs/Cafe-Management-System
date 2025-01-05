package com.inn.cafe.ServiceImpl;

import com.inn.cafe.Constents.CafeConstants;
import com.inn.cafe.DAO.UserDao;
import com.inn.cafe.POJO.User;
import com.inn.cafe.Service.UserService;
import com.inn.cafe.Utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
public class UserServiceImpl implements UserService {
    // creating object of UserDao class using @Autowired
    @Autowired
    UserDao userDao;

    // implement the method [ see how to implement in UserRestImpl ]
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside the signUp {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                // now we have to check whether user's info (name, email etc.) is present in database or not so for that
                // we will create a query [named query] for email validation in UserDao class as: User findByEmailId(@Param("email") String email);

                User user = userDao.findByEmailId(requestMap.get("email"));  // we'll extract email id from requestMap
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));  // in case user is not present
                    return CafeUtils.getResponseEntity("Registration Completed", HttpStatus.OK);

                } else {
                    return CafeUtils.getResponseEntity("Email Already Registered", HttpStatus.BAD_REQUEST);
                }


            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    public boolean validateSignUpMap(Map<String, String> requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        } else {
            return false;
        }

    }

    // then create the user and save in the db, so we will create a separate class to extract the value from that Map and set it into the 'User' and return the User object
    // method to return the user object in case user information is not present in the database

    // here the User belongs to POJO's[ie. Entity] User class
    private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();

        // all the keys[ie. name, contactNumber, email, password] should be same as written in validateSignUpMap class
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }
}
