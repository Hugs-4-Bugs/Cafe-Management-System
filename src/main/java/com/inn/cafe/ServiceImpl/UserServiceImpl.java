package com.inn.cafe.ServiceImpl;

import com.inn.cafe.Constents.CafeConstants;
import com.inn.cafe.DAO.UserDao;
import com.inn.cafe.JWT.CustomerUserDetailsService;
import com.inn.cafe.JWT.JwtUtil;
import com.inn.cafe.POJO.User;
import com.inn.cafe.Service.UserService;
import com.inn.cafe.Utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
public class UserServiceImpl implements UserService {
    // creating object/bean of UserDao class using @Autowired
    @Autowired
    UserDao userDao;

    // creating object/bean of AuthenticationManager class using @Autowired
    @Autowired
    AuthenticationManager authenticationManager;

    // creating object/bean of CustomerUserDetailsService class using @Autowired
    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    // creating object/bean of JwtUtil class using @Autowired
    @Autowired
    JwtUtil jwtUtil;



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
            // Used log.error to log the exception details during the sign-up process,
            // so that we can capture and debug the specific error when receiving a 500 status code.
            log.error("Error during signUp: ", ex);
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


    // Method for login

    /**
     first we have extracted the email & password from requestMap [from this requestMap: public ResponseEntity<String> login(Map<String, String> requestMap) { ]
     and after that authentication is done.
     If  "[ if(auth.isAuthenticated()){ ]" is authenticated then the user role & status will be extracted from it or will be validated.
     And then "jwtUtil" method will be called to generate the token for the user as:
               "jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail()......."
     */
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");

        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));

            // If User is approved
            if(auth.isAuthenticated()){
// here if in case user is not "true" for now, then it means user is not approved by the admin. And we'll return the message "Wait for admin approval."
                if(customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                                    customerUserDetailsService.getUserDetail().getRole()) + "\"}", HttpStatus.OK);  // here we have extracted the role and set into the token
                }
            // If User is not approved

                else {
                    return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval."+"\"}", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials."+"\"}", HttpStatus.BAD_REQUEST);

    }
}
