package com.example.t1security.repository;

import com.example.t1security.model.SystemUserDetails;
import com.example.t1security.model.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findTopByUserDetailsOrderByExpirationDateDesc(SystemUserDetails userDetails);
}
