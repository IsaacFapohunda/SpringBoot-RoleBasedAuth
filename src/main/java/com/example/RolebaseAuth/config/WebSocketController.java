package com.example.RolebaseAuth.config;


import com.example.RolebaseAuth.blog.BlogResponse;
import com.example.RolebaseAuth.blog.comments.CommentResponse;
import com.example.RolebaseAuth.blog.likes.LikesModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/uploadBlog")
    @SendTo("/role_base/uploadBlog")
    public BlogResponse sendBlogNotification(BlogResponse blogResponse){
        System.out.println("New blog by you");
        return blogResponse;
    }

    @MessageMapping("/blogComment")
    @SendTo("/role_base/blogComment")
    public CommentResponse sendCommentNotification(CommentResponse commentResponse){
        return commentResponse;
    }

    @MessageMapping("/addReply")
    @SendTo("/role_base/addReply")
    public CommentResponse sendReplyNotification(CommentResponse commentResponse){
        return commentResponse;
    }

    @MessageMapping("/likePost")
    @SendTo("/role_base/likePost")
    public LikesModel sendPostLikeNotification(LikesModel likesModel){
        return likesModel;
    }


    @MessageMapping("/likeComment")
    @SendTo("/role_base/likeComment")
    public LikesModel sendCommentLikeNotification(LikesModel likesModel){
        return likesModel;
    }

}
