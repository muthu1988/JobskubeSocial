package com.jobskube.controller;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value="google")
@CrossOrigin(origins = {"http://localhost:3000", "http://jobskube.com"})
public class GoogleController {
	
	@RequestMapping(value="accesstoken")
	private ResponseEntity<String> getAccessToken(@RequestParam String code) {
		String url = "https://accounts.google.com/o/oauth2/token", token="";
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
		MultiValueMap<String, String> payload = new LinkedMultiValueMap<String,String>();
	    payload.add("client_id", "16005081426-k9irnocljp0oho5n4f2abfrfktovlg3b.apps.googleusercontent.com");
	    payload.add("client_secret", "o-S-PazKhekorhetu-NSr7FK");
	    payload.add("code", code);
	    payload.add("grant_type", "authorization_code");
	    payload.add("redirect_uri", "http://jobskube.com/callback/google");
				
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(payload, headers);
        
        ResponseEntity<Map> response = new RestTemplate().postForEntity( url, entity , Map.class );
		if(response.getBody().containsKey("access_token")) {
			token = (String) response.getBody().get("access_token");
			httpStatus = HttpStatus.OK;
		}
		return new ResponseEntity<String>(token, httpStatus);
	}
	
	@RequestMapping(value="getdata")
	private ResponseEntity<Object> getData(@RequestParam String token) {
		String url = "https://www.googleapis.com/userinfo/v2/me";
		HttpStatus httpStatus = HttpStatus.OK;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<Object> response = new RestTemplate().exchange(url, HttpMethod.GET, entity, Object.class);
		return new ResponseEntity<Object>(response.getBody(), httpStatus);
	}

}
