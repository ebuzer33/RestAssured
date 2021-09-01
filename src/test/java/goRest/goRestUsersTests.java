package goRest;

import goRest.Model.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class goRestUsersTests {

    @Test
    public void getUsers() {
        List<User> userList =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .log().body()
                        .extract().jsonPath().getList("data", User.class);

        for (User a : userList) {
            System.out.println("a = " +a);
        }

    }

    int userID;

    @Test
    public void createUser() {
        userID =
                given()
                        .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\"Michael\", \"gender\":\"male\", \"email\":\"" + getRandomEmail() + "\", \"status\":\"active\"}")

                        .when()
                        .post("https://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().jsonPath().getInt("data.id")
        ;

        System.out.println("userID = " + userID);
    }

    public String getRandomEmail() {
        String randomString = RandomStringUtils.randomAlphabetic(8).toLowerCase();
        return randomString + "@gmail.com";
    }

    @Test(dependsOnMethods = "createUser", priority = 1)
    public void getUserByID() {
        given()
                .pathParam("userID", userID)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users/{userID}")

                .then()
                .log().body()
                .statusCode(200)
                .body("data.id", equalTo(userID))
        ;
    }

    @Test(dependsOnMethods = "createUser", priority = 2)
    public void updateUserById() {

        String nameLastname = "Jess Pinkman";

        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + nameLastname + "\"}")
                .pathParam("userID", userID)
                //.log().uri()

                .when()
                .put("https://gorest.co.in/public/v1/users/{userID}")

                .then()
                //.log().body()
                .statusCode(200)
                .body("data.name", equalTo(nameLastname))
        ;
    }

    @Test(dependsOnMethods = "createUser", priority = 3)
    public void deleteUserById() {

        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .pathParam("userID", userID)

                .when()
                .delete("https://gorest.co.in/public/v1/users/{userID}")

                .then()
                .statusCode(204)
                .log().body()
        ;
    }

    @Test(dependsOnMethods = "deleteUserById")
    public void deleteUserByIdNegative() {
        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .pathParam("userID", userID)

                .when()
                .delete("https://gorest.co.in/public/v1/users/{userID}")

                .then()
                .log().body()
                .statusCode(404)
        ;
    }

    @Test
    public void responseSample() {
        Response returnedResult =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .extract().response();

        List<User> userList = returnedResult.jsonPath().getList("data", User.class);
        int total = returnedResult.jsonPath().getInt("meta.pagination.total");
        int Limit = returnedResult.jsonPath().getInt("meta.pagination.limit");
        User firstUser = returnedResult.jsonPath().getObject("data[0]", User.class);

        System.out.println("userList = " + userList);
        System.out.println("total = " + total);
        System.out.println("Limit = " + Limit);
        System.out.println("firstUser = " + firstUser);

    }

    @Test
    public void createUserBodyMap() {
        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", "Alex");
        newUser.put("gender", "male");
        newUser.put("email", getRandomEmail());
        newUser.put("status", "active");
        userID =
                given()
                        .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                        .contentType(ContentType.JSON)
                        .body(newUser)

                        .when()
                        .post("https://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().jsonPath().getInt("data.id")
        ;

        System.out.println("userID = " + userID);
    }

    @Test
    public void createUserBodyObject() {
        User newUser = new User();
        newUser.setName("Jamal");
        newUser.setGender("male");
        newUser.setEmail(getRandomEmail());
        newUser.setStatus("active");

        userID =
                given()
                        .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                        .contentType(ContentType.JSON)
                        .body(newUser)

                        .when()
                        .post("https://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().jsonPath().getInt("data.id")
        ;

        System.out.println("userID = " + userID);
    }

}
