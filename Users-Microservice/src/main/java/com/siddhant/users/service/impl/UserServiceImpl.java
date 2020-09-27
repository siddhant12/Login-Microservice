package com.siddhant.users.service.impl;

import com.siddhant.users.entity.UserEntity;
import com.siddhant.users.repository.UserRepository;
import com.siddhant.users.service.UserService;
import com.siddhant.users.shared.UserDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND = "User not found with emailId: ";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private ModelMapper modelMapper;


    @PostConstruct
    public void onCreate(){
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }


    @Override
    public UserDTO createUser(UserDTO userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        UserEntity userEntity = modelMapper.map(userDetails,UserEntity.class);
        userRepository.save(userEntity);
        UserDTO userDTO = modelMapper.map(userEntity,UserDTO.class);
        return userDTO;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null)
            throw new UsernameNotFoundException(email);
        UserDTO userDTO = modelMapper.map(userEntity,UserDTO.class);
        return userDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null)
            throw new UsernameNotFoundException(USER_NOT_FOUND + email);
        return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),true,true,true,true,new ArrayList<>());

    }
}
