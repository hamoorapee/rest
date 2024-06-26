package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTests {

    /*
    1. Make request to https://selenoid.autotests.cloud/status
    2. Get response {"total":20,"used":0,"queued":0,"pending":0,"browsers":{"chrome":{"100.0":{},"120.0":{},"121.0":{},
    "122.0":{},"99.0":{}},"firefox":{"122.0":{},"123.0":{}},"opera":{"106.0":{},"107.0":{}}}}
    3. Check total is 20
     */
    @Test
    void checkTotal() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkTotalWithGiven() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkTotalWithSomeLogs() {
        given()
                .log().uri()
                .log().body()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(20));
    }

    @Test
    void checkChromeVersion() {
        given()
                .log().all()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().all()
                .statusCode(200)
                .body("browsers.chrome", hasKey("100.0"));
    }

    @Test
    void checkTotalBadPractice() {
        String expectedResponseString = "{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0," +
                "\"browsers\":{\"chrome\":{\"100.0\":{},\"120.0\":{},\"121.0\":{},\"122.0\":{}," +
                "\"99.0\":{}},\"firefox\":{\"122.0\":{},\"123.0\":{}},\"opera\":{\"106.0\":{},\"107.0\":{}}}}\n";

        Response actualResponse = given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().response();

        System.out.println(actualResponse);
        String actualResponseString = actualResponse.asString();
        System.out.println(actualResponseString);

        assertEquals(expectedResponseString, actualResponseString);
    }

    @Test
    void checkTotalGoodPractice() {
        int expectedTotal = 20;
        int actualTotal = given()
                .log().uri()
                .log().body()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract()
                .path("total");

        assertEquals(expectedTotal, actualTotal);
    }

    /*
        1. Make request to https://selenoid.autotests.cloud/wd/hub
        2. Get response {"total":20,"used":0,"queued":0,"pending":0,"browsers":{"chrome":{"100.0":{},"120.0":{},"121.0":{},
        "122.0":{},"99.0":{}},"firefox":{"122.0":{},"123.0":{}},"opera":{"106.0":{},"107.0":{}}}}
        3. Check total is 20
         */
    @Test
    void check401WDStatus() {
        given()
                .log().uri()
                .log().body()
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(401)
                .body("value.ready", is(true));

    }

    @Test
    void checkWDHubStatusWithAuthInUrl() {
        given()
                .log().uri()
                .log().body()
                .when()
                .get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("value.ready", is(true));
    }

    @Test
    void checkWDHubStatusWithAuth() {
        given()
                .auth().basic("user1", "1234")
                .log().uri()
                .log().body()
                .when()
                .get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("value.ready", is(true));
    }
}
