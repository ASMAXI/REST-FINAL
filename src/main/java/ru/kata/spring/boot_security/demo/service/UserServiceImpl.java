package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Сохраняем роли, если они новые
        Set<Role> rolesToSave = new HashSet<>();
        for (Role role : user.getRoles()) {
            Role existingRole = roleDao.findRoleByName(role.getName());
            if (existingRole != null) {
                // Если роль уже существует, используем ее
                rolesToSave.add(existingRole);
            } else {
                // Если роли нет в базе данных, создаем новую
                rolesToSave.add(roleDao.saveRole(role));
            }
        }
        user.setRoles(rolesToSave);

        User createdUser = userDao.createUser(user);

        // Добавляем роли пользователю
        for (Role role : rolesToSave) {
            userDao.addRoleToUser(createdUser.getId(), role.getId());
        }

        return createdUser;
    }



    @Override
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userDao.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userDetails.getName());
        user.setLast_name(userDetails.getLast_name());
        user.setAge(userDetails.getAge());
        user.setEmail(userDetails.getEmail());

        // Обновляем роли пользователя
        Set<Role> rolesToSave = new HashSet<>();

        // Проверяем, есть ли роли в userDetails
        if (userDetails.getRoles() == null || userDetails.getRoles().isEmpty()) {
            // Если ролей нет, устанавливаем роль по умолчанию (например, ROLE_USER)
            Role defaultRole = roleDao.findRoleByName("ROLE_USER");
            if (defaultRole == null) {
                throw new RuntimeException("Default role ROLE_USER not found");
            }
            rolesToSave.add(defaultRole);
        } else {
            for (Role role : userDetails.getRoles()) {
                Role existingRole = roleDao.findRoleByName(role.getName());
                if (existingRole != null) {
                    // Если роль уже существует, используем ее
                    rolesToSave.add(existingRole);
                } else {
                    // Если роли нет в базе данных, создаем новую
                    rolesToSave.add(roleDao.findRoleByName(role.getName())); // Предполагаем, что есть метод saveRole
                }
            }
        }

        user.setRoles(rolesToSave);

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        return userDao.updateUser(id, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }
}