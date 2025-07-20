package tests;

import data.Order;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.ClientOrder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreatingTest {

    @Parameterized.Parameter(0)
    public String firstName;
    @Parameterized.Parameter(1)
    public String lastName;
    @Parameterized.Parameter(2)
    public String address;
    @Parameterized.Parameter(3)
    public String metroStation;
    @Parameterized.Parameter(4)
    public String phone;
    @Parameterized.Parameter(5)
    public int rentTime;
    @Parameterized.Parameter(6)
    public String deliveryDate;
    @Parameterized.Parameter(7)
    public String comment;
    @Parameterized.Parameter(8)
    public List<String> color;

    @Parameterized.Parameters(name = "{index} → colors={8}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                // только чёрный
                { "Иван",   "Иванов",   "ул. Ленина, 1", "5", "+79161234567", 3, "2025-07-25", "Комментарий", Collections.singletonList("BLACK") },
                // только серый
                { "Пётр",   "Петров",   "ул. Чехова, 2", "7", "+79161234568", 1, "2025-07-26", "Комментарий", Collections.singletonList("GREY") },
                // оба цвета
                { "Сергей", "Сергеев",  "ул. Пушкина, 3","3", "+79161234569", 2, "2025-07-27", "Комментарий", Arrays.asList("BLACK","GREY") },
                // без цвета (null → используется конструктор с full‑списком, color == null)
                { "Мария",  "Смирнова", "ул. Горького, 4","1", "+79161234560", 4, "2025-07-28", "Комментарий", null }
        });
    }
    private Integer orderId;
    private ClientOrder clientOrder;
    ValidatableResponse validatableResponse;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        clientOrder = new ClientOrder();
    }

    @Test
    public void orderCanBeCreated() {
        Order order = new Order(
                firstName, lastName, address,
                metroStation, phone, rentTime,
                deliveryDate, comment, color
        );

        validatableResponse = clientOrder.create(order)
                .statusCode(SC_CREATED)
                .body("track", notNullValue());

        orderId = validatableResponse.extract().path("track");
    }

    @After
    public void tearDown() {
        if (orderId != null) {
            clientOrder.cancel(orderId)
                    .statusCode(SC_OK)
                    .body("ok", equalTo(true));
        }
    }
}
