package com.irembo.urlshortner.service;

import java.util.HashMap;
import java.util.Map;

public class Cache<K, V> {
    private final long timeout;
    private final Map<K, CacheObject> cacheMap;

    public Cache(long timeout) {
        this.timeout = timeout;
        this.cacheMap = new HashMap<>();
    }

    public void put(K key, V value) {
        synchronized (cacheMap) {
            CacheObject co = new CacheObject(value);
            cacheMap.put(key, co);
        }
    }

    public V get(K key) {
        synchronized (cacheMap) {
            CacheObject co = cacheMap.get(key);
            if (co == null) {
                return null;
            } else {
                co.lastAccessed = System.currentTimeMillis();
                return co.value;
            }
        }
    }

    public void remove(K key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }

    private class CacheObject {
        private final V value;
        private long lastAccessed;

        private CacheObject(V value) {
            this.value = value;
            this.lastAccessed = System.currentTimeMillis();
        }
    }
}
