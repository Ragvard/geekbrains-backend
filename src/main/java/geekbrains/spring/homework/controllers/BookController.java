package geekbrains.spring.homework.controllers;

import geekbrains.spring.homework.dto.BookDto;
import geekbrains.spring.homework.entites.Book;
import geekbrains.spring.homework.exceptions.LibraryError;
import geekbrains.spring.homework.exceptions.ResourceNotFoundException;
import geekbrains.spring.homework.service.BookService;
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
@RequestMapping("/library/books")
@Api("Books")
public class BookController {
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET http://localhost:8080/library/books
    @GetMapping("/all")
    @ApiOperation("Returns all books")
    public List<BookDto> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        ArrayList<BookDto> result = new ArrayList<>();
        for ( Book book : books) {
            result.add(new BookDto(book));
        }
        return result;
    }

    // 4. Реализуйте отправку JSON'а с ошибкой 404, если клиент запросил несуществующий ресурс
    // GET http://localhost:8080/library/books/{id}
    @GetMapping("/{id}")
    @ApiOperation("Returns book by id. 404 if not found")
    public BookDto getBookById(@ApiParam("Id of the book to be get") @PathVariable Long id) {
        Book book = bookService.getBookById(id).orElseThrow(() -> new ResourceNotFoundException("Unable to find book with id: " + id));;
        return new BookDto(book);
    }

    // 6. ** Попробуйте реализовать поиск книг по имени автора
    // GET http://localhost:8080/library/books?author_name=Programmer
    @GetMapping
    @ApiOperation("Returns book by id. 404 if not found")
    public List<BookDto> getBooksByAuthor(@ApiParam("Name of the author") @RequestParam(name = "author_name") String name) {
        List<Book> books = bookService.getBooksByAuthor(name);
        ArrayList<BookDto> result = new ArrayList<>();
        for ( Book book : books) {
            result.add(new BookDto(book));
        }
        return result;
    }

    @PostMapping
    @ApiOperation("Creates new book if id null")
    // POST http://localhost:8080/library/books
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        if (book.getId() != null) {
            return new ResponseEntity<>(new LibraryError(HttpStatus.BAD_REQUEST.value(), "Id must be null"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete book by id")
    // DELETE http://localhost:8080/library/books/{id}
    public void deleteBookById(@ApiParam("Id of the book to delete") @PathVariable Long id) {
        bookService.deleteById(id);
    }
}
