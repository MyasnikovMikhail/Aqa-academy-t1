package hooks;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ApiHooks implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        RestAssured.filters(new AllureRestAssured());
    }

}
