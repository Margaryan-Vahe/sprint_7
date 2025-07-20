package base;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import steps.CourierClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public class BaseTest {
    protected CourierClient courierClient;
    protected Integer courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courierClient = new CourierClient();
    }

    @After
    public void tearDownBase() {
        if (courierId != null) {
            courierClient.delete(courierId)
                    .statusCode(SC_OK)
                    .body("ok", equalTo(true));
        }
    }
}
