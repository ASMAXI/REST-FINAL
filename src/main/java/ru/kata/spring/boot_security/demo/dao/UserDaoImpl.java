package ru.kata.spring.boot_security.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

            // Обновляем роли пользователя
            Set<Role> rolesToSave = new HashSet<>();
            for (Role role : userDetails.getRoles()) {
                if (role.getId() == null) {
                    // Если у роли нет ID, значит она новая и ее нужно сохранить
                    entityManager.persist(role);
                }
                rolesToSave.add(role);
            }
            user.setRoles(rolesToSave);

            entityManager.merge(user);
        }
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            user.getRoles().clear(); // Удаляем связи с ролями
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
            if (!user.getRoles().contains(role)) {
                user.getRoles().add(role);
                entityManager.merge(user);
            }
        }
    }
}