package com.example.RolebaseAuth.blog.comments;

import com.example.RolebaseAuth.blog.BlogModel;
import com.example.RolebaseAuth.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_comment")
public class CommentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "blogpostId")
    private BlogModel blog;

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
