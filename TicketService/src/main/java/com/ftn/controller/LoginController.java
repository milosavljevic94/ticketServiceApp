package com.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ftn.dtos.LoggedInUserDTO;
import com.ftn.dtos.LoginDTO;
import com.ftn.model.User;
import com.ftn.security.TokenUtils;
import com.ftn.service.UserService;

@RestController
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @Autowired
    TokenUtils tokenUtils;

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginDTO user) {
        try {

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails details = userDetailsService.loadUserByUsername(user.getUsername());
            User userDb = userService.findByUsername(user.getUsername());
            LoggedInUserDTO loggedIn = new LoggedInUserDTO(userDb.getId(), tokenUtils.generateToken(details),
                    userDb.getUsername(), userDb.getEmail(), details.getAuthorities());

            if (!userDb.getActive()) {
                return new ResponseEntity<>("You must confirm registration to login!", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(loggedIn, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
