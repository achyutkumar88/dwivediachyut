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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
        Users user = loginService.loadUserByUsername(authenticationRequest.getEmailid());
        String token = jwtUtil.CreateJWTToken(user);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody Users user) throws Exception {
        String result = loginService.save(user);
        return ResponseEntity.ok(result);
    }


}