package com.example.RolebaseAuth.RoleAndPermission;

import com.example.RolebaseAuth.RoleAndPermission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
