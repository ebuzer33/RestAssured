import io.restassured.http.ContentType;
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
                .body("places.'state'", hasItem("California1"))
                .statusCode(200)
        ;
    }

}
