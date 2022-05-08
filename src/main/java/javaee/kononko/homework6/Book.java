package javaee.kononko.homework6;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "book_name")
    private String Name;

    @Column(name = "author")
    private String Author;

    @Column(name = "isbn")
    private String Isbn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Name.equals(book.Name) && Author.equals(book.Author) && Isbn.equals(book.Isbn);
    }
}
