import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierLoginTest {
    private CourierClient client;
    private Integer courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        client = new CourierClient();
    }

    @Test
    public void loginCourierShouldReturn200AndId() {
        String login = "courier" + System.currentTimeMillis();
        String password = "1234";
        Courier courier = new Courier(login, password);
        CourierCredentials courierCredentials = new CourierCredentials(login, password);

        client.create(courier)
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = client.login(courierCredentials);
    }

    @Test
    public void loginWithMissingFieldsReturns400() {
        CourierCredentials missingPwd = new CourierCredentials("anyLogin", "");
        client.loginRaw(missingPwd)
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void loginWithWrongPasswordReturnsError() {
        String login = "courier" + System.currentTimeMillis();
        String password = "1234";
        Courier courier = new Courier(login, password, "YandexTestName");

        client.create(courier)
                .statusCode(201)
                .body("ok", equalTo(true));

        CourierCredentials validCourierCredentials = new CourierCredentials(login, "1234");
        courierId = client.login(validCourierCredentials);

        CourierCredentials invalidCourierCredentials = new CourierCredentials(login, "12345");
        client.loginRaw(invalidCourierCredentials)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginNonExistentUserReturnsError() {
        CourierCredentials fake = new CourierCredentials("no_such_user", "1234");
        client.loginRaw(fake)
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
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
