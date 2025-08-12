package org.learning;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.learning.domain.entity.User;

import java.util.Objects;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    @DisplayName("Transitions from transient state.")
    @Test
    @Order(1)
    void testOne(){
        log.info("Test one starts *****");
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        var u1 = User.builder()
                .userName("Akshay")
                .emailId("akshay@gmail.com")
                .country("India")
                .build();
        entityManager.persist(u1);
        log.info("After persisting entity ******");

        var userWithIdTwo = entityManager.find(User.class, 2L);
        if (Objects.isNull(userWithIdTwo)) {
            log.info("User with id 2 not found ***");
        } else {
            log.info("User with id 2 found ***");
        }

        var u2 = User.builder()
                .userName("Mehul")
                .emailId("mehul@gmail.com")
                .country("India")
                .build();
        entityManager.merge(u2);
        log.info("After merging entity ******");

        var u3 = User.builder()
                .userName("Paras")
                .emailId("paras@gmail.com")
                .country("India")
                .build();
        entityManager.remove(u3);
        log.info("After removing entity ******");

        entityManager.getTransaction().commit();
        log.info("After commit ******");
        entityManager.close();
    }

    @DisplayName("Transitions from Managed state.")
    @Test
    @Order(2)
    void testTwo(){
        log.info("Test two starts *****");
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        var u1 = entityManager.find(User.class, 1L);
        u1.setEmailId("agcr@gmail.com");

        entityManager.getTransaction().commit();
        log.info("After commit ******");
        entityManager.close();
    }

    @DisplayName("Transitions from Detached state..")
    @Test
    @Order(3)
    void testThree(){
        log.info("Test three starts *****");
        var entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        var userWithIdTwo = entityManager.find(User.class, 2L);
        entityManager.getTransaction().commit();
        entityManager.close();
        log.info("Detaching managed entity *****");

        /*
        Persist call results in :-
        jakarta.persistence.EntityExistsException: detached entity passed to persist: org.learning.domain.entity.User

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(userWithIdTwo);
        entityManager.getTransaction().commit();
        entityManager.close();
        log.info("Persisting detached object without updating *****");
         */

        // merge after updating
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        userWithIdTwo.setCountry("Switzerland");
        entityManager.merge(userWithIdTwo);
        entityManager.getTransaction().commit();
        entityManager.close();
        log.info("merging detached object after updating *****");

        /*
         removing detached object results in :-
         java.lang.IllegalArgumentException: Given entity is not associated with the persistence context

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(userWithIdTwo);
        entityManager.getTransaction().commit();
        entityManager.close();
        log.info("removing detached object*****");
        */

    }

    @DisplayName("Transitions from Removed state.")
    @Test
    @Order(4)
    void testFour(){
        log.info("Test four starts *****");
        var entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        var userWithIdTwo = entityManager.find(User.class, 2L);
        entityManager.remove(userWithIdTwo);
        entityManager.getTransaction().commit();
        log.info("Removed user with id 2 *****");


        /*
        jakarta.persistence.EntityExistsException: detached entity passed to persist: org.learning.domain.entity.User

        entityManager.getTransaction().begin();
        entityManager.persist(userWithIdTwo);
        entityManager.getTransaction().commit();
         */

        entityManager.getTransaction().begin();
        var u = entityManager.find(User.class, 2L);
        if (Objects.isNull(u)) {
            log.info("User with id 2 not found ***");
        } else {
            log.info("User with id 2 found ***");
        }
        entityManager.getTransaction().commit();

        /*
        jakarta.persistence.OptimisticLockException: Row was already updated or deleted by another transaction for entity [org.learning.domain.entity.User with id '2']

        entityManager.getTransaction().begin();
        entityManager.merge(userWithIdTwo);
        entityManager.getTransaction().commit();
         */

        /*
        java.lang.IllegalArgumentException: Given entity is not associated with the persistence context

        entityManager.getTransaction().begin();
        entityManager.remove(userWithIdTwo);
        entityManager.getTransaction().commit();
         */

        entityManager.close();
    }

}
