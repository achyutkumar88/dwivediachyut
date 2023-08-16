package com.app.blog.controllers;

import java.util.List;
import java.util.Optional;


import com.app.blog.bean.AuthenticationRequest;
import com.app.blog.bean.AuthenticationResponse;
import com.app.blog.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.app.blog.dto.LoginDto;
import com.app.blog.dto.RegisterUserDTO;
import com.app.blog.models.Users;
import com.app.blog.repository.UserRepository;
import com.app.blog.util.EntitiyHawk;
import com.app.blog.util.JWTUtils;
import org.springframework.web.client.RestTemplate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 1460344
 */
@RestController
@RequestMapping("/")
public class UserController extends EntitiyHawk {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LoginService loginService;

    @Autowired
    private JWTUtils jwtUtil;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
   //@PostMapping(value = { "/authenticate" }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
        System.out.println("Inside createAuthenticationToken method ");
        try {
            System.out.println("email ID ----->"+authenticationRequest.getEmailid());
            System.out.println("Password -------------> "+authenticationRequest.getPassword());
            //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
              //      authenticationRequest.getEmailid(), authenticationRequest.getPassword()));
        }catch(Exception e) {
            System.out.println(e);
        }
        System.out.println(" In Middle -------------->");
       // Users user = loginService.loadUserByUsername(authenticationRequest.getEmailid());
        Users user = new Users();
        user.setEmail("achyutkumar88@gmail.com");
        user.setUserId(1234);
        user.setUserName("achyut");
        System.out.println(" DB call success -------------->");
        String token = jwtUtil.CreateJWTToken(user);
        System.out.println("get toke ------------------>"+token);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }


}