package com.jobskube.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value="linkedin")
@CrossOrigin(origins = {"http://localhost:3000", "http://jobskube.com"})
public class LinkedinController {
	
	@Value("${linkedin.client_id}")
	private String client_id;
	
	@Value("${linkedin.client_secret}")
	private String client_secret;
	
	@Value("${linkedin.grant_type}")
	private String grant_type;
	
	@Value("${linkedin.token_url}")
	private String token_url;
	
	@Value("${linkedin.redirect_uri}")
	private String redirect_uri;
	
	@RequestMapping(value="accesstoken", method=RequestMethod.GET)
	private ResponseEntity<String> getAccessToken(@RequestParam String code) {
		String token="";
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
		String params = "?client_id=" + client_id + "&client_secret="+client_secret
				+ "&grant_type="+grant_type + "&redirect_uri="+redirect_uri + "&code=" + code;
		ResponseEntity<Map> response = new RestTemplate().getForEntity(token_url+params,Map.class);
		if(response.getBody().containsKey("access_token")) {
			token = (String) response.getBody().get("access_token");
			httpStatus = HttpStatus.OK;
		}
		return new ResponseEntity<String>(token, httpStatus);
	}
	
	@RequestMapping(value="getdata", method=RequestMethod.GET)
	private ResponseEntity<Object> getData(@RequestParam String token) {
		String url = "https://api.linkedin.com/v1/people/~?format=json";
		HttpStatus httpStatus = HttpStatus.OK;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<Object> response = new RestTemplate().exchange(url, HttpMethod.GET, entity, Object.class);
		return new ResponseEntity<Object>(response.getBody(), httpStatus);
	}

}