package com.example.RolebaseAuth.RoleAndPermission;

import com.example.RolebaseAuth.RoleAndPermission.enums.PermissionName;
import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Enumerated(value = EnumType.STRING)
    private PermissionName name;

    @Column(name = "permission_desc")
    private String PermissionDescription;

    public Permission(PermissionName name, String PermissionDescription) {
        this.name = name;
        this.PermissionDescription = PermissionDescription;

    }


}
