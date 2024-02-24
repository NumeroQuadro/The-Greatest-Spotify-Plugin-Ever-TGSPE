package source;

import MaybeUnused.SpotifyCredentialsController;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.net.URISyntaxException;

public class InitialState {
    private final AuthorizationCodeUriExtractor authorizationCodeUriExtractor = new AuthorizationCodeUriExtractor();
    private final SpotifyApiInitializer spotifyApiInitializer = new SpotifyApiInitializer();
    private static final RefreshAuthorizationCodeManager refreshAuthorizationCodeManager = new RefreshAuthorizationCodeManager();
    public void GetMusicInfoPerDay() {
        var spotifyApi = spotifyApiInitializer.GetFreshSpotifyApi();

        var tracksBuilder = spotifyApi.getCurrentUsersRecentlyPlayedTracks()
                .limit(50)
                .build();
        try {
            var tracks = tracksBuilder.execute();
            for (var track : tracks.getItems()) {
                System.out.println("Track: " + track.getTrack().getName());
                System.out.println("Artist: " + track.getTrack().getArtists()[0].getName());
                System.out.println("Played at: " + track.getPlayedAt());
                System.out.println();
            }
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        var initialState = new InitialState();
        var authorizationCodeExtractor = new AuthorizationCodeExtractor();
        authorizationCodeExtractor.StartHttpExchange();
        //refreshAuthorizationCodeManager.RefreshAuthorizationCode();
        initialState.authorizationCodeUriExtractor.MakeUriAuthorizationRequest();
        //initialState.GetMusicInfoPerDay();
    }
}
