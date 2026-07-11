package com.example.RolebaseAuth.profilePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilePictureRepo extends JpaRepository<ProfilePictureModel, String> {
    Optional<ProfilePictureModel> findById(String id);

}
