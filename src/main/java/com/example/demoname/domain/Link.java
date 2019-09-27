package com.example.demoname.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String link;

    @ManyToOne
    private People people;

}
