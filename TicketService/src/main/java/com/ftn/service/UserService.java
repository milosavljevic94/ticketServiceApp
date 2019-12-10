package com.ftn.service;

import com.ftn.model.User;
import com.ftn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService{

    @Autowired
    UserRepository userRepository;


    public List<User> finfAllUsers(){
        return userRepository.findAll();
    }

   public User findOneUser(Long id){
        return userRepository.findById(id).orElse(null);
   }

   public void addUser(User u){
        userRepository.save(u);
   }

   public void deleteUser(Long id){
        userRepository.deleteById(id);
   }

   public void deleteAll(){
        userRepository.deleteAll();
   }

   public Boolean ifExist(Long id){
        return userRepository.existsById(id);
   }

}
