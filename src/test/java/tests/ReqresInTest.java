package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresInTest {
    /*
       1. Make POST- request to https://reqres.in/api/login
       { "name": "morpheus", "job": "leader" }

       2. { "name": "morpheus", "job": "leader", "id": "128", "createdAt": "2024-06-14T06:56:07.993Z" }
       3. Check createdAt = "2024-06-14T06:56:07.993Z"
    */
    @Test
    void loginTest() {
        String body = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }"; //todo bad practice
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(body)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void negativeLoginTest() {
        String body = "{ \"email\": \"peter@klaven\" }"; //todo bad practice
        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(body)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void ListUsersTest() {
        String body = "{ \"email\": \"peter@klaven\" }"; //todo bad practice
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("page", is(2));
    }

    @Test
    void SingleUserTest() {
        given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"));
    }

    @Test
    void SingleUserNotFound() {
        given()
                .when()
                .get("https://reqres.in/api/users/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @Test
    void createUserTest() {
        String body = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
        given()
                .contentType(JSON)
                .body(body)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().body()
                .log().status()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void updateUser() {
        String body = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";
        given()
                .contentType(JSON)
                .body(body)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .log().body()
                .log().status()
                .body("name", is("morpheus"))
                .body("job", is("zion resident"));
    }
    @Test
    void patchUserTest(){
        String body = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";
        given()
                .contentType(JSON)
                .body(body)
                .log().uri()
                .log().body()
                .when()
                .patch("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .log().body()
                .log().status()
                .body("job", is("zion resident"));
    }
    @Test
    void deleteUserTest(){
        given()
                .log().uri()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .statusCode(204);
    }
}
