package dev.prithvis.blogbackend.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDTO {
    private int id;
    @NotBlank
    @Size(min = 5, max = 100, message = "Title size must be between 5 and 100 characters")
    private String title;
    @NotBlank
    @Size(min = 1, max = 256, message = "Content must have size between 1 and 256 characters")
    private String content;
    private String imageURL;
    private String createdDate;
    private String updatedDate;
    private Integer userId;
    private Integer categoryId;
}
