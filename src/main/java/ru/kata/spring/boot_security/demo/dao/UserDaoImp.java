package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Component
public class UserDaoImp implements UserDao {
    @PersistenceContext
    private EntityManager em;


    @Override
    public List<User> getUsers() {
        return em.createQuery("from users", User.class).getResultList();
    }

    @Override
    public boolean addUser(User user) {
        em.persist(user);
        return true;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));

    }

    @Override
    public void removeUser(Long id) {
        User user = em.find(User.class, id);
        em.remove(user);
    }

    @Override
    public void updateUser(User userUp, Long id) {
        User user = em.find(User.class, id);
        user.setFirstName(userUp.getFirstName());
        user.setLastName(userUp.getLastName());
        user.setEmail(userUp.getEmail());
        user.setPassword(userUp.getPassword());
        user.setRoles(userUp.getRoles());
        em.merge(user);

    }

    @Override
    public Optional<User> findByEmail(String email) {
        return em.createQuery("select u from users u where u.email =:userEmail", User.class)
                .setParameter("userEmail", email)
                .getResultList()
                .stream()
                .findFirst();
    }

}
