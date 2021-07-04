package com.kamrul.server.repositories;

import com.github.javafaker.Faker;
import com.kamrul.server.Utils;
import com.kamrul.server.models.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    Faker faker =  new Faker();
    @Test
    void findByUserName() {
        User user = new User();
        String userName = Utils.getRandomString();
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