import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class zippoTest {

    @Test
    public void test()
    {
        given()
                // preparatory operations
                .when()
                // link and action actions

                .then()
                // test and extract operations
        ;
    }

    @Test
    public void statusCodeTest()
    {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                //.log().all() shows all response
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

    private ResponseSpecification responseSpecification;
    private RequestSpecification requestSpecification;
    @BeforeClass
    public void setup()
    {
        baseURI="http://api.zippopotam.us";
        responseSpecification= new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();

        requestSpecification=new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();

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

    @Test
    public void responseSpecificationTest()
    {
        given()
                .log().uri()
                .when()
                .get("/us/90210")

                .then()
                .body("places", hasSize(1))
                .spec(responseSpecification)
        ;
    }

    @Test
    public void requestSpecificationTest()
    {
        given()
                .spec(requestSpecification)
                .when()
                .get("/us/90210")

                .then()
                .body("places", hasSize(1))
                .spec(responseSpecification)
        ;
    }

    @Test
    public void extractingJsonPath()
    {
        String place_name= given()
                .spec(requestSpecification)
                .when()
                .get("/us/90210")
                .then()
                .spec(responseSpecification)
                .extract().path("places[0].'place name'")
        ;
        System.out.println("place name = "+ place_name);
    }

    @Test
    public void extractingJsonPath2() {

        int limit=
        given()
                .param("page", 1)
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .extract().path("meta.pagination.limit")
        ;

        System.out.println("limit = "+ limit);

    }

    @Test
    public void extractingJsonPathIntList() {

        List<Integer> ids=
                given()
                        .param("page", 1)
                        .log().uri()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .extract().path("data.id")
                ;

        System.out.println("ids = "+ids);

    }

    @Test
    public void extractingJsonPathStringList() {

        List<String> emails=
                given()
                        .param("page", 1)
                        .log().uri()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .extract().path("data.email")
                ;

        System.out.println("emails= "+emails);

    }

    @Test
    public void extractingJsonPathStringList2()
    {
        List<String> villages= given()
                .spec(requestSpecification)
                .when()
                .get("/tr/01000")
                .then()
                .spec(responseSpecification)
                .extract().path("places.'place name'")
                ;
        System.out.println("villages = "+ villages);
        Assert.assertTrue(villages.contains("Büyükdikili Köyü"));
    }
}
