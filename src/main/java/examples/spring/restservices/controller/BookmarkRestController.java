package examples.spring.restservices.controller;

import examples.spring.restservices.domain.Bookmark;
import examples.spring.restservices.exception.UserNotFoundException;
import examples.spring.restservices.repository.AccountRepository;
import examples.spring.restservices.repository.BookmarkRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;


@RestController
@RequestMapping("/{username}/bookmarks")
public class BookmarkRestController {

    private final AccountRepository accountRepository;
    private final BookmarkRepository bookmarkRepository;


    public BookmarkRestController(AccountRepository accountRepository, BookmarkRepository bookmarkRepository) {
        this.accountRepository = accountRepository;
        this.bookmarkRepository = bookmarkRepository;
    }


    @RequestMapping(method = RequestMethod.GET)
    Collection<Bookmark> readBookmarks(@PathVariable String username) {

        this.validateUser(username);
        return this.bookmarkRepository.findByAccountUsername(username);
    }


    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@PathVariable String username, @RequestBody Bookmark bookmark) {

        this.validateUser(username);

        return this.accountRepository
                .findByUsername(username)
                .map(account -> {
                    Bookmark result = bookmarkRepository.save(new Bookmark(account,
                            bookmark.getUri(), bookmark.getDescription()));

                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest().path("/{id}")
                            .buildAndExpand(result.getId()).toUri();

                    return ResponseEntity.created(location).build();
                })
                .orElse(ResponseEntity.noContent().build());
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{bookmarkId}")
    Bookmark readBookmark(@PathVariable String username, @PathVariable Long bookmarkId) {

        this.validateUser(username);
        return this.bookmarkRepository.findOne(bookmarkId);
    }


    private void validateUser(String username) {
        this.accountRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
}

