package com.rgt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rgt.service.GoogleOAuthLoginService;

@RestController
public class GoogleOAuthLoginController {
	
	@Autowired
	private GoogleOAuthLoginService  googleOAuthLoginService;

    /**
     * 구글 로그인 후 리다이렉션 될 api
     * @param code
     * @param registrationId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = { "/login/oauth2/code/{registrationId}" }, method = RequestMethod.GET)
	public String socialLogin(@RequestParam String code, @PathVariable String registrationId) throws Exception {
		
		return googleOAuthLoginService.socialLogin(code, registrationId);
		
	}
    
    /**
     * 구글 로그인 화면 요청
     * @param registrationId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = { "/google/auth/url/{registrationId}" }, method = RequestMethod.GET)
	public ResponseEntity<?>  getGoogleAuthUrl(@PathVariable String registrationId) throws Exception {
		
		return googleOAuthLoginService.getGoogleAuthUrl(registrationId);
		
	}
    
    
}
