package org.caps.microservice.service.sso.service.consumer;

import org.caps.microservice.service.sso.service.consumer.fallback.RedisCacheServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(value = "microservice-service-redis", fallback = RedisCacheServiceFallback.class)
public interface RedisCacheService {
    @RequestMapping(value = "put", method = RequestMethod.POST)
    String put(
            @RequestParam(value = "key") String key,
            @RequestParam(value = "value") String value,
            @RequestParam(value = "seconds") long seconds);

    @RequestMapping(value = "get", method = RequestMethod.GET)
    String get(@RequestParam(value = "key") String key);
}
