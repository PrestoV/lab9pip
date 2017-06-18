package models.db;


import models.shared.Encryption;
import models.users.User;
import models.users.UserRepository;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.function.Function;


public class JPAUserRepository implements UserRepository {
    private final JPAApi jpaApi;

    @Inject
    public JPAUserRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public void add(User user) {
        user.setPassword(
                Encryption.encryptString( user.getPassword() )
        );
        wrap(em -> insert(em, user));
    }

    @Override
    public boolean isValid(String login, String password) {
        return wrap(em -> isValid(em, login, Encryption.encryptString(password)));
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private User insert(EntityManager em, User user) {
        em.persist(user);
        return user;
    }

    private boolean isValid(EntityManager em, String login, String password) {
        return !em.createQuery(
                "SELECT u FROM User u WHERE u.login=:login AND u.password=:password")
                .setParameter("login", login)
                .setParameter("password", password)
                .getResultList().isEmpty();
    }
}
