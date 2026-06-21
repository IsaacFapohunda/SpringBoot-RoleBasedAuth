package com.example.RolebaseAuth.repository;

import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.model.UserDTO;
import com.example.RolebaseAuth.model.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {


    Optional<User> findByEmail(String email);

    //lets write a code here whereby i only get some data from the user details
    @Transactional
    @Query("SELECT u.email, u.firstName, u.profilePicture, u.id FROM User u WHERE u.email = :email")
    UserDTO findByUserEmail(@Param("email") String email);

//    @Transactional
//    @Query("SELECT u.email FROM User u WHERE u.email = :email")
//    UserProjection findByUserEmail(@Param("email") String email);

    //    @Transactional
//    @Modifying
//    @Query("SELECT new com.example.RolebaseAuth.model.UserDTO(u.email, u.firstName, u.id, u.profilePicture) " +
//            "FROM BlogModel b JOIN b.user u WHERE b.blogpostId = :blogpostId")
//    Optional<UserDTO> findUserDetailsByBlogId(String blogpostId);
    @Override
    Optional<User> findById(String id);

    @Transactional
    @Modifying
    @Query(
            "UPDATE User a " +
                    "SET a.enabled = TRUE WHERE a.email = ?1"
    )
    int enableAppUser(String email);



}
