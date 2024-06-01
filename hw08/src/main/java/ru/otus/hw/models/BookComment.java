package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "book_comments")
//@Getter
//@Setter
//@EqualsAndHashCode(exclude = {"book"})
//@ToString(exclude = {"book"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookComment {
    @Id
    private String id;

    @Field(value = "comment_text", targetType = FieldType.STRING)
    private String commentText;

    @Field(value = "book")
    private Book book;
}
