package POJO;

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

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class pojoTest {

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
    public void extractingJsonPojo()
    {
        Location location=
         given()

                .when()
                .get("/us/90210")

                .then()
                .extract().as(Location.class)
                ;

        System.out.println("location = " + location);
        System.out.println("location.getCountry = " + location.getCountry());
        System.out.println("location.getPlaces() = " + location.getPlaces());
        System.out.println("location.getPlaces().get(0).getPlacename() = " + location.getPlaces().get(0).getPlacename());

    }
}
