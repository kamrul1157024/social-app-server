package com.kamrul.server.repositories;

import com.kamrul.server.configuration.PageConfiguration;
import com.kamrul.server.fixtureFactories.CommentFixtureFactory;
import com.kamrul.server.fixtureFactories.PostFixtureFactory;
import com.kamrul.server.fixtureFactories.UserFixtureFactory;
import com.kamrul.server.models.comment.Comment;
import com.kamrul.server.models.comment.CommentForType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CommentRepositoryTest {

    User user1,user2;
    Post post;
    Comment comment,reply1,reply2;

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserFixtureFactory userFixtureFactory;
    @Autowired
    PostFixtureFactory postFixtureFactory;
    @Autowired
    CommentFixtureFactory commentFixtureFactory;

    @BeforeEach
    void setUp(){
        user1 = userFixtureFactory.createAUser();
        user2 = userFixtureFactory.createAUser();
        post = postFixtureFactory.createAPost(user1);
        comment = commentFixtureFactory.createComment(user1, CommentForType.POST,post.getPostId());
        reply1 = commentFixtureFactory.reply(user1,comment);
        reply2 = commentFixtureFactory.reply(user2,comment);
    }

    @Test
    void shouldGetCommentsWithRepliesByCommentForTypeAndCommentFor(){
        Page<Comment> comments = commentRepository.getCommentsByCommentForTypeAndAndCommentFor(
                CommentForType.POST,
                post.getPostId(),
                PageRequest.of(0, PageConfiguration.COMMENT_PAGE_SIZE)
        );
        assertEquals(3,comments.getTotalElements());
        assertEquals(true,comments.getContent().get(2).equals(comment));
        assertEquals(true,comments.getContent().get(1).equals(reply1));
        assertEquals(true,comments.getContent().get(0).equals(reply2));
    }
}
