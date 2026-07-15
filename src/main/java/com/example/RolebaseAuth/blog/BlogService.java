package com.example.RolebaseAuth.blog;

import com.example.RolebaseAuth.blog.comments.CommentModel;
import com.example.RolebaseAuth.blog.comments.CommentRepository;
import com.example.RolebaseAuth.blog.comments.CommentRequest;
import com.example.RolebaseAuth.blog.comments.CommentResponse;
import com.example.RolebaseAuth.blog.likes.LikesModel;
import com.example.RolebaseAuth.blog.likes.LikesRepository;
import com.example.RolebaseAuth.blog.likes.LikesRequest;
import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.model.UserDTO;
import com.example.RolebaseAuth.model.UserProjection;
import com.example.RolebaseAuth.payloads.BaseServerResponse;
import com.example.RolebaseAuth.repository.UserRepository;
import com.example.RolebaseAuth.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Data
@Slf4j
public class BlogService {
    private BlogModel blogModel;
    private final BlogRepository blogRepository;
    private BlogRequest blogRequest;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;

   private final SimpMessagingTemplate messagingTemplate;



    public BaseServerResponse CreateBlog(HttpServletRequest request, HttpServletResponse response, BlogRequest blogRequest){
        final String jwtToken;
        final String email;
        final String authHeader = request.getHeader("Authorization");
        BaseServerResponse serverResponse = new BaseServerResponse<>();
        jwtToken = authHeader.substring(7);
      try{
          System.out.println("inside uploadBlog service");
          email = jwtService.extractUserName(jwtToken);
          Optional<User> user = userRepository.findByEmail(email);
          System.out.println("user found by email");

          BlogModel blogModel = new BlogModel();
          blogModel.setBlogContent(blogRequest.getBlogContent());
          blogModel.setBlogTitle(blogRequest.getBlogTitle());
          blogModel.setUser(user.get());
       // blogModel.setTimeCreated(LocalDateTime.now());
          System.out.println("blog request");
          blogRepository.save(blogModel);
          System.out.println("blog request saved");

          CommentResponse commentResponse = new CommentResponse();

          System.out.println("commen");
          UserDTO userDTO = new UserDTO();
          System.out.println("user dto initialized");
          userDTO.setId(user.get().getId());
          System.out.println("user");
          userDTO.setEmail(user.get().getEmail());
          System.out.println("dto initialized");
          userDTO.setFirstName(user.get().getFirstName());
          System.out.println("user dto ");
          //userDTO.setProfilePicture(user.get().getProfilePicture().getPictureS33link());
          System.out.println("userdto");
          BlogResponse blogResponse = new BlogResponse();
          blogResponse.setBlogpostId(blogModel.getBlogpostId());
          blogResponse.setBlogTitle(blogModel.getBlogTitle());
          blogResponse.setBlogContent(blogModel.getBlogContent());
          blogResponse.setTimeCreated(blogModel.getTimeCreated());
          blogResponse.setUpdatedAt(blogModel.getUpdatedAt());
          //blogResponse.getComments().add(commentResponse);
          blogResponse.setBlog_image(blogModel.getBlog_image());
          blogResponse.setLikesCount (blogModel.getLikesCount());
          blogResponse.setUserDTO(userDTO);



          serverResponse.setResponseMessage("Successful");
          serverResponse.setResponseData(blogResponse);

         messagingTemplate.convertAndSend("/role_base/uploadBlog", blogResponse);
          System.out.println("boy");
          return serverResponse;
      } catch (Exception e){
          serverResponse.setResponseMessage("failedd");
          return serverResponse;
      }

    }




