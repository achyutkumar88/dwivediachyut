package com.app.blog.repository;

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
public interface UserRepository extends JpaRepository<Users,Integer> {

    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM USERS where email = :emailId" , nativeQuery = true)
    List<Users> findByEmail(String emailId);
   
}
