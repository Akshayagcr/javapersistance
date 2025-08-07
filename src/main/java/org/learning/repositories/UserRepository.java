package org.learning.repositories;

import org.learning.domain.entity.User;

public interface UserRepository {

    void save(User user);

    User findById(long id);

    void update(User user);

    void delete(long id);
}
