package User;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.User;
import ru.yandex.praktikum.UserClient;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class UserCreateTest {
    ValidatableResponse response;
    private User user;
    private UserClient userClient;
    private String accessToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        user = User.getRandomUser();
        userClient = new UserClient();
    }
    @Test
    @DisplayName("Регистрация пользователя")
    @Description("Пользователь успешно регистрируется, код ответа 200 OK")
    public void createUserTest() {
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        boolean isUserCreate = response.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertTrue(isUserCreate);
        userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
    }
    @Test
    @DisplayName("Регистрация уже зарегистрированного пользователя")
    @Description("Ошибка 403")
    public void createAlredyRegisterUserTest() {
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();
        boolean isCreate = response.extract().path("success");
        assertFalse(isCreate);
        assertEquals(SC_FORBIDDEN, statusCode);
        userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
    }
    @Test
    @DisplayName("Регистрация пользователя без обязательных полей")
    @Description("Ошибка 403")
    public void creatingUserWithoutRequiredFieldsTest() {
        user.setPassword(null);
        response = userClient.createUser(user);
        int statusCode = response.extract().statusCode();
        boolean isUserNotCreate = response.extract().path("success");
        assertFalse(isUserNotCreate);
        assertEquals(SC_FORBIDDEN, statusCode);
        userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
    }
}
