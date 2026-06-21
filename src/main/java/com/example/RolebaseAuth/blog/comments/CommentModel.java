package com.example.RolebaseAuth.blog.comments;

import com.example.RolebaseAuth.blog.likes.LikesModel;
import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.model.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_comment")
public class CommentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDateTime dateCreated;
    private LocalDateTime dateEdited;
    private String comment;

    private int likesCount = 0;

    @ManyToOne //this means many comments can be made by one user
    private User commentee;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentModel> replies = new ArrayList<>();

    @ManyToOne //Many replies can have one parent comment
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnore
    private CommentModel parentComment;

}
