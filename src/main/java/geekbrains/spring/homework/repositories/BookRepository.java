package geekbrains.spring.homework.repositories;

import geekbrains.spring.homework.entites.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.authorId=(SELECT a.id FROM Author a WHERE a.name=:name)")
    List<Book> getBooksByAuthor(@Param("name") String name);
}
