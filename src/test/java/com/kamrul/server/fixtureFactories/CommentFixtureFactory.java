package com.kamrul.server.fixtureFactories;

import com.github.javafaker.Faker;
import com.kamrul.server.Utils;
import com.kamrul.server.models.comment.Comment;
import com.kamrul.server.models.comment.CommentForType;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentFixtureFactory {
    private final CommentRepository commentRepository;
    private final Faker faker = new Faker();

    @Autowired
    CommentFixtureFactory(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public Comment createComment(User user, CommentForType commentForType, Long commentFor,Comment ...overrides){
        String commentText = faker.lorem().sentence(2);
        Comment comment = new Comment(user,commentText,commentForType,commentFor);
        comment = Utils.override(comment,overrides);
        comment = commentRepository.save(comment);
        return commentRepository.findById(comment.getCommentId()).get();
    }

    public Comment reply(User user,Comment comment,Comment ...overrides){
        String replyText = faker.lorem().sentence(2);
        Comment reply = new Comment(user,comment,replyText);
        reply = Utils.override(reply,overrides);
        reply = commentRepository.save(reply);
        return commentRepository.findById(reply.getCommentId()).get();
    }
}
