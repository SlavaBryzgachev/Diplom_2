package ru.yandex.praktikum;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.config.Config;
import ru.yandex.praktikum.endPoints.EndPoints;

import static io.restassured.RestAssured.given;
public class UserClient extends Config {
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .log().all()
                .post(EndPoints.USER_PATH + "register")
                .then()
                .log().all();
    }
    public ValidatableResponse loginUser(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(user)
                .log().all()
                .post(EndPoints.USER_PATH + "login")
                .then()
                .log().all();
    }
    public ValidatableResponse getAllIngredients() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(EndPoints.INGREDIENTS_PATH)
                .then()
                .log().all();
    }

    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .log().all()
                .delete(EndPoints.USER_PATH + "user")
                .then()
                .log().all();
    }
    public ValidatableResponse updateUserWithAuth(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(user)
                .log().all()
                .patch(EndPoints.USER_PATH + "user")
                .then()
                .log().all();
    }
    public ValidatableResponse updateUserWithoutAuth(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .log().all()
                .patch(EndPoints.USER_PATH + "user")
                .then()
                .log().all();
    }
}



