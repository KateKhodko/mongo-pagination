package com.khodko.mongo.mongopagination.controller;

import com.khodko.mongo.mongopagination.model.Person;
import com.khodko.mongo.mongopagination.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class PersonController {

    private final PersonService personService;

    /*
    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        return personService.findAll();
    }
     */

    @GetMapping("/persons/{id}")
    public ResponseEntity<Person> getAllPersons(@PathVariable("id") String id) {
        Person person = personService.findById(id);
        return ResponseEntity.ok().body(person);
    }

    @GetMapping("/persons")
    public ResponseEntity<Page<Person>> getAll(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestParam(required = false, defaultValue = "name;ASC") String[] sortBy,
            @RequestParam(required = false, defaultValue = "") String query) {

        Sort sort = Sort.by(
                Arrays.stream(sortBy)
                        .map(s -> s.split(";", 2))
                        .map(array ->
                                new Sort.Order(replaceOrderStringThroughDirection(array[1]),array[0])
                        ).collect(Collectors.toList())
        );

        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(personService.findAllByName(query, pageable));
    }

    private Sort.Direction replaceOrderStringThroughDirection(String sortDirection) {
        if (sortDirection.equalsIgnoreCase("DESC")) {
            return Sort.Direction.DESC;
        } else {
            return Sort.Direction.ASC;
        }
    }

    @PostMapping("/persons")
    public List<Person> createEmployees() {
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            personList.add(generateRandomPerson());
        }
        return personService.createAll(personList);
    }

    private Person generateRandomPerson() {
        return Person.builder()
                .name(UUID.randomUUID().toString())
                .build();
    }
}
