package source;

import models.JsonCacheModel;
import models.JsonCachePathInfo;
import models.SpotifyAuthorizationInfo;
import models.SpotifyClientManager;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;

import java.io.IOException;

public class RefreshAuthorizationCodeManager {
    private final SpotifyAuthorizationInfo spotifyAuthorizationInfo = new SpotifyAuthorizationInfo();
    private final JsonCachePathInfo jsonCachePathInfo = new JsonCachePathInfo();
    private final JsonCacheModel jsonCacheModel = new JsonCacheModel();
    private final SpotifyClientManager spotifyClientManager = new SpotifyClientManager();

    public void RefreshAuthorizationCode() {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(spotifyClientManager.getClientId())
                .setClientSecret(spotifyClientManager.getClientSecret())
                .setRefreshToken(jsonCacheModel.getRefreshToken())
                .build();

        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());

            System.out.println("NEW TOKEN IS: " + authorizationCodeCredentials.getAccessToken());
        }
        catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
