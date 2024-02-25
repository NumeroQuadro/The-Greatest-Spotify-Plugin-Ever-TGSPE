package source;

import models.SpotifyCredentials;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SpotifyApiInitializer {
    private final SpotifyTokenManager spotifyTokenManager = new SpotifyTokenManager();
    private final AuthorizationCodeManager authorizationCodeManager = new AuthorizationCodeManager();
    private final SpotifyCredentials spotifyCredentials = new SpotifyCredentials();
    public SpotifyApi GetFreshSpotifyApi() {
        String authorizationCode = authorizationCodeManager.GetAuthorizationCodeFromJson().getAuthorizationCode();

        try {
            SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setClientId(spotifyCredentials.getClientId())
                    .setClientSecret(spotifyCredentials.getClientSecret())
                    .setRedirectUri(new URI(spotifyCredentials.getRedirectUri()))
                    .build();

            AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(authorizationCode)
                    .build();

            try {
                final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
                spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
                spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

                var oldJson = spotifyTokenManager.getStoredJsonCache();
                oldJson.setAccessToken(authorizationCodeCredentials.getAccessToken());
                oldJson.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

                spotifyTokenManager.storeNewCacheJson(oldJson);
                return spotifyApi;
            }
            catch (IOException | SpotifyWebApiException | ParseException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
