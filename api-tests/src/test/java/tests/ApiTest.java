package tests;

import hooks.ApiHooks;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

import static APISteps.Steps.*;

public class ApiTest extends ApiHooks {

    @Tag("api-tests")
    @Test
    @DisplayName("Регистрация пользователя")
    public void registration() throws IOException {
        createPerson();
    }

    @Tag("api-tests")
    @Test
    @DisplayName("Авторизация пользователя")
    public void authentication() throws IOException {
        authenticationPerson();
    }

    @Tag("api-tests")
    @Test
    @DisplayName("Получить список всех продуктов")
    public void getProducts() {
        findAllProduct();
    }


    @Tag("api-tests")
    @Test
    @DisplayName("Добавление продукта")
    public void postProducts() throws IOException {
        findProduct();
    }

    @Tag("api-tests")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"1, 200", "10, 404"})
    @DisplayName("Получение информации о продукте с id: {id} на статус код {statusCode}")
    public void infoProduct(Long id, Integer statusCode) {
        findProductId(id, statusCode);
    }

    @Tag("api-tests")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"1, 200", "10, 404"})
    @DisplayName("Обновление продукта c id: {id} на статус код {statusCode}")
    public void updateProducts(Long id, int statusCode) {
        updateProduct(id, statusCode);
    }

    @Tag("api-tests")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"1, 200", "10, 404"})
    @DisplayName("Удаление продукта c id: {id} на статус код {statusCode}")
    public void deleteProducts(Long id, int statusCode) {
        deleteProduct(id, statusCode);
    }

    @Tag("api-tests")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"200"})
    @DisplayName("Просмотр корзины c авторизацией на статус код {statusCode}")
    public void listCartPos(int statusCode) throws IOException {
        authenticationPerson();
        findAllCart(statusCode);
    }

    @Tag("api-tests")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"401","422"})
    @DisplayName("Просмотр корзины без авторизации на статус код {statusCode}")
    public void listCartNeg(int statusCode) {
        findAllCart(statusCode);
    }

    @Tag("api-tests")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"1, 2, 201, 200"})
    @DisplayName("Просмотр корзины с авторизацией на статус код {statusCode}")
    public void addProductInCartPos(int id, int quantity, int statusCodeCart, int statusCodeList) throws IOException {
        authenticationPerson();
        findAllCart(statusCodeList);
        postCart(id, quantity, statusCodeCart);
        findAllCart(statusCodeList);
    }

    @Tag("api-tests")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"1, 2, 401", "1, 2, 422"})
    @DisplayName("Просмотр корзины без авторизации на статус код {statusCode}")
    public void addProductInCartNeg(int id, int quantity, int statusCodeCart) {
        postCart(id, quantity, statusCodeCart);
    }
}
