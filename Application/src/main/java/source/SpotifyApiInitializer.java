package source;

import models.SpotifyClientManager;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SpotifyApiInitializer {
    private final AuthorizationCodeManager authorizationCodeManager = new AuthorizationCodeManager();
    private final SpotifyClientManager spotifyClientManager = new SpotifyClientManager();
    public SpotifyApi GetFreshSpotifyApi() {
        String authorizationCode = authorizationCodeManager.GetAuthorizationCodeFromJson().getAuthorizationCode();

        try {
            SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setClientId(spotifyClientManager.getClientId())
                    .setClientSecret(spotifyClientManager.getClientSecret())
                    .setRedirectUri(new URI(spotifyClientManager.getRedirectUri()))
                    .build();

            AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(authorizationCode)
                    .build();

            try {
                final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
                spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
                spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
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
