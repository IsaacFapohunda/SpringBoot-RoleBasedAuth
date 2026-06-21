package com.example.RolebaseAuth;

import com.example.RolebaseAuth.enums.PermissionName;
import com.example.RolebaseAuth.enums.RoleName;
import com.example.RolebaseAuth.model.Permission;
import com.example.RolebaseAuth.model.User;
import com.example.RolebaseAuth.model.UserRole;
import com.example.RolebaseAuth.repository.PermissionRepository;
import com.example.RolebaseAuth.repository.RoleRepository;
import com.example.RolebaseAuth.repository.UserRepository;
import jakarta.servlet.http.PushBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static software.amazon.awssdk.profiles.ProfileProperty.AWS_ACCESS_KEY_ID;

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
//			Permission readUser = new Permission(PermissionName.READ_USER, "Allows a user to read another user details");

			permissionRepository.saveAll(List.of(deleteUser, updateUser));

			UserRole publicRole = new UserRole(RoleName.PUBLIC, "public domain");
			UserRole adminRole = new UserRole(RoleName.ADMIN, "Responsible for overseeing");

			//publicRole.getPermissions().add(readUser);

			adminRole.getPermissions().add(deleteUser);
			adminRole.getPermissions().add(updateUser);
			//adminRole.getPermissions().add(readUser);

			roleRepository.saveAll(List.of(adminRole, publicRole));

			User adminUser = new User("isaacfeppy@gmail.com", "123", "Isaac");
			adminUser.getRoles().add(adminRole);

			userRepository.save(adminUser);


		}

	}

}
