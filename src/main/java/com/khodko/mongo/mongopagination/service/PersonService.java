package com.khodko.mongo.mongopagination.service;

import com.khodko.mongo.mongopagination.model.Person;
import com.khodko.mongo.mongopagination.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PersonService implements BaseService<Person>{

    private final PersonRepository personRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Page<Person> findAll(Pageable page) {
        return personRepository.findAll(page);
    }

    public Page<Person> findAllByName(String query, Pageable page) {
        Query q = new Query().with(page);
        q.addCriteria(Criteria.where("name").regex(query, "i"));
        return  PageableExecutionUtils.getPage(
                mongoTemplate.find(q, Person.class),
                page,
                () -> mongoTemplate.count(q.skip(0).limit(0), Person.class));
    }

    @Override
    public Person create(Person entityDto) {
        return personRepository.save(entityDto);
    }

    public List<Person> createAll(List<Person> entityDto) {
        return personRepository.saveAll(entityDto);
    }

    @Override
    public void delete(String id) {
        personRepository.deleteById(id);
    }

    @Override
    public Person findById(String id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElse(null);
    }
}
