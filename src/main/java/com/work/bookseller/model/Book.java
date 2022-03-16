package com.work.bookseller.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Book extends Base{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false)
    private Double price;

    @OneToMany(mappedBy = "book", orphanRemoval = true)
    private List<Purchase> purchases = new ArrayList<>();
}
