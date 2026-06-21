package com.example.RolebaseAuth.profilePicture;

import com.example.RolebaseAuth.blog.BlogModel;
import com.example.RolebaseAuth.config.S3Service;
import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.payloads.BaseServerResponse;
import com.example.RolebaseAuth.repository.PermissionRepository;
import com.example.RolebaseAuth.repository.RoleRepository;
import com.example.RolebaseAuth.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class ProfilePictureService {

    private final UserRepository userRepository;
    private final ProfilePictureRepo profilePictureRepo;
    private final S3Service s3Service;


    public BaseServerResponse s3SaveLinkToDb(String userId, MultipartFile file){
        System.out.println("inside uploadDp service");
        BaseServerResponse serverResponse = new BaseServerResponse<>();
      try{
          String pictureLink = s3Service.uploadS3(file);
          System.out.println(pictureLink + "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^66");
          String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        byte[] imageBytes = file.getBytes();
          ProfilePictureModel profilePicture = new ProfilePictureModel(fileName, file.getContentType(), pictureLink);
          profilePictureRepo.save(profilePicture);
          System.out.println(profilePicture);
          Optional<User> user = userRepository.findById(userId);

          user.get().setProfilePicture(profilePicture);
          userRepository.save(user.get());
          System.out.println(user);
          serverResponse.setResponseCode("200");
          serverResponse.setResponseData(pictureLink);
          serverResponse.setSuccess(true);
          return serverResponse;
      } catch (Exception e){
          serverResponse.setResponseCode("200");
          serverResponse.setResponseMessage("Unable to upload file" + e);
          serverResponse.setSuccess(true);
          return serverResponse;
      }
    }




    public BaseServerResponse getFile(String id){
        System.out.println("inside get file service");
        BaseServerResponse serverResponse = new BaseServerResponse<>();
        System.out.println("server");
       Optional<ProfilePictureModel> fileModel =  profilePictureRepo.findById(id);
        System.out.println("icic");
        System.out.println(fileModel);
        if (fileModel.isPresent()) {
            ProfilePictureModel file = fileModel.get();
            System.out.println("found");
            Map<String, String> fileContent =  new HashMap<>();
            String link = file.getPictureS33link();
            String fileName = file.getFileName();
            fileContent.put("PictureLink", link);
            fileContent.put("PictureName", fileName);
            serverResponse.setSuccess(true);
            serverResponse.setResponseMessage("Successfull");
            serverResponse.setResponseCode("200");
            serverResponse.setResponseData(fileContent);
            return serverResponse;
        } else {
            serverResponse.setResponseMessage("failed");
            serverResponse.setResponseCode("400");
            return serverResponse;
        }


    }



}
