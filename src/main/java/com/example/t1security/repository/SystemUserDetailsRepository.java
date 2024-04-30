package com.example.t1security.repository;

import com.example.t1security.model.SystemUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemUserDetailsRepository extends JpaRepository<SystemUserDetails, Long> {

    Optional<SystemUserDetails> findByUsername(String username);
    Optional<SystemUserDetails> findByEmail(String email);
}