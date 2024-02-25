package source;

import models.Tokens;
import models.ResourcePaths;
import models.SpotifyCredentials;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;
import java.net.URISyntaxException;

public class AuthorizationCodeUriMaker {
    private final SpotifyCredentials spotifyCredentials = new SpotifyCredentials();
    private final ResourcePaths resourcePaths = new ResourcePaths();
    private final Tokens tokens = new Tokens();

    public void MakeUriAuthorizationRequest() {
        try {
            SpotifyApi spotifyApi = new SpotifyApi.Builder()
                    .setClientId(spotifyCredentials.getClientId())
                    .setClientSecret(spotifyCredentials.getClientSecret())
                    .setRedirectUri(new URI(spotifyCredentials.getRedirectUri()))
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
