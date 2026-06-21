package com.example.RolebaseAuth.blog;

import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.model.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<BlogModel, String> {

    Optional<BlogModel> findById(String id);


    Optional<BlogModel> findByblogpostIdAndUser(String blogpostId, User user);


    @Transactional
    @Modifying
    @Query("UPDATE BlogModel b SET b.likesCount = b.likesCount + 1 WHERE b.id = :blogpostId")
    void incrementLikesCount(@Param("blogpostId") String blogpostId);



}
