package com.kamrul.server.controllers;

import com.github.javafaker.Faker;
import com.kamrul.server.MockRequest;
import com.kamrul.server.dto.CommentDTO;
import com.kamrul.server.fixtureFactories.CommentFixtureFactory;
import com.kamrul.server.fixtureFactories.PostFixtureFactory;
import com.kamrul.server.models.comment.Comment;
import com.kamrul.server.models.comment.CommentForType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("controller_test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentControllerTest{
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private MockRequest mockRequest;
    @Autowired
    private PostFixtureFactory postFixtureFactory;
    @Autowired
    private CommentFixtureFactory commentFixtureFactory;
    private final Faker faker = new Faker();
    private User user;
    private Post post;

    @BeforeAll
    void setUp(){
        mockRequest = new MockRequest(mockMvc,userRepository);
        user = mockRequest.getUser();
    }

    @BeforeEach
    void beforeEach(){
        post = postFixtureFactory.createAPost(user);
    }

    @Test
    void shouldCreateComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        String commentText = faker.lorem().sentence(20);
        commentDTO.setCommentText(commentText);
        commentDTO.setCommentForType(CommentForType.POST);
        commentDTO.setCommentFor(post.getPostId());
        mockRequest.post(String.format("/api/comment/", post.getPostId()),commentDTO)
                .andExpect(jsonPath("$.commentId").isNumber())
                .andExpect(jsonPath("$.commentText").value(commentText))
                .andExpect(jsonPath("$.user.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.creationDate").isString());
    }

    @Test
    void shouldGetCommentsWithReplies() throws Exception{
        Comment comment1 = commentFixtureFactory.createComment(user,CommentForType.POST,post.getPostId());
        Comment comment2 = commentFixtureFactory.createComment(user,CommentForType.POST,post.getPostId());
        Comment reply1 = commentFixtureFactory.reply(user,comment1);
        Comment reply2 = commentFixtureFactory.reply(user,comment1);
        ResultActions response = mockRequest.get(String.format("/api/comment/commentForType/POST/commentFor/%s?page=0",post.getPostId()))
                .andExpect(jsonPath("$.numberOfElements").value(4));
        ExpectationMatcher.expectToBeComment(response,reply2,"content[0]");
        ExpectationMatcher.expectToBeComment(response,reply1,"content[1]");
        ExpectationMatcher.expectToBeComment(response,comment2,"content[2]");
        ExpectationMatcher.expectToBeComment(response,comment1,"content[3]");
    }

    @Test
    void shouldUpdateComment() throws Exception{
        Comment comment = commentFixtureFactory.createComment(user,CommentForType.POST,post.getPostId());
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(comment.getCommentId());
        commentDTO.setCommentText(faker.lorem().sentence(20));
        Comment expectedComment = comment;
        expectedComment.setCommentText(commentDTO.getCommentText());
        expectedComment.setUpdated(true);
        ResultActions response = mockRequest.put("/api/comment/",commentDTO);
        ExpectationMatcher.expectToBeComment(response,expectedComment);
    }

    @Test
    void shouldReplyToTheComment() throws Exception{
        Comment comment = commentFixtureFactory.createComment(user,CommentForType.POST,post.getPostId());
        CommentDTO reply = new CommentDTO();
        reply.setCommentText(faker.lorem().sentence(20));
        ResultActions response = mockRequest.post(String.format("/api/comment/replyTo/comment/%s",comment.getCommentId()),reply)
                .andExpect(jsonPath("$.commentId").isNumber());
        Comment expectedReply = new Comment(user,comment,reply.getCommentText());
        ExpectationMatcher.expectToBeComment(response,expectedReply);
    }

    // @Test
    // void shouldDeleteCommentAndReplies() throws Exception{
    //     Comment comment = commentFixtureFactory.createComment(user,CommentForType.POST,post.getPostId());
    //     Comment reply1 = commentFixtureFactory.reply(user,comment);
    //     Comment reply2 = commentFixtureFactory.reply(user,comment);
    //     mockRequest.delete(String.format("/api/comment/%s",comment.getCommentId()));
    //     ResultActions response = mockRequest.get(String.format("/api/comment/commentForType/POST/commentFor/%s?page=0",post.getPostId()))
    //             .andExpect(jsonPath("$.numberOfElements").value(0));
    // }
}
