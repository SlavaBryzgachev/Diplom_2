package ru.yandex.praktikum;

import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.Order;
import ru.yandex.praktikum.User;
import ru.yandex.praktikum.config.Config;
import ru.yandex.praktikum.endPoints.endPoints;

import static io.restassured.RestAssured.given;
public class UserClient extends Config {
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .log().all()
                .post(endPoints.USER_PATH + "register")
                .then()
                .log().all();
    }
    public ValidatableResponse loginUser(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(user)
                .log().all()
                .post(endPoints.USER_PATH + "login")
                .then()
                .log().all();
    }
    public ValidatableResponse getAllIngredients() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(endPoints.INGREDIENTS_PATH)
                .then()
                .log().all();
    }
    public ValidatableResponse orderCreate(Order order, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .log().all()
                .post(endPoints.ORDER_PATH)
                .then()
                .log().all();
    }
    public ValidatableResponse createOrderWithoutAuthorization(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .log().all()
                .post(endPoints.ORDER_PATH)
                .then()
                .log().all();
    }
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .log().all()
                .delete(endPoints.USER_PATH + "user")
                .then()
                .log().all();
    }
    public ValidatableResponse updateUserWithAuth(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(user)
                .log().all()
                .patch(endPoints.USER_PATH + "user")
                .then()
                .log().all();
    }
    public ValidatableResponse updateUserWithoutAuth(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .log().all()
                .patch(endPoints.USER_PATH + "user")
                .then()
                .log().all();
    }
    public ValidatableResponse getOrdersByAuth(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .log().all()
                .get(endPoints.ORDER_PATH)
                .then()
                .log().all();
    }
    public ValidatableResponse getOrdersWithoutAuth() {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .get(endPoints.ORDER_PATH)
                .then()
                .log().all();
    }
}



