package com.Study.Board.Repository;

import com.Study.Board.Model.Post;
import com.Study.Board.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User read(String username) {
        User user = em.createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();

        return user;
    }

    public Long exist(String userInfo) {
        Long count = em.createQuery("select COUNT(u) from User u where u.username = :username or u.email = :email", Long.class)
                .setParameter("username", userInfo)
                .setParameter("email", userInfo)
                .setMaxResults(1)
                .getSingleResult();

        return count;
    }
}
