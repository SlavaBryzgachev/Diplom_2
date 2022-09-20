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
import java.util.List;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class OrderCreateTest {
    private UserClient userClient;
    private OrderClient orderClient;
    private ValidatableResponse response;
    private Order order;
    private User user;
    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        order = new Order();
        user = User.getRandomUser();
    }
    @Test
    @DisplayName("Создание заказа после авторизации пользователя")
    @Description("Заказ создан, код ответа 200")
    public void orderCreateWithAuthTest(){
        fillListIngredients();
        response = userClient.createUser(user);
        String accessToken = response.extract().path("accessToken");
        userClient.loginUser(user, accessToken);
        response = orderClient.orderCreate(order,accessToken);
        int statusCode = response.extract().statusCode();
        boolean isCreate = response.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertTrue(isCreate);
        userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
    }
    @Test
    @DisplayName("Создание заказа без авторизации пользователя")
    @Description("Заказ создан, код ответа 200")
    public void orderCreateWithoutAuthorization(){
        fillListIngredients();
        response = orderClient.createOrderWithoutAuthorization(order);
        int statusCode = response.extract().statusCode();
        boolean isCreate = response.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertTrue(isCreate);
    }
    @Test
    @DisplayName("Создание заказа без авторизации пользователя и без ингредиентов")
    @Description("Ошибка 400")
    public void orderCreateWithoutAuthorizationAndIngredients(){
        response = orderClient.createOrderWithoutAuthorization(order);
        int statusCode = response.extract().statusCode();
        boolean isCreate = response.extract().path("success");
        assertEquals(SC_BAD_REQUEST, statusCode);
        assertFalse(isCreate);
    }

    @Test
    @DisplayName("Создние заказа без авторизации пользователя и с неверным хешом ингредиентов")
    @Description("Ошибка 500")
    public void orderCreateWithoutAuthorizationAndWrongHashIngredient(){
        response = userClient.getAllIngredients();
        List<String> list = response.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(5).replaceAll("a", "l"));
        ingredients.add(list.get(0));
        response = orderClient.createOrderWithoutAuthorization(order);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }
    private void fillListIngredients() {
        response = userClient.getAllIngredients();
        List<String> list = response.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(5));
        ingredients.add(list.get(0));
    }
}
