package com.ftn.controller;

import com.ftn.dtos.RegistrationDTO;
import com.ftn.dtos.TicketDto;
import com.ftn.dtos.UserDto;
import com.ftn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/user")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping(value = "/allUsers")
    public ResponseEntity<List<UserDto>> getAllUsers(){

        List<UserDto> usersDto = userService.allToDto();

        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }


    @PostMapping(value = "/registration", consumes = "application/json")
    public ResponseEntity<?> registration(@RequestBody RegistrationDTO registrationDTO)
    {
        String regMessage = userService.registration(registrationDTO);
        return new ResponseEntity<>(regMessage, HttpStatus.CREATED);
    }

    @PutMapping(value = "/updateUser", consumes = "application/json")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto){

        userService.updateUserBasicFields(userDto);

        return new ResponseEntity<>("User updated successfully!",  HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> logicDeleteUser(@PathVariable Long id){

        userService.deleteUser(id);

        return new ResponseEntity<>("User successfully deleted!", HttpStatus.OK);
    }
}
