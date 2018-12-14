package com.example.fulexweb.handler;

import com.example.fulexweb.dao.CityRepository;
import com.example.fulexweb.domain.City;
import io.micrometer.core.annotation.Timed;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;

/**
 * @Author:lhz
 * @Description:
 * @Date:10:28 2018-12-1
 */
@Component
public class CityHandler {

  private final CityRepository cityRepository;


  public CityHandler(CityRepository cityRepository) {
    this.cityRepository = cityRepository;
  }

  public Mono<Long> save(City city) {
    return Mono.create(cityMonoSink -> cityMonoSink.success(cityRepository.save(city)));
  }

  public Mono<City> findCityById(Long id) {
    return Mono.justOrEmpty(cityRepository.findCityById(id));
  }

  public Flux<City> findAllCity() {
    return Flux.fromIterable(cityRepository.findAll());
  }

  public Mono<Long> modifyCity(City city) {
    return Mono.create(cityMonoSink -> cityMonoSink.success(cityRepository.updateCity(city)));
  }

  public Mono<Long> deleteCity(Long id) {
    return Mono.create(cityMonoSink -> cityMonoSink.success(cityRepository.deleteCity(id)));
  }

  @Timed
  @Cacheable(cacheNames="locationSearch", key="'locationByCode'")
  public Mono<ServerResponse> helloCity(ServerRequest request) {
    System.out.println("goo 000 ...");
    return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
        .body(BodyInserters.fromObject("Hello, City!".concat(request.queryParam("name").orElse(request.pathVariable("id")))));
  }
}
