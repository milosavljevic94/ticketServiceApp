package com.ftn.services.user;

import com.ftn.constants.UserConst;
import com.ftn.dtos.RoleDto;
import com.ftn.dtos.UserDto;
import com.ftn.dtos.UserDtoRes;
import com.ftn.exceptions.AplicationException;
import com.ftn.exceptions.EmailExistsException;
import com.ftn.exceptions.EntityAlreadyExistException;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Role;
import com.ftn.model.User;
import com.ftn.repository.RoleRepository;
import com.ftn.repository.UserRepository;
import com.ftn.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void setUp(){

    }

    @Test
    public void allUsersSuccessTest(){
        List<User> expected = UserConst.allUsers();
        when(userRepository.findAll()).thenReturn(expected);
        List<User> result = userService.finfAllUsers();

        assertFalse(result.isEmpty());
        assertEquals(expected.size(), result.size());
    }

    @Test
    public void findOneUserSuccessTest(){
        User expectedUser = UserConst.returnOneUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));
        User result = userService.findOneUser(1L);

        assertNotNull(result);
        assertEquals(expectedUser.getUsername(), result.getUsername());
        assertEquals(expectedUser.getEmail(), result.getEmail());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneUserBadId_thenThrowException(){
        when(userRepository.findById(44L)).thenReturn(Optional.empty());
        User result = userService.findOneUser(44L);
    }

    @Test
    public void saveUserSuccessTest(){

        User userToSave = UserConst.returnOneUser();
        userService.saveUser(userToSave);

        verify(userRepository, times(1)).save(userToSave);
    }

    @Test
    public void deleteUserSuccessTest(){
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test(expected = EntityAlreadyExistException.class)
    public void updateUserBasicFields_usernameOrEmailExist_thenThrowException(){
        UserDto dtoToUpdate = UserConst.returnDtoForUpdate();
        User existingUser = new User();
        existingUser.setUsername(dtoToUpdate.getUserName());

        when(userRepository.findByUsernameOrEmail(dtoToUpdate.getUserName(), dtoToUpdate.getEmail()))
                .thenReturn(existingUser);

        User result = userService.updateUserBasicFields(dtoToUpdate);
    }

    @Test
    public void updateUserBasicFieldsSuccessTest(){
        User expectedUser = UserConst.returnOneUser();
        UserDto dtoToUpdate = UserConst.returnDtoForUpdate();

        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));
        User result = userService.updateUserBasicFields(dtoToUpdate);

        assertNotNull(result);
        assertEquals(dtoToUpdate.getEmail(), result.getEmail());
        assertEquals(dtoToUpdate.getUserName(), result.getUsername());
        assertEquals(dtoToUpdate.getFirstName(), result.getFirstName());
        assertEquals(dtoToUpdate.getLastName(), result.getLastName());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findByUsernameNotValid_thenThrowException(){
        when(userRepository.findByUsername("notValidUsername")).thenReturn(null);
        User result = userService.findByUsername("notValidUsername");
    }

    @Test
    public void findByUsernameSuccessTest(){
        User expectedUser = UserConst.returnOneUser();
        when(userRepository.findByUsername(expectedUser.getUsername())).thenReturn(expectedUser);
        User result = userService.findByUsername("testUsername");

        assertNotNull(result);
        assertEquals(expectedUser.getUsername(), result.getUsername());
    }

    //register tests

    @Test(expected = EmailExistsException.class)
    public void registerUsernameOrEmailExist_thenThrowExeption()throws EmailExistsException{

        UserDto userDto = UserConst.returnDtoForRegister();
        User user = new User();
        user.setUsername(userDto.getUserName());
        user.setEmail(userDto.getEmail());

        when(userRepository.findByUsernameOrEmail(userDto.getUserName(), userDto.getEmail()))
                .thenReturn(user);

        UserDtoRes result = userService.register(userDto);
    }

    @Test(expected = AplicationException.class)
    public void registerPasswordsNotSame_thenThrowExeption(){

        UserDto userDto = UserConst.returnDtoForRegister();
        userDto.setMatchingPassword("wrongPassword");

        UserDtoRes result = userService.register(userDto);
    }

    @Test(expected = AplicationException.class)
    public void registerAdminExist_thenThrowExeption(){

        RoleDto roleDto = new RoleDto();
        roleDto.setRoleName("ADMIN");

        UserDto userDto = UserConst.returnDtoForRegister();
        userDto.setRole(roleDto);

        Role role = new Role();
        role.setRoleName("ADMIN");
        User user = new User();
        user.setRole(role);
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findAll())
                .thenReturn(users);

        UserDtoRes result = userService.register(userDto);
    }

    @Test
    public void registerSuccessTest(){

        UserDto userDto = UserConst.returnDtoForRegister();

        when(userRepository.findAll())
                .thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(userDto.getPassword()))
                .thenReturn(userDto.getPassword());
        when(roleRepository.getOne(userDto.getRole().getId()))
                .thenReturn(UserConst.returnRoleUser());

        UserDtoRes result = userService.register(userDto);

        assertNotNull(result);
        assertEquals(userDto.getUserName(), result.getUserName());
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getFirstName(), result.getFirstName());
        assertEquals(userDto.getLastName(), result.getLastName());
        assertEquals(userDto.getPassword(), result.getPassword());
        assertEquals(userDto.getRole().getRoleName(), result.getRole());
    }

    //get logged user tests.



}
