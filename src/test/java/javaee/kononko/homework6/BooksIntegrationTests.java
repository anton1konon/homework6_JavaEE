package javaee.kononko.homework6;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BooksIntegrationTests {

    @SpyBean
    private BookRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    void setPort(int port) {
        RestAssured.port = port;
    }

    @Test
    void addBookAndFetchBook_emptyQueryReturnsAll() throws Exception {
        var form = new BookForm("The Great Gatsby", "F. Scott Fitzgerald", "33565-43");
        var json = objectMapper.writeValueAsString(form);

        RestAssured
                .given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/addBook")
                .then()
                .statusCode(HttpStatus.CREATED.value());


        var actual = RestAssured
                .given()
                .contentType("application/json")
                .when()
                .get("/bookList?query=")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(Book[].class);

        assertThat(actual, hasItemInArray(Book.builder().Name(form.getName()).Author(form.getAuthor()).Isbn(form.getIsbn()).build()));

        Mockito.verify(repository).allBooks();
        Mockito.verify(repository, Mockito.never()).searchBooks("");
    }


    @Test
    void addBookAndSearchBook_success() throws Exception {
        var form = new BookForm("Don Quixote", "Servantes", "4454-654");
        var json = objectMapper.writeValueAsString(form);

        RestAssured
                .given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/addBook")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        Map<String, Object> expected = new HashMap<>();
        expected.put("author", form.getAuthor());
        expected.put("name", form.getName());
        expected.put("isbn", form.getIsbn());

        var query = "Don";

        var actual = RestAssured
                .given()
                .contentType("application/json")
                .when()
                .get("/bookList?query=" + query)
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .extract()
                .as(Book[].class);

        assertThat(actual, hasItemInArray(Book.builder().Name(form.getName()).Author(form.getAuthor()).Isbn(form.getIsbn()).build()));

        Mockito.verify(repository, Mockito.never()).allBooks();
        Mockito.verify(repository).searchBooks(query);
    }

    @Test
    void addBookAndSearchWrongQuery_notContains() throws Exception {
        var form = new BookForm("Don Quixote", "Servantes", "1242-5445");
        var json = objectMapper.writeValueAsString(form);

        RestAssured
                .given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/addBook")
                .then()
                .statusCode(HttpStatus.CREATED.value());


        var query = "Gatsby";

        var actual = RestAssured
                .given()
                .contentType("application/json")
                .when()
                .get("/bookList?query=" + query)
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .extract()
                .as(Book[].class);

        assertThat(actual, not(hasItemInArray(Book.builder().Name(form.getName()).Author(form.getAuthor()).Isbn(form.getIsbn()).build())));

        Mockito.verify(repository, Mockito.never()).allBooks();
        Mockito.verify(repository).searchBooks(query);
    }

    @Test
    void addMultipleAndSearch_containsOne() throws Exception {
        var form1 = new BookForm("The Great Gatsby", "F. Scott Fitzgerald", "12345");
        var json = objectMapper.writeValueAsString(form1);

        RestAssured
                .given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/addBook")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        var form2 = new BookForm("Don Quixote", "Servantes", "67890");
        var json2 = objectMapper.writeValueAsString(form2);

        RestAssured
                .given()
                .contentType("application/json")
                .body(json2)
                .when()
                .post("/addBook")
                .then();

        var query = "12345";

        var actual = RestAssured
                .given()
                .contentType("application/json")
                .when()
                .get("/bookList?query=" + query)
                .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body("", Matchers.hasSize(1))
                .extract()
                .as(Book[].class);

        assertThat(actual, hasItemInArray(Book.builder().Name(form1.getName()).Author(form1.getAuthor()).Isbn(form1.getIsbn()).build()));

        Mockito.verify(repository, Mockito.never()).allBooks();
        Mockito.verify(repository).searchBooks(query);
    }
}
