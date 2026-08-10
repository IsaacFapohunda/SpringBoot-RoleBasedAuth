package com.example.RolebaseAuth.model;

import com.example.RolebaseAuth.RoleAndPermission.UserRole;
import com.example.RolebaseAuth.profilePicture.ProfilePictureModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "email")
    private String email;


    @Column(name = "password")
    private String password;


    @OneToOne

    private ProfilePictureModel profilePicture;

    @Column(name = "firstName")
    private String firstName;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<UserRole> roles = new HashSet<>();
    private Boolean enabled = false;
    private Boolean locked = false;
    private  Boolean isCredentialsNonExpired = true;
    private  Boolean isAccountNonExpired = true;


    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return email;
    }


    public boolean isAccountNonExpired() {
        return true;
    }


    public boolean isAccountNonLocked() {
        return locked;
    }


    public boolean isCredentialsNonExpired() {
        return true;
    }


    public boolean isEnabled() {
        return enabled;
    }


    public User(String email, String password, String firstName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
    }


}
