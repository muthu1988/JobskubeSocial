package com.jobskube.controller;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value="linkedin")
@CrossOrigin(origins = "http://localhost:3000")
public class LinkedinController {
	
	@RequestMapping(value="accesstoken")
	private ResponseEntity<String> getAccessToken(@RequestParam String code) {
		String url = "https://www.linkedin.com/oauth/v2/accessToken?", token="";
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
		String params = "client_id=81xtk6xgkoswse"
				+ "&client_secret=AqKa8BnR4xHNniC5"
				+ "&grant_type=authorization_code"
				+ "&redirect_uri=http://localhost:3000/callback/linkedin"
				+ "&code=" + code;
		ResponseEntity<Map> response = new RestTemplate().getForEntity(url+params,Map.class);
		if(response.getBody().containsKey("access_token")) {
			token = (String) response.getBody().get("access_token");
			httpStatus = HttpStatus.OK;
		}
		return new ResponseEntity<String>(token, httpStatus);
	}
	
	@RequestMapping(value="getdata")
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