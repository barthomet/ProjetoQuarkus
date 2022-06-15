package io.github.barthomet.quarkus.rest;
import io.github.barthomet.quarkus.rest.dto.CreateUserRequest;
import io.github.barthomet.quarkus.rest.dto.ResponseError;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {

    @TestHTTPResource("/users")
    URL apiURL;

    @Test
    @DisplayName("Deve criar um usuario com sucesso")
    @Order(1)
    public void createUserTest(){
        var user = new CreateUserRequest();
        user.setNome("Fulano");
        user.setEmail("teste@gmail.com");

        var response =
        given()
            .contentType(ContentType.JSON)
            .body(user)
        .when()
            .post(apiURL)
        .then()
            .extract().response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("Deve retornar erro quando o Json não for válido")
    @Order(2)
    public void CreateUserValidationErrorTest(){
        var user = new CreateUserRequest();
        user.setNome(null);
        user.setEmail(null);

        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(user)
                .when()
                        .post(apiURL)
                .then()
                        .extract().response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
        assertEquals("validation error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));
    }
}