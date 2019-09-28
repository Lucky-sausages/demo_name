package com.example.demoname.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
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

    private Date date;

    @OneToMany(mappedBy = "post")
    private List<Media> media;
}
