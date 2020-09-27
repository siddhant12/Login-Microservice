package com.siddhant.users.service;

import com.siddhant.users.shared.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserByEmail(String email);
}
