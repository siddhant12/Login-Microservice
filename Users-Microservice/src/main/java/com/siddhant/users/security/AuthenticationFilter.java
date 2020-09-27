package com.siddhant.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siddhant.users.model.LoginRequestModel;
import com.siddhant.users.service.UserService;
import com.siddhant.users.shared.UserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private Environment environment;
    private UserService userService;

    public AuthenticationFilter(UserService userService, Environment environment , AuthenticationManager authenticationManager){
        this.userService = userService;
        this.environment = environment;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            LoginRequestModel creds = new ObjectMapper()
                    .readValue(request.getInputStream(),LoginRequestModel.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    ));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        UserDTO userDetails = userService.getUserByEmail(username);

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime()+ Long.parseLong(environment.getProperty("token.expiration.time")));

        String userId = userDetails.getUserId();
        //claims are information about user
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("username",userDetails.getEmail());
        claims.put("firstname",userDetails.getFirstName());
        claims.put("lastname",userDetails.getFirstName());
        claims.put("userId",userDetails.getUserId());

        String jwtToken = Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, environment.getProperty("token.secret"))
                .compact();
        response.addHeader("token",jwtToken);
        response.addHeader("userID",userDetails.getUserId());

    }
}