    public BaseServerResponse commentOnBlog(String blogpostId, CommentRequest commentRequest, HttpServletResponse servletResponse, HttpServletRequest servletRequest){
        final String jwtToken;
        final String email;
        final String authHeader = servletRequest.getHeader("Authorization");
        jwtToken = authHeader.substring(7);
        BaseServerResponse serverResponse = new BaseServerResponse<>();

       try{

           email = jwtService.extractUserName(jwtToken);
           Optional<User> user = userRepository.findByEmail(email);
           System.out.println("user found in the repo");

           BlogModel blog = blogRepository.findById(blogpostId).orElseThrow();
           System.out.println("userblog found in the repo");

           CommentModel commentModel = new CommentModel();
           commentModel.setComment(commentRequest.getComment());
           commentModel.setCommentee(user.get());


           CommentModel comments = commentRepository.save(commentModel);
           System.out.println("comment successfully saved");

           System.out.println(comments);

           UserDTO userDTO = new UserDTO();
           userDTO.setEmail(blog.getUser().getEmail());
           userDTO.setId(blog.getUser().getId());
           userDTO.setFirstName(blog.getUser().getFirstName());
           userDTO.setProfilePicture(blog.getUser().getProfilePicture().getPictureS33link());

           UserDTO newCommentUserDTO = new UserDTO();

           newCommentUserDTO.setId(comments.getCommentee().getId());
           newCommentUserDTO.setEmail(comments.getCommentee().getEmail());
           newCommentUserDTO.setFirstName(comments.getCommentee().getFirstName());
           newCommentUserDTO.setProfilePicture(comments.getCommentee().getProfilePicture().getPictureS33link());


           CommentResponse commentResponse = new CommentResponse();
           commentResponse.setId(comments.getId());
           commentResponse.setLikesCount(comments.getLikesCount());
           commentResponse.setComment(comments.getComment());
           commentResponse.setDateEdited(comments.getDateEdited());
           commentResponse.setDateCreated(comments.getDateCreated());
           commentResponse.setUserDTO(newCommentUserDTO);


           List<CommentResponse> commentResponseList = blog.getComments().stream()
                   .map(existingComment -> {
                       CommentResponse existingCommentResponse = new CommentResponse();

                       existingCommentResponse.setId(existingComment.getId());
                       existingCommentResponse.setLikesCount(existingComment.getLikesCount());
                       existingCommentResponse.setComment(existingComment.getComment());
                       existingCommentResponse.setDateEdited(existingComment.getDateEdited());
                       existingCommentResponse.setDateCreated(existingComment.getDateCreated());

                       UserDTO existingCommentUserDTO = new UserDTO();

                       existingCommentUserDTO.setId(existingComment.getCommentee().getId());
                       existingCommentUserDTO.setEmail(existingComment.getCommentee().getEmail());
                       existingCommentUserDTO.setFirstName(existingComment.getCommentee().getFirstName());
                       existingCommentUserDTO.setProfilePicture(existingComment.getCommentee().getProfilePicture().getPictureS33link());

                       existingCommentResponse.setUserDTO(existingCommentUserDTO);


                       return existingCommentResponse;

                   })
                           .collect(Collectors.toList());


           commentResponseList.add(commentResponse);


           blog.getComments().add(comments);
           BlogModel blogcontent = blogRepository.save(blog);


           BlogResponse blogResponse = new BlogResponse();

           blogResponse.setBlogpostId(blogcontent.getBlogpostId());
           blogResponse.setBlogTitle(blogcontent.getBlogTitle());
           blogResponse.setBlogContent(blogcontent.getBlogContent());
           blogResponse.setTimeCreated(blogcontent.getTimeCreated());
           blogResponse.setUpdatedAt(blogcontent.getUpdatedAt());
           blogResponse.setCommentsResponse(commentResponseList);
           blogResponse.setBlog_image(blogcontent.getBlog_image());
           blogResponse.setLikesCount(blogcontent.getLikesCount());
           blogResponse.setUserDTO(userDTO);


           serverResponse.setResponseData(blogResponse);
           messagingTemplate.convertAndSend("/role_base/blogComment", blogResponse);

           return serverResponse;
       }
       catch (Exception e){
           serverResponse.setResponseMessage("FAILED TO FETCH BLOG");
           return serverResponse;
       }
    }




    public BaseServerResponse deleteComment(String blogpostId,  String commentId, HttpServletResponse servletResponse,
                                            HttpServletRequest servletRequest){
        final String jwtToken;
        final String email;
        final String authHeader = servletRequest.getHeader("Authorization");
        jwtToken = authHeader.substring(7);
        BaseServerResponse serverResponse = new BaseServerResponse<>();
        email = jwtService.extractUserName(jwtToken);
        Optional<User> user = userRepository.findByEmail(email);
        Optional<BlogModel> blog = blogRepository.findById(blogpostId);
       boolean isPresent =  blogRepository.findByblogpostIdAndUser(blogpostId, user.get()).isPresent();
       Optional<CommentModel> comment = commentRepository.findById(commentId);
       if(!isPresent){
           serverResponse.setSuccess(false);
           serverResponse.setResponseMessage("You don't have permission to delete comment");
           return serverResponse;
       }
       if(blog.isPresent() && comment.isPresent()){
           BlogModel blogModel = blog.get();
           blog.get().getComments().removeIf(c -> c.getId().equals(commentId));
          // blog.get().getComments().remove(comment); or simply this
           blogRepository.save(blogModel);
       }


        commentRepository.delete(comment.get());
        serverResponse.setSuccess(true);
        serverResponse.setResponseMessage("The comment have been deleted successfully");
        serverResponse.setResponseData(blogModel);
        return serverResponse;


    }


