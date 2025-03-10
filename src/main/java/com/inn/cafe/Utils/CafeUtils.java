package com.inn.cafe.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/* utility method is going to be the generic method which can be used in any service in the
 classes or any classes
 like suppose we want the unique id So we write a method which is going to return the unique
 id, So in that case we are going to use that type of code we need to write in the utility classes
 */
public class CafeUtils {
    // private constructor so that we can't create object of this class and every method will be
    // static inside the below defined constructor so that we can directly access it with class name
    private CafeUtils() {  // private constructor
    }

        public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
//            return new ResponseEntity<String>("{\"message\":\"Something Went Wrong\"}", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<String>("{\"message\":\"" + responseMessage + "\"}", httpStatus);






    }
}
