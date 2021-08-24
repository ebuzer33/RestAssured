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
}
