package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MongoAuthor {
    @Id
    private String id;

    @Field(value = "full_name", targetType = FieldType.STRING)
    private String fullName;
}
