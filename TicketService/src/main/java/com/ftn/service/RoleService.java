package com.ftn.service;

import com.ftn.model.Role;
import com.ftn.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Boolean ifExist(Long id){
        return roleRepository.existsById(id);
    }
}
