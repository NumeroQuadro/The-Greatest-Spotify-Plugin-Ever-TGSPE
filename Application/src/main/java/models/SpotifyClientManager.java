package models;

import lombok.Getter;

@Getter
public class SpotifyClientManager {
    private final String clientId = "81537d7d799146288372e604ec4bb9c8";
    private final String clientSecret = "5b36844348f4496b8f527a8035c8d2b0";
    private final String redirectUri = "http://localhost:5000/redirect";
}
