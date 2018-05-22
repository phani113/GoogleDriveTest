package com.google.demo.drive;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@Component
public class CommonUitil {
   
    private void getJsonContent(Drive driveService) throws IOException {
    	FileList result = driveService.files().list().setPageSize(10).execute();
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
                if(file.getMimeType().equalsIgnoreCase("application/json")) {
                	 
                	String response = getJsonResponse(driveService, file.getId());
                	System.out.println(response);
                	
                }
               
            }
        }
    }

	private static void getFileNames(Drive driveService) throws IOException {
		FileList result = driveService.files().list().setPageSize(10).execute();
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
              //  if(file.getName().equals("jessy.jpg"))
                //downloadFile(driveService, file.getId());
            }
        }
	}
    
    public static File uploadFile(Drive driveService) throws IOException {
    	
    	File fileMetadata = new File();
    	fileMetadata.setName("clientsecret.json");  
    //	fileMetadata.setParents(Collections.singletonList(createFolder(driveService))); 	
    	java.io.File filePath = new java.io.File("C:\\Users\\phanichandra.d\\Desktop\\client_secret.json");
    	FileContent mediaContent = new FileContent("application/json", filePath);
    	File file = driveService.files().create(fileMetadata, mediaContent)
    	    .setFields("id")
    	    .execute();
    	return file;
    }
    
    public static String createFolder(Drive driveService) throws IOException {
    	File fileMetadata = new File();
    	fileMetadata.setName("driveuploadtest");
    	fileMetadata.setMimeType("application/vnd.google-apps.folder");

    	File file = driveService.files().create(fileMetadata)
    	    .setFields("id")
    	    .execute();
    	return file.getId();
    }
    
    public String getJsonResponse(Drive driveService, String fileId) throws IOException {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    	driveService.files().get(fileId)
    	    .executeMediaAndDownloadTo(outputStream);
    	InputStream in = new ByteArrayInputStream(outputStream.toByteArray());
    	BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
            return buffer.lines().collect(Collectors.joining("\n"));
    	
    } 

}