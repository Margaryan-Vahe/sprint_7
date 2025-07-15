import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierClient {
    private static final String BASE_PATH = "/api/v1/courier";

    @Step("Создание курьера {courier.login}")
    public ValidatableResponse create(Courier courier) {
        return given()
                .basePath(BASE_PATH)
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post()
                .then();
    }

    @Step("Авторизация курьером {credentials.login}")
    public int login(CourierCredentials credentials) {
        return given()
                .basePath(BASE_PATH + "/login")
                .header("Content-type", "application/json")
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }
    @Step("Отправка запроса авторизации курьера {credentials.login}")
    public ValidatableResponse loginRaw(CourierCredentials credentials) {
        return given()
                .basePath(BASE_PATH + "/login")
                .header("Content-type", "application/json")
                .body(credentials)
                .when()
                .post()
                .then();
    }

    @Step("Удаление курьера с id={id}")
    public ValidatableResponse delete(int id) {
        return given()
                .basePath(BASE_PATH + "/" + id)
                .when()
                .delete()
                .then();
    }
}
