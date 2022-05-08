package javaee.kononko.homework6;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;

@SpringBootTest
public class BookRepositoryTests {
    @Autowired
    private BookRepository repository;

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void allBooks_does_not_throw(){
        repository.allBooks();
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void allBooks_contains_two(){
        var list = repository.allBooks();
        assertThat(list, hasSize(2));
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void search_books_contains_donQuixote(){
        var list = repository.searchBooks("Don Quixote");
        assertThat(list, hasItem(hasProperty("name", equalTo("Don Quixote"))));
        assertThat(list, not(hasItem(hasProperty("name", equalTo("The Great Gatsby")))));
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void search_books_contains_nothing(){
        var list = repository.searchBooks("@#$%");
        assertThat(list, hasSize(0));
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void add_book_success(){
        var book = Book.builder().Name("Don Quixote").Author("Servantes").Isbn("123456").build();
        var added = repository.addBook(book.getName(), book.getAuthor(), book.getIsbn());
        assertThat(added, equalTo(book));
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void add_book_and_get_book_by_id(){
        var book = Book.builder().Name("Don Quixote").Author("Servantes").Isbn("123456").build();
        var id = repository.addBook(book.getName(), book.getAuthor(), book.getIsbn()).getId();
        var loaded = repository.getBook(id);
        assertThat(loaded, equalTo(book));
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void add_book_and_get_books_contains_added(){
        var book = Book.builder().Name("War and Peace").Author("Leo Tolstoy").Isbn("9780786112517").build();
        repository.addBook(book.getName(), book.getAuthor(), book.getIsbn());
        var books = repository.allBooks();
        assertThat(books, hasItem(book));
    }

    @Test
    @Sql("setup.sql")
    @Sql(value = "teardown.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void add_book_and_search_books_contains_added(){
        var book = Book.builder().Name("War and Peace").Author("Leo Tolstoy").Isbn("9780786112517").build();
        repository.addBook(book.getName(), book.getAuthor(), book.getIsbn());
        var books = repository.searchBooks(book.getIsbn());
        assertThat(books, hasItem(book));
    }
}
