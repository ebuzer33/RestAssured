import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class zippoTest {

    @Test
    public void test()
    {
        given()
                // hazirlik islemleri
                .when()
                // link ve aksiyon islemleri

                .then()
                // test ve extract islemleri
        ;
    }

    @Test
    public void statusCodeTest()
    {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                //.log().all() tum respons u gosterir
                .log().body()
                .statusCode(200)
        ;
    }

    @Test
    public void contentTypeTest()
    {
        given()
                .when()
                .get()
                .then()
                .log().body()
                .contentType(ContentType.JSON)

                ;
    }

    @Test
    public void logTest()
    {
        given()
                .log().all()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()

                ;
    }

    @Test
    public void checkStateInResponseBody()
    {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("country",equalTo("United States"))
                .statusCode(200)
                .body("places[0].state",equalTo("California"))
        ;
    }

    @Test
    public void bodyJsonPathTest()
    {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].state",equalTo("California"))
                .statusCode(200)
        ;
    }

    @Test
    public void bodyJsonPathTestHasItem()
    {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places.state", hasItem("California"))
                .statusCode(200)
        ;
    }

    @Test
    public void bodyJsonPathTest2()
    {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                .statusCode(200)
        ;
    }

    @Test
    public void bodyArrayHasSizeTest()
    {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places", hasSize(1))

                .statusCode(200)
        ;
    }
    @Test
    public void combiningTest()
    {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places", hasSize(1))
                .body("places.state", hasItem("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
                .statusCode(200)
        ;
    }

    @Test
    public void pathParamTest()
    {
        given()
                .pathParam("country","us")
                .pathParam("zipcode","90210")
                .log().uri()

                .when()
                .get("http://api.zippopotam.us/{country}/{zipcode}")

                .then()
                .log().body()
                .body("places", hasSize(1))
                .statusCode(200)
        ;
    }

    @Test
    public void pathParamTest2() {
        String country = "us";

        for (int i = 90210; i < 90214; i++) {

            given()
                    .pathParam("country", country)
                    .pathParam("zipcode", i)
                    .log().uri()

                    .when()
                    .get("http://api.zippopotam.us/{country}/{zipcode}")

                    .then()
                    .log().body()
                    .body("places", hasSize(1))
                    .statusCode(200)
            ;
        }
    }

    @Test
    public void queryParamTest2() {
        // https://gorest.co.in/public/v1/users?page=1

            given()
                    .param("page", 1)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(1))
                    .statusCode(200)
            ;

    }

    @Test
    public void queryParamTest3() {
        // https://gorest.co.in/public/v1/users?page=1
        for (int i = 1; i < 10; i++) {
            given()
                    .param("page", i)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(i))
                    .statusCode(200)
            ;
        }
    }

    @BeforeClass
    public void setup()
    {
        baseURI="http://api.zippopotam.us";
    }

    @Test
    public void baseUriTest()
    {
        given()
                .log().uri()
                .when()
                .get("/us/90210")

                .then()
                .log().body()
                .body("places", hasSize(1))
                .statusCode(200)
        ;
    }
}
