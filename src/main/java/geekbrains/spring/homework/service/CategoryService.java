package geekbrains.spring.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import geekbrains.spring.homework.entites.Category;
import geekbrains.spring.homework.repositories.CategoryRepository;

import java.util.Optional;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<Category> getOneById(Long id) {
        return categoryRepository.findById(id);
    }
}
