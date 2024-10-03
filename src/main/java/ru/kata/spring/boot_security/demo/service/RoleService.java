package ru.kata.spring.boot_security.demo.service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Transactional(readOnly = true)
    public Role getRoleById(Long id) {
        return roleDao.getRoleById(id);
    }

    @Transactional
    public Role findRoleByName(String name) {
        return roleDao.findRoleByName(name);
    }
}