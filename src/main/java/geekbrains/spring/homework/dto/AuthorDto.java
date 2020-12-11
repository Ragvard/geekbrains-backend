package geekbrains.spring.homework.dto;

import geekbrains.spring.homework.entites.Author;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

// 1. Создайте две сущности: авторы и книги, каждая книга написана только одним автором
@Data
@NoArgsConstructor
@ApiModel(description = "Author dto")
public class AuthorDto {
    @ApiModelProperty(notes = "Unique author ID", example = "1", required = true, position = 0)
    private Long id;

    @ApiModelProperty(notes = "Author name", example = "Ivan Ivanov", required = true, position = 1)
    private String name;

    @ApiModelProperty(notes = "Number of books authored", example = "5", required = true, position = 2)
    private int booksAuthored;

    public AuthorDto(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.booksAuthored = author.getBooks().size();
    }
}
