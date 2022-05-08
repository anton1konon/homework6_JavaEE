package javaee.kononko.homework6;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

@Controller
public class BookController {

    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/")
    public String allBooks(Model model, final HttpServletResponse response){
        response.addHeader("Cache-Control", "no-store");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");
        var books = repository.allBooks();
        model.addAttribute("books", books);
        return "books";
    }

    @RequestMapping("/book/{id}")
    public String book(Model model, @PathVariable String id){
        var book = repository.getBook(Integer.parseInt(id));
        model.addAttribute("book", book);
        return "book";
    }

}
