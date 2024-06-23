package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;

@Document(collection = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MongoBook {
    @Id
    private String id;

    @Field(value = "title", targetType = FieldType.STRING)
    private String title;

    @Field(value = "author")
    private MongoAuthor author;

    @Field(value = "genres")
    private List<MongoGenre> genres;

}
