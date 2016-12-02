package com.example;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

 private final static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

 @Override
 protected void configure(HttpSecurity http) throws Exception {
  http.addFilterAt((Filter)ssoFilter(), AbstractPreAuthenticatedProcessingFilter.class)
     .authenticationProvider(
       preauthAuthProvider())
    .csrf().disable()
    .authorizeRequests().anyRequest().authenticated();
 }

 @Autowired
 public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
  auth.authenticationProvider(preauthAuthProvider());
 }

 @Bean
 public UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() {
  UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = 
                 new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>();
  //wrapper.setUserDetailsService(new CustomUserDetailsService());
  return wrapper;
 }

 @Bean
 public PreAuthenticatedAuthenticationProvider preauthAuthProvider() {
  PreAuthenticatedAuthenticationProvider preauthAuthProvider = 
   new PreAuthenticatedAuthenticationProvider();
  preauthAuthProvider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper());
  return preauthAuthProvider;
 }

 @Bean
 public SSORequestHeaderAuthenticationFilter ssoFilter() throws Exception {
  SSORequestHeaderAuthenticationFilter filter = new SSORequestHeaderAuthenticationFilter();
  //filter.setAuthenticationDetailsSource((AuthenticationDetailsSource<HttpServletRequest, ?>) authenticationManager());
  return filter;
 }

}