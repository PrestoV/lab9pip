package models.users;

import com.google.inject.ImplementedBy;
import models.db.JPAUserRepository;


@ImplementedBy(JPAUserRepository.class)
public interface UserRepository {
    Boolean add(User user);
    Boolean isValid(String login, String password);
}
