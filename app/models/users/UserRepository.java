package models.users;

import com.google.inject.ImplementedBy;
import models.db.JPAUserRepository;


@ImplementedBy(JPAUserRepository.class)
public interface UserRepository {
    void add(User user);
    boolean isValid(String login, String password);
}
