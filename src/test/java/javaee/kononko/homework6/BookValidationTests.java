package javaee.kononko.homework6;

import org.junit.jupiter.api.Test;

import javax.xml.bind.ValidationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class BookValidationTests {

    private BookValidator validator = new BookValidator();

    @Test
    void postNoNameError()  {
        var book = new BookForm(null, "AA", "123");
        assertThatThrownBy(()->validator.validate(book)).isInstanceOf(ValidationException.class);
    }

    @Test
    void postNoAuthorError() {
        var book = new BookForm("AA", null, "123");
        assertThatThrownBy(()->validator.validate(book)).isInstanceOf(ValidationException.class);
    }

    @Test
    void postNoIsbnError() {
        var book = new BookForm("ABC", "AA", null);
        assertThatThrownBy(()->validator.validate(book)).isInstanceOf(ValidationException.class);
    }

    @Test
    void postWrongIsbnError() {
        var book = new BookForm(null, "AA", "ABCD");
        assertThatThrownBy(()->validator.validate(book)).isInstanceOf(ValidationException.class);
    }

    @Test
    void postOkBookSuccess() throws Exception {
        var book = new BookForm("ABC", "AA", "123");
        validator.validate(book);
    }
}
