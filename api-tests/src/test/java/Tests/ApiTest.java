package Tests;

import Hooks.ApiHooks;
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
    @DisplayName("Получить список всех продуктов")
    public void getProducts() {
        checkProduct();
    }


    @Tag("api-tests")
    @Test
    @DisplayName("Добавление продукта (отсутствие метода)")
    public void postProducts() throws IOException {
        addProduct();
    }

    @Tag("api-tests")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"1"})
    @DisplayName("Получение информации о продукте с id: {id}")
    public void infoProductPositive(String id){
        checkProductId(id, 200, "");
    }

    @Tag("api-tests")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"10"})
    @DisplayName("Получение информации о продукте с id: {id}")
    public void infoProductNegative(String id) {
        checkProductId(id, 404, "Product not found");
    }

   /* @Tag("api-tests")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"1"})
    @DisplayName("Работа с пользователем и продуктами")
    public void authorization(String id) throws IOException {
        createPerson();
        authenticationPerson();
        checkProduct();
        addProduct();
        checkProductId(id);
        updateProduct(id);
        deleteProduct(id);
        getCart();
        postCart();
        deleteCart(id);
    }


    @Tag("api-tests")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"1"})
    @DisplayName("Работа с пользователем и продуктами")
    public void authorization(String id) throws IOException {
        createPerson();
        authenticationPerson();
        checkProduct();
        addProduct();
        checkProductId(id);
        updateProduct(id);
        deleteProduct(id);
        getCart();
        postCart();
        deleteCart(id);
    }


    @Tag("api-tests")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"1"})
    @DisplayName("Работа с пользователем и продуктами")
    public void authorization(String id) throws IOException {
        createPerson();
        authenticationPerson();
        checkProduct();
        addProduct();
        checkProductId(id);
        updateProduct(id);
        deleteProduct(id);
        getCart();
        postCart();
        deleteCart(id);
    }


//    @Tag("api-tests")
//    @ParameterizedTest(name = "{displayName}: {arguments}")
//    @CsvSource({"1"})
//    @DisplayName("Работа с пользователем и продуктами")
//    public void authorization(String id) throws IOException {
//        createPerson();
//        authenticationPerson();
//        checkProduct();
//        addProduct();
//        checkProductId(id);
//        updateProduct(id);
//        deleteProduct(id);
//        getCart();
//        postCart();
//        deleteCart(id);
//    }

*/
}
