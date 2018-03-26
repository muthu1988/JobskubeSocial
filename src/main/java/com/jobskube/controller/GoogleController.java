package com.jobskube.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value="google")
@CrossOrigin(origins = {"http://localhost:3000", "http://jobskube.com"})
public class GoogleController {
	
	@Value("${google.client_id}")
	private String client_id;
	
	@Value("${google.client_secret}")
	private String client_secret;
	
	@Value("${google.grant_type}")
	private String grant_type;
	
	@Value("${google.token_url}")
	private String token_url;
	
	@Value("${google.redirect_uri}")
	private String redirect_uri;
	
	@RequestMapping(value="accesstoken", method=RequestMethod.GET)
	private ResponseEntity<String> getAccessToken(@RequestParam String code) {
	
		String token="";
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
		MultiValueMap<String, String> payload = new LinkedMultiValueMap<String,String>();
	    payload.add("client_id", client_id);
	    payload.add("client_secret",client_secret);
	    payload.add("code", code);
	    payload.add("grant_type", grant_type);
	    payload.add("redirect_uri",redirect_uri);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(payload, headers);

        ResponseEntity<Map> response = new RestTemplate().postForEntity( token_url, entity , Map.class );
		if(response.getBody().containsKey("access_token")) {
			token = (String) response.getBody().get("access_token");
			httpStatus = HttpStatus.OK;
		}
		return new ResponseEntity<String>(token, httpStatus);
	}
	
	@RequestMapping(value="getdata", method=RequestMethod.GET)
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
