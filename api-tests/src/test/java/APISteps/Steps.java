package APISteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.AddProduct;
import model.Product;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Steps {
    public static String token;

    public static String answerJson;

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

        token = new JSONObject(authenticationPerson.getBody().asString()).get("access_token").toString();
        System.out.println("Токен для входа: " + token);
    }

    @Step("Просмотр продуктов")
    public static void findAllProduct() {
        List<Product> listProducts = RestAssured.given()
                .header("Content-type", "application/json")
                .spec(website)
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .extract().as(new ObjectMapper().getTypeFactory().constructCollectionType(List.class, Product.class));
        for (Product product : listProducts) {
            System.out.println(product);
        }
    }

    @Step("Добавление продукта")
    public static void findProduct() throws IOException {
        JSONObject body = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/description-product.json"))));
        Response findProduct = given()
                .header("Content-type", "application/json")
                .spec(website)
                .body(body.toString())
                .when()
                .post("/products")
                .then()
                .statusCode(201)
                .extract().as(new ObjectMapper().getTypeFactory().constructType(Product.class));

        System.out.println(findProduct.toString());
    }


    @Step("Просмотр продукта c id и проверкой на статус код")
    public static void findProductId(Long id, int statusCode) {
        switch (statusCode) {
            case (200):
                List<Product> listProduct = RestAssured.given()
                        .header("Content-type", "application/json")
                        .spec(website)
                        .when()
                        .get("/products/" + id)
                        .then()
                        .statusCode(statusCode)
                        .extract().as(new ObjectMapper().getTypeFactory().constructCollectionType(List.class, Product.class));
                System.out.println(listProduct.toString());
                break;
            case (404):
                Response errorMessage = given()
                        .header("Content-type", "application/json")
                        .spec(website)
                        .when()
                        .get("/products/" + id)
                        .then()
                        .statusCode(statusCode)
                        .extract()
                        .response();
                System.out.println("Error message: " + new JSONObject(errorMessage.getBody().asString()).get("message"));
                break;
        }
    }


    @Step("Обновление продукта c id и проверкой на статус код")
    public static void updateProduct(Long id, int statusCode) {
        switch (statusCode) {
            case (200):
                Product product = RestAssured.given()
                        .header("Content-type", "application/json")
                        .spec(website)
                        .body(new Product(id, "Holod", "Electr", 123.1, 10.0))
                        .when()
                        .put("/products/" + id)
                        .then()
                        .statusCode(statusCode)
                        .extract().as(new ObjectMapper().getTypeFactory().constructType(Product.class));
                System.out.println(product);
                break;

            case (404):
                Response errorMessage = given()
                        .header("Content-type", "application/json")
                        .spec(website)
                        .when()
                        .get("/products/" + id)
                        .then()
                        .statusCode(statusCode)
                        .extract()
                        .response();
                System.out.println("Error message: " + new JSONObject(errorMessage.getBody().asString()).get("message"));
                break;
        }
    }

    @Step("Удаление продукта c id и проверкой на статус код")
    public static void deleteProduct(Long id, int statusCode) {
        switch (statusCode) {
            case (200):
                Response deleteProduct = given()
                        .header("Content-type", "application/json")
                        .spec(website)
                        .when()
                        .delete("/products/" + id)
                        .then()
                        .statusCode(statusCode)
                        .extract()
                        .response();
                System.out.println("Удаление завершено: " + new JSONObject(deleteProduct.getBody().asString()).get("message"));
                break;

            case (404):
                Response errorMessage = given().header("Content-type", "application/json")
                        .spec(website)
                        .when()
                        .delete("/products/" + id)
                        .then()
                        .statusCode(statusCode)
                        .extract()
                        .response();

                System.out.println("Error message: " + new JSONObject(errorMessage.getBody().asString()).get("message"));
                break;
        }
    }

    @Step("Просмотр корзины")
    public static void findAllCart(int statusCode) {
        Response errorMessage;
        switch (statusCode) {
            case (200):
                Response getCart = given()
                        .header("Content-type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .spec(website)
                        .when()
                        .get("/cart")
                        .then()
                        .statusCode(statusCode)
                        .extract()
                        .response();

                System.out.println("Список товаров в корзине: " + getCart.getBody().asString());
                break;

            case (401):
                errorMessage = given()
                        .header("Content-type", "application/json")
                        .spec(website)
                        .when()
                        .get("/cart")
                        .then()
                        .statusCode(statusCode)
                        .extract()
                        .response();

                System.out.println("Error message: " + new JSONObject(errorMessage.getBody().asString()).get("msg") + "\n");
                break;
            case (422):
                errorMessage = given()
                        .header("Content-type", "application/json")
                        .header("Authorization", "Bearer " + "")
                        .spec(website)
                        .when()
                        .get("/cart")
                        .then()
                        .statusCode(statusCode)
                        .extract()
                        .response();

                System.out.println("Error message: " + new JSONObject(errorMessage.getBody().asString()).get("msg") + "\n");

                break;
        }
    }

    @Step("Добавление в корзину")
    public static void postCart(int id, int quantity, int statusCode) {
        Response errorMessage;
        String product;
        switch (statusCode) {
            case (200):
                Response postCart = given()
                        .header("Content-type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .spec(website)
                        .body(new AddProduct(id, quantity))
                        .when()
                        .post("/cart")
                        .then()
                        .statusCode(statusCode)
                        .extract()
                        .response();

                product = postCart.getBody().asString();
                System.out.println("Добавление в корзину: " + product);
                break;
            case (401):
                errorMessage = given()
                        .header("Content-type", "application/json")
                        .spec(website)
                        .body(new AddProduct(id, quantity))
                        .when()
                        .post("/cart")
                        .then()
                        .statusCode(statusCode)
                        .extract()
                        .response();

                System.out.println("Error message: " + new JSONObject(errorMessage.getBody().asString()).get("msg") + "\n");

                break;
            case (422):
                errorMessage = given()
                        .header("Content-type", "application/json")
                        .header("Authorization", "Bearer " + "123")
                        .spec(website)
                        .body(new AddProduct(id, quantity))
                        .when()
                        .post("/cart")
                        .then()
                        .statusCode(statusCode)
                        .extract()
                        .response();

                System.out.println("Error message: " + new JSONObject(errorMessage.getBody().asString()).get("msg") + "\n");
                break;
        }
    }

    @Step("Удаление из корзины продукта с id: {id}")
    public static void deleteCart(Long id) throws IOException {
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

}
