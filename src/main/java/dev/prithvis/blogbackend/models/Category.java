package dev.prithvis.blogbackend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(nullable = false,unique = true)
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Post> posts=new LinkedList<>();
}
