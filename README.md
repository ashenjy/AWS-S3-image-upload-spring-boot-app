# AWS-S3-image-upload-spring-boot-app

A java spring-boot photo uploading app which saves all the photos uploaded from a simple UI to an AWS S3 Bucket. 

Below AWS services are used to achieve the functionality.
1. AWS EC2
2. AWS S3
3. AWS IAM
4. AWS CodeCommit
5. AWS SDK for Java

## Explanation

The photo uploading system is hosted in a t2.micro AWS EC2 instance. And this application runs on port 8080.

When the user uploads the images through the application UI, all the images are saved in an AWS S3 bucket named "AshenTestawsbucket"

AWS IAM service is used in order to enable the web application to access AWS services via an IAM programmatic user. That user  is set to a ‘Group’ named ‘S3_App_User’ which has 'AmazonS3FullAccess'

AWS SDK for Java is used in order to upload the images to AWS S3. Below is the maven dependency for the aws java client.

```
<dependency>
		   <groupId>com.amazonaws</groupId>
		   <artifactId>aws-java-sdk</artifactId>
		   <version>1.11.106</version>
</dependency>
```

To upload a file, AmazonS3.putObject() method is used.

 After a file has been uploaded by the user through the UI, the file will be received to /home/uploadFile path as a multipart POST request 
 
```java
 @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile multipartFile) {
```
 
 Thereafter, the 'org.springframework.web.multipart.MultipartFile' is converted to 'java.io.file' using the below code since the PutObjectRequest(String bucketName, String key, File file) use java.io.File.
 
```java
 File convFile = new File(file.getOriginalFilename());
 FileOutputStream fos = new FileOutputStream(convFile);
 fos.write(file.getBytes());
 fos.close();
```
 
 Next, we use the putObject() method which includes s3 bucket name (taken from application.properties file), file name and converted file as params to upload the file to the specified amazon s3 bucket.
 
```java
s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
```

## Testing Steps and Commands used

1. added ssh port 22 connection of the ip address of my computer to the security group of the ec2 instance. 

2. Copied the aws-image upload app's jar file to the ec2 instances using below commands

```
    sudo scp -i awsPrivateKey.pem target/aws-assignment-s3-0.0.1-SNAPSHOT.jar ubuntu@13.126.13.229:/var/tmp
    sudo ssh -i awsPrivateKey.pem ubuntu@13.126.13.229
    Copied tmp folder to home folder
    cp /var/tmp/aws-assignment-s3-0.0.1-SNAPSHOT.jar .
 ```
    
3. Run the java app using below command

  ``` java -jar aws-assignment-s3-0.0.1-SNAPSHOT.jar```
   
4. when login in next time, Don't forget to change the ip address of your computer in the security group section of ec2 instance port 22.
 




