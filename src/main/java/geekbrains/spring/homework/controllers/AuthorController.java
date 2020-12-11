package geekbrains.spring.homework.controllers;

import geekbrains.spring.homework.dto.AuthorDto;
import geekbrains.spring.homework.entites.Author;
import geekbrains.spring.homework.exceptions.LibraryError;
import geekbrains.spring.homework.exceptions.ResourceNotFoundException;
import geekbrains.spring.homework.service.AuthorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// 2. Пропишите REST контроллеры для этих сущностей (для http-методов GET/ *POST)
@RestController
@RequestMapping("/library/authors")
@Api("Authors")
public class AuthorController {
    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @ApiOperation("Returns all authors")
    // GET http://localhost:8080/library/authors
    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        ArrayList<AuthorDto> result = new ArrayList<>();
        for ( Author author : authors) {
            result.add(new AuthorDto(author));
        }
        return result;
    }

    // 3. Через REST API дайте возможность запрашивать автора по id со следующей структурой:
    // {
    //   "id": ...,
    //   "name": ...,
    //   "booksCount": ... // количество написанных книг
    // }
    // 4. Реализуйте отправку JSON'а с ошибкой 404, если клиент запросил несуществующий ресурс
    // GET http://localhost:8080/library/authors/{id}
    @GetMapping("/{id}")
    @ApiOperation("Returns author and number of books authored by id. 404 if not found")
    public AuthorDto getAuthorById(@ApiParam("Id of the author to get") @PathVariable Long id) {
        Author author = authorService.getAuthorById(id).orElseThrow(() -> new ResourceNotFoundException("Unable to find author with id: " + id));
        return new AuthorDto(author);
    }


    @PostMapping
    @ApiOperation("Creates new author if id null")
    // POST localhost://localhost:8080/library/authors
    public ResponseEntity<?> createAuthor(@RequestBody Author author) {
        if (author.getId() != null) {
            return new ResponseEntity<>(new LibraryError(HttpStatus.BAD_REQUEST.value(), "Id must be null"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(authorService.save(author), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete author by id")
    // DELETE http://localhost:8080/library/authors/{id}
    public void deleteAuthorById(@ApiParam("Id of the author to delete") @PathVariable Long id) {
        authorService.deleteById(id);
    }
}
