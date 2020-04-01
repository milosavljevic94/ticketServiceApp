package com.ftn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.dtos.UserDto;
import com.ftn.dtos.UserDtoRes;
import com.ftn.exceptions.EmailExistsException;
import com.ftn.model.User;
import com.ftn.service.UserService;

@RestController
@RequestMapping(value = "api/user")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping(value = "/allUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers(){

        List<UserDto> usersDto = userService.allToDto();

        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }
    
    @GetMapping(value = "/byUsername/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username){

        UserDto user = userService.mapToDTO(userService.findByUsername(username));

        return new ResponseEntity<>(user,HttpStatus.OK);
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserDtoRes> register(@RequestBody @Validated UserDto userDto) throws EmailExistsException {
    	UserDtoRes userDtoRes = userService.register(userDto);
        return new ResponseEntity<>(userDtoRes, HttpStatus.OK);
    }


    @PutMapping(value = "/updateUser", consumes = "application/json")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto){

        User u = userService.updateUserBasicFields(userDto);

        return new ResponseEntity<>("User updated successfully!",  HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> logicDeleteUser(@PathVariable Long id){

        userService.deleteUser(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
