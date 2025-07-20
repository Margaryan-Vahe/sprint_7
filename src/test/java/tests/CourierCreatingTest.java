package tests;

import base.BaseTest;
import data.Courier;
import data.CourierCredentials;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreatingTest extends BaseTest {

    @Test
    public void createCourierShouldReturn201AndOkTrue() {
        String login = "courier" + System.currentTimeMillis();
        String password = "1234";
        Courier courier = new Courier(login, password, "YandexTestName");
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        courierClient.create(courier)
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

        courierId = courierClient.login(courierCredentials);
    }

    @Test
    public void duplicateCourierReturns409() {
        String login = "courier" + System.currentTimeMillis();
        String password = "1234";
        Courier courier = new Courier(login, password, "YandexTestName");
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        courierClient.create(courier)
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
        courierId = courierClient.login(courierCredentials);

        courierClient.create(courier)
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется"));

        /* данный тест проходит с ошибкой, а именно:
        Expected: Этот логин уже используется
        Actual: Этот логин уже используется. Попробуйте другой.

        Поскольку в чате было указано, что документацию принимаем "как есть", то
        не стал менять текст сообщения на актуальный
        */
    }

    @Test
    public void missingPasswordReturns400() {
        Courier courier = new Courier("courier" + System.currentTimeMillis(), "", "YandexTestName");

        courierClient.create(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    public void missingLoginReturns400() {
        Courier courier = new Courier("", "1234", "YandexTestName");

        courierClient.create(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
