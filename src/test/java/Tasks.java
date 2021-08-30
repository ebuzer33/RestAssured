import POJO.Location;
import POJO.Place;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class Tasks {
    /** Task 1
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     */

    @Test
    public void task1()
    {
        given()

                .when()
                .get("https://httpstat.us/203")
                .then()
                .statusCode(203)
                .contentType(ContentType.TEXT)
        ;
    }

    @Test
    public void task2()
    /** Task 2
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     * expect BODY to be equal to "203 Non-Authoritative Information"
     */

    {
       // String bodyText=
        given()

                .when()
                .get("https://httpstat.us/203")
                .then()
                .statusCode(203)
                .contentType(ContentType.TEXT)
                .body(equalTo("203 Non-Authoritative Information"))
              //  .extract().body().asString()
        ;
       // Assert.assertTrue(bodyText.equalsIgnoreCase("203 Non-Authoritative Information"));
    }

    @Test
    public void task3()
/** Task 3
 *  create a request to https://jsonplaceholder.typicode.com/todos/2
 *  expect status 200
 *  expect content type JSON
 *  expect title in response body to be "quis ut nam facilis et officia qui"
 */

    {
        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title",equalTo("quis ut nam facilis et officia qui"))
        ;
    }

    @Test
    public void task4()

    /** Task 4
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     *  expect status 200
     *  expect content type JSON
     *  expect response completed status to be false
     */
    {

        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed",equalTo(false))
        ;

    }


    @Test
    public void task5()
    /** Task 5
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * expect content type JSON
     * expect third item have:
     *      title = "fugiat veniam minus"
     *      userId = 1
     */

    {

        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos")

                .then()
                .statusCode(200)
                .log().body()
                .contentType(ContentType.JSON)
                .body("title[2]",equalTo("fugiat veniam minus"))
                .body("userId[2]",equalTo(1))

        ;

    }


    @Test
    public void task6()
    /** Task 6
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Converting Into POJO
     */

    {
User user=
        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .extract().as(User.class)
        ;

        System.out.println("user = " + user);
        System.out.println("user.getUserId() = " + user.getUserId());
        System.out.println("user.getId() = " + user.getId());
        System.out.println("user.getTitle() = " + user.getTitle());
    }


}
