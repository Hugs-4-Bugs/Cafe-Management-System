package com.inn.cafe.RestImpl;

import com.inn.cafe.Constents.CafeConstants;
import com.inn.cafe.Rest.UserRest;
import com.inn.cafe.Service.UserService;
import com.inn.cafe.Utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class UserRestImpl implements UserRest {

    /*
   to get below method directly click on the line in which compiler error is showing
   with red underline and then red bulb will arise and just click on that bulb, you will
   get multiple option then just click on "Implement Methods" and ok, and it will override the methods
   [ here UserRestImpl class is implementing ie, Overriding the method of UserRest interface]

   and replace the 'return null' and put everything inside the try and catch block

   @Override
   public ResponseEntity<String> signUp(Map<String, String> requestMapping) {
       return null;
   }
    */

    @Autowired  // by autowiring UserService we'll be able to access all the methods available in userService
    UserService userService;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try{
            return userService.signUp(requestMap);

        } catch(Exception ex){
            ex.printStackTrace();
            // since i don't want to send the exception to the end user so in that case we'll return the custom message as

        }

        // we can write the below return statement here also but the problem is we need to write this again and again so we will move this into the
        // utility class so that whenever this will be called then it will be called from utility class [CafeUtil class] directly

        // return new ResponseEntity<String>("{\"message\":\"Something Went Wrong\"}", HttpStatus.INTERNAL_SERVER_ERROR);



        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
