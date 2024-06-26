package ru.otus.hw.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookCommentDTO {

    private Long id;

    //    @NotBlank(message = "comment cannot be blank")
    private String commentText;

    private BookDetailDTO book;

}
