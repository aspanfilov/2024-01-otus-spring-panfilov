package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "book_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MongoBookComment {
    @Id
    private String id;

    @Field(value = "comment_text", targetType = FieldType.STRING)
    private String commentText;

    @DBRef
    @Field(value = "book")
    private MongoBook book;
}
