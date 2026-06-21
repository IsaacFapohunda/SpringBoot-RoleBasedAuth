package com.example.RolebaseAuth.blog.likes;

import com.example.RolebaseAuth.blog.BlogModel;
import com.example.RolebaseAuth.blog.comments.CommentModel;
import com.example.RolebaseAuth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "blog_id", "comment_Id"})
})

public class LikesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

   private Boolean isLiked;

  @ManyToOne //one like can be made by one user
  @JoinColumn(name = "user_Id", nullable = false)
  private User user;

  //lets just use blogid and userid so as to keep the likes entity lightweight for simple retrieval and storage, unlike  blog and comment
  @ManyToOne
  @JoinColumn(name = "blog_Id")

  private BlogModel blog;

    @ManyToOne
    @JoinColumn(name = "comment_Id")
    private CommentModel comment;



    private LocalDateTime likedAt = LocalDateTime.now();


}
