```
Blog
├───main
│   ├───java
│   │   └───com
│   │       └───kamrul
│   │           └───blog
│   │               │   BlogApplication.java
│   │               │
│   │               ├───configuration
│   │               │       SecurityConfiguration.java
│   │               │
│   │               ├───controllers
│   │               │       CommentController.java
│   │               │       CommentReplyController.java
│   │               │       PostController.java
│   │               │       UserController.java
│   │               │
│   │               ├───dto
│   │               │       CommentDTO.java
│   │               │       CommentReplyDTO.java
│   │               │       PostDTO.java
│   │               │       UserDTO.java
│   │               │
│   │               ├───exception
│   │               │       ErrorDetails.java
│   │               │       GlobalExceptionHandler.java
│   │               │       ResourceNotFoundException.java
│   │               │       UnauthorizedException.java
│   │               │
│   │               ├───models
│   │               │       Comment.java
│   │               │       CommentReply.java
│   │               │       Post.java
│   │               │       User.java
│   │               │
│   │               ├───repositories
│   │               │       CommentReplyRepository.java
│   │               │       CommentRepository.java
│   │               │       GeneralQueryRepository.java
│   │               │       PostRepository.java
│   │               │       UserRepository.java
│   │               │
│   │               ├───security
│   │               │   ├───filters
│   │               │   │       JWTRequestFilter.java
│   │               │   │
│   │               │   ├───jwt
│   │               │   │       JWTUtil.java
│   │               │   │
│   │               │   ├───models
│   │               │   │       AppUserDetails.java
│   │               │   │       AuthenticationRequest.java
│   │               │   │       AuthenticationResponse.java
│   │               │   │
│   │               │   └───services
│   │               │           AppUserDetailsService.java
│   │               │           AuthenticationService.java
│   │               │
│   │               └───utils
│   │                       Message.java
│   │                       RESPONSE_MSG.java
│   │
│   └───resources
│       │   application.properties
│       │
│       ├───static
│       └───templates
└───test
    └───java
        └───com
            └───kamrul
                └───blog
                    │   BlogApplicationTests.java
                    │
                    └───repositories
                            UserRepositoryTest.java
```
