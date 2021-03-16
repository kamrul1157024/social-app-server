package com.kamrul.blog.services.verify;

import com.kamrul.blog.models.comment.Comment;
import com.kamrul.blog.services.verify.exception.VerificationException;
import org.springframework.stereotype.Service;

@Service
public class CommentVerifier
        extends Verifications
        implements Verifier<Comment>{

    @Override
    public void verify(Comment comment)
            throws VerificationException {
        verifyForPoliticalPost(comment.getCommentText());
    }
}
