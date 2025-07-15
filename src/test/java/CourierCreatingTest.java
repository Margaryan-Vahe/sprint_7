import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierCreatingTest {
    private CourierClient client;
    private Integer courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        client = new CourierClient();
    }

    @Test
    public void createCourierShouldReturn201AndOkTrue() {
        String login = "courier" + System.currentTimeMillis();
        String password = "1234";
        Courier courier = new Courier(login, password, "YandexTestName");
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        client.create(courier)
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = client.login(courierCredentials);
    }

    @Test
    public void duplicateCourierReturns409() {
        String login = "courier" + System.currentTimeMillis();
        String password = "1234";
        Courier courier = new Courier(login, password, "YandexTestName");
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        client.create(courier)
                .statusCode(201)
                .body("ok", equalTo(true));
        courierId = client.login(courierCredentials);

        client.create(courier)
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));

        /* данный тест проходит с ошибкой, а именно:
        Expected: Этот логин уже используется
        Actual: Этот логин уже используется. Попробуйте другой.

        Поскольку в чате было указано, что документацию принимаем "как есть", то
        не стал менять текст сообщения на актуальный
        */
    }

    @Test
    public void missingDataReturns400() {
        Courier courier = new Courier("courier" + System.currentTimeMillis(), "", "YandexTestName");

        client.create(courier)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            client.delete(courierId)
                    .statusCode(200)
                    .body("ok", equalTo(true));
        }
    }
}
