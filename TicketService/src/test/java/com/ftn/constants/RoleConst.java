package com.ftn.constants;

import com.ftn.dtos.RoleDto;
import com.ftn.model.Role;

public class RoleConst {

    public static Long VALID_ID = 1L;
    public static Long NOT_VALID_ID = 111L;

    public static String VALID_ROLE_NAME = "USER";

    public static Role newRoleToAddRole(){
        Role newRoleTest = new Role("SUPER_ADMIN");
        newRoleTest.setId(3L);

        return newRoleTest;
    }

    public static RoleDto newRoleDtoToUpdate(){
        RoleDto roleDtoTest = new RoleDto();
        roleDtoTest.setRoleName("USER_TEST");
        return roleDtoTest;
    }

    public RoleConst() {
    }
}
