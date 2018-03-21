package com.jobskube.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value="github")
@CrossOrigin(origins = "http://localhost:3000")
public class GithubController {
	
	@RequestMapping(value="accesstoken")
	private ResponseEntity<String> getAccessToken(@RequestParam String code) {
		String url = "https://github.com/login/oauth/access_token", token="";
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
		Map<String, String> payload = new HashMap<String,String>();
	    payload.put("client_id", "c5cca39ee7744cca0cab");
	    payload.put("client_secret", "d8999ef024cc18cdd53a26b02278547ee0b840f0");
	    payload.put("code", code);
		HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(payload);
		ResponseEntity<String> response = new RestTemplate().postForEntity(url, request, String.class);
		if(response.getBody().contains("access_token=")) {
			token = response.getBody().split("&scope")[0].replace("access_token=", "");
			httpStatus = HttpStatus.OK;
		}
		return new ResponseEntity<String>(token, httpStatus);
	}
	
	@RequestMapping(value="getdata")
	private ResponseEntity<Object> getData(@RequestParam String token) {
		String url = "https://api.github.com/user?access_token="+token;
		HttpStatus httpStatus = HttpStatus.OK;
		ResponseEntity<Object> response = new RestTemplate().getForEntity(url, Object.class);
		return new ResponseEntity<Object>(response.getBody(), httpStatus);
	}

}
