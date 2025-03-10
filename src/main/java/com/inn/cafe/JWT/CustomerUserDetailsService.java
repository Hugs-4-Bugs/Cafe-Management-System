package com.inn.cafe.JWT;


import com.inn.cafe.DAO.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService {

   /*
    UserDetailsService has a method named 'loadUserByUsername()' and as UserDetailsService will call the 'loadUserByUsername()'
    then the loadUserByUsername() method will interact with the UserRepository and will find the user with the requested username
    and will return to the UserDetailsService and then UserDetailsService will forward that username to the particular Authentication Provider
    who requested the username
    */

    @Autowired
    UserDao userDao;
    private com.inn.cafe.POJO.User userDetail;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername{}", username);
        userDetail = userDao.findByEmailId(username);
        if(!Objects.isNull(userDetail))
            return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        else
            throw new UsernameNotFoundException("user not found");
    }
    public com.inn.cafe.POJO.User getUserDetail(){

        return userDetail;
    }
}
