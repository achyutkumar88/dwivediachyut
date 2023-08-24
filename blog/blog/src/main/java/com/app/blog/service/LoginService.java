package com.app.blog.service;

import com.app.blog.models.Users;
import com.app.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class LoginService  {

	
	@Autowired(required = true)
	private UserRepository userRepository;
	
	//@Override
	public List<Users> loadUserByUsername(String emailid) {
		List<SimpleGrantedAuthority> roles = null;	
		List<Users> result = userRepository.findByEmail(emailid);
		return result;
	}

	public String save(Users user) {
		userRepository.save(user);
		return " User Registered successfully ";
	}
}
