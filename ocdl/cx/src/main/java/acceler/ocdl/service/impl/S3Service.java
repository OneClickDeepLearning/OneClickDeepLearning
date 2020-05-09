package acceler.ocdl.service.impl;

import acceler.ocdl.service.StorageService;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.stereotype.Component;
import java.io.File;
import java.net.URL;


@Component
public class S3Service implements StorageService {

    private static AmazonS3 s3client;

    private void createStorage() {

        if (s3client == null) {
            System.out.println("create the S3 services  ====================================");
            //AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);

            // change the regions if you don't use us_east_virginia
            s3client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new DefaultAWSCredentialsProviderChain())
                    .withRegion(Regions.US_EAST_1)
                    .build();
        }
    }


    @Override
    public void uploadObject(String bucketName, String modelName, File file) {

        createStorage();

        s3client.putObject(new PutObjectRequest(bucketName, modelName,file)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        );
    }


    @Override
    public String getObkectUrl(String bucketName, String objectKey) {

        createStorage();
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
