package ru.otus.hw.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookCommentDTO {

    private Long id;

    @NotBlank(message = "comment cannot be blank")
    private String commentText;

    private Long bookId;

    public BookCommentDTO(@JsonProperty("id") Long id,
                          @JsonProperty("commentText") String commentText,
                          @JsonProperty("bookId") Long bookId) {
        this.id = id;
        this.commentText = commentText;
        this.bookId = bookId;

    }
}
