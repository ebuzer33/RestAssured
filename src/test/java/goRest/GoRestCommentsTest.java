package goRest;

import goRest.Model.CommentsBody;
import goRest.Model.Comments;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GoRestCommentsTest {

    /**
      Task 1: https://gorest.co.in/public/v1/comments
              Get the data in the data returned from the API
              as a List with the help of an object.
     */

    @Test(enabled = false)
    public void getCommentsResponse() {
        Response returnedResult =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/comments")

                        .then()
                        .log().body()
                        .extract().response()
                ;

        List<Comments> commentsList = returnedResult.jsonPath().getList("data", Comments.class);

        System.out.println("commentsList = " + commentsList);

    }

    @Test(enabled = false)
    public void getComments() {
        List<Comments> commentsList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        .log().body()
                        .extract().jsonPath().getList("data");
        System.out.println("commentsList = " + commentsList);

    }

    /**
     Task 2 : Get a list of emails from all Comments and
              verify that it contains "acharya_rajinder@ankunding.biz".
     **/
    @Test(enabled = false)
    public void getEmailList() {

         Response returnedResult =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/comments")

                        .then()
                        .extract().response()
                ;

        List<String> emailList =returnedResult.jsonPath().getList("data.email");

        System.out.println("emailList = " + emailList);

        Assert.assertTrue(emailList.contains("acharya_rajinder@ankunding.biz"));

    }

    @Test(enabled = false)
    public void getEmailList2() {

        List<String> emailList=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        //.log().body()
                        .extract().path("data.email")
                ;
        System.out.println("emailList = " + emailList);
        Assert.assertTrue(emailList.contains("acharya_rajinder@ankunding.biz"));

    }

    /**
     Task 3 : https://gorest.co.in/public/v1/comments
     from api convert all returned data into a single object
     **/

    @Test()
    public void task3() {
        CommentsBody commentsBody =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/comments")

                        .then()
                        .log().body()
                        .extract().as(CommentsBody.class)
                ;

        System.out.println("commentsBody = " + commentsBody);
        System.out.println("commentsBody.getData().get(5).getEmail() = " + commentsBody.getData().get(5).getEmail());
        System.out.println("commentsBody.getMeta().getPagination().getLinks().getCurrent() = " + commentsBody.getMeta().getPagination().getLinks().getCurrent());
    }

    /**
    Task 4 : https://gorest.co.in/public/v1/comments
     Create 1 Comments to Api
    */

    @Test
    public void createComment() {

        Comments newComment = new Comments();
        newComment.setName("Jamal");
        newComment.setEmail("jamal@gmail.com");
        newComment.setBody("afjafaniwogwfiwnf");

                given()
                        .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                        .contentType(ContentType.JSON)
                        .body(newComment)

                        .when()
                        .post("https://gorest.co.in/public/v1/posts/123/comments ")

                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("data.id")
        ;

    }
/**
    Comments Update
    PUT https://gorest.co.in/public/v1/comments/1394

    {
      "body": "afjafaniwogwfiwnf"
    }
 */

@Test(enabled = false)
public void commentUpdate() {

    String body = "updated comment";

    given()
            .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
            .contentType(ContentType.JSON)
            .body("{\"body\":\"" + body + "\"}")

            .when()
            .put("https://gorest.co.in/public/v1/comments/1446")

            .then()
            .log().body()
    ;
   Assert.assertTrue(body.equalsIgnoreCase("updated comment"));
}

    int commentId;
    @Test(dependsOnMethods = "createComment",priority = 2)
    public void commentUpdate2() {

        String body = "updated comment";

        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .contentType(ContentType.JSON)
                .body("{\"body\":\"" + body + "\"}")
                .pathParam("commentId",commentId)
                .log().body()

                .when()
                .put("https://gorest.co.in/public/v1/comments/{commentId}")

                .then()
                .log().body()
                .body("data.body",equalTo(body))
        ;

    }

    @Test(dependsOnMethods ="createComment", priority = 3)
    public void deleteComment() {

        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .pathParam("commendId", commentId)

                .when()
                .delete("https://gorest.co.in/public/v1/comments/{commentId}")

                .then()
                .statusCode(204)
                .log().body()
        ;
    }

    @Test(dependsOnMethods ="createComment", priority = 4)
    public void deleteCommentNegative() {

        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .pathParam("commentId", commentId)

                .when()
                .delete("https://gorest.co.in/public-api/v1/comments/{commentId}")

                .then()
                .statusCode(404)
                .log().body()
        ;
    }

}
