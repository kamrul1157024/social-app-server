package com.kamrul.server.controllers;

import com.kamrul.server.exception.ResourceNotFoundException;
import com.kamrul.server.exception.UnauthorizedException;
import com.kamrul.server.models.booklet.Booklet;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.BookletContentRepository;
import com.kamrul.server.repositories.BookletRepository;
import com.kamrul.server.repositories.GeneralQueryRepository;
import com.kamrul.server.repositories.UserRepository;
import com.kamrul.server.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

import static com.kamrul.server.utils.GeneralResponseMSG.BOOKLET_NOT_FOUND_MSG;
import static com.kamrul.server.utils.GeneralResponseMSG.USER_NOT_FOUND_MSG;

@CrossOrigin
@RestController
@RequestMapping("/api/booklet")
public class BookletController {

    @Autowired
    private BookletRepository bookletRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookletContentRepository bookletContentRepository;
    @GetMapping
    ResponseEntity<?> getBookLetById(@RequestParam("id") Long bookletId)
            throws ResourceNotFoundException {
        Booklet booklet= GeneralQueryRepository.getByID(
          bookletRepository,
          bookletId, BOOKLET_NOT_FOUND_MSG
        );
        return new ResponseEntity<>(booklet, HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackOn = {Exception.class})
    ResponseEntity<?> postBooklet(
            @RequestBody  Booklet booklet,
            @RequestHeader("Authorization")Optional<String> jwt)
            throws UnauthorizedException, ResourceNotFoundException {

        Long loggedInUserId= JWTUtil.getUserIdFromJwt(jwt.get());
        User loggedInUser=GeneralQueryRepository.getByID(
                userRepository,
                loggedInUserId,
                USER_NOT_FOUND_MSG);

        Booklet bookletToSaveOnDatabase=new Booklet();
        bookletToSaveOnDatabase.setBookletTitle(booklet.getBookletTitle());
        bookletToSaveOnDatabase.setBookletDescription(booklet.getBookletDescription());
        bookletToSaveOnDatabase.setUser(loggedInUser);
        Booklet savedBooklet=bookletRepository.save(bookletToSaveOnDatabase);
        bookletRepository.flush();
        booklet.getBookletContents()
                .forEach(bookletContent ->{
                    bookletContent.setBooklet(savedBooklet);
                    bookletContentRepository.save(bookletContent);
                });
        savedBooklet.setBookletContents(booklet.getBookletContents());
        return new ResponseEntity<>(savedBooklet,HttpStatus.OK);
    }

    @PutMapping
    ResponseEntity<?> updateBooklet(
            @RequestBody Booklet updatedBooklet,
            @RequestHeader("Authorization") Optional<String> jwt)
            throws UnauthorizedException, ResourceNotFoundException {
      Long userId=JWTUtil.getUserIdFromJwt(jwt.get());
      User user= GeneralQueryRepository.getByID(
              userRepository,
              userId,
              USER_NOT_FOUND_MSG
      );
      /* Checking Booklet Exist! */
      GeneralQueryRepository.getByID(
              bookletRepository,
              updatedBooklet.getBookletId(),
              BOOKLET_NOT_FOUND_MSG
      );

      updatedBooklet.setUser(user);
      bookletRepository.save(updatedBooklet);

      return new ResponseEntity<>(updatedBooklet,HttpStatus.OK);
    }

    @DeleteMapping
    ResponseEntity<?> deleteBookLetById(@RequestParam("bookletId") Long bookletId)
            throws ResourceNotFoundException {
        Booklet booklet=GeneralQueryRepository.getByID(
                bookletRepository,
                bookletId,
                BOOKLET_NOT_FOUND_MSG
        );
        bookletRepository.deleteInBatch(Arrays.asList(booklet));
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


}