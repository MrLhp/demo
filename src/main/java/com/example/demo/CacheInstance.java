package com.example.demo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheInstance {

    private static Cache<String, CinemaInfo> cinemaCache = null;

    public static Cache<String, CinemaInfo> getCache(){
        if(cinemaCache == null){
            cinemaCache
            //CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
            = CacheBuilder.newBuilder()
            //设置并发级别为8，并发级别是指可以同时写缓存的线程数
            .concurrencyLevel(8)
            //设置写缓存后8秒钟过期
            .expireAfterWrite(1, TimeUnit.DAYS)
            //设置缓存容器的初始容量为10
            .initialCapacity(5000)
            //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
            .maximumSize(10000).build();
        }
        return cinemaCache;
    }

}
