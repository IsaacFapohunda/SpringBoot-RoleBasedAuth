package com.example.RolebaseAuth.blog.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentModel, String> {
    Optional<CommentModel> findById(String id);



    @Transactional
    @Modifying
    @Query("UPDATE CommentModel b SET b.likesCount = b.likesCount + 1 WHERE b.id = :id")
    void incrementCommentLikesCount(@Param("id") String id);
}
