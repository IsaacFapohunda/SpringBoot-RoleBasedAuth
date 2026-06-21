package com.example.RolebaseAuth.blog.comments;

import com.example.RolebaseAuth.blog.likes.LikesModel;
import com.example.RolebaseAuth.model.UserDTO;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private String id;
    private LocalDateTime dateCreated;
    private LocalDateTime dateEdited;
    private String comment;
    private int likesCount = 0;
    private UserDTO userDTO;
    private List<CommentResponse> reply;
}
