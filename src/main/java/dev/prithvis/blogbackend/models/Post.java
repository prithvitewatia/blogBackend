package dev.prithvis.blogbackend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column
    private String imageURL;
    @Column
    private LocalDate createdDate;
    @Column
    private LocalDate updatedDate;
    @ManyToOne
    private BlogUser user;
    @ManyToOne
    private Category category;
}
