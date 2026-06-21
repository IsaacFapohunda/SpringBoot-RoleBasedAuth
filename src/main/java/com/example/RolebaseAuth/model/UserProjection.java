package com.example.RolebaseAuth.model;

import lombok.ToString;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProjection {
    String getEmail();

}
