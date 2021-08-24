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
}
