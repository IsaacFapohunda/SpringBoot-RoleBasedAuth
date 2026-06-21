package com.example.RolebaseAuth.model.otp;

import com.example.RolebaseAuth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Otp")
@Entity
@Builder

public class OtpModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private int otp;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    private User user;

    public OtpModel(
            int otp,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            User user) {
        this.otp = otp;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }

}
