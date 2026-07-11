package com.example.RolebaseAuth.profilePicture;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProfilePictureModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true, nullable = true)


    private String fileName;
    @Column(nullable = true)
    private String contentType;
    @Column(nullable = true)
   private String pictureS33link;

    public ProfilePictureModel(String fileName, String contentType, String pictureS3Link) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.pictureS33link = pictureS3Link;
    }
}
