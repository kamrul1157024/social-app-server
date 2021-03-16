package com.kamrul.blog.services.verify;

import com.kamrul.blog.models.post.Post;
import com.kamrul.blog.services.verify.exception.VerificationException;
import org.springframework.stereotype.Service;


@Service
public class PostVerifier
        extends Verifications
        implements Verifier<Post> {

    @Override
    public void verify(Post post) throws VerificationException {
        verifyForPoliticalPost(post.getPostText());
    }

}
