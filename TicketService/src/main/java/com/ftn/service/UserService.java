package com.ftn.service;

import com.ftn.dtos.RegistrationDTO;
import com.ftn.dtos.UserDto;
import com.ftn.model.User;
import com.ftn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> finfAllUsers() {
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


    public UserDto mapToDTO(User user){

        UserDto uDto = new UserDto(user);

        return uDto;
    }

    public List<UserDto> allToDto(){

        List<User> users = finfAllUsers();
        List<UserDto> udto = new ArrayList<>();

        for (User u : users) {
            udto.add(mapToDTO(u));
        }
        return udto;
    }


    public User mapFromRegDTO(RegistrationDTO registrationDTO){

        User user = new User();

        user.setEmail(registrationDTO.getEmail());
        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setPassword(registrationDTO.getPassword());
        user.setActive(registrationDTO.getActive());

        return user;
    }

   public String registration(RegistrationDTO registrationDTO) {

        User user = mapFromRegDTO(registrationDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

       for (User u: finfAllUsers()) {
           if(user.getEmail() == u.getEmail()){
               System.out.println("User with this email exist!");
           }
       }

       addUser(user);

       return "User "+ user.getFirstName() + " " + user.getLastName() + " register!";
   }
}
