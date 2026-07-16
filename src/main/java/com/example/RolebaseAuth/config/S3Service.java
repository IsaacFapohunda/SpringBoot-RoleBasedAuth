package com.example.RolebaseAuth.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Text;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;


@Data
@Service
@RequiredArgsConstructor
public class S3Service {
    //private BitlyShortLink bitlyShortLink;
    private S3Client s3Client;

    private S3Presigner s3Presigner;

    @Value("${aws.access.key.id}") String accessKey;

    @Value("${aws.secret.key.id}") String secretKey;


    @Value("${aws.region}")
    private String region;

    @Value("${bitly.token}")
    private String bitlyToken;

    private StaticCredentialsProvider provider(){
        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(accessKey, secretKey);
        return StaticCredentialsProvider.create(credentials);
    }


    @Value("${aws.s3.bucket.name}")
    private String bucketName;
    //private final String bucketName = "isaacfeppyawsbucket";

    public String uploadS3(MultipartFile file){
        this.s3Client = S3Client.builder().region(Region.of(region)).
        credentialsProvider(provider()).build();
        System.out.println("s3 client initialized");
        this.s3Presigner = S3Presigner.builder().region(Region.of(region)).credentialsProvider(provider()).build();
        System.out.println("s3 presigner successful");

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println(fileName);

        try{
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .contentType(file.getContentType())
                    .key(fileName)
                    .build();
            System.out.println("put object request successful");
            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            System.out.println("file upload successful");

            GetObjectPresignRequest presignRequest= GetObjectPresignRequest.builder()

                    //    .getObjectRequest(builder -> builder.bucket("isaacfeppyawsbucket").key(localfile))

                    .signatureDuration(Duration.ofHours(24))
                 //   .getObjectRequest(builder -> builder.bucket("isaacfeppyawsbucket").key(fileName))
                    .getObjectRequest(builder -> builder.bucket(bucketName).key(fileName))
                    .build();
            System.out.println("presign request ready");

            URL preSignedUrl = s3Presigner.presignGetObject(presignRequest).url();
            System.out.println(preSignedUrl.toString());

            final String url = "https://api-ssl.bitly.com/v4/shorten";
            HttpHeaders header = new HttpHeaders();
            header.set("Authorization", "Bearer " + bitlyToken);
            com.example.RolebaseAuth.config.RequestBody requestBody = new com.example.RolebaseAuth.config.RequestBody(preSignedUrl.toString());
            System.out.println("done here");

            HttpEntity<com.example.RolebaseAuth.config.RequestBody> requestEntity = new HttpEntity<>(requestBody, header);
            RestTemplate restTemplate =  new RestTemplate();
                ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<Map<String, Object>>() {}

                );

            Map<String, Object> responseBody = response.getBody();
            System.out.println("sure");
            //System.out.println(responseBody);
                System.out.println("okay");
                String link= (String) responseBody.get("link");
                System.out.println(link);
                String date_created = (String) responseBody.get("created_at");
                return link;

        } catch (IOException e){
            throw new RuntimeException("Failed to upload file and generate url", e);
        }
    }









////    public S3Service(S3Client s3Client, S3Presigner s3Presigner){
////
////        this.s3Client = s3Client;
////        this.s3Presigner = s3Presigner;
////    }
//
//    public String uploadFileAndGetUrl(MultipartFile file, String bucketName){
//        System.out.println("inside s3 service");
//
//        String uniqueFileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
//        System.out.println(uniqueFileName);
//        System.out.println(s3Client);
//
////        AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(
////
////        )
////        S3Client s3Client = S3Client.builder()
////                .region(region)
////                .credentialsProvider(StaticCredentialsProvider.create(s))
////                .build();
//        try{
//            //create putobjectrequest
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket("isaacfeppyawsbucket")
//                    .key("Region.EU_NORTH_1" + uniqueFileName)
//                    .contentType(file.getContentType())
//                    .build();
//
//            //upload file
//            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
//
//            //generating s pre-signed url for the uploaded file
//            GetObjectPresignRequest presignRequest= GetObjectPresignRequest.builder()
//                    .signatureDuration(Duration.ofHours(24))
//                    .getObjectRequest(builder -> builder.bucket("isaacfeppyawsbucket").key(uniqueFileName))
//                    .build();
//
//            URL preSignedUrl = s3Presigner.presignGetObject(presignRequest).url();
//
//            return preSignedUrl.toString();
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to upload file and generaate url", e);
//        }
//    }
}



