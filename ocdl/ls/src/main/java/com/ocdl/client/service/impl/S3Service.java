package com.ocdl.client.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import com.amazonaws.services.s3.model.*;
import com.ocdl.client.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.List;

@Component
public class S3Service implements StorageService {

    private String accesskey;
    private String secretkey;

    private static AmazonS3 s3client;

    public S3Service() {
    }

    @Value("${S3.server.accesskey}")
    public void setAccesskey(String accesskey) { this.accesskey = accesskey; }

    @Value("${S3.server.secretkey}")
    public void setSecretkey(String secretkey) { this.secretkey = secretkey; }

    @Override
    public void createStorage() {

        if (s3client == null) {
//            accesskey = "AKIAJMVONNFPI6FOUNUQ";
//            secretkey = "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";

            System.out.println("create the S3 services  ====================================");
            AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);

            //change the regions if you don't use us_east_virginia
            s3client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.US_EAST_1)
                    .build();
        }
    }

    public void listBucket() {
        List<Bucket> buckets = s3client.listBuckets();
        for(Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }
    }

    public void createBucket(String bucketName) {

        if(s3client.doesBucketExist(bucketName)) {
            System.out.println("Bucket name is not available."
                    + " Try again with a different Bucket name.");
            return;
        }

        s3client.createBucket(bucketName);
    }

    public void deleteBucket(String bucketName) {
        try {
            s3client.deleteBucket(bucketName);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            return;
        }
    }

    @Override
    public void uploadObject(String bucketName, String fileName, File file) {

        s3client.putObject(new PutObjectRequest(bucketName, fileName,file)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        );
    }

    public void listObject(String bucketName) {
        ObjectListing objectListing = s3client.listObjects(bucketName);
        for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
            System.out.println(os.getKey());
        }
    }

    @Override
    public String getObkectUrl(String bucketName, String objectKey) {
        // Generate the presigned URL.
        System.out.println("Generating pre-signed URL.");
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.GET);
        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

        System.out.println("Pre-Signed URL: " + url.toString());
        return url.toString();
    }


}
