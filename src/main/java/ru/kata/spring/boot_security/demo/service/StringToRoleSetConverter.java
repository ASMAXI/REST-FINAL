package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.HashSet;
import java.util.Set;

@Component
public class StringToRoleSetConverter implements Converter<String, Set<Role>> {

    @Autowired
    private RoleService roleService;

    @Override
    public Set<Role> convert(String source) {
        Set<Role> roles = new HashSet<>();
        if (source != null && !source.isEmpty()) {
            String[] roleIds = source.split(",");
            for (String roleId : roleIds) {
                try {
                    Long id = Long.parseLong(roleId.trim());
                    Role role = roleService.getRoleById(id);
                    if (role != null) {
                        roles.add(role);
                    } else {
                        // Обработка случая, если роль не найдена
                        System.err.println("Role with ID " + id + " not found.");
                    }
                } catch (NumberFormatException e) {
                    // Обработка ошибки, если идентификатор роли не является числом
                    System.err.println("Invalid role ID: " + roleId);
                }
            }
        }
        return roles;
    }
}