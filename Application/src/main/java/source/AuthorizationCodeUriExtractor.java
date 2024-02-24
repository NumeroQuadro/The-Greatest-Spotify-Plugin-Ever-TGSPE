package source;

import models.JsonCacheModel;
import models.JsonCachePathInfo;
import models.SpotifyClientManager;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;
import java.net.URISyntaxException;

public class AuthorizationCodeUriExtractor {
    private final SpotifyClientManager spotifyClientManager = new SpotifyClientManager();
    private final JsonCachePathInfo jsonCachePathInfo = new JsonCachePathInfo();
    private final JsonCacheModel jsonCacheModel = new JsonCacheModel();

    public void MakeUriAuthorizationRequest() {
        try {
            SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setClientId(spotifyClientManager.getClientId())
                    .setClientSecret(spotifyClientManager.getClientSecret())
                    .setRedirectUri(new URI(spotifyClientManager.getRedirectUri()))
                    .build();

            AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                    .scope("user-library-read playlist-modify-public playlist-modify-private user-read-currently-playing user-read-playback-state user-read-recently-played")
                    .show_dialog(true)
                    .build();

            final URI uri = authorizationCodeUriRequest.execute();

            System.out.println("URI: " + uri.toString());

        }
        catch (URISyntaxException e) {
            System.out.println("URISyntaxException error: " + e.getMessage());
        }
    }
}
