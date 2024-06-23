package ru.otus.hw.models.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MongoGenre {
    @Id
    private String id;

    @Field(value = "name", targetType = FieldType.STRING)
    private String name;
}
