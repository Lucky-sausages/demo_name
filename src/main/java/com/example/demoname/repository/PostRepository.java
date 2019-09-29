package com.example.demoname.repository;

import com.example.demoname.domain.People;
import com.example.demoname.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>
{
    Post findFirstByPeopleOrderByDateAsc(People people);

    List<Post> findAllByPeopleInOrderByDateDesc(Collection<People> people);
}
