package com.google.demo.drive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@Service
public class GoogleCloudServiceImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCloudServiceImpl.class);

	@Autowired
	private CommonUitil quickstart;

	public String getJsonContent(String fileName) throws IOException {
		FileList result = GoogleCloudAuthServiceImpl.driveService.files().list().setPageSize(10).execute();
		List<File> files = result.getFiles();
		if (files == null || files.size() == 0) {
			return "";
		} else if (files.stream().filter(file -> file.getMimeType().equalsIgnoreCase("application/json")).count() > 0) {

			File jsonfile = files.stream().filter(file -> file.getMimeType().equalsIgnoreCase("application/json"))
					.findFirst().get();
			return quickstart.getJsonResponse(GoogleCloudAuthServiceImpl.driveService, jsonfile.getId());
		}
		return fileName;
	}

	public List<String> getFileNames(int pageSize) throws IOException {
		FileList result = GoogleCloudAuthServiceImpl.driveService.files().list().setPageSize(pageSize).execute();
		List<String> resp = new ArrayList<>();
		List<File> files = result.getFiles();
		if (files == null || files.size() == 0) {
			LOGGER.info("No files found.");
		} else {
			System.out.println("Files:");
			for (File file : files) {
				LOGGER.info("FileName: {}", file.getName());
				resp.add(file.getName());
			}
		}
		return resp;
	}

	public byte[] downloadFile(String fileName) throws IOException {
		String pageToken = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		do {
			FileList result = GoogleCloudAuthServiceImpl.driveService.files().list()
					.setQ("name =" + "'" + fileName + "'").setSpaces("drive")
					.setFields("nextPageToken, files(id, name)").setPageToken(pageToken).execute();
			for (File file : result.getFiles()) {
				GoogleCloudAuthServiceImpl.driveService.files().get(file.getId())
						.executeMediaAndDownloadTo(outputStream);
			}
			pageToken = result.getNextPageToken();
		} while (pageToken != null);
		return outputStream.toByteArray();

	}

}
