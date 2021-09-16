package mersys_Demo;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import mersys_Demo.model.Country;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CountryTest {

    Cookies cookies;
  @BeforeClass
  public void loginMersys()
  {
      baseURI = "https://demo.mersys.io";

      Map<String,String> credential =new HashMap<>();

      credential.put("username","richfield.edu");
      credential.put("password", "Richfield2020!");
      credential.put("rememberMe","true");


     cookies= given()
              .body(credential)
              .contentType(ContentType.JSON)

              .when()
              .post("/auth/login")
              .then()
              .statusCode(200)
              .log().body()
      .extract().response().getDetailedCookies()
      ;
      System.out.println("cookies = " + cookies);

  }

    String randomGenName=RandomStringUtils.randomAlphabetic(6);
    String randomGenCode=RandomStringUtils.randomAlphabetic(3);


    String countryId;
    @Test
    public void createCountry()
    {

        Country country=new Country();
        country.setName(randomGenName);
        country.setCode(randomGenCode);

        countryId= given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)

                .when()
                .post("/school-service/api/countries")

                .then()
                .statusCode(201)
                .body("name",equalTo(randomGenName))
                .log().body()
                .extract().jsonPath().getString("id")
        ;
    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative()
    {
        Country country=new Country();
        country.setName(randomGenName);
        country.setCode(randomGenCode);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)

                .when()
                .post("/school-service/api/countries/")

                .then()
                .statusCode(400)
                .body("message",equalTo("The Country with Name \""+randomGenName+"\" already exists."))
                .log().body()
        ;
    }

    @Test(dependsOnMethods = "createCountry", priority = 1)
    public void updateCountry()
    {
        String updateName= RandomStringUtils.randomAlphabetic(9);

        Country country=new Country();
        country.setId(countryId);
        country.setName(updateName);
        country.setCode(randomGenCode);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)

                .when()
                .put("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(updateName))
        ;
    }

    @Test(dependsOnMethods ="createCountry", priority = 2)
    public void deleteCountry() {

        given()
                .cookies(cookies)
                .pathParam("countryId",countryId)

                .when()
                .delete("/school-service/api/countries/{countryId}")

                .then()
                .statusCode(201)
                .log().body()
        ;
    }

    @Test(dependsOnMethods = "deleteCountry")
    public void DeleteCountryNegative() {

        given()
                .cookies(cookies)
                .pathParam("countryId", countryId)

                .when()
                .delete("/school-service/api/countries/{countryId}")

                .then()
                .statusCode(404);
    }

}
