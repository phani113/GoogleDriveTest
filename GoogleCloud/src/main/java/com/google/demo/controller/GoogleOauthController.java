package com.google.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.google.demo.drive.GoogleCloudAuthServiceImpl;

@RestController
public class GoogleOauthController {

	@Autowired
	private GoogleCloudAuthServiceImpl googleCloudAuthServiceImpl;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
		public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
			return new RedirectView(googleCloudAuthServiceImpl.authorize());
		}
	 
	 @RequestMapping(value = "/oauth2callback", method = RequestMethod.GET, params = "code")
		public void oauth2Callback(@RequestParam(value = "code") String code) {

		 googleCloudAuthServiceImpl.getDriveService(code);

		}
	
}
