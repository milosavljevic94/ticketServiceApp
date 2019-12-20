package com.ftn.service;

import com.ftn.dtos.RoleDto;
import com.ftn.model.Role;
import com.ftn.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public List<Role> finfAllRoles(){
        return roleRepository.findAll();
    }

    public Role findOneRole(Long id){
        return roleRepository.findById(id).orElse(null);
    }

    public void addRole(Role u){
        roleRepository.save(u);
    }

    public void deleteRole(Long id){
        roleRepository.deleteById(id);
    }

    public void deleteAll(){
        roleRepository.deleteAll();
    }

    public void updateRole(RoleDto roleDto){
        Role r = findOneRole(roleDto.getId());
        r.setRoleName(roleDto.getRoleName());
        addRole(r);
    }

    public Boolean ifExist(Long id){
        return roleRepository.existsById(id);
    }

    public RoleDto mapToDTO(Role role){

        RoleDto rDto = new RoleDto(role);

        return rDto;
    }

    public List<RoleDto> allToDto(){

        List<Role> roles = finfAllRoles();
        List<RoleDto> rdto = new ArrayList<>();

        for (Role r : roles) {
            rdto.add(mapToDTO(r));
        }
        return rdto;
    }

    public Role mapFromDto(RoleDto roleDto){

        Role r = new Role();
        r.setId(roleDto.getId());
        r.setRoleName(roleDto.getRoleName());

        return r;
    }
}
