package com.example.RolebaseAuth.repository;

import com.example.RolebaseAuth.model.otp.OtpModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpModel, String> {

    Optional<OtpModel> findByOtp(int otp);
    //i feel like here it doesnt know which one to bring out since i didnt specify the otp to find with

    @Transactional
    @Modifying
    @Query(
            "UPDATE OtpModel c " +
                    "SET c.confirmedAt = ?2 " +
                    "WHERE c.otp = ?1")
    int updateConfirmedAt(int otp,
                          LocalDateTime confirmedAt);

}
