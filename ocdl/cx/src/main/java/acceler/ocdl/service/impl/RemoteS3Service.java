package acceler.ocdl.service.impl;


import acceler.ocdl.service.StorageService;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class RemoteS3Service implements StorageService {

    // todo: modify the accesskey and secretkey
    private String accesskey;
    private String secretkey;

    private static AmazonS3 s3client;

    public RemoteS3Service() {
    }

    @Value("S3.server.accesskey")
    public void setAccesskey(String accesskey) { this.accesskey = accesskey; }

    @Value("S3.server.secretkey")
    public void setSecretkey(String secretkey) { this.secretkey = secretkey; }

    @Override
    public void createStorage() {

        if (s3client == null) {
            System.out.println("create the S3 services  ====================================");
            AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);

            // todo: change the regions if you don't use us_east_virginia
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
    public void uploadObject(String bucketName, String modelName, File file) {

        s3client.putObject(new PutObjectRequest(bucketName, modelName,file)
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

    @Override
    public void downloadObject(String bucketName, String ObjectKey) {

        S3Object s3object = s3client.getObject(bucketName, ObjectKey);
        S3ObjectInputStream inputStream = s3object.getObjectContent();

        try {
            String aimPath = "";
            Path path = Paths.get(aimPath, ObjectKey);
            FileUtils.copyInputStreamToFile(inputStream, new File(((Path) path).toString()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
