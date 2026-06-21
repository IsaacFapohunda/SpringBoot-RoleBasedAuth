package com.example.RolebaseAuth.blog.likes;
import com.example.RolebaseAuth.blog.BlogModel;
import com.example.RolebaseAuth.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class LikesRequest {
    private Boolean isLiked;


}
