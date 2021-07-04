package com.kamrul.server.services.verify;

import com.github.javafaker.Faker;
import com.kamrul.server.dto.CommentReplyDTO;
import com.kamrul.server.politicalPostFilter.FilterPost;
import com.kamrul.server.politicalPostFilter.models.TextAnalyzingResponse;
import com.kamrul.server.services.draftJS.DraftJSTextParsing;
import com.kamrul.server.services.verify.exception.PoliticalPostException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles("verifier_service_test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CommentReplyVerifierTest {
    @Autowired
    FilterPost filterPost;
    @Autowired
    DraftJSTextParsing draftJSTextParsing;
    @Autowired
    CommentReplyVerifier commentReplyVerifier;
    Faker faker =  new Faker();
    TextAnalyzingResponse isPoliticalPost = new TextAnalyzingResponse();
    TextAnalyzingResponse isNotPoliticalPost = new TextAnalyzingResponse();
    String text =  faker.lorem().paragraph(100);

    @BeforeEach
    void setUp(){
        doReturn(text).when(draftJSTextParsing).getAllText(text);
        isPoliticalPost.setProbabilityOfBeingPolitical(.9);
        isNotPoliticalPost.setProbabilityOfBeingPolitical(.1);
    }

    @Test
    void shouldDetectPoliticalCommentProperly() {
        CommentReplyDTO commentReplyDTO=new CommentReplyDTO();
        commentReplyDTO.setCommentReplyText(text);

        doReturn(isPoliticalPost).when(filterPost).isPoliticalStatement(text);
        assertThrows(PoliticalPostException.class,()->commentReplyVerifier.verify(commentReplyDTO));

        doReturn(isNotPoliticalPost).when(filterPost).isPoliticalStatement(text);
        assertDoesNotThrow(()->commentReplyVerifier.verify(commentReplyDTO));
    }
}