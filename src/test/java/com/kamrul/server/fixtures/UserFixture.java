package com.kamrul.server.fixtures;

import com.github.javafaker.Faker;
import com.kamrul.server.Config;
import com.kamrul.server.Utils;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFixture {
    private final UserRepository userRepository;

    @Autowired
    public UserFixture(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createAUser(User ...overrides){
        Faker faker = new Faker();
        User user= new User();
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setGender("male");
        String userName = faker.name().username();
        user.setUserName(userName);
        user.setEmail(String.format("%s@mail.com",userName));
        user.setPassword(Config.User.password);
        user.setCountry(faker.address().country());
        user.setCity(faker.address().cityName());
        user.setDateOfBirth(faker.date().birthday(14,1000));
        user.setIsEmailVerified(true);
        user.setDeleted(false);
        user.setProfilePicture("http:/s3.amazon.com/test.jpg");
        user.setUserDescription(faker.harryPotter().quote());
        user = Utils.override(user,overrides);
        return userRepository.save(user);
    }
}
