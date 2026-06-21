package com.example.RolebaseAuth.blog.likes;
import com.example.RolebaseAuth.blog.BlogModel;
import com.example.RolebaseAuth.blog.comments.CommentModel;
import com.example.RolebaseAuth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<LikesModel, String> {
    Optional<LikesModel> findByUserAndBlog(User user, BlogModel blogModel);

    //Optional<LikesModel> findByUserAndComment(User user, CommentModel comment);
}
