package org.learning;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class EntityManagerBasicsTest {

    private static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit");
    }

    @AfterAll
    public static void close() {
        entityManagerFactory.close();
    }

    @DisplayName("EntityManager basics")
    @Test
    void testOne(){
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();


        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
