package javaee.kononko.homework6;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;

@RequiredArgsConstructor
@RestController
public class BookRestController {
    private final BookRepository repository;
    private final BookValidator validator;


    @ResponseBody
    @GetMapping("/bookList")
    public Iterable<Book> bookList(@RequestParam String query){
        return query.isEmpty() ? repository.allBooks() : repository.searchBooks(query);
    }

    @PostMapping("/addBook")
    public ResponseEntity<String> addBook(@RequestBody final BookForm form){
        try {
            validator.validate(form);
            repository.addBook(form.getName(), form.getAuthor(), form.getIsbn());
            return new ResponseEntity<>("Added successfully!", HttpStatus.CREATED);
        } catch (ValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
