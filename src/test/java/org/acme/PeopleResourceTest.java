package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class PeopleResourceTest {

    @Test
    public void testBasicQuery() {
        String query = """
                {
                    "query": "
                      query {
                        users {
                          foos {
                            foo
                          }
                        }
                      }"
                }""".trim();

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(query)
                .post("/graphql")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }


}
