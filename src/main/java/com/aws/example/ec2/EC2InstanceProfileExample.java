package com.aws.example.ec2;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectResult;

public class EC2InstanceProfileExample {

	static AmazonS3 s3;

	private static void init() throws Exception {
		InstanceProfileCredentialsProvider credentialsProvider = new InstanceProfileCredentialsProvider(true);
		s3 = AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider).build();
	}

	public static void main(String[] args) throws Exception {
		init();
		try {
			String bucketName = "mytestbucket";
			String s3FileName = "simple-file";
			System.out.println("Creating new bucket....");
			Bucket bucket = s3.createBucket(bucketName);
			System.out.println("Bucket created with name: " + bucket.getName());
			System.out.println("Creating a new file under s3 bucket....");
			PutObjectResult result = s3.putObject(bucketName, s3FileName, "Hello world!");
			System.out.println("File created under s3 filesystem : " + result.getMetadata());
			System.out.println("Deleting s3 file object....");
			s3.deleteObject(bucketName, s3FileName);
			System.out.println("S3 object deleted!");
			System.out.println("Deleting S3 bucket....");
			s3.deleteBucket(bucketName);
			System.out.println("Bucket deleted!");
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to AWS, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with AWS, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}

	}
}
