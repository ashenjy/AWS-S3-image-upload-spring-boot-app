package com.awsrecap.app.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;


@RestController
@RequestMapping("/home/")
public class UploadController {

	@Autowired
	private AmazonS3 s3client;
	
	@Value("${endpointUrl}")
    private String endpointUrl;
	
    @Value("${bucketName}")
    private String bucketName;

    
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile multipartFile) {

    	String fileUrl = "";
    	String  status = null;
    	try {

    		//converting multipart file to file
    		File file = convertMultiPartToFile(multipartFile);

    		//filename
    		String fileName = multipartFile.getOriginalFilename();

    		fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;

    		status = uploadFileTos3bucket(fileName, file);

    		file.delete();

    	} catch (Exception e) {

    		return "UploadController().uploadFile().Exception : " + e.getMessage();

    	}

    	return status + " " +  fileUrl;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
    	File convFile = new File(file.getOriginalFilename());
    	FileOutputStream fos = new FileOutputStream(convFile);
    	fos.write(file.getBytes());
    	fos.close();
    	return convFile;
    }


    private String uploadFileTos3bucket(String fileName, File file) {
    	try {
    		s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
    	}catch(AmazonServiceException e) {
    		return "uploadFileTos3bucket().Uploading failed :" + e.getMessage(); 
    	}
    	return "Uploading Successfull -> ";
    }
    
}
