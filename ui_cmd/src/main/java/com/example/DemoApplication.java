package com.example;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	  @RequestMapping("/resource")
	  public Map<String,Object> home() {
	    Map<String,Object> model = new HashMap<String,Object>();
	    model.put("id", UUID.randomUUID().toString());
	    model.put("content", "Hello World");
	    return model;
	  }
	  
	  //trick 
	  @RequestMapping("/user")
	  @CrossOrigin(origins="*", maxAge=3600,allowedHeaders={"x-auth-token", "x-requested-with"})    //csrf enable
	  public Principal user(Principal user) {
	    return user;
	  }
	  
	  @RequestMapping("/token")
	  @ResponseBody
	  public Map<String,String> token(HttpSession session) {
	    return Collections.singletonMap("token", session.getId());
	  }
	  
	  @Configuration
	  @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	  protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	      http
	        .httpBasic()
	      .and()
	        .authorizeRequests()
	          .antMatchers("/index.html", "/home.html", "/login.html", "/").permitAll()
	          .anyRequest().authenticated().and()
	          .csrf()      //csrf cookie setting
	          .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	    }
	    
	    @Bean
	    public HttpSessionStrategy httpSessionStrategy() {
	        return new HeaderHttpSessionStrategy();
	    }
	  }
	  
	  
}

