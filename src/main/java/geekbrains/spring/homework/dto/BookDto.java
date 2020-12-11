package geekbrains.spring.homework.dto;

import geekbrains.spring.homework.entites.Book;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

// 1. Создайте две сущности: авторы и книги, каждая книга написана только одним автором
@Data
@NoArgsConstructor
@ApiModel(description = "Book dto")
public class BookDto {
    @ApiModelProperty(notes = "Unique book ID", example = "1", required = true, position = 0)
    private Long id;

    @ApiModelProperty(notes = "Book title", example = "How to Title a Book", required = true, position = 1)
    private String title;

    @ApiModelProperty(notes = "Number of pages in the book", example = "100", required = true, position = 2)
    private Long pages;

    @ApiModelProperty(notes = "Author of the book", example = "Author", required = true, position = 3)
    private String authorName;

    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.pages = book.getPages();
        this.authorName = book.getAuthorId().getName();
    }
}
