package com.inn.cafe.JWT;

import com.inn.cafe.POJO.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtFilter extends OncePerRequestFilter {  // `OncePerRequestFilter` ensures that the filter is executed only once per request, preventing multiple invocations within the same request.
    // JWT filter will be done here so we will create the bean of JwtUtil & CustomerUserDetailsService using @AutoWired annotation

    @Autowired
     private JwtUtil jwtUtil;
    @Autowired
    private CustomerUserDetailsService service;

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     */

    Claims claims = null;
    private String userName = null;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(httpServletRequest.getServletPath().matches("/user/login | /user/signup | /user/forgotPassword")){  // if this condition is satisfied then no teken validation is required
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {     // otherwise do filteration if user details not found/matches
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String token = null;

            // here we are extracting the values 👇
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){   // total 7 character including 1 space as [Bearer ]
                token = authorizationHeader.substring(7); // count of character "Bearer " with 1 space
                userName = jwtUtil.extractUsername(token);
                claims = jwtUtil.extractAllClaims(token);

            }
            // here 👇 now we have to check for the values if they are valid or not and also we have to check for the session for the user
            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = service.loadUserByUsername(userName); // this is the username which we have extracted from token
                if(jwtUtil.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }
            }

            // now the validation is done and afterthat we'll do filterChain().doFilter() and let the user pass and access out API and get the response
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    // method to check the user is Admin/User and to get the user's name
    public boolean isAdmin(){
        return "admin".equalsIgnoreCase((String) claims.get("role"));  // for Admin role
    }
    public boolean isUser(){
        return "user".equalsIgnoreCase((String) claims.get("role"));  // for User role
    }

    public String getCurrentUser(){   // to get username
        return userName;    // this is userName which we have extracted from token itself

    }
}
