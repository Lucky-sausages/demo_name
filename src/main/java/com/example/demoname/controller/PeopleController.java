package com.example.demoname.controller;

import com.example.demoname.domain.User;
import com.example.demoname.dto.PeopleDTO;
import com.example.demoname.exception.*;
import com.example.demoname.service.PeopleService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/people")
public class PeopleController extends ApiController{
    private PeopleService peopleService;

    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @PostMapping
    public ResponseEntity<?> add_person(User user, @Valid @RequestBody PeopleDTO peopleDTO,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            new ValidationException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        peopleService.save(peopleDTO, user);
        return ResponseEntity.ok().build();
    }
}
