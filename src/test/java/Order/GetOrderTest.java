package Order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.GenerateRandomUser;
import ru.yandex.praktikum.Order;
import ru.yandex.praktikum.User;
import ru.yandex.praktikum.UserClient;

import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.apache.hc.core5.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class GetOrderTest {
    private UserClient userClient;
    private User user;
    private String accessToken;
    private ValidatableResponse response;
    private Order order;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        user = GenerateRandomUser.getRandomUser();
        userClient = new UserClient();
        order = new Order();
    }
    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Приходит список заказов, код ответа 200")
    public void GetOrdersWithAuthTest() {
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = userClient.loginUser(user, accessToken);
        response = userClient.orderCreate(order, accessToken);
        response = userClient.getOrdersByAuth(accessToken);
        int statusCode = response.extract().statusCode();
        boolean isGet = response.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertTrue(isGet);
        response = userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
    }
    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Description("Код ошибки 401")
    public void GetOrdersWithoutAuthTest(){
        response = userClient.createOrderWithoutAuthorization(order);
        response = userClient.getOrdersWithoutAuth();
        int statusCode = response.extract().statusCode();
        boolean isGet = response.extract().path("success");
        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(isGet);
    }
}