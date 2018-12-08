package com.example.fulexweb;

import com.example.fulexweb.ratelimter.RateLimiterResponse;
import com.example.fulexweb.ratelimter.RedisRateLimiter;
import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Author:lhz
 * @Description:
 * @Date:11:38 2018-12-8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {

  @Autowired
  @Qualifier("reactiveRedisTemplate")
  private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

  @Autowired
  private RedisRateLimiter redisRateLimiter;

  @Autowired
  private RedisTemplate<String, Serializable> redisTemplate;



  @org.junit.Test
  public void testRateLimiter()
  {    String key=UUID.randomUUID().toString();
             for(int i=0;i<10;i++) {
               RateLimiterResponse RateLimiterResponse = redisRateLimiter
                   .isAllowed(key, 30, 8).block();
               System.out.println(RateLimiterResponse.isAllowed() + "_" + RateLimiterResponse.getTokensRemaining());
             }

  }

  //@org.junit.Test
  public void testRedisTemplate()
  {

    List<String> keys = getKeys(UUID.randomUUID().toString());
    Long[] script= {60l,5l,122223l,1l};
    //String[] strs={"60","5","555","1"};
    for(int i=0;i<1;i++) {
      List<Long>  resultFlux = ( List<Long> )redisTemplate.execute(this.redisScript(), keys, script);

      for(Long l :resultFlux)
      {
        System.out.println("reslut_"+l);
      }


    }

  }
  //@org.junit.Test
  public void testReactLuaLimit()
  {
    List<String> keys = getKeys(UUID.randomUUID().toString());
    List<String> scriptArgs = Arrays.asList( "60", "5",
        Instant.now().getEpochSecond() + "", "1");
       for(int i=0;i<5;i++) {
         Flux<List<Long>> resultFlux = reactiveRedisTemplate.execute(this.redisScript(), keys, scriptArgs);
         resultFlux.blockFirst().forEach(x->System.out.println("result:"+x));
       }

  }

  public RedisScript redisScript() {
    DefaultRedisScript redisScript = new DefaultRedisScript<>();
    redisScript.setScriptSource(new ResourceScriptSource(
        new ClassPathResource("/META-INF/scripts/request_rate_limiter.lua")));
    redisScript.setResultType(List.class);
    return redisScript;
  }

  private  List<String> getKeys(final String id) {
    String prefix = "request_rate_limiter.{" + id;
    String tokenKey = prefix + "}.tokens";
    String timestampKey = prefix + "}.timestamp";
    return Arrays.asList(tokenKey, timestampKey);
  }


}
