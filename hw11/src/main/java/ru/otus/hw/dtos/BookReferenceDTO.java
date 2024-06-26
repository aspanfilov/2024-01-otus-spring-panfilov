package ru.otus.hw.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReferenceDTO {

    private Long id;

    private String title;

    private Long authorId;

    private Set<Long> genreIds;
}
