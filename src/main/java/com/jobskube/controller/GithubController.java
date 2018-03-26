package com.jobskube.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.swagger.models.HttpMethod;

@RestController
@RequestMapping(value="github")
@CrossOrigin(origins = {"http://localhost:3000", "http://jobskube.com"})
public class GithubController {
	
	@Value("${github.client_id}")
	private String client_id;
	
	@Value("${github.client_secret}")
	private String client_secret;
	
	@Value("${github.token_url}")
	private String token_url;
	
	@RequestMapping(value="accesstoken", method=RequestMethod.GET)
	private ResponseEntity<String> getAccessToken(@RequestParam String code) {
		String token="";
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
		Map<String, String> payload = new HashMap<String,String>();
	    payload.put("client_id", client_id);
	    payload.put("client_secret", client_secret);
	    payload.put("code", code);
		HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(payload);
		ResponseEntity<String> response = new RestTemplate().postForEntity(token_url, request, String.class);
		if(response.getBody().contains("access_token=")) {
			token = response.getBody().split("&scope")[0].replace("access_token=", "");
			httpStatus = HttpStatus.OK;
		}
		return new ResponseEntity<String>(token, httpStatus);
	}
	
	@RequestMapping(value="getdata", method=RequestMethod.GET)
	private ResponseEntity<Object> getData(@RequestParam String token) {
		String url = "https://api.github.com/user?access_token="+token;
		HttpStatus httpStatus = HttpStatus.OK;
		ResponseEntity<Object> response = new RestTemplate().getForEntity(url, Object.class);
		return new ResponseEntity<Object>(response.getBody(), httpStatus);
	}

}
