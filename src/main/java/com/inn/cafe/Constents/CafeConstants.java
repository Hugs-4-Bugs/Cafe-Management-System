package com.inn.cafe.Constents;

import com.inn.cafe.Utils.CafeUtils;
import org.springframework.http.HttpStatus;

public class CafeConstants {

    // here the method is static so that we can access it with the class name in UserRestImpl class as return statement:
   //  return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    //  and above return statement will replace the [""] which is used to print the custom message shown below
//    return CafeUtils.getResponseEntity("", HttpStatus.INTERNAL_SERVER_ERROR)

    public static final String SOMETHING_WENT_WRONG = "Something Went Wrong";


    // in case of user is inserting invalid data [ used to validate whether user is inserting correct info or not ]
    // see the validation method code in UserServiceImpl
    public static final String INVALID_DATA = "Invalid Data";

    public static final String USER_CREATED_SUCCESSFULLY = "User created successfully.";
    public static final String USER_ALREADY_EXISTS = "User with this email already exists.";

    public static final String  UNAUTHOROZED_ACCESS = "Unauthorized access";

}
