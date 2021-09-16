package goRest;

import goRest.Model.Todos;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GoRestTodosTests {


    @BeforeClass
    public void starUp() {
        baseURI = "https://gorest.co.in/public/v1";
    }


    /**
     Task 2: https://gorest.co.in/public/v1/todos
     Find the id of the last todon with the largest id in the
     data returned from the above api address.
    */
    @Test(enabled = false)
    public void findBigIdOfTodos()  {
        ArrayList<Integer> user_idList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/todos")
                        .then()
                        .log().body()
                        .extract().jsonPath().get("data.id");

        Collections.sort(user_idList);
        System.out.println("user_idList = " + user_idList);
        System.out.println(user_idList.get(user_idList.size() - 1));

    }

    // another solution
    @Test(enabled = false)
    public void findBigIdOfTodos2()  {
        List<Todos> todosList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/todos")
                        .then()
                        //.log().body()
                        .extract().jsonPath().getList("data", Todos.class);

        System.out.println("todosList = " + todosList);
        int maxId = 0;
        for (int i = 0; i < todosList.size(); i++) {

            if (todosList.get(i).getId() > maxId) {
                maxId = todosList.get(i).getId();
            }
        }
        System.out.println("maxId = " + maxId);
    }

    @Test(enabled = false)
    public void task3() {
        int maxId = 0;
        int totalPage = 2;
        for (int page = 1; page < totalPage; page++) {

            Response response =
                    given()
                            .param("page", 1)
                            .when()
                            .get("/todos")
                            .then()
                            //.log().body()
                            .extract().response();

            totalPage = response.jsonPath().getInt("meta.pagination.pages");
            List<Todos> pageList = response.jsonPath().getList("data", Todos.class);

            for (int i = 0; i < pageList.size(); i++) {

                if (maxId < pageList.get(i).getId())
                    maxId = pageList.get(i).getId();
            }
        }
        System.out.println("maxId = " + maxId);
    }

    @Test(enabled = false)
    public void task4() {
        int totalPage = 0;
        int page = 1;
        int maxId = 0;
        do {

            Response response =
                    given()
                            .param("page", page)
                            .when()
                            .get("/todos")
                            .then()
                            .log().body()
                            .extract().response();

            if (page == 1)
                totalPage = response.jsonPath().getInt("meta.pagination.pages");

            List<Todos> pageList = response.jsonPath().getList("data", Todos.class);

            for (int i = 0; i < pageList.size(); i++) {

                if (maxId < pageList.get(i).getId())
                    maxId = pageList.get(i).getId();

            }
            page++;

        }
        while (page <= totalPage);
        System.out.println("maxId = " + maxId);

    }

    @Test(enabled = false)
    public void task5() {

        List<Integer> idList = new ArrayList<>();
        int totalPage = 0;
        int page = 1;
        do {

            Response response =
                    given()
                            .param("page", page)
                            .when()
                            .get("/todos")
                            .then()
                            //.log().body()
                            .extract().response();

            idList.addAll(response.jsonPath().getList("data.id"));

            if (page == 1)
                totalPage = response.jsonPath().getInt("meta.pagination.pages");

            page++;

        } while (page <= totalPage);
        System.out.println("idList = " + idList);
    }

    int todosId;

    @Test
    public void createTodos() {

        String todosTitle = "hello restAssured";
        String todosDate = "2021-09-25T00:00:00.000+05:32";
        String todosStatus = "completed";


        Todos todos = new Todos();
        todos.setTitle(todosTitle);
        todos.setDue_on(todosDate);
        todos.setStatus(todosStatus);
        todos.setUser_id(7);

        todosId =
                given()
                        .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                        .contentType(ContentType.JSON)
                        .body(todos)

                        .when()
                        .post("/todos ")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getInt("data.id")

        ;

        System.out.println("todosId = " + todosId);
    }

    @Test(dependsOnMethods = "createTodos", priority = 1)
    public void getTodosID() {
        given()
                .pathParam("todosId", todosId)

                .when()
                .get("/todos/{todosId}")

                .then()
                //.log().body()
                .statusCode(200)
                .body("data.id", equalTo(todosId))
        ;

    }

    @Test(dependsOnMethods = "createTodos", priority = 2)
    public void todosUpdate() {

        String status = "pending";

        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .contentType(ContentType.JSON)
                .body("{\"status\":\"" + status + "\"}")
                .pathParam("todosId", todosId)
                .log().body()

                .when()
                .put("todos/{todosId}")

                .then()
                .log().body()
                .statusCode(200)
                .body("data.status", equalTo(status))
        ;
    }

    @Test(dependsOnMethods = "createTodos", priority = 3)
    public void deleteTodos() {

        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .pathParam("todosId", todosId)

                .when()
                .delete("todos/{todosId}")

                .then()
                .statusCode(204)
                .log().body()
        ;
    }

    @Test(dependsOnMethods = "deleteTodos")
    public void deleteTodosNegative() {

        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .pathParam("todosId", todosId)

                .when()
                .delete("todos/{todosId}")

                .then()
                .statusCode(404)
        ;

    }
}
