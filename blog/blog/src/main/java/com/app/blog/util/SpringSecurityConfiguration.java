package com.app.blog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private CustomJwtAuthenticationFilter customJwtAuthenticationFilter;
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		 //http.csrf().disable()
		//	.authorizeRequests().antMatchers("/**").permitAll();
		http.csrf().disable().cors().disable()
				.authorizeRequests().antMatchers("/api/**").hasRole("USER")
				.antMatchers("/login","/h2-console/**","/register").permitAll()
				.anyRequest().authenticated()
				.and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).
				and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
				and().addFilterBefore(customJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}



}
