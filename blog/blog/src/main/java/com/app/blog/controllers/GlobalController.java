package com.app.blog.controllers;



import java.util.ArrayList;
import java.util.List;


import com.app.blog.dto.PostDTO;
import com.app.blog.dto.UpdatePostDTO;
import com.app.blog.models.Users;
import com.app.blog.service.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import com.app.blog.models.Posts;
import com.app.blog.util.EntitiyHawk;

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
@RequestMapping("/api")
public class GlobalController extends EntitiyHawk {

    @Autowired
    private GlobalService globalService;

    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public String GetHello(){
        System.out.println("Inside GetHello --->");
        return "You are in side application";
    }

    @PostMapping(value = "/publish")
    public ResponseEntity<?> addPost(@RequestBody Posts post)
    {
        return ResponseEntity.ok(globalService.save(post));
    }

    @GetMapping(value = "/getPost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPost()
    {
        List<Posts> posts = globalService.getAllPost();
        List<PostDTO> postDTOList = prepareDTO(posts);
        return ResponseEntity.ok(genericSuccess(postDTOList));
    }

    private List<PostDTO> prepareDTO(List<Posts> posts) {
        List<PostDTO> postDTOList = new ArrayList<>();
        for(Posts post : posts){
            PostDTO postDTO = new PostDTO();
            postDTO.setBody(post.getPostBody());
            postDTO.setTitle(post.getPostTitle());
            postDTOList.add(postDTO);
        }
        return postDTOList;
    }

    @GetMapping(value = "/getPostCount", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPostCount()
    {
        long count = globalService.getPostCount();
        //return count;
        return genericSuccess(count);
    }

    @GetMapping(value = "/getPostByUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPostByUser(Integer userId)
    {
        List<Posts> posts = globalService.getPostByUser(userId);
        if(posts.size()==0){
            return genericSuccess("No posts by user Id ");
        }
        List<PostDTO> postDTOList = prepareDTO(posts);
        return genericSuccess(postDTOList);
    }

    @GetMapping(value = "/getUserPost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPostforUser(Integer postId)
    {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String emailId = user.getUsername();
        List<Posts> posts = globalService.getPostforUser(postId, emailId);
        List<PostDTO> postDTOList = prepareDTO(posts);
        return genericSuccess(postDTOList);
    }

    @GetMapping(value = "/deletePost")
    public ResponseEntity<?> deletePost(Integer postId)
    {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String emailId = user.getUsername();
        String result = globalService.deletePost(postId, emailId);
        return genericSuccess(result);
    }

    @PostMapping(value = "/updatePost")
    ResponseEntity<?> updateProduct(@RequestBody UpdatePostDTO updatePostDTO)
    {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String emailId = user.getUsername();
        String result = globalService.updatePost(updatePostDTO, emailId);
        return genericSuccess(result);
    }

}

