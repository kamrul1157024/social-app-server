package com.kamrul.server.fixtures;

import com.kamrul.server.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.server.models.medal.Medal;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.MedalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MedalFixture {
    private final MedalRepository medalRepository;

    @Autowired
    public MedalFixture(MedalRepository medalRepository){
        this.medalRepository = medalRepository;
    }

    public Medal giveMedal(User user, Post post, MedalType medalType){
        Medal medal = new Medal();
        UserAndPostCompositeKey userAndPostCompositeKey = new UserAndPostCompositeKey(user.getUserId(),post.getPostId());
        medal.setUserAndPostCompositeKey(userAndPostCompositeKey);
        medal.setUser(user);
        medal.setPost(post);
        medal.setMedalType(medalType);
        return medalRepository.save(medal);
    }
}
