package com.example.RolebaseAuth.admin;

import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.payloads.BaseServerResponse;
import com.example.RolebaseAuth.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@AllArgsConstructor
public class AdminService {
        private final UserRepository userRepository;

    public BaseServerResponse<Object> DeleteUser(AdminRequest adminRequest){
        BaseServerResponse serverResponse = new BaseServerResponse<>();
   User user = userRepository.findByEmail(adminRequest.getEmail())
           .orElseThrow(()-> new RuntimeException("User not found"));
    if(!Objects.equals(adminRequest.getEmail(), user.getEmail())){
        serverResponse.setSuccess(false);
        serverResponse.setResponseCode("500");
        serverResponse.setResponseMessage("Wrong email");
        return serverResponse;
    }
   userRepository.delete(user);
        serverResponse.setSuccess(true);
        serverResponse.setResponseCode("200");
        serverResponse.setResponseMessage("User details successfully deleted");
        return serverResponse;

    }
}
