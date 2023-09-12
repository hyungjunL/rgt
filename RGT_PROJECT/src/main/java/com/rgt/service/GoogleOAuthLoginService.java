package com.rgt.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class GoogleOAuthLoginService {
	
	@Autowired
	private Environment env;
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final String googleLoginUrl = "https://accounts.google.com/";
	
    public String socialLogin(String code, String registrationId) {
        String accessToken = getAccessToken(code, registrationId);
        JsonNode userResourceNode = getUserResource(accessToken, registrationId);

        String id = userResourceNode.get("id").asText();
        String email = userResourceNode.get("email").asText();
        String nickname = userResourceNode.get("name").asText();
        
        return "id = " + id + ", " + "email = " + email + ", " + "nickname = " + nickname;
    }
    
    /**
     * token을 얻음
     * @param authorizationCode
     * @param registrationId
     * @return
     */
    private String getAccessToken(String authorizationCode, String registrationId) {
        
    	String clientId = env.getProperty("oauth2." + registrationId + ".client-id");
        String clientSecret = env.getProperty("oauth2." + registrationId + ".client-secret");
        String redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri");
        
        String tokenUri = env.getProperty("oauth2." + registrationId + ".token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }
    
    /**
     * 사용자 정보를 가져옴
     * @param accessToken
     * @param registrationId
     * @return
     */
    private JsonNode getUserResource(String accessToken, String registrationId) {
    	//yml에 있는 oauth config를 가져옴
        String resourceUri = env.getProperty("oauth2."+registrationId+".resource-uri");
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
    
	public ResponseEntity<?>  getGoogleAuthUrl(String registrationId) {
		
		String clientId = env.getProperty("oauth2." + registrationId + ".client-id");
	    String redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri");
	    
	    //로그인 화면 url 조립
		String reqUrl = googleLoginUrl + "/o/oauth2/v2/auth?client_id=" + clientId + "&redirect_uri=" + redirectUri
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
		
		HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(reqUrl));

        //구글 로그인 후 리다이렉션 호출
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
	}
}
