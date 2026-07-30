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
                    "WHERE c.otp = ?1" +
                    //check not used already
                    "AND c.confirmedAt IS NULL " +
                    //update row if time is after time passed as the 3rd parameter
                    "AND c.expiresAt > ?3")
    int confirmedTokenIfValid(int otp,
                          LocalDateTime confirmedAt,
                              LocalDateTime expiresAt);

    //1,2,3 simply means the parameters passed in order
    //returns 1 if worked 0 if failed

}
