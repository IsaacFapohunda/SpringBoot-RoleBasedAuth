package com.example.RolebaseAuth.blog;

import com.example.RolebaseAuth.blog.comments.CommentModel;
import com.example.RolebaseAuth.blog.comments.CommentResponse;
import com.example.RolebaseAuth.blog.likes.LikesModel;
import com.example.RolebaseAuth.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponse {
    private String blogpostId;
    private String blogTitle;
    private String blogContent;
    private LocalDateTime timeCreated;
    private LocalDateTime updatedAt;
    private List<CommentResponse> commentsResponse;
    private byte[] blog_image;
    private int likesCount = 0;
    private UserDTO userDTO;



}