    public BaseServerResponse addReply(String parentCommentId,
                                       CommentModel reply,
                                       HttpServletResponse servletResponse,
                                       HttpServletRequest servletRequest){

        System.out.println("inside add reply service");
        final String jwtToken;
        final String email;
        final String authHeader = servletRequest.getHeader("Authorization");
        jwtToken = authHeader.substring(7);
        BaseServerResponse serverResponse = new BaseServerResponse<>();
        Optional<CommentModel> parentComment = commentRepository.findById(parentCommentId);

        try{
            email = jwtService.extractUserName(jwtToken);
            Optional<User> user = userRepository.findByEmail(email);

            reply.setParentComment(parentComment.get());
            reply.setCommentee(user.get());

            CommentModel savedReply = commentRepository.save(reply);
            parentComment.get().getReplies().add(savedReply);
            CommentModel savedParentComment = commentRepository.save(parentComment.get());


            System.out.println(".......");

            CommentResponse commentResponse = mapToCommentResponse(savedParentComment);


            List<CommentResponse> replyResponseList = savedParentComment.getReplies().stream()
                            .map(this::mapToCommentResponse)
                    .collect(Collectors.toList());
            commentResponse.setReply(replyResponseList);


            savedParentComment.getReplies().add(reply);


             serverResponse.setSuccess(true);
            serverResponse.setResponseMessage("Reply sent");
            serverResponse.setResponseData(commentResponse);
            return serverResponse;
        } catch (Exception e){
            e.printStackTrace();
            serverResponse.setSuccess(false);
            serverResponse.setResponseMessage("Failed to send reply");
            return serverResponse;
        }

    }

    public CommentResponse mapToCommentResponse(CommentModel comment){
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setLikesCount(comment.getLikesCount());
        response.setComment(comment.getComment());
        response.setDateEdited(comment.getDateEdited());
        response.setDateCreated(comment.getDateCreated());



        UserDTO userDTO = new UserDTO();


        User commentee = comment.getCommentee();
        if(commentee != null) {
            userDTO.setId(comment.getCommentee().getId());
            userDTO.setEmail(comment.getCommentee().getEmail());
            userDTO.setFirstName(comment.getCommentee().getFirstName());
            userDTO.setProfilePicture(comment.getCommentee().getProfilePicture().getPictureS33link());
            response.setUserDTO(userDTO);

            System.out.println(response);
        }

        return response;


    }

    public String likeBlog(HttpServletResponse servletResponse,
                           HttpServletRequest servletRequest, LikesRequest likesRequest, String postId){
        final String jwtToken;
        final String email;
        final String authHeader = servletRequest.getHeader("Authorization");
        jwtToken = authHeader.substring(7);
        BaseServerResponse serverResponse = new BaseServerResponse<>();

        email = jwtService.extractUserName(jwtToken);
        Optional<User> user = userRepository.findByEmail(email);

        System.out.println("got user");
        //do that if user is not authenticated he cant even access ...
        Optional<BlogModel> blogModel = blogRepository.findById(postId);
        System.out.println("got blogmodel");
        System.out.println(postId + "Blog model id");
        //Optional<CommentModel> commentModel = commentRepository.findById(postId);

        LikesModel likesModel = new LikesModel();
        likesModel.setIsLiked(likesRequest.getIsLiked());
        likesModel.setUser(user.get());
        likesModel.setBlog(blogModel.get());

        likesRepository.save(likesModel);

        blogRepository.incrementLikesCount(postId);

        messagingTemplate.convertAndSend("/role_base/likePost", likesModel);

        return "Blog liked successfully";

        //its inthe controller when i call each endpoint for blog or comment that i will now pass

    }


    public String likeComment(HttpServletResponse servletResponse,
                           HttpServletRequest servletRequest, LikesRequest likesRequest, String commentId){
        final String jwtToken;
        final String email;
        final String authHeader = servletRequest.getHeader("Authorization");
        jwtToken = authHeader.substring(7);
        BaseServerResponse serverResponse = new BaseServerResponse<>();

        email = jwtService.extractUserName(jwtToken);
        Optional<User> user = userRepository.findByEmail(email);

        System.out.println("got user");
        //do that if user is not authenticated he cant even access ...
        Optional<CommentModel> commentModel = commentRepository.findById(commentId);

        LikesModel likesModel = new LikesModel();
        likesModel.setIsLiked(likesRequest.getIsLiked());
        likesModel.setUser(user.get());
        likesModel.setComment(commentModel.get());

        likesRepository.save(likesModel);

        commentRepository.incrementCommentLikesCount(commentId);

        return "comment liked successfully";

    }


