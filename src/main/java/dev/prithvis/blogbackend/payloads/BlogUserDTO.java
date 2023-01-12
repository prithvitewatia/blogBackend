package dev.prithvis.blogbackend.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BlogUserDTO {
    private int id;
    @NotBlank(message = "Name must not be blank")
    private String name;
    @Email(message = "Please enter a valid email")
    private String email;
    @Pattern(regexp = "\\w{8,16}", message = "Password length must be 8-16 characters and can include letter, numbers and _")
    private String password;
    private String about;
}
