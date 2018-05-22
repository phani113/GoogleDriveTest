package com.google.demo.drive;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

@Service
public class GoogleCloudAuthServiceImpl {
	
	@Value("${redirectUri}")
	private String redirectUri;
	
	public static GoogleAuthorizationCodeFlow flow;
	private Credential credential;
	
	public static Drive driveService;
	
	 private static HttpTransport HTTP_TRANSPORT;
	 private static final Set<String> SCOPES = DriveScopes.all();
	 
	 static {
	        try {
	            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();	          
	        } catch (Throwable t) {
	            t.printStackTrace();
	            System.exit(1);
	        }
	    }
	 
	  /** Global instance of the JSON factory. */
	    private static final JsonFactory JSON_FACTORY =
	        JacksonFactory.getDefaultInstance();
	    
	    private static final String APPLICATION_NAME = "GoogleDriveTest";
	

	
	 public String authorize() throws Exception {
			AuthorizationCodeRequestUrl authorizationUrl;
			if (flow == null) {			
				 InputStream in =
				            CommonUitil.class.getResourceAsStream("/client_secret.json");
				 GoogleClientSecrets clientSecrets =
				            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
				
				  flow = new GoogleAuthorizationCodeFlow.Builder(
	                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
	                .setAccessType("offline")
	                .build();			
			}
			authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri);

			System.out.println("drive authorizationUrl ->" + authorizationUrl.build());
			return authorizationUrl.build();
		}

	
	public void getDriveService(String code) {
		try {
			TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
			credential = flow.createAndStoreCredential(response, "userID");
			System.out.println(credential);
			driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
					.build();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}