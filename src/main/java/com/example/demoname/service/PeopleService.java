package com.example.demoname.service;

import com.example.demoname.domain.Link;
import com.example.demoname.domain.People;
import com.example.demoname.domain.User;
import com.example.demoname.dto.PeopleDTO;
import com.example.demoname.repository.LinkRepository;
import com.example.demoname.repository.PeopleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PeopleService {
    PeopleRepository peopleRepository;
    LinkRepository linkRepository;

    public PeopleService(PeopleRepository peopleRepository, LinkRepository linkRepository) {
        this.peopleRepository = peopleRepository;
        this.linkRepository = linkRepository;
    }

    public void save(PeopleDTO peopleDTO, User user) {
        People people = new People();
        people.setName(peopleDTO.getName());
        //people.setLinks(links);
        people.setUser(user);
        people = peopleRepository.save(people);
        List<Link> links = new ArrayList<Link>();
        links.add(new Link("https://www.instagram.com/" + peopleDTO.getInst_name(), people));
        links.add(new Link("https://vk.com/" + peopleDTO.getVk_name(), people));
        links = links.stream().map(link -> linkRepository.save(link)).collect(Collectors.toList());
    }
}
