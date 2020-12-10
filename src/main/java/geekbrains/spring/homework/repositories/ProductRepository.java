package geekbrains.spring.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import geekbrains.spring.homework.entites.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
