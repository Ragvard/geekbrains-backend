package ru.geekbrains.mini.market.tests;

import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class MyTests {

    static ArrayList<Product> products;

    @BeforeAll
    static void init() {
        products = new ArrayList<>();
        products.add(new Product(1L, "Milk", 95, "Food"));
        products.add(new Product(2L, "Bread", 25, "Food"));
        products.add(new Product(3L, "Cheese", 360, "Food"));
        products.add(new Product(4L, "Samsung Watch X1000", 20000, "Electronic"));
        products.add(new Product(5L, "LG TV 1", 50000, "Electronic"));
    }

    // C - Create

    @Test
    public void testCreate() {
        Map<String, String> productMap = new HashMap<>();
        productMap.put("title", "Pepsi 2L");
        productMap.put("price", "120");
        productMap.put("categoryTitle", "Food");

        Product created = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(productMap)
                .when()
                .post("http://localhost:8189/market/api/v1/products")
                .then()
                .statusCode(201)
                .log().ifValidationFails(LogDetail.BODY)
                .body("title", equalTo("Pepsi 2L"))
                .body("price", equalTo(120))
                .body("categoryTitle", equalTo("Food"))
                .extract()
                .as(Product.class);

        when()
                .delete("http://localhost:8189/market/api/v1/products/" + created.getId())
                .then()
                .statusCode(200);
    }

    @Test
    public void testCreateWithId() {
        Map<String, String> productMap = new HashMap<>();
        productMap.put("id", "2");
        productMap.put("title", "Bepis 2.1L");
        productMap.put("price", "40");
        productMap.put("categoryTitle", "Food");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(productMap)
                .when()
                .post("http://localhost:8189/market/api/v1/products")
                .then()
                .statusCode(400);
    }

    @Test
    public void testCreateWithoutData() {
        Map<String, String> productMap = new HashMap<>();
        productMap.put("title", "Pepsi 2L");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(productMap)
                .when()
                .post("http://localhost:8189/market/api/v1/products")
                .then()
                .statusCode(500);
    }

    // R - Read

    @Test
    public void testReadSingle() {
        when()
                .get("http://localhost:8189/market/api/v1/products/1")
                .then()
                .statusCode(200)
                .and()
                .body("id", equalTo(products.get(0).getId().intValue()))
                .body("title", equalTo(products.get(0).getTitle()))
                .body("price", equalTo(products.get(0).getPrice()))
                .body("categoryTitle", equalTo(products.get(0).getCategoryTitle()));

    }

    @Test
    public void testReadAll() {
        Product[] productsTest = given().when().
                get("http://localhost:8189/market/api/v1/products")
                .then()
                .statusCode(200)
                .extract()
                .as(Product[].class);

        // Проверяются только первые 5 известных продуктов, далее могут быть другие
        for (int i = 0; i < products.size(); i++) {
            assertEquals(productsTest[i].getId(), products.get(i).getId());
            assertEquals(productsTest[i].getTitle(), products.get(i).getTitle());
            assertEquals(productsTest[i].getCategoryTitle(), products.get(i).getCategoryTitle());
            assertEquals(productsTest[i].getPrice(), products.get(i).getPrice());
        }
    }

    @Test
    public void testReadNone() {
        when()
                .get("http://localhost:8189/market/api/v1/products/999999999")
                .then()
                .statusCode(404);

    }

    // U - Update

    @Test
    public void testUpdate() {
        // Создаю продукт
        Map<String, String> productMap = new HashMap<>();
        productMap.put("title", "Mars");
        productMap.put("price", "80");
        productMap.put("categoryTitle", "Food");

        // Запоминаю ID
        Product created = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(productMap)
                .when()
                .post("http://localhost:8189/market/api/v1/products")
                .then()
                .statusCode(201)
                .log().ifValidationFails(LogDetail.BODY)
                .body("title", equalTo("Mars"))
                .body("price", equalTo(80))
                .body("categoryTitle", equalTo("Food"))
                .extract()
                .as(Product.class);

        // Обновляю продукт
        productMap = new HashMap<>();
        productMap.put("id", created.getId().toString());
        productMap.put("title", "Snickers");
        productMap.put("price", "85");
        productMap.put("categoryTitle", "Food");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(productMap)
                .when()
                .put("http://localhost:8189/market/api/v1/products")
                .then()
                .statusCode(200)
                .log().ifValidationFails(LogDetail.BODY)
                .body("id", equalTo(created.getId().intValue()))
                .body("title", equalTo("Snickers"))
                .body("price", equalTo(85))
                .body("categoryTitle", equalTo("Food"));

        // Проверяю
        when()
                .get("http://localhost:8189/market/api/v1/products/" + created.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(created.getId().intValue()))
                .body("title", equalTo("Snickers"))
                .body("price", equalTo(85))
                .body("categoryTitle", equalTo("Food"));

        when()
                .delete("http://localhost:8189/market/api/v1/products/" + created.getId())
                .then()
                .statusCode(200);
    }

    @Test
    public void testUpdateInvalidId() {

        HashMap<String, String> productMap = new HashMap<>();
        productMap.put("id", "9999999999999999999");
        productMap.put("title", "Snickers");
        productMap.put("price", "85");
        productMap.put("categoryTitle", "Food");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(productMap)
                .when()
                .put("http://localhost:8189/market/api/v1/products")
                .then()
                .statusCode(400);
    }

    // D - Delete

    @Test
    public void testDelete() {
        // Создаю продукт
        Map<String, String> productMap = new HashMap<>();
        productMap.put("title", "toDelete");
        productMap.put("price", "0");
        productMap.put("categoryTitle", "Food");

        // Запоминаю id
        Product deleted = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(productMap)
                .when()
                .post("http://localhost:8189/market/api/v1/products")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .as(Product.class);

        // Удаляю
        when()
                .delete("http://localhost:8189/market/api/v1/products/" + deleted.getId())
                .then()
                .statusCode(200);

        // Проверяю
        when()
                .get("http://localhost:8189/market/api/v1/products/" + deleted.getId())
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteNone() {
        when().
                delete("http://localhost:8189/market/api/v1/products/99999999999999")
                .then()
                .statusCode(500);

    }
}
