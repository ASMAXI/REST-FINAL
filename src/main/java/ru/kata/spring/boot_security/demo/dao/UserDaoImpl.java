package ru.kata.spring.boot_security.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User createUser(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            user.setName(userDetails.getName());
            user.setLast_name(userDetails.getLast_name());
            user.setAge(userDetails.getAge());
            user.setEmail(userDetails.getEmail());
            user.setRoles(userDetails.getRoles());
            entityManager.merge(user);
        }
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public Optional<User> getUserByName(String name) {
        return entityManager.createQuery("SELECT u FROM User u JOIN FETCH u.roles WHERE u.name = :name", User.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void addRoleToUser(Long userId, Long roleId) {
        User user = entityManager.find(User.class, userId);
        Role role = entityManager.find(Role.class, roleId);
        if (user != null && role != null) {
            user.getRoles().add(role);
            entityManager.merge(user);
        }
    }
}