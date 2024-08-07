package ru.otus.hw.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("genres")
@Data
@Builder
public class Genre {
    @Id
    @Column("id")
    private final Long id;

    @Column("name")
    @NotNull
    private final String name;

    @PersistenceCreator
    public Genre(@JsonProperty("id") Long id, @JsonProperty("name")String name) {
        this.id = id;
        this.name = name;
    }
}
