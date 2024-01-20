package net.leaderos.shared.helpers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RequestUtil {

    private static final Cache<UUID, Byte> CACHE = CacheBuilder.newBuilder().expireAfterWrite(20, TimeUnit.SECONDS).build();

    public static void addRequest(UUID uuid) {
        CACHE.put(uuid, (byte) 1);
    }

    public static boolean canRequest(UUID uuid) {
        return CACHE.getIfPresent(uuid) == null;
    }

    public static void invalidate(UUID uuid) {
        CACHE.invalidate(uuid);
    }

}
