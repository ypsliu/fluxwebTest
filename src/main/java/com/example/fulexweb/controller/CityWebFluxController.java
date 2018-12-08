package com.example.fulexweb.controller;

import com.example.fulexweb.domain.City;
import com.example.fulexweb.handler.CityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Author:lhz
 * @Description:
 * @Date:10:46 2018-12-1
 */
@RestController
@RequestMapping(value = "/city")
public class CityWebFluxController {
  @Autowired
  private CityHandler cityHandler;

  @GetMapping(value = "/{id}")
  public Mono<City> findCityById(@PathVariable("id") Long id) {
    return cityHandler.findCityById(id);
  }

  @GetMapping()
  public Flux<City> findAllCity() {
    return cityHandler.findAllCity();
  }

  @PostMapping()
  public Mono<Long> saveCity(@RequestBody City city) {
    return cityHandler.save(city);
  }

  @PutMapping()
  public Mono<Long> modifyCity(@RequestBody City city) {
    return cityHandler.modifyCity(city);
  }

  @DeleteMapping(value = "/{id}")
  public Mono<Long> deleteCity(@PathVariable("id") Long id) {
    return cityHandler.deleteCity(id);
  }
}
