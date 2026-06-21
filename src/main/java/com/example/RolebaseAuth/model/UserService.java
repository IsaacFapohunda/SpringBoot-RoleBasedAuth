package com.example.RolebaseAuth.model;

import com.example.RolebaseAuth.enums.PermissionName;
import com.example.RolebaseAuth.enums.RoleName;
import com.example.RolebaseAuth.model.Dp.DpRequest;
import com.example.RolebaseAuth.model.otp.OtpModel;
import com.example.RolebaseAuth.model.otp.OtpService;
import com.example.RolebaseAuth.model.password.ChangePasswordRequest;
import com.example.RolebaseAuth.payloads.BaseServerResponse;
import com.example.RolebaseAuth.profilePicture.ProfilePictureModel;
import com.example.RolebaseAuth.profilePicture.ProfilePictureRepo;
import com.example.RolebaseAuth.repository.PermissionRepository;
import com.example.RolebaseAuth.repository.RoleRepository;
import com.example.RolebaseAuth.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ProfilePictureRepo profilePictureRepo;


    public User loadUserByUsername(String username){
        return userRepository.findByEmail(username)
                .orElseThrow(()->new RuntimeException("User not found"));
    }

    public User getUserById(String id) {
        log.info("user service here baby");
        BaseServerResponse serverResponse = new BaseServerResponse();
        boolean userExists = userRepository.findById(id).isPresent();
        if(!userExists){
            serverResponse.setSuccess(false);
            serverResponse.setResponseCode("500");
            serverResponse.setResponseMessage("User does not exist");
           // String jsonResponse = objectMapper.writeValueAsString(serverResponse);
        }

        Optional<User> optionalUser = userRepository.findById(id);

            return optionalUser.get();

    }


    public int enableAppUser(String email){
        return userRepository.enableAppUser(email);
    }

    public String ChangePassword(HttpServletRequest request, ChangePasswordRequest changePasswordRequest){
        String email = (String) request.getAttribute("email");
      Optional<User> currentUser = userRepository.findByEmail(email);
        if(email!=null){

            if(!changePasswordRequest.getCurrentPassword().equals(currentUser.get().getPassword())){
                 throw new IllegalStateException("Wrong password");
            }
            if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmationPassword())){
                throw new IllegalStateException("Password are not the same");
            }

            currentUser.get().setPassword(changePasswordRequest.getNewPassword());
            log.info(currentUser.get().getPassword());
            userRepository.save(currentUser.get());
            return "Password Sucessfully updated";
        }
        else {
            return "User unknown";
        }
    }



    //There are certain benefits of being an admin on this blog, apply below
    //As an admin you can also edit blog information
    //Do you wish to make .. an admin on your blog, if so Make ... an admin on your blog
    //if i make them
    //make a table that has the blogId and the person
    //on blogModel i have contributors, its a list so when i add someone, i add them// by their userId to the list so i check if they are there and hence
    //can make edit on blog or same things i do



}
