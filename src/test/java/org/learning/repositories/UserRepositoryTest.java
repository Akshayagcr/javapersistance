package org.learning.repositories;

import org.junit.jupiter.api.Test;
import org.learning.domain.entity.User;

class UserRepositoryTest {

    @Test
    void testOne(){
        try(var userRepository = new UserRepositoryImpl()){
            userRepository.save(new User());
        }
    }

}
