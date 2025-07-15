import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class OrderCreatingTest {
    private Integer orderId;
    private ClientOrder clientOrder;
    ValidatableResponse validatableResponse;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        clientOrder = new ClientOrder();
    }

    @Test
    public void orderWithBlackColorCanBeCreated() {
        Order order = new Order(
                "Иван", "Иванов", "ул. Ленина, 1",
                "5", "+79161234567", 3,
                "2025-07-25", "Комментарий",
                Collections.singletonList("BLACK")
        );

        validatableResponse = clientOrder.create(order)
                .statusCode(201)
                .body("track", notNullValue());

        orderId = validatableResponse.extract().path("track");

    }

    @Test
    public void orderWithGreyColorCanBeCreated() {
        Order order = new Order(
                "Пётр", "Петров", "ул. Чехова, 2",
                "7", "+79161234568", 1,
                "2025-07-26", "Комментарий",
                Collections.singletonList("GREY")
        );

        validatableResponse = clientOrder.create(order)
                .statusCode(201)
                .body("track", notNullValue());

        orderId = validatableResponse.extract().path("track");
    }

    @Test
    public void orderWithBothColorsCanBeCreated() {
        Order order = new Order(
                "Сергей", "Сергеев", "ул. Пушкина, 3",
                "3", "+79161234569", 2,
                "2025-07-27", "Комментарий",
                Arrays.asList("BLACK", "GREY")
        );

        validatableResponse = clientOrder.create(order)
                .statusCode(201)
                .body("track", notNullValue());

        orderId = validatableResponse.extract().path("track");
    }

    @Test
    public void orderWithoutColorCanBeCreated() {
        Order order = new Order(
                "Мария", "Смирнова", "ул. Горького, 4",
                "1", "+79161234560", 4,
                "2025-07-28", "Комментарий"
        );

        validatableResponse = clientOrder.create(order)
                .statusCode(201)
                .body("track", notNullValue());

        orderId = validatableResponse.extract().path("track");
    }

    @After
    public void tearDown() {
        if (orderId != null) {
            clientOrder.cancel(orderId)
                    .statusCode(200)
                    .body("ok", equalTo(true));
        }
    }
}
