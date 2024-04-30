package com.example.t1security.service;

import com.example.t1security.model.SystemUserDetails;
import com.example.t1security.model.UserRole;
import com.example.t1security.model.dto.RegisterRequest;

import java.util.Set;

public class SystemUserDetailsMapper {

    public static SystemUserDetails createUserFromRegisterRequest(RegisterRequest userDto) {
        SystemUserDetails user = new SystemUserDetails();

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRoles(Set.of(UserRole.USER));

        return user;
    }
}
