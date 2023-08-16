package com.app.blog.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.app.blog.models.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author 1460344
 */
@Service
public class JWTUtils {
    public String CreateJWTToken(Users user) {
        
        Claims claims= Jwts.claims();
        claims.put("name", user.getUserName());
        claims.put("email", user.getEmail());
        claims.put("user_id", user.getUserId());
        claims.setSubject("MY Blog");
        claims.setIssuedAt(new Date());

        Collection<? extends GrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));;

        if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            claims.put("isUser", true);
        }
        
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, Constants.JWT_SECRET)
                .compact();
        
        return token;
    }
    public boolean validateToken(String authToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(Constants.JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public String getEmailIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(Constants.JWT_SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(Constants.JWT_SECRET).parseClaimsJws(token).getBody();

        List<SimpleGrantedAuthority> roles = null;

        Boolean isAdmin = claims.get("isUser", Boolean.class);

        if (isAdmin != null && isAdmin) {
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }


        System.out.println(roles);
        return roles;

    }

}

