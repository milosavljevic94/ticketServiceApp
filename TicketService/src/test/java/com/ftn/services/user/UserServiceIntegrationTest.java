package com.ftn.services.user;

import com.ftn.constants.UserConst;
import com.ftn.dtos.UserDto;
import com.ftn.dtos.UserDtoRes;
import com.ftn.exceptions.AplicationException;
import com.ftn.exceptions.EmailExistsException;
import com.ftn.exceptions.EntityAlreadyExistException;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Role;
import com.ftn.model.User;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.RoleRepository;
import com.ftn.repository.UserRepository;
import com.ftn.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketServiceApplication.class)
@Transactional
@TestPropertySource("classpath:application-test.properties")
public class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @Test
    public void allUsersSuccessTest(){
        List<User> result = userService.finfAllUsers();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    public void findOneUserSuccessTest(){
        User result = userService.findOneUser(UserConst.DB_ID);

        assertNotNull(result);
        assertEquals(UserConst.DB_USERNAME, result.getUsername());
        assertTrue(result.getActive());
        assertEquals(UserConst.DB_EMAIL, result.getEmail());
        assertEquals(UserConst.DB_NAME, result.getFirstName());
        assertEquals(UserConst.DB_LAST_NAME, result.getLastName());

    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneUserBadId_thenThrowException(){
        User result = userService.findOneUser(UserConst.NOT_VALID_ID);
    }

    @Test
    public void saveUserSuccessTest(){
        Role role = roleRepository.findById(1L).get();
        User userToAdd = UserConst.userToAdd();
        userToAdd.setRole(role);
        int sizeBeforeAdd = userRepository.findAll().size();
        userService.saveUser(userToAdd);
        int sizeAfterAdd = userRepository.findAll().size();

        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);
    }

    @Test
    public void deleteUserSuccessTest(){
        User userBeforeDel = userRepository.findById(UserConst.DB_ID).get();
        assertTrue(userBeforeDel.getActive());

        userService.deleteUser(UserConst.DB_ID);

        Boolean userAfterDel = userRepository.findById(UserConst.DB_ID).isPresent();
        assertFalse(userAfterDel);
    }


    @Test(expected = EntityAlreadyExistException.class)
    public void updateUserBasicFields_EmailExist_thenThrowException(){
        UserDto dtoToUpdate = UserConst.returnDtoForUpdate();
        dtoToUpdate.setEmail(UserConst.DB_EMAIL);

        User result = userService.updateUserBasicFields(dtoToUpdate);
    }

    @Test(expected = EntityAlreadyExistException.class)
    public void updateUserBasicFields_usernameExist_thenThrowException(){
        UserDto dtoToUpdate = UserConst.returnDtoForUpdate();
        dtoToUpdate.setUserName(UserConst.DB_USERNAME);

        User result = userService.updateUserBasicFields(dtoToUpdate);
    }

    @Test
    public void updateUserBasicFieldsSuccessTest(){
        UserDto dtoToUpdate = UserConst.returnDtoForUpdate();
        User result = userService.updateUserBasicFields(dtoToUpdate);

        assertNotNull(result);
        assertEquals(dtoToUpdate.getEmail(), result.getEmail());
        assertEquals(dtoToUpdate.getUserName(), result.getUsername());
        assertEquals(dtoToUpdate.getFirstName(), result.getFirstName());
        assertEquals(dtoToUpdate.getLastName(), result.getLastName());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findByUsernameNotValid_thenThrowException(){
        User result = userService.findByUsername(UserConst.NOT_VALID_USERNAME);
    }

    @Test
    public void findByUsernameSuccessTest(){

        User result = userService.findByUsername(UserConst.DB_USERNAME);

        assertNotNull(result);
        assertEquals(UserConst.DB_USERNAME, result.getUsername());
    }

    //register tests

    @Test(expected = EmailExistsException.class)
    public void registerUsernameOrEmailExist_thenThrowExeption()throws EmailExistsException{

        UserDtoRes result = userService.register(UserConst.registerDtoExistEmail());
    }

    @Test(expected = AplicationException.class)
    public void registerPasswordsNotSame_thenThrowExeption(){
        UserDto userDto = UserConst.returnDtoForRegister();
        userDto.setMatchingPassword("wrongPassword");

        UserDtoRes result = userService.register(userDto);
    }

    @Test(expected = AplicationException.class)
    public void registerAdminExist_thenThrowExeption(){
        UserDto userDto = UserConst.registerDtoAdminRole();

        UserDtoRes result = userService.register(userDto);
    }

    @Test
    public void registerSuccessTest(){

        UserDto userDto = UserConst.validDtoUser();

       /* BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String hash = bCryptPasswordEncoder.encode(userDto.getPassword());*/

        int sizeBeforeRegister = userRepository.findAll().size();
        UserDtoRes result = userService.register(userDto);

        assertNotNull(result);
        assertEquals(userDto.getUserName(), result.getUserName());
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getFirstName(), result.getFirstName());
        assertEquals(userDto.getLastName(), result.getLastName());
        //assertTrue(bCryptPasswordEncoder.matches(userDto.getPassword(), hash));
        assertEquals(userDto.getRole().getRoleName(), result.getRole());
    }
}
