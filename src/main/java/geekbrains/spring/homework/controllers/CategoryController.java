package geekbrains.spring.homework.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import geekbrains.spring.homework.dto.CategoryDto;
import geekbrains.spring.homework.entites.Category;
import geekbrains.spring.homework.exceptions.ResourceNotFoundException;
import geekbrains.spring.homework.service.CategoryService;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getOneById(id).orElseThrow(() -> new ResourceNotFoundException("Unable to find category with id: " + id));
        return new CategoryDto(category);
    }
}
