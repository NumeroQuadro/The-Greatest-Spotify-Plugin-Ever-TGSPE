package models;

import lombok.Getter;
import lombok.Setter;

@Getter
public class JsonCachePathInfo {
    private final String cacheJsonPath = "Application/src/main/java/resources/cache.json";
    private final String spotifyAuthorizationInfoPath = "Application/src/main/java/resources/spotify_auth.json";
}
