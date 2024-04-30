package com.example.t1security.service;

import com.example.t1security.model.SystemUserDetails;
import com.example.t1security.repository.SystemUserDetailsRepository;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SystemUserDetailsService implements UserDetailsService {

    private final SystemUserDetailsRepository systemUserDetailsRepository;

    @Autowired
    public SystemUserDetailsService(@Nonnull SystemUserDetailsRepository systemUserDetailsRepository) {
        this.systemUserDetailsRepository = systemUserDetailsRepository;
    }

    @Override
    @Transactional
    public SystemUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return systemUserDetailsRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    public void createUser(SystemUserDetails user) {
        Optional<SystemUserDetails> maybeUser = systemUserDetailsRepository.findByUsername(user.getUsername());

        if (maybeUser.isPresent()) {
            throw new RuntimeException("Пользователь с таким username уже существует");
        }

        maybeUser = systemUserDetailsRepository.findByEmail(user.getEmail());
        if (maybeUser.isPresent()) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        systemUserDetailsRepository.save(user);
    }
}
