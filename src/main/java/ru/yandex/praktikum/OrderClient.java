package ru.yandex.praktikum;

import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.config.Config;
import ru.yandex.praktikum.endPoints.endPoints;

import static io.restassured.RestAssured.given;

public class OrderClient extends Config {
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
