package com.example.demoname.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class People {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "people")
    private List<Post> posts;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "people",cascade = CascadeType.ALL)
    private List<Link> links;
}
