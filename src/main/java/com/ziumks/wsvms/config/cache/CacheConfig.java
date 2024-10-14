package com.ziumks.wsvms.config.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

/**
 * 로컬 캐시 저장 설정
 *
 * @author  이상민
 * @since   2024.05.21 16:30
 */
@EnableCaching
@RequiredArgsConstructor
@Configuration
public class CacheConfig {

    /**
     * CacheManager를 생성하여 반환합니다.
     *
     * @return CacheManager 객체
     */
    @Bean
    public CacheManager cacheManager() {

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Collections.singletonList(new ConcurrentMapCache("localCacheStore")));

        return cacheManager;

    }

}