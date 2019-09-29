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

    String domain;
    String name;

    @OneToMany(mappedBy = "people")
    private List<Post> posts;

    @OneToMany(mappedBy = "people")
    private List<Link> links;
}
