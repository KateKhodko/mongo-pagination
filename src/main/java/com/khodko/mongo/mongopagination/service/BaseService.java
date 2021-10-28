package com.khodko.mongo.mongopagination.service;

import java.util.List;

public interface BaseService<DTO> {

    List<DTO> findAll();

    DTO create(DTO entityDto);

    void delete(String id);

    DTO findById(String id);
}
