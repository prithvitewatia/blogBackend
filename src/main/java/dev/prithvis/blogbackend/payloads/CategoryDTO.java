package dev.prithvis.blogbackend.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CategoryDTO {
    private int id;
    @NotBlank(message = "name must not be blank")
    private String name;
}
