package com.example.RolebaseAuth.blog;

import com.example.RolebaseAuth.authentication.AuthenticationRequest;
import com.example.RolebaseAuth.authentication.AuthenticationService;
import com.example.RolebaseAuth.blog.comments.CommentModel;
import com.example.RolebaseAuth.blog.comments.CommentRequest;
import com.example.RolebaseAuth.blog.comments.CommentResponse;
import com.example.RolebaseAuth.blog.likes.LikesRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("/api/v1/role_base")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;
    private BlogRequest blogRequest;

    @PostMapping("/uploadBlog")
    public ResponseEntity<Object> uploadBlog(@RequestBody BlogRequest blogRequest, HttpServletResponse response, HttpServletRequest request){
        System.out.println("inside uploadBlog controller");
        return ResponseEntity.ok().body(blogService.CreateBlog(request,  response, blogRequest));
    }

    @GetMapping("/blogComment")
    public ResponseEntity<Object> commentOnBlog(@RequestParam String blogPostId, @RequestBody CommentRequest commentRequest,
                                                HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        System.out.println("inside get blog controller");
        return ResponseEntity.ok().body(blogService.commentOnBlog(blogPostId, commentRequest, servletResponse, servletRequest));
    }

    @DeleteMapping("/deleteComment")
    public ResponseEntity<Object> deleteComment(@RequestParam String blogPostId, @RequestParam String commentId,
                                                HttpServletRequest servletRequest, HttpServletResponse servletResponse
                                                ){
        System.out.println("inside controller to delete comment");
        return ResponseEntity.ok().body(blogService.deleteComment(blogPostId, commentId, servletResponse, servletRequest));
    }

    @PostMapping("/addReply")
    public ResponseEntity<Object> addReply(@RequestParam String parentCommentId, @RequestBody CommentRequest commentRequest,
                                           CommentModel reply,
                                           HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        System.out.println("inside get add reply method of blog controller");
        reply.setComment(commentRequest.getComment());

        return ResponseEntity.ok().body(blogService.addReply(parentCommentId, reply, servletResponse, servletRequest));
    }


    @PostMapping("/likePost")
    public ResponseEntity<Object> likePost(@RequestParam String postId,
                                           @RequestBody LikesRequest likesRequest,
                                           HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        System.out.println("inside like post controller");
        return ResponseEntity.ok().body(blogService.likeBlog(servletResponse, servletRequest, likesRequest, postId));
    }

    @PostMapping("/likeComment")
    public ResponseEntity<Object> likeComment(@RequestParam String commentId,
                                           @RequestBody LikesRequest likesRequest,
                                           HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        System.out.println("inside like comment controller");
        return ResponseEntity.ok().body(blogService.likeComment(servletResponse, servletRequest, likesRequest, commentId));
    }

    @PatchMapping("/addContributor")
    public ResponseEntity<Object> addContributor(@RequestParam String blogpostId,
                                              @RequestBody ContributorRequest contributorRequest,
                                              HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        System.out.println("inside add contributor controller");
        return ResponseEntity.ok().body(blogService.addBlogContributor(contributorRequest, blogpostId, servletResponse, servletRequest));
    }


    @PostMapping("/editBlog")
    public ResponseEntity<Object> editBlog(@RequestParam String blogpostId,
                                                 @RequestBody BlogRequest blogRequest,
                                                 HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        System.out.println("inside edit blog controller method");
        return ResponseEntity.ok().body(blogService.editBlog(blogpostId, blogRequest, servletResponse, servletRequest));
    }


    @GetMapping("/getBlogContributors")
    public ResponseEntity<Object> getBlogContributors(@RequestParam String blogpostId,
                                           HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        System.out.println("inside getBlogContributors controller method");
        return ResponseEntity.ok().body(blogService.getBlogContributors(blogpostId, servletResponse, servletRequest));
    }

}


