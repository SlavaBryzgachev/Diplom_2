package User;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.GenerateRandomUser;
import ru.yandex.praktikum.User;
import ru.yandex.praktikum.UserClient;

import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.apache.hc.core5.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class LoginUserTest {
    private UserClient userClient;
    private User user;
    private String accessToken;
    private ValidatableResponse response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        user = GenerateRandomUser.getRandomUser();
        userClient = new UserClient();
    }
    @After
    public void clearState() {
        userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
    }
    @Test
    @DisplayName("Авторизация зарегистрированного пользователя")
    @Description("Пользователь успешно авторизуется, код ответа 200 OK")
    public void LoginUserTest() {
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = userClient.loginUser(user, accessToken);
        int statusCode = response.extract().statusCode();
        boolean isUserLogin = response.extract().path("success");
        assertEquals(SC_OK, statusCode);
        assertTrue(isUserLogin);
    }

    @Test
    @DisplayName("Авторизация пользователя с пустым полем email")
    @Description("Ошибка 401")
    public void loginWithEmptyEmailTest() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        user.setEmail(null);
        ValidatableResponse validatableResponse = userClient.loginUser(user, accessToken);
        int statusCode = validatableResponse.extract().statusCode();
        boolean isUserNotLogin = validatableResponse.extract().path("success");
        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(isUserNotLogin);
    }
    @Test
    @DisplayName("Авторизация пользователя с пустым полем password")
    @Description("Ошибка 401")
    public void loginWithEmptyPasswordTest(){
        ValidatableResponse response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
        user.setPassword(null);
        ValidatableResponse validatableResponse = userClient.loginUser(user,accessToken);
        int statusCode = validatableResponse.extract().statusCode();
        boolean isUserNotLogin = validatableResponse.extract().path("success");
        assertFalse(isUserNotLogin);
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }
}
