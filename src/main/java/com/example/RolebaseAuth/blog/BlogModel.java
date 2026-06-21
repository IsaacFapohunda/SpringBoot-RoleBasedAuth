package com.example.RolebaseAuth.blog;

import com.example.RolebaseAuth.blog.comments.CommentModel;
import com.example.RolebaseAuth.blog.likes.LikesModel;
import com.example.RolebaseAuth.model.Permission;
import com.example.RolebaseAuth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_blog")
public class BlogModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String blogpostId;
    private String blogTitle;
    private String blogContent;
    private LocalDateTime timeCreated;
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL) //this means one blog post can have many comments
    private List<CommentModel> comments;

    private byte[] blog_image;

   @Column(name = "likes_count")
   private int likesCount = 0;

    @ManyToOne //means many blog post can be made by one users
    private User user;

    List<String> contributors = new ArrayList<>();

}
