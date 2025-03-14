package com.seoulmilk.be.auth.persistence;

import com.seoulmilk.be.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmployeeId(String employeeId);

    Optional<User> findByBusinessId(String businessId);

    boolean existsByEmployeeId(String employeeId);

    boolean existsByBusinessId(String businessId);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

}
