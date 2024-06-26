package ru.otus.hw.models;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "authors")
@Data
@Builder
public class Author {
    @Id
    @Column("id")
    private final Long id;

    @Column("full_name")
    @NotNull
    private final String fullName;

    @PersistenceCreator
    public Author(Long id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

}
