package com.khodko.mongo.mongopagination.repository;

import com.khodko.mongo.mongopagination.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {
    Page<Person> findByNameLike(String name, Pageable pageable);
}
