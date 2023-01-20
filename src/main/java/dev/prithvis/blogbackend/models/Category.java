package dev.prithvis.blogbackend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table
@Data
public class Category {
    @Id
    @Column
    private int id;
    @Column(nullable = false,unique = true)
    private String name;
}
