package com.example.fulexweb.router;

import com.example.fulexweb.handler.CityHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @Author:lhz
 * @Description:
 * @Date:11:01 2018-12-1
 */
@Configuration
public class CityRouter {

  @Bean
  public RouterFunction<ServerResponse> routeCity(CityHandler cityHandler) {
    return RouterFunctions
        .route(RequestPredicates.GET("/hello/{id}")
                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
            cityHandler::helloCity);
  }


}
