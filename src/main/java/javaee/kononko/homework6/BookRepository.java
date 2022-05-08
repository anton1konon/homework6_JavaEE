package javaee.kononko.homework6;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Repository
@RequiredArgsConstructor
public class BookRepository {
    private final EntityManager entityManager;

    @Transactional
    public Book addBook(String name, String author, String isbn) {
        var book = new Book();
        book.setName(name);
        book.setAuthor(author);
        book.setIsbn(isbn);
        return entityManager.merge(book);
    }

    @Transactional
    public List<Book> allBooks() {
        var query = entityManager.createQuery("select b from Book b", Book.class);
        return query.getResultList();
    }

    @Transactional
    public List<Book> searchBooks(String search) {
        var query = entityManager.createQuery(
                "select b from Book b " +
                        "where lower(b.Name) like ?1 or " +
                        "lower(b.Isbn) like ?1 or " +
                        "lower(b.Author) like ?1", Book.class);
        query.setParameter(1,'%' + search.toLowerCase(Locale.ROOT) + '%');
        return query.getResultList();
    }

    @Transactional
    public Book getBook(Integer id) {
        return entityManager.find(Book.class, id);
    }
}
