package goRest;

import goRest.Model.Comments;
import goRest.Model.CommentsBody;
import goRest.Model.Posts;
import goRest.Model.PostsBody;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GoRestPostsTest {
    /**
     * Task 1 : https://gorest.co.in/public/v1/posts
     * API sinden dönen data bilgisini bir class yardımıyla
     * List ini alınız.
     */

    @BeforeClass
    public void startUp()
    {
        baseURI="https://gorest.co.in/public/v1/";
    }

    @Test
    public void getAllPosts() {
        List<Posts> postsList =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/posts")
                        .then()
                        .log().body()
                        .extract().jsonPath().getList("data", Posts.class)
                ;

        for (Posts p:postsList) {
            System.out.println("p= " + p);
        }

    }
    /**
    Task 2 : https://gorest.co.in/public/v1/posts
             API sinden sadece 1 kişiye ait postları listeletiniz.
             https://gorest.co.in/public/v1/users/87/posts
     */
    @Test
    public void getUsersPosts() {
        List<Posts> postsList87 =
                given()
                        .when()
                        .get("/users/87/posts")
                        .then()
                        //.log().body()
                        .extract().jsonPath().getList("data", Posts.class)
                ;

        for (Posts p:postsList87) {
            System.out.println("p= " + p);
        }
    }
    /**
    Task 3 : https://gorest.co.in/public/v1/posts
             API sinden dönen bütün bilgileri tek bir nesneye atınız
     */

    @Test()
    public void getAllPostsAsObject() {
        PostsBody postsBody =
                given()

                        .when()
                        .get("/posts")

                        .then()
                        .log().body()
                        .extract().as(PostsBody.class)
                ;

        System.out.println("postsBody = " + postsBody);
        System.out.println("postsBody.getData().get(3).get() = " + postsBody.getData().get(3).getTitle());
        System.out.println("postsBody.getMeta().getPagination().getLinks().getCurrent() = " + postsBody.getMeta().getPagination().getLinks().getCurrent());
    }

    int postId;

    @Test(enabled = false)
    public void createPosts() {

        String postTitle ="hello restAssured";
        String postBody ="good by restAssured";

        Posts newPosts = new Posts();
        newPosts.setTitle(postTitle);
        newPosts.setBody(postBody);

        postId=
        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .contentType(ContentType.JSON)
                .body(newPosts)

                .when()
                .post("/users/87/posts ")

                .then()
                .log().body()
                .body("data.title",equalTo(postTitle))
                .body("data.body",equalTo(postBody))
                .extract().jsonPath().getInt("data.id")
        ;

        System.out.println("postId = " + postId);
    }

    @Test
    public void createPosts2() {

        String postTitle = "hello restAssured";
        String postBody = "good by restAssured";

        Posts newPosts = new Posts();
        newPosts.setTitle(postTitle);
        newPosts.setBody(postBody);
        newPosts.setUser_id(87);

        postId =
                given()
                        .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                        .contentType(ContentType.JSON)
                        .body(newPosts)

                        .when()
                        .post("/posts ")

                        .then()
                        .log().body()
                        .body("data.title", equalTo(postTitle))
                        .body("data.body", equalTo(postBody))
                        .extract().jsonPath().getInt("data.id")
        ;

        System.out.println("postId = " + postId);
    }
    // Task 5 : Create edilen Post ı get yaparak id sini kontrol ediniz.

        @Test(dependsOnMethods = "createPosts2", priority = 1)
        public void getPostID() {
            given()
                    .pathParam("postId", postId)

                    .when()
                    .get("/posts/{postId}")

                    .then()
                    //.log().body()
                    .statusCode(200)
                    .body("data.id", equalTo(postId))
            ;

    }
    // Task 6 : Create edilen Post un body sini güncelleyerek, bilgiyi kontrol ediniz.

    @Test(dependsOnMethods = "createPosts2",priority = 2)
    public void postsUpdate() {

        String body = "updated posts";

        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .contentType(ContentType.JSON)
                .body("{\"body\":\"" + body + "\"}")
                .pathParam("postId",postId)
                .log().body()

                .when()
                .put("posts/{postId}")

                .then()
                .log().body()
                .body("data.body",equalTo(body))
        ;

    }

    @Test(dependsOnMethods ="createPosts2", priority = 3)
    public void deletePosts() {

        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .pathParam("postId", postId)

                .when()
                .delete("posts/{postId}")

                .then()
                .statusCode(204)
                .log().body()
        ;
    }

    @Test(dependsOnMethods ="deletePosts")
    public void deletePostsNegative() {

        given()
                .header("Authorization", "Bearer 36e95c8fd3e7eb89a65bad6edf4c0a62ddb758f9ed1e15bb98421fb0f1f3e57f")
                .pathParam("postId", postId)

                .when()
                .delete("https://gorest.co.in/public/v1/posts/{postId}")

                .then()
                .statusCode(404)
        ;

    }
}
