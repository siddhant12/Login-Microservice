package com.siddhant.users.controller;

import com.siddhant.users.model.CreateUserRequestDTO;
import com.siddhant.users.model.CreateUserResponseDTO;
import com.siddhant.users.service.UserService;
import com.siddhant.users.shared.UserDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private ModelMapper modelMapper;

    @PostConstruct
    public void onCreate(){
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @GetMapping
    public String hello(){return "Hello";}

    @PostMapping
    public ResponseEntity<CreateUserResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO){
        UserDTO userDTO = modelMapper.map(createUserRequestDTO,UserDTO.class);
        UserDTO createdUser = userService.createUser(userDTO);
        CreateUserResponseDTO createUserResponseDTO = modelMapper.map(createdUser,CreateUserResponseDTO.class);
        return new ResponseEntity<>(createUserResponseDTO, HttpStatus.CREATED);
    }
}
