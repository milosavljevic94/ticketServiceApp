package com.ftn.controller;


import com.ftn.dtos.RoleDto;
import com.ftn.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping(value = "/allRole")
    public ResponseEntity<List<RoleDto>> getAllRole(){

        List<RoleDto> roleDtos = roleService.allToDto();

        return new ResponseEntity<>(roleDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RoleDto> getRole(@PathVariable Long id) {

        return new ResponseEntity<>(new RoleDto(roleService.findOneRole(id)), HttpStatus.OK);
    }

    @PostMapping(value = "/addRole", consumes = "application/json")
    public ResponseEntity<RoleDto> addRole(@RequestBody RoleDto roleDto) {

        roleService.addRole(roleService.mapFromDto(roleDto));

        return new ResponseEntity<>(new RoleDto(roleService.mapFromDto(roleDto)) , HttpStatus.CREATED);
    }

    @PutMapping(value = "/updateRole", consumes = "application/json")
    public ResponseEntity<RoleDto> updateRole(@RequestBody RoleDto roleDto){

        roleService.updateRole(roleDto);

        return new ResponseEntity<>(new RoleDto(roleService.mapFromDto(roleDto)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id){

        roleService.deleteRole(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}