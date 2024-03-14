package Tests;

import Hooks.ApiHooks;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

import static APISteps.Steps.*;

public class ApiTest extends ApiHooks {

    @Tag("api-tests")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @CsvSource({"1"})
    @DisplayName("Работа с пользователем и продуктами")
    public void registration(String id) throws IOException {
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
}
