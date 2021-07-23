package com.lola.digiccy.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MECHREVO
 */
public class TokenCache {
    /**
     * token缓存
     */
    public static Map<String ,String> TokenCache= new ConcurrentHashMap<>();
}
