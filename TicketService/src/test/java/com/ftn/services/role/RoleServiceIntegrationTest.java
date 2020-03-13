package com.ftn.services.role;

import com.ftn.constants.RoleConst;
import com.ftn.dtos.RoleDto;
import com.ftn.exceptions.EntityNotFoundException;
import com.ftn.model.Role;
import com.ftn.project.TicketServiceApplication;
import com.ftn.repository.RoleRepository;
import com.ftn.service.RoleService;
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
public class RoleServiceIntegrationTest {

    @Autowired
    RoleService roleService;

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void findAllRolesTest_thenReturnRoleList(){
        List<Role> list = roleService.finfAllRoles();
        assertEquals(2, list.size());
    }

    @Test
    public void findOneRoleExistTest_thenReturnRole(){

        Role r = roleService.findOneRole(RoleConst.VALID_ID);

        assertNotNull(r);
        assertEquals(RoleConst.VALID_ROLE_NAME, r.getRoleName());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findOneRoleNotExistTest_thenThrowException(){

        Role r = roleService.findOneRole(RoleConst.NOT_VALID_ID);
    }

    @Test
    public void addRoleSuccesstest_thenReturnRole(){
        int sizeBeforeAdd = roleRepository.findAll().size();
        Role result = roleService.addRole(RoleConst.newRoleToAddRole());
        int sizeAfterAdd = roleRepository.findAll().size();

        assertNotNull(result);
        assertEquals(sizeBeforeAdd + 1, sizeAfterAdd);
        assertEquals("SUPER_ADMIN", result.getRoleName());
    }

    @Test
    public void deleteRoleSuccess(){

        Role roleToAdd = roleService.addRole(RoleConst.newRoleToAddRole());
        int sizeBeforeDel = roleRepository.findAll().size();
        roleService.deleteRole(3L);
        int sizeAfterDel = roleRepository.findAll().size();

        assertEquals(sizeBeforeDel - 1, sizeAfterDel);
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateRoleIdNotExistTest_thenThrowException(){

        RoleDto dto = RoleConst.newRoleDtoToUpdate();
        dto.setId(RoleConst.NOT_VALID_ID);
        Role result = roleService.updateRole(dto);

        assertNotNull(result);
        assertEquals(dto.getRoleName(), result.getRoleName());
    }

    @Test
    public void updateRoleSucessTest(){

        RoleDto dto = RoleConst.newRoleDtoToUpdate();
        dto.setId(RoleConst.VALID_ID);

        Role result = roleService.updateRole(dto);

        assertNotNull(result);
        assertEquals(dto.getRoleName(), result.getRoleName());
    }
}
