package dev.prithvis.blogbackend.payloads;

import lombok.Data;

@Data
public class BlogUserDTO {
    private int id;
    private String name;
    private String email;
    private String password;
    private String about;
}
