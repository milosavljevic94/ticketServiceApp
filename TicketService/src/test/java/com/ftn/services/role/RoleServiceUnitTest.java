package com.ftn.services.role;


import com.ftn.dtos.RoleDto;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Role;
import com.ftn.repository.RoleRepository;
import com.ftn.service.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RoleServiceUnitTest {

    @InjectMocks
    RoleService roleService;

    @Mock
    RoleRepository roleRepository;

    @Before
    public void setUp() {

        Role role1 = new Role();
        role1.setId(1L);
        role1.setRoleName("ADMIN");
        Role role2 = new Role();
        role2.setId(2L);
        role2.setRoleName("USER");

        List<Role> roleList = new ArrayList<>();
        roleList.add(role1);
        roleList.add(role2);

        when(roleRepository.findAll()).thenReturn(roleList);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role1));

    }

    @Test
    public void findAllRolesTest_thenReturnRoleList(){
        List<Role> list = roleService.finfAllRoles();
        assertEquals(2, list.size());
    }

    @Test
    public void findOneRoleExistTest_thenReturnRole(){
        Long id = 1L;
        Role r = roleService.findOneRole(id);
        assertNotNull(r);
        assertEquals(id, r.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneRoleNotExistTest_thenThrowException(){
        Long id = 456L;
        Role r = roleService.findOneRole(id);
    }

    @Test
    public void addRoleSuccesstest_thenReturnRole(){

        Role newRoleTest = new Role("SUPER_ADMIN");
        newRoleTest.setId(3L);

        when(roleRepository.save(any(Role.class))).thenAnswer(returnsFirstArg());

        Role result = roleService.addRole(newRoleTest);

        assertNotNull(result);
        assertEquals(newRoleTest.getRoleName(), result.getRoleName());
        verify(roleRepository, times(1)).save(newRoleTest);
    }

    @Test
    public void deleteRoleSuccess(){

        roleService.deleteRole(1L);

        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateRoleIdNotExistTest_thenThrowException(){

        RoleDto roleDtoTest = new RoleDto();
        roleDtoTest.setRoleName("USER_TEST");

        Role result = roleService.updateRole(roleDtoTest);

        assertNotNull(result);
        assertEquals(roleDtoTest.getRoleName(), result.getRoleName());
    }

    @Test
    public void updateRoleSucessTest(){

        RoleDto roleDtoTest = new RoleDto();
        roleDtoTest.setId(1L);
        roleDtoTest.setRoleName("USER_TEST");

        Role result = roleService.updateRole(roleDtoTest);

        assertNotNull(result);
        assertEquals(roleDtoTest.getRoleName(), result.getRoleName());
    }
}
