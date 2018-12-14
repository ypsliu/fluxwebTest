package com.example.fulexweb.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *
 * Configuration class for setting up the caffeine caches used
 * in calls to the Location API.
 */
@Configuration
public class LocationApiConfig {


    @Bean
    public CacheManager cacheManager(Ticker ticker, MeterRegistry meterRegistry) {
        CaffeineCache locationSearch = buildCache("locationSearch", ticker, 3,
                10, meterRegistry);
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(locationSearch));

        return manager;
    }

    /**
     * Build a Caffeine cache based on properties configured.
     * @param name Name of the cache.
     * @param ticker Time tracking object.
     * @param minutesToExpire Number of minutes entries will last in the cache.
     * @param maxSize Maximum amount of entries allowed in the cache.
     * @param meterRegistry Registry for metrics.
     * @return A configured caffeine cache.
     */
    protected CaffeineCache buildCache(String name, Ticker ticker, int minutesToExpire, int maxSize, MeterRegistry meterRegistry) {
        Cache cache = Caffeine.newBuilder()
                .expireAfterWrite(minutesToExpire, TimeUnit.SECONDS)
                .maximumSize(maxSize)
                .recordStats()
                .ticker(ticker)
                .build();

        CaffeineCacheMetrics.monitor(meterRegistry, cache, name);

        return new CaffeineCache(name, cache);
    }

    /**
     * Caffeine cache time tracking object.
     * @return Caffeine ticker object.
     */
    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
