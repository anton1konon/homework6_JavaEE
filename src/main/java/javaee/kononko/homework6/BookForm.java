package javaee.kononko.homework6;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookForm {
    private String Name;
    private String Author;
    private String Isbn;
}
