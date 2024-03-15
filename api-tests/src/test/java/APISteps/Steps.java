package APISteps;

import groovy.json.JsonOutput;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class Steps {
    public static String token;

    public static String errorMessage;

    static RequestSpecification website = new RequestSpecBuilder()
            .setBaseUri("http://9b142cdd34e.vps.myjino.ru:49268")
            .build();

    @Step("Регистрация пользователя")
    public static void createPerson() throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/authentication.json"))));
        Response creatingPerson = given()
                .header("Content-type", "application/json")
                .spec(website)
                .body(body.toString())
                .when()
                .post("/register")
                .then()
                .statusCode(201)
                .extract()
                .response();

        String authorizationJson = new JSONObject(creatingPerson.getBody().asString()).toString();
        System.out.println(authorizationJson);
    }

    @Step("Авторизация пользователя")
    public static void authenticationPerson() throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/authentication.json"))));
        Response authenticationPerson = given()
                .header("Content-type", "application/json")
                .spec(website)
                .body(body.toString())
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .response();
        System.out.println(new JSONObject(authenticationPerson.getBody().asString()));

        token = new JSONObject(authenticationPerson.getBody().asString()).get("access_token").toString();
        System.out.println("Токен для входа: " + token);
    }

    @Step("Просмотр продуктов")
    public static void checkProduct() {
        Response checkProduct = given()
                .header("Content-type", "application/json")
                .spec(website)
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String listProduct = checkProduct.getBody().asString();
        System.out.println("Вывод всех продуктов: " + listProduct);
    }

    @Step("Добавление продукта")
    public static void addProduct() throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/description-product.json"))));
        Response addProduct = given()
                .header("Content-type", "application/json")
                .spec(website)
                .body(body.toString())
                .when()
                .post("/products")
                .then()
                .statusCode(405)
                .extract()
                .response();

        String listProduct = addProduct.getBody().asString();
        System.out.println("Добавление продукта: " + listProduct);
    }

    @Step("Просмотр продукта c id: {id}")
    public static void checkProductId(String id, int statusCode, String message) {
        Response addProduct = given()
                .header("Content-type", "application/json")
                .spec(website)
                .when()
                .get("/products/" + id)
                .then()
                .statusCode(statusCode)
                .extract()
                .response();

        String productWithId = addProduct.getBody().asString();
        System.out.println("Данные по продукту с id = " + id + ": " + productWithId);
         Assertions.assertEquals(statusCode > 400? new JSONObject(productWithId).get("message").toString() : "",message) ;

    }

    @Step("Обновление продукта c id: {id}")
    public static void updateProduct(String id) throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/description-product.json"))));
        Response updateProduct = given()
                .header("Content-type", "application/json")
                .spec(website)
                .body(body.toString())
                .when()
                .put("/products/" + id)
                .then()
                .statusCode(405)
                .extract()
                .response();

        String product = updateProduct.getBody().asString();
        System.out.println("Обновление продукта: " + product);
    }

    @Step("Удаление продукта c id: {id}")
    public static void deleteProduct(String id) throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/description-product.json"))));
        Response deleteProduct = given()
                .header("Content-type", "application/json")
                .spec(website)
                .body(body.toString())
                .when()
                .delete("/products/" + id)
                .then()
                .statusCode(405)
                .extract()
                .response();

        String product = deleteProduct.getBody().asString();
        System.out.println("Удаление продукта: " + product);
    }

    @Step("Просмотр корзины")
    public static void getCart() {

        Response getCart = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .spec(website)
                .when()
                .get("/cart")
                .then()
                .statusCode(404)
                .extract()
                .response();

        String product = getCart.getBody().asString();
        System.out.println("Список карт: " + product);
    }

    @Step("Добавление в корзину")
    public static void postCart() throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/add-product.json"))));
        Response postCart = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .spec(website)
                .body(body.toString())
                .when()
                .post("/cart")
                .then()
                .statusCode(201)
                .extract()
                .response();

        String product = postCart.getBody().asString();
        System.out.println("Добавление в корзину: " + product);
    }

    @Step("Удаление из корзины продукта с id: {id}")
    public static void deleteCart(String id) throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/add-product.json"))));
        Response postCart = given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + token)
                .spec(website)
                .body(body.toString())
                .when()
                .delete("/cart/" + id)
                .then()
//                .statusCode(201)
                .extract()
                .response();

        String product = postCart.getBody().asString();
        System.out.println("Удаление из корзины: " + product);
    }

    @Step("Проверка сообщения")
    public static void checkMessage(String message) {
        Assertions.assertEquals(errorMessage, message);
    }
}
