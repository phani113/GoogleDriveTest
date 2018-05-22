package com.google.demo.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.demo.drive.GoogleCloudServiceImpl;

@RestController
public class GoogleCloudController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCloudController.class);
	
	@Autowired
	private GoogleCloudServiceImpl googleCloudService;
	
	 @GetMapping(value="getfiledata/{fileName:.+}", produces=MediaType.APPLICATION_JSON_VALUE)
		public String getFileData(@PathVariable("fileName") final String fileName) throws IOException {
		 LOGGER.info("calling getFileData api for: {}", fileName);
			String response = googleCloudService.getJsonContent(fileName);
			return response;
		}
	 
	 @GetMapping(value="getfilenames/{pageSize}", produces=MediaType.APPLICATION_JSON_VALUE)
	 public List<String> getFileName(@PathVariable("pageSize") final Integer pageSize) throws IOException {
		 List<String> response = googleCloudService.getFileNames(pageSize);
			return response;
	 }
	 
	 @RequestMapping(value = "/download/{fileName:.+}", method = RequestMethod.GET)
	 public HttpEntity<byte[]> downloadDoc(@PathVariable("fileName") final String fileName) throws IOException{
		byte[] resp = googleCloudService.downloadFile(fileName);
		return new HttpEntity<byte[]>(resp);
	 }

}
