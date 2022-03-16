package com.work.bookseller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.work.bookseller.enumeration.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    @JsonIgnore
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Purchase> purchases = new ArrayList<>();

    // UserAuthResponseDto class'ı oluşturduğumdan burada transient olarak token kullanmama gerek kalmadı.
    // Veritabanında olmucak, anlık işlemler için kullanılacak
//    @Transient
//    private String token;

}
