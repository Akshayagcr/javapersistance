package org.learning.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.learning.domain.entity.User;

public class UserRepositoryImpl implements UserRepository, AutoCloseable {

    private final EntityManager entityManager;

    public UserRepositoryImpl() {
        try (EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit")) {
            this.entityManager = entityManagerFactory.createEntityManager();
        }
    }


    @Override
    public void save(User user) {
        // Hibernate will start and commit a transaction for each database operation if there is no active transaction.
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
    }

    @Override
    public User findById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void update(User updatedUserEntity) {
        var user = entityManager.find(User.class, updatedUserEntity.getId());
        //TODO: Complete
    }

    @Override
    public void delete(long id) {
        var user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }

    @Override
    public void close() {
        entityManager.close();
    }

}
