package com.example;



import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ResourceApplication extends WebSecurityConfigurerAdapter {

	 @Override
	  protected void configure(HttpSecurity http) throws Exception {
	    http.cors().and().authorizeRequests()
	      .anyRequest().authenticated();
	  }
	 
	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}
	
	@RequestMapping("/")
	@CrossOrigin(origins="*", maxAge=3600,allowedHeaders={"x-auth-token", "x-requested-with"})    //csrf enable
	  public Message home() {
	    return new Message("Hello World");
	  }
	
	@RequestMapping("/resource")
	@CrossOrigin(origins="*", maxAge=3600,allowedHeaders={"x-auth-token", "x-requested-with"})    //csrf enable
	  public Map<String,Object> resource() {
		    Map<String,Object> model = new HashMap<String,Object>();
		    model.put("id", UUID.randomUUID().toString());
		    model.put("content", "Hello World");
		    return model;
		  }
	
	 //http header session strategy "x-auth-token" with session id
	  @Bean
	  HeaderHttpSessionStrategy sessionStrategy() {
	    return new HeaderHttpSessionStrategy();
	  }
}
