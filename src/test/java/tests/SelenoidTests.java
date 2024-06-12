package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

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

}
