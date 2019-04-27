package com.ocdl.client.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;
import com.ocdl.client.Client;
import com.ocdl.client.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.List;

@Component
public class S3Service implements StorageService {
    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);


    @Value("${S3.server.accesskey}")
    private String accesskey;

    @Value("${S3.server.secretkey}")
    private String secretkey;

    private static AmazonS3 s3client;

    public S3Service() {
    }

    @Override
    public void createStorage() {
        if (s3client == null) {

            logger.info("create the S3 services.");
            AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);

            //change the regions if you don't use us_east_virginia
            s3client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.US_EAST_1)
                    .build();
            s3client.setS3ClientOptions(S3ClientOptions.builder().setPathStyleAccess(true).disableChunkedEncoding().build());
        }
    }

    @Override
    public void uploadObject(String bucketName, String fileName, File file) {

        s3client.putObject(new PutObjectRequest(bucketName, fileName,file)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        );
    }

    @Override
    public String getObkectUrl(String bucketName, String objectKey) {

        logger.info("Generating pre-signed URL.");
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.GET);
        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

        logger.info("Pre-Signed URL: " + url.toString());
        return url.toString();
    }


}
