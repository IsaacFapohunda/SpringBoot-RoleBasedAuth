package com.example.RolebaseAuth.model.otp;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class OtpResponse {
        private String id;
        private Integer otp;

        public OtpResponse(
               Integer otp) {

            this.otp = otp;
        }




}
