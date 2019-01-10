package com.xf.zhang.repository;

import com.xf.zhang.domain.City;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CityRepository extends ElasticsearchRepository<City, Long> {

}