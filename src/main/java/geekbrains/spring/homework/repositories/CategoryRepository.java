package geekbrains.spring.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import geekbrains.spring.homework.entites.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
