package com.example.RolebaseAuth;

import com.example.RolebaseAuth.RoleAndPermission.enums.PermissionName;
import com.example.RolebaseAuth.RoleAndPermission.enums.RoleName;
import com.example.RolebaseAuth.RoleAndPermission.Permission;
import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.RoleAndPermission.UserRole;
import com.example.RolebaseAuth.RoleAndPermission.PermissionRepository;
import com.example.RolebaseAuth.RoleAndPermission.RoleRepository;
import com.example.RolebaseAuth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class RolebaseAuthApplication {

//	private S3Client s3Client;




	public static void main(String[] args) {

//		RolebaseAuthApplication demo = new RolebaseAuthApplication();
//		demo.uploadS3();
		//lets say i want to write an implementation that i want to see if it works immediately this is how i could do it
		//like in an init state of fluter
		SpringApplication.run(RolebaseAuthApplication.class, args);
	}





	//private void uploadS3(){}



	@Transactional
	@RequiredArgsConstructor
	@Component
	public class DatabaseSeeder implements CommandLineRunner{

		private final UserRepository userRepository;
		private final RoleRepository roleRepository;
		private final PermissionRepository permissionRepository;

		@Override
		public void run(String... args) throws Exception {
			Permission deleteUser = new Permission(PermissionName.DELETE_USER, "Allows a user to delete other user");
			Permission updateUser = new Permission(PermissionName.UPDATE_USER, "Allows a user to update himself or another user");
			Permission readUser = new Permission(PermissionName.READ_USER, "Allows a user to read another user details");
			//Permission createUser = new Permission(PermissionName.CREATE_USER, "Allows a user to create another user");

			permissionRepository.saveAll(List.of(deleteUser, updateUser, readUser));

			UserRole publicRole = new UserRole(RoleName.PUBLIC, "public domain");
			UserRole adminRole = new UserRole(RoleName.ADMIN, "Responsible for overseeing");

			///publicRole.getPermissions().add(readUser);

			adminRole.getPermissions().add(deleteUser);
			adminRole.getPermissions().add(updateUser);
			///adminRole.getPermissions().add(readUser);

			roleRepository.saveAll(List.of(adminRole, publicRole));

			User adminUser = new User("isaacfeppy@gmail.com", "123", "Isaac");
			adminUser.getRoles().add(adminRole);

			userRepository.save(adminUser);


		}

	}

}
