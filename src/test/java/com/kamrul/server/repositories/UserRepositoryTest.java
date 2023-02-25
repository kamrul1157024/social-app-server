package com.kamrul.server.repositories;

import com.github.javafaker.Faker;
import com.kamrul.server.MockUserCreator;
import com.kamrul.server.models.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    Faker faker =  new Faker();
    @Test
    void findByUserName() {
        User user = new User();
        String userName = MockUserCreator.getRandomString();
        user.setUserName(userName);
        user.setPassword("14Adfnf#dajkf");
        user.setEmail(String.format("%s@test.com",userName));
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setCity(faker.address().city());
        user.setCountry(faker.address().country());
        user.setDateOfBirth(faker.date().birthday(13,90));
        user.setGender("male");
        userRepository.save(user);
        Optional<User> optionalUser= userRepository.findByUserName(userName);
        assertEquals(optionalUser.isEmpty(),false);
        assertEquals(optionalUser.get().getUserName(),userName);
    }
}