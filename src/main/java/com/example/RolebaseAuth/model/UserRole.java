package com.example.RolebaseAuth.model;

import com.example.RolebaseAuth.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role-name", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoleName roleName;

    @Column(name = "role_desc", nullable = false)
    private String roleDescription;

//    @Column(name = "created_at", nullable = true)
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at", nullable = true)
//    private LocalDateTime updatedAt;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    //cascade mean what we persist in the user role entitiy will be persisted in the permissions entity
    @JoinTable(
            name = "user_role_permissions",
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_role_id", "permissions_id"})
    )
    private Set<Permission> permissions = new HashSet<>();


    public UserRole(RoleName roleName,
                    String roleDescription
                   // LocalDateTime createdAt,
                    //LocalDateTime updatedAt,
                    //Set<Permission> permissions
    ) {
        this.roleName = roleName;
        this.roleDescription = roleDescription;
       // this.createdAt = createdAt;
        //this.updatedAt = updatedAt;
        //this.permissions = permissions;
    }
}
