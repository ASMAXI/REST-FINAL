package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleService {
    public List<Role> getAllRoles();
    public Role getRoleById(Long id);
    public Role findRoleByName(String name);
}
