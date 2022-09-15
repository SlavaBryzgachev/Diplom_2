package Order;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.Order;
import ru.yandex.praktikum.OrderClient;
import ru.yandex.praktikum.User;
import ru.yandex.praktikum.UserClient;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class GetOrderTest {
    private UserClient userClient;
    private OrderClient orderClient;
    private User user;
    private String accessToken;
    private ValidatableResponse response;
    private Order order;

    @Before
    public void setUp() {
        user = user.getRandomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
        order = new Order();
    }
    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Приходит список заказов, код ответа 200")
    public void getOrdersWithAuthTest() {
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        userClient.loginUser(user, accessToken);
        orderClient.orderCreate(order, accessToken);
        response = orderClient.getOrdersByAuth(accessToken);
        int statusCode = response.extract().statusCode();
        boolean isGet = response.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertTrue(isGet);
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
    }
    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Description("Код ошибки 401")
    public void getOrdersWithoutAuthTest(){
        orderClient.createOrderWithoutAuthorization(order);
        response = orderClient.getOrdersWithoutAuth();
        int statusCode = response.extract().statusCode();
        boolean isGet = response.extract().path("success");
        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(isGet);
    }
}