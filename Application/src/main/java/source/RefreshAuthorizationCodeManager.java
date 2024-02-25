package source;

import models.Tokens;
import models.ResourcePaths;
import models.AuthorizationCode;
import models.SpotifyCredentials;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;

import java.io.IOException;

public class RefreshAuthorizationCodeManager {
    private final SpotifyTokenManager spotifyTokenManager = new SpotifyTokenManager();
    private final Tokens tokens = new Tokens();
    private final SpotifyCredentials spotifyCredentials = new SpotifyCredentials();

    public void RefreshAuthorizationCode() {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(spotifyCredentials.getClientId())
                .setClientSecret(spotifyCredentials.getClientSecret())
                .setRefreshToken(tokens.getRefreshToken())
                .build();

        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());

            var oldTokenJson = spotifyTokenManager.getStoredJsonCache();
            oldTokenJson.setAccessToken(authorizationCodeCredentials.getAccessToken());

            spotifyTokenManager.storeNewCacheJson(oldTokenJson);

            System.out.println("NEW TOKEN IS: " + authorizationCodeCredentials.getAccessToken());
        }
        catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
