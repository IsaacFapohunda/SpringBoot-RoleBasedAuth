package com.example.RolebaseAuth.AuthenticationToken;

import com.example.RolebaseAuth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authToken")
public class AuthTokenModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "access_token",
            columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "refresh_token",
            columnDefinition = "TEXT")
    private String refreshToken;

    @Column(name = "created at")
    private LocalDateTime createdDate;

    @Column(name = "expiry date")
    private LocalDateTime expiryDate;



    @OneToOne
    @JoinColumn(name = "user_id")
   private User user;

    public AuthTokenModel(
            String accessToken,
            String refreshToken,
            LocalDateTime expiryDare,
            LocalDateTime createdDate){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.createdDate = LocalDateTime.now();
        this.expiryDate = createdDate.plusMinutes(20);

    }


}
