package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class demoWebShopTests {

    @BeforeAll
    static void setUp(){
        Configuration.baseUrl = "https://demowebshop.tricentis.com";
        Configuration.headless = true;
        RestAssured.baseURI = "https://demowebshop.tricentis.com";
    }

    @Test
    void addToCartTest() {

        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&addtocart_72.EnteredQuantity=1";

        given()
                .contentType("application/x-www-form-urlencoded")
                .cookie("Nop.customer", "7982c077-77c0-48c7-b49a-6735f960b27b;", "ARRAffinity", "137614fac67fa378fc768d517b05a9ed454c8e40ad7b5122980c4c11c6e6ffd6;")
                .body(body)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    void addToNewCartAsAnonymTest() {

        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&addtocart_72.EnteredQuantity=1";

        given()
                .contentType("application/x-www-form-urlencoded")
                .cookie("Nop.customer", "7982c077-77c0-48c7-b49a-6735f960b27b;", "ARRAffinity", "137614fac67fa378fc768d517b05a9ed454c8e40ad7b5122980c4c11c6e6ffd6;")
                .body(body)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    void addToOldCartAsAnonymTest() {

        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&addtocart_72.EnteredQuantity=1";

        given()
                .contentType("application/x-www-form-urlencoded")
                .body(body)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("updatetopcartsectionhtml", is("(1)"));
    }

    @Test
    void addToOldCartAsAuthorizedTest() {

        String authCookie = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("Email", "kola@mail.com")
                .formParam("Password", "Rjkz2002")
                .log().all()
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract()
                .cookie("NOPCOMMERCE.AUTH");

        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&addtocart_72.EnteredQuantity=1";

        given()
                .contentType("application/x-www-form-urlencoded")
                .cookie("NOPCOMMERCE.AUTH", authCookie)
                .body(body)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    void addToOldCartAsAuthorizedSizeTest() {

        String authCookieName = "NOPCOMMERCE.AUTH";

        String authCookieValue = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("Email", "kola@mail.com")
                .formParam("Password", "Rjkz2002")
                .log().all()
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(302)
                .extract()
                .cookie("NOPCOMMERCE.AUTH");

        String body = "product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&addtocart_72.EnteredQuantity=1";

        String cartSize = given()
                .contentType("application/x-www-form-urlencoded")
                .cookie(authCookieName, authCookieValue)
                .body(body)
                .log().all()
                .when()
                .post("/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .extract()
                .path("updatetopcartsectionhtml");


        open("/Themes/DefaultClean/Content/images/free-shipping.png");

        Cookie authCookie = new Cookie(authCookieName, authCookieValue);
        WebDriverRunner.getWebDriver().manage().addCookie(authCookie);

        open("");
        $(".cart-qty").shouldHave(text(cartSize));
    }

}