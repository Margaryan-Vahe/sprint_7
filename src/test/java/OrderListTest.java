
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class OrderListTest {
    private ClientOrder clientOrder;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        clientOrder = new ClientOrder();
    }

    @Test
    public void getAllOrdersReturnsArray() {
        clientOrder.getAllOrders()
                .statusCode(200)
                .body("orders", notNullValue());
    }

    @Test
    public void getAllOrdersHasNoDuplicates() {
        ExtractableResponse<Response> resp = clientOrder.getAllOrders()
                .statusCode(200)
                .extract();

        List<Integer> ids = resp.path("orders.id");
        Set<Integer> unique = new HashSet<>(ids);

        assertEquals("Найдены дубли в списке заказов", unique.size(), ids.size());
    }
}