    public BaseServerResponse addBlogContributor(ContributorRequest contributorRequest, String blogpostId, HttpServletResponse servletResponse,
                                                 HttpServletRequest servletRequest){
        final String jwtToken;
        final String email;
        final String authHeader = servletRequest.getHeader("Authorization");
        jwtToken = authHeader.substring(7);
        BaseServerResponse serverResponse = new BaseServerResponse<>();

        email = jwtService.extractUserName(jwtToken);
        Boolean userIsPresent = userRepository.findByEmail(email).isPresent();
        System.out.println(userIsPresent);
        if(!userIsPresent){
            serverResponse.setSuccess(false);
            serverResponse.setResponseMessage("User not present");
            servletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        Optional<User> contributor = userRepository.findByEmail(contributorRequest.getContributorEmail());
        System.out.println(contributor.get());
        System.out.println(contributorRequest.getContributorEmail() + "????????????????????");
        if(userIsPresent && contributor.isPresent()){
            System.out.println("both are present");
            Optional<BlogModel> blogModel = blogRepository.findById(blogpostId);
            BlogModel blog = blogModel.get();
            System.out.println("we got blog");
            if(blog.getContributors().contains(contributorRequest.getContributorEmail())){
                System.out.println("if email already exist in blog");
                serverResponse.setSuccess(false);
                serverResponse.setResponseMessage("This user is already a contributor on your blog");
                servletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            }
          else {
                blog.getContributors().add(contributorRequest.getContributorEmail());
                System.out.println(blog.getContributors() + "contributor email added successfully");
                blogRepository.save(blog);
                System.out.println("blog saved");
                serverResponse.setSuccess(true);
                serverResponse.setResponseMessage(contributorRequest.getContributorEmail() + " has been added as a contributor on your blog");

            }
        }
        return serverResponse;
    }


    public BaseServerResponse editBlog(String blogpostId, BlogRequest blogRequest, HttpServletResponse servletResponse,
                                       HttpServletRequest servletRequest){
        final String jwtToken;
        final String email;
        final String authHeader = servletRequest.getHeader("Authorization");
        jwtToken = authHeader.substring(7);
        BaseServerResponse serverResponse = new BaseServerResponse<>();

        email = jwtService.extractUserName(jwtToken);
        Boolean userIsPresent = userRepository.findByEmail(email).isPresent();
        Optional<BlogModel> blogmodel = blogRepository.findById(blogpostId);

        BlogModel blog = blogmodel.get();
        Optional<User> user = userRepository.findByEmail(email);
       if(blogmodel.isPresent()){
           if(blog.getContributors().contains(user.get().getEmail())){
               blog.setBlogContent(blogRequest.getBlogContent());
               blogRepository.save(blog);
               System.out.println("here we are");
           }
           else {
               blog.setBlogContent(blogRequest.getBlogContent());
               blog.setBlogTitle(blogRequest.getBlogTitle());

               blogRepository.save(blog);
               serverResponse.setSuccess(true);
               serverResponse.setResponseMessage("Blog content updated");
               servletResponse.setStatus(HttpServletResponse.SC_OK);

           }
       }
       else{
           serverResponse.setSuccess(false);
           serverResponse.setResponseMessage("Failed to update blog");
           servletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);

       }
        return  serverResponse;
    }


    public BaseServerResponse getBlogContributors(String blogpostId, HttpServletResponse servletResponse,
                                                 HttpServletRequest servletRequest){
        final String jwtToken;
        final String email;
        final String authHeader = servletRequest.getHeader("Authorization");
        jwtToken = authHeader.substring(7);
        BaseServerResponse serverResponse = new BaseServerResponse<>();

        email = jwtService.extractUserName(jwtToken);
        Boolean userIsPresent = userRepository.findByEmail(email).isPresent();
        if(userIsPresent){
            Optional<BlogModel> blogModel = blogRepository.findById(blogpostId);
            BlogModel blog = blogModel.get();
            blog.getContributors();
            if(blog.getContributors().isEmpty()){
                serverResponse.setSuccess(true);
                serverResponse.setResponseMessage("No contributor on your blog");
                servletResponse.setStatus(HttpServletResponse.SC_OK);
            }
            else{
                serverResponse.setSuccess(true);
                serverResponse.setResponseMessage("Contributor fetched");
                serverResponse.setResponseData(blog.getContributors());
                servletResponse.setStatus(HttpServletResponse.SC_OK);
            }
            return serverResponse;

        } else{
            serverResponse.setSuccess(false);
            serverResponse.setResponseMessage("User not present");
            servletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return serverResponse;

        }

    }

}


