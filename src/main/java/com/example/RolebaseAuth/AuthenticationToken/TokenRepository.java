package com.example.RolebaseAuth.AuthenticationToken;

import com.example.RolebaseAuth.AuthenticationToken.AuthTokenModel;
import com.example.RolebaseAuth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<AuthTokenModel, String> {

    Optional<AuthTokenModel> findByrefreshToken(String refreshToken);

    Optional<AuthTokenModel> findByUser(User user);

    void deleteByUser(User user);

}
