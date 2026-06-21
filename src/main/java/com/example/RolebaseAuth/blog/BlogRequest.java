package com.example.RolebaseAuth.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogRequest {

    private String blogTitle;
    private String blogContent;

}
