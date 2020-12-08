package com.geekbrains.spring.homework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// http://localhost:8189/app/products
@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductRepository productRepository;

    // Из вебинара --------------------------------------------------------------
    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id).get();
    }

    @PostMapping
    public Product saveProduct(@RequestBody Product product) {
        product.setId(null);
        return productRepository.save(product);
    }

    @GetMapping("/increase_cost_by_10/{id}")
    public void increaseCost(@PathVariable Long id) {
        Product p = productRepository.findById(id).get();
        p.setPrice(p.getPrice() + 10);
        productRepository.save(p);
    }

    @GetMapping("/count")
    public Long getProductsCount() {
        return productRepository.count();
    }

    // --------------------------------------------------------------


    // ДЗ 1

    // 1. Обработать запрос вида: GET /products/filtered?min_price=100
    // В результате должен вернуться список товаров, цена которых >= 100
    // GET http://localhost:8080/products/filtered?min_price=100
    @GetMapping("/filtered")
    @ResponseBody
    public List<Product> filterByMinPrice(@RequestParam Integer min_price) {
        return productRepository.filterByMinPrice(min_price);
    }

    // 2. Обработать запрос вида: GET /products/delete/1
    // В результате должен удалиться товар с соответствющим id
    // GET http://localhost:8080/products/delete/{id}
    @GetMapping("/delete/{id}")
    @ResponseBody
    public void deleteById(@PathVariable(name = "id") Long id) {
        if(productRepository.findById(id).isPresent()) productRepository.deleteById(id);
    }

    // 3. * Попробуйте реализовать возможность изменения названия товара по id
    // Что-то вроде: /products/{id}/change_title...
    // POST http://localhost:8080/products/{id}/change_title?title=new_title
    // Через POST не вышло, сделал через GET
    @GetMapping("/{id}/change_title")
    public void changeTitle(@PathVariable Long id, @RequestParam(name = "title") String new_title) {
        if(productRepository.findById(id).isPresent()) {
            Product p = productRepository.findById(id).get();
            p.setTitle(new_title);
            productRepository.save(p);
        }
    }
}
