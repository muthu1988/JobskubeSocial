package com.jobskube.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value="stackoverflow")
@CrossOrigin(origins = {"http://localhost:3000", "http://jobskube.com"})
public class StackOverFlowController {
	
	@RequestMapping(value="accesstoken")
	private ResponseEntity<String> getAccessToken(@RequestParam String code) {
		String url = "https://stackoverflow.com/oauth/access_token/json", token="";
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
		Map<String, String> payload = new HashMap<String,String>();
	    payload.put("client_id", "12098");
	    payload.put("client_secret", "DNL6D4BG*pmgdyJgQ0IlSw((");
	    payload.put("code", code);
	    payload.put("redirect_uri", "http://jobskube.com/callback/stackoverflow");
		HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(payload);
		ResponseEntity<Map> response = new RestTemplate().postForEntity(url, request, Map.class);
		if(response.getBody().containsKey("access_token")) {
			token = (String) response.getBody().get("access_token");
			httpStatus = HttpStatus.OK;
		}
		return new ResponseEntity<String>(token, httpStatus);
	}
	
	@RequestMapping(value="getdata")
	private ResponseEntity<Object> getData(@RequestParam String token) {
		String url = "https://api.stackexchange.com/2.2/me?site=stackoverflow&access_token="+token+"&key=ipVjbHPtJXI*UaOs2ri16w((";
		HttpStatus httpStatus = HttpStatus.OK;
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
		RestTemplate template = new RestTemplate(clientHttpRequestFactory);
		ResponseEntity<Object> response = template.getForEntity(url, Object.class);
		return new ResponseEntity<Object>(response.getBody(), httpStatus);
	}

}
