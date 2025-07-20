package tests;

import base.BaseTest;
import data.Courier;
import data.CourierCredentials;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CourierLoginTest extends BaseTest {

    private String login;
    private String password;
    private CourierCredentials courierCredentials;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        login = "courier" + System.currentTimeMillis();
        password = "1234";
        Courier courier = new Courier(login, password);

        courierClient.create(courier)
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    public void loginCourierShouldReturn200AndId() {
        courierCredentials = new CourierCredentials(login, password);
        courierId = courierClient.login(courierCredentials);
    }

    @Test
    public void loginWithMissingFieldsReturns400() {
        courierCredentials = new CourierCredentials(login, "");
        courierClient.loginRaw(courierCredentials)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    public void loginWithMissingLoginReturns400() {
        courierCredentials = new CourierCredentials("", password);
        courierClient.loginRaw(courierCredentials)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void loginWithWrongPasswordReturnsError() {

        courierCredentials = new CourierCredentials(login, "12345");
        courierClient.loginRaw(courierCredentials)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void loginNonExistentUserReturnsError() {
        courierCredentials = new CourierCredentials("no_such_user", "1234");
        courierClient.loginRaw(courierCredentials)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
