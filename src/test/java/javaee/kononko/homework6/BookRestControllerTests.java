package javaee.kononko.homework6;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.xml.bind.ValidationException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsInAnyOrder;

@WebMvcTest(BookRestController.class)
public class BookRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BookValidator bookValidator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addBook() throws Exception {
        var form = new BookForm("Don Quixote", "Servantes", "9780521485478");
        var json = objectMapper.writeValueAsString(form);
        mockMvc.perform(MockMvcRequestBuilders.post("/addBook")
                .contentType("application/json")
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void addBook_onException_BadRequest() throws Exception {
        var form = new BookForm("Don Quixote", "Servantes", "ABCD");

        Mockito.doThrow(new ValidationException("Exception")).when(bookValidator).validate(Mockito.any());

        var json = objectMapper.writeValueAsString(form);
        mockMvc.perform(MockMvcRequestBuilders.post("/addBook")
                .contentType("application/json")
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void allBooks() throws Exception {
        var books = List.of(
                Book.builder().Name("Don Quixote").Author("Servantes").Isbn("9780521485478").build(),
                Book.builder().Name("The Great Gatsby").Author("F. Scott Fitzgerald").Isbn("9780521485470").build()
        );

        Mockito.when(bookRepository.allBooks()).thenReturn(books);

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/bookList?query="))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        var actual = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<Book>>() {
        });

        assertThat(actual, containsInAnyOrder(books.toArray()));
    }

    @Test
    void searchBooks() throws Exception {
        var books = List.of(
                Book.builder().Name("Don Quixote").Author("Servantes").Isbn("9780521485478").build(),
                Book.builder().Name("The Great Gatsby").Author("F. Scott Fitzgerald").Isbn("9780521485470").build()
        );

        var json = objectMapper.writeValueAsString(books);

        Mockito.when(bookRepository.searchBooks("9780")).thenReturn(books);

        var response = mockMvc.perform(MockMvcRequestBuilders.get("/bookList?query=9780"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        var actual = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<Book>>() {
        });

        assertThat(actual, containsInAnyOrder(books.toArray()));
    }

    @Test
    void searchBooksEmpty() throws Exception {
        Mockito.when(bookRepository.searchBooks("9780")).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/bookList?query=9780"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }
}
