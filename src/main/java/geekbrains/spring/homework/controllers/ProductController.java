package geekbrains.spring.homework.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import geekbrains.spring.homework.dto.ProductDto;
import geekbrains.spring.homework.entites.Product;
import geekbrains.spring.homework.exceptions.MarketError;
import geekbrains.spring.homework.exceptions.ResourceNotFoundException;
import geekbrains.spring.homework.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Api("Set of endpoints for products")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @ApiOperation("Returns all products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @ApiOperation("Returns a specific product by their identifier. 404 if does not exist.")
    public ProductDto getProductById(@ApiParam("Id of the book to be obtained. Cannot be empty.") @PathVariable Long id) {
        Product p = productService.getOneById(id).orElseThrow(() -> new ResourceNotFoundException("Unable to find product with id: " + id));
        return new ProductDto(p);
    }

    @PostMapping
    @ApiOperation("Creates a new product. If id != null, then throw bad request response")
    public ResponseEntity<?> createNewProduct(@RequestBody Product p) {
        if (p.getId() != null) {
            return new ResponseEntity<>(new MarketError(HttpStatus.BAD_REQUEST.value(), "Id must be null for new entity"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productService.save(p), HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("Modify product")
    public ResponseEntity<?> modifyProduct(@RequestBody Product p) {
        if (p.getId() == null) {
            return new ResponseEntity<>(new MarketError(HttpStatus.BAD_REQUEST.value(), "Id must be not null for new entity"), HttpStatus.BAD_REQUEST);
        }
        if (!productService.existsById(p.getId())) {
            return new ResponseEntity<>(new MarketError(HttpStatus.BAD_REQUEST.value(), "Product with id: " + p.getId() + " doesn't exist"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productService.save(p), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete product")
    public void deleteById(@ApiParam("Id of the product") @PathVariable Long id) {
        productService.deleteById(id);
    }
}
