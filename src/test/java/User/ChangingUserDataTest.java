package User;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.User;
import ru.yandex.praktikum.UserClient;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class ChangingUserDataTest {
    private UserClient userClient;
    private User user;
    private String accessToken;
    private ValidatableResponse response;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
        response = userClient.createUser(user);
        accessToken = response.extract().path("accessToken");
    }
    @After
    public void clearState() {
        userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
    }
        @Test
        @DisplayName("Изменение данных пользователя")
        @Description("Данные успешно изменены код ответа 200")
        public void updateDataUserTest(){
                userClient.loginUser(user, accessToken);
                response =  userClient.updateUserWithAuth(User.getRandomUser(), accessToken);
                int statusCode = response.extract().statusCode();
                boolean isChange = response.extract().path("success");
                assertEquals(SC_OK, statusCode);
                assertTrue(isChange);
                userClient.deleteUser(StringUtils.substringAfter(accessToken, " "));
    }
    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Ошибка 401")
    public void updateDataUserWithoutAuthTest(){
        response = userClient.updateUserWithoutAuth(User.getRandomUser());
        int statusCode = response.extract().statusCode();
        boolean isDataNotChange = response.extract().path("success");
        assertEquals(SC_UNAUTHORIZED, statusCode);
        assertFalse(isDataNotChange);
    }
}

