package javaee.kononko.homework6;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;

@Service
public class BookValidator {

    public void validate(BookForm book) throws ValidationException {
        if (book.getName() == null || book.getAuthor() == null || book.getIsbn() == null)
            throw new ValidationException("Some fields are missing");
        if (book.getName().length() > 250)
            throw new ValidationException("Name is too long.");
        if (book.getAuthor().length() > 250)
            throw new ValidationException("Author name is too long.");
        if (book.getIsbn().length() > 50)
            throw new ValidationException("ISBN is too long.");
        if (!book.getIsbn().matches("[\\d*-?]+\\d$"))
            throw new ValidationException("Invalid ISBN format");
    }
}
