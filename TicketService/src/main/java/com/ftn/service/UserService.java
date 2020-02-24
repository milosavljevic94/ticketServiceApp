package com.ftn.service;

import com.ftn.dtos.UserDto;
import com.ftn.dtos.UserDtoRes;
import com.ftn.exceptions.AplicationException;
import com.ftn.exceptions.EmailExistsException;
import com.ftn.model.Role;
import com.ftn.model.User;
import com.ftn.repository.RoleRepository;
import com.ftn.repository.UserRepository;
import com.ftn.security.SecurityConfiguration;
import com.ftn.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService{

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    SecurityConfiguration configuration;

    public List<User> finfAllUsers() {
        return userRepository.findAll();
    }

   public User findOneUser(Long id){
        return userRepository.findById(id).orElse(null);
   }
   
   public void saveUser(User user) {
		userRepository.save(user);
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
   
   public User findByConfirmationToken(String confirmationToken) {
		return userRepository.findByConfirmationToken(confirmationToken);
	}
   
   public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
   
   

   public void updateUserBasicFields(UserDto userDto){

        User u = findOneUser(userDto.getId());

        if(u.getEmail() == userDto.getEmail()){
            throw new AplicationException("This email: "+userDto.getEmail()+" is already used.");
        }


        u.setId(userDto.getId());

        if(userDto.getFirstName() != null)
            u.setFirstName(userDto.getFirstName());

        if(userDto.getLastName() != null)
            u.setLastName(userDto.getLastName());

       if(userDto.getEmail() != null)
           u.setEmail(userDto.getEmail());

        saveUser(u);
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


    public User mapFromRegDTO(UserDto userDto){

        User user = new User();
        user.setUsername(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setActive(userDto.getActive());

        return user;
    }
    
    public UserDtoRes userToUserDtoRes(User user){

        UserDtoRes userDtoRes = new UserDtoRes();
        userDtoRes.setUserName(user.getUsername());
        userDtoRes.setEmail(user.getEmail());
        userDtoRes.setFirstName(user.getFirstName());
        userDtoRes.setLastName(user.getLastName());
        userDtoRes.setPassword(user.getPassword());
        userDtoRes.setActive(user.getActive());
        userDtoRes.setRole(user.getRole().getRoleName());

        return userDtoRes;
    }
    
    private boolean emailExist(String email){
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }


	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public UserDtoRes register(UserDto userDto) throws EmailExistsException {
		User user = userRepository.findByUsernameOrEmail(userDto.getUserName(), userDto.getEmail());
        if (user != null) {
            String message = user.getEmail().equals(userDto.getEmail()) ? "Odabrani email i/ili username vec postoji!" : "Odabrani username vec postoji!";
            throw new EmailExistsException(HttpStatus.BAD_REQUEST, message);
        }

        List<User> users = userRepository.findAll();
        User admin = null;
        for(User u : users){
            if(u.getRole().getRoleName().equals("ADMIN")){
                admin = u;
            }
        }
        if(userDto.getRole().getRoleName().equals("ADMIN") && admin != null){
            throw new AplicationException("You can not register like admin, because admin exist. Try again like user!");
        }

        user = mapFromRegDTO(userDto);
        user.setPassword(configuration.passwordEncoder().encode(user.getPassword()));
        user.setActive(true); //TO:DO
        String token = TokenUtils.generateToken();
        user.setConfirmationToken(token);
        //Role role = roleRepository.getOne((long) RoleEnum.USER.getValue());
        Role r = roleRepository.getOne(userDto.getRole().getId());
        user.setRole(r);
        //user.setRole(role);
        userRepository.save(user);
        return userToUserDtoRes(user);
	}

	public User getloggedInUser(){

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();

        User u = findByUsername(username);

        return u;
    }
}
