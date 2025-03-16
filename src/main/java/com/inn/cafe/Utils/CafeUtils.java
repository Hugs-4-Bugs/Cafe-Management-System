package com.inn.cafe.Utils;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
/**
 * utility method is going to be the generic method which can be used in any service in the
 * classes or any classes
 * like suppose we want the unique id So we write a method which is going to return the unique
 * id, So in that case we are going to use that type of code we need to write in the utility classes
 */
public class CafeUtils {
    // private constructor so that we can't create object of this class and every method will be
    // static inside the below defined constructor so that we can directly access it with class name
    private CafeUtils() {  // private constructor
    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
//            return new ResponseEntity<String>("{\"message\":\"Something Went Wrong\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<String>("{\"message\":\"" + responseMessage + "\"}", httpStatus);

    }

    public static String getUUID() {
        Date date = new Date();
        long time = date.getTime();
        return "BILL-" + time;
    }

    // method to convert the String into JSON Array
    public static JSONArray getJsonArrayFromString(String data) throws JSONException {
        JSONArray jsonArray = new JSONArray(data);
        return jsonArray;
    }

    public static Map<String, Object> getMapFromJson(String data) {
        if (!Strings.isNullOrEmpty(data))
            return new Gson().fromJson(data, new TypeToken<Map<String, Object>>() {
            }.getType());
//        }
        return new HashMap<>();

    }


    public static Boolean isFileExists(String path) {
        log.info("Inside isFileExists {}", path);
        try {
            File file = new File(path);
            return (file != null && file.exists()) ? Boolean.TRUE : Boolean.FALSE;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
