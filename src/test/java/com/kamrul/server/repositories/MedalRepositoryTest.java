package com.kamrul.server.repositories;

import com.kamrul.server.fixtures.MedalFixture;
import com.kamrul.server.fixtures.PostFixture;
import com.kamrul.server.fixtures.UserFixture;
import com.kamrul.server.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.server.models.medal.Medal;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MedalRepositoryTest {
    @Autowired
    MedalRepository medalRepository;
    @Autowired
    PostFixture postFixture;
    @Autowired
    MedalFixture medalFixture;
    @Autowired
    UserFixture userFixture;

    User[] user = new User[3];
    HashMap<User,MedalType> userMedalType = new HashMap<>();
    Post post;


    @BeforeEach
    void setup(){
        for (int i = 0; i < 3; i++) {
            user[i] = userFixture.createAUser();
        }
        post = postFixture.createAPost(user[0]);
        userMedalType.put(user[0],MedalType.BRONZE);
        userMedalType.put(user[1],MedalType.SILVER);
        userMedalType.put(user[2],MedalType.GOLD);
        for (int i = 0; i < 3; i++) {
            medalFixture.giveMedal(user[i],post,userMedalType.get(user[i]));
        }
    }

    @Test
    void shouldGetMedalByPostId(){
        List<Medal> medals=medalRepository.findMedalsByPostId(post.getPostId());
        medals.forEach(medal -> assertEquals(post.getPostId(),medal.getPost().getPostId()));
        for (int i = 0; i < 3; i++) {
            assertEquals(user[i].getUserId(),medals.get(i).getUser().getUserId());
            assertEquals(userMedalType.get(user[i]),medals.get(i).getMedalType());
        }
    }

    @Test
    void shouldGetMedalByUserId(){
        for (int i = 0; i < 3; i++) {
            List<Medal> medals = medalRepository.findMedalsByUserId(user[i].getUserId());
            assertEquals(user[i].getUserId(),medals.get(0).getUser().getUserId());
            assertEquals(userMedalType.get(user[i]),medals.get(0).getMedalType());
            assertEquals(post.getPostId(),medals.get(0).getPost().getPostId());
        }
    }

    @Test
    void shouldFindUserByUserAndPostCompositeKey(){
        for (int i = 0; i < 3; i++) {
            UserAndPostCompositeKey userAndPostCompositeKey = new UserAndPostCompositeKey(user[i].getUserId(),post.getPostId());
            Optional<Medal> medal = medalRepository.findMedalByUserAndPostCompositeKey(userAndPostCompositeKey);
            assertEquals(true,medal.isPresent());
            assertEquals(user[i].getUserId(),medal.get().getUser().getUserId());
            assertEquals(post.getPostId(),medal.get().getPost().getPostId());
        }
    }

    @Test
    void shouldFindMedalsThatCurrentlyLoggedInUserGivenToUserId(){
        Post post2 = postFixture.createAPost(user[0]);
        postFixture.createAPost(user[0]);
        medalFixture.giveMedal(user[1],post2,MedalType.BRONZE);
        List<Medal> medals = medalRepository
                .findMedalsThatCurrentlyLoggedInUserGivenToUserId(user[1].getUserId(),user[0].getUserId());
        assertEquals(2,medals.size());
        assertEquals(post.getPostId(),medals.get(0).getPost().getPostId());
        assertEquals(post.getUser().getUserId(),medals.get(0).getPost().getUser().getUserId());
        assertEquals(MedalType.SILVER,medals.get(0).getMedalType());
        assertEquals(post2.getPostId(),medals.get(1).getPost().getPostId());
        assertEquals(post2.getUser().getUserId(),medals.get(1).getPost().getUser().getUserId());
        assertEquals(MedalType.BRONZE,medals.get(1).getMedalType());
    }
}
