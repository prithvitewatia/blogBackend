package dev.prithvis.blogbackend.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class BlogUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column
    private String about;
}
