package com.example.demoname.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String link;

    @ManyToOne(cascade=CascadeType.ALL)
    private Post post;

}
