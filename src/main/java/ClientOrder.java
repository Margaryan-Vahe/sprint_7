import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class ClientOrder {
    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Создание заказа: {order}")
    public ValidatableResponse create(Order order) {
        return given()
                .basePath(ORDER_PATH)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post()
                .then();
    }

    @Step("Отмена заказа: {order}")
    public ValidatableResponse cancel(int trackId) {
        return given()
                .basePath(ORDER_PATH + "/cancel")
                .header("Content-type", "application/json")
                .param("track", trackId)
                .when()
                .put()
                .then();
    }

    @Step("Получение списка всех заказов без параметров")
    public ValidatableResponse getAllOrders() {
        return given()
                .basePath(ORDER_PATH)
                .header("Content-type", "application/json")
                .when()
                .get()
                .then();
    }
}
