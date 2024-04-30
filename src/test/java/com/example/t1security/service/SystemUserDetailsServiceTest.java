package com.example.t1security.service;

import com.example.t1security.model.SystemUserDetails;
import com.example.t1security.repository.SystemUserDetailsRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SystemUserDetailsServiceTest {

    @Mock
    private SystemUserDetailsRepository systemUserDetailsRepository;

    @InjectMocks
    private SystemUserDetailsService systemUserDetailsService;

    private static SystemUserDetails testUser;

    private static final String TEST_STRING = "test";

    @BeforeAll
    public static void initUser() {
        testUser = new SystemUserDetails();
        testUser.setUsername(TEST_STRING);
        testUser.setPassword(TEST_STRING);
        testUser.setEmail(TEST_STRING);
    }

    @Test
    public void testLoadUserByUsername() {
        when(systemUserDetailsRepository.findByUsername(TEST_STRING)).thenReturn(Optional.of(testUser));

        UserDetails result = systemUserDetailsService.loadUserByUsername(TEST_STRING);

        verify(systemUserDetailsRepository).findByUsername(TEST_STRING);
        assertSame(testUser, result);
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(systemUserDetailsRepository.findByUsername(TEST_STRING)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            systemUserDetailsService.loadUserByUsername(TEST_STRING);
        });
    }

    @Test
    public void testCreateUser() {
        when(systemUserDetailsRepository.findByUsername(TEST_STRING)).thenReturn(Optional.empty());
        when(systemUserDetailsRepository.findByEmail(TEST_STRING)).thenReturn(Optional.empty());

        systemUserDetailsService.createUser(testUser);

        verify(systemUserDetailsRepository).save(testUser);
    }

    @Test
    public void testCreateUser_UsernameExists() {
        when(systemUserDetailsRepository.findByUsername(TEST_STRING)).thenReturn(Optional.of(new SystemUserDetails()));

        assertThrows(RuntimeException.class, () -> {
            systemUserDetailsService.createUser(testUser);
        });
    }

    @Test
    public void testCreateUser_EmailExists() {

        when(systemUserDetailsRepository.findByUsername(TEST_STRING)).thenReturn(Optional.empty());
        when(systemUserDetailsRepository.findByEmail(TEST_STRING)).thenReturn(Optional.of(new SystemUserDetails()));

        assertThrows(RuntimeException.class, () -> {
            systemUserDetailsService.createUser(testUser);
        });
    }

}