package com.example.demoname.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    private String link;

    @ManyToOne
    private People people;

    private String text;

    @OneToMany(mappedBy = "post")
    private List<Media> media;
}
