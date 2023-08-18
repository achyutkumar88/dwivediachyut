package com.app.blog.repository;

import com.app.blog.models.Posts;
import com.app.blog.models.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 1460344
 */
public interface PostRepository extends JpaRepository<Posts,Integer> {

    @Transactional
    @Modifying
    @Query(value = "select * from POSTS where PUBLISHED_BY  = :userId" , nativeQuery = true)
    List<Posts> getPostByUser(Integer userId);
    @Transactional
    @Modifying
    @Query(value = "select * from POSTS where post_id= :postId and PUBLISHED_BY = (select user_id from users where email = :emailId)" , nativeQuery = true)
    List<Posts> getPostforUser(Integer postId, String emailId);

    @Transactional
    @Modifying
    @Query(value = "delete from POSTS where post_id= :postId and PUBLISHED_BY = (select user_id from users where email = :emailId)" , nativeQuery = true)
    void deletePost(Integer postId, String emailId);

    @Transactional
    @Modifying
    @Query(value = "update POSTS set post_body = :body, post_title = :title where post_id= :postId" , nativeQuery = true)
    void updatePost(Integer postId, String body, String title);
}
