package com.ftn.controller;

import com.ftn.dtos.UserDto;
import com.ftn.dtos.UserDtoRes;
import com.ftn.model.User;
import com.ftn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/allUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<UserDto> usersDto = userService.allToDto();

        return new ResponseEntity<>(usersDto, HttpStatus.OK);
    }

    /*
      It will be used in the further development of the project.
    */
    @PostMapping("/register")
    public ResponseEntity<UserDtoRes> register(@Valid @RequestBody UserDto userDto) {

        UserDtoRes userDtoRes = userService.register(userDto);

        return new ResponseEntity<>(userDtoRes, HttpStatus.OK);
    }

    /*
     It will be used in the further development of the project.
   */
    @PutMapping(value = "/updateUser", consumes = "application/json")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {

        User u = userService.updateUserBasicFields(userDto);

        return new ResponseEntity<>(new UserDto(u), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> logicDeleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
