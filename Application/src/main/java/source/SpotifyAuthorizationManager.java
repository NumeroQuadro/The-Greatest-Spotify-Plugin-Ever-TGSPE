package source;

import models.JsonCacheModel;
import models.JsonCachePathInfo;
import models.SpotifyClientManager;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;

import java.net.URI;
import java.net.URISyntaxException;

public class SpotifyAuthorizationManager {
    private final SpotifyClientManager spotifyClientManager = new SpotifyClientManager();
    private final JsonCachePathInfo jsonCachePathInfo = new JsonCachePathInfo();
    private final JsonCacheModel jsonCacheModel = new JsonCacheModel();
    public void authorize() {
        try {
            SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setClientId(spotifyClientManager.getClientId())
                    .setClientSecret(spotifyClientManager.getClientSecret())
                    .setRedirectUri(new URI(spotifyClientManager.getRedirectUri()))
                    .build();

//            AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
//                    .build();
        }
        catch (URISyntaxException e) {
            System.out.println("URISyntaxException error: " + e.getMessage());
        }
    }
}
