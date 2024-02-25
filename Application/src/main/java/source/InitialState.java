package source;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class InitialState {
    private final AuthorizationCodeUriMaker authorizationCodeUriMaker = new AuthorizationCodeUriMaker();
    private final SpotifyApiInitializer spotifyApiInitializer = new SpotifyApiInitializer();
    private static final RefreshAuthorizationCodeManager refreshAuthorizationCodeManager = new RefreshAuthorizationCodeManager();
    public void GetMusicInfoPerDay() {
        var spotifyApi = spotifyApiInitializer.GetFreshSpotifyApi();

        var tracksBuilder = spotifyApi.getCurrentUsersRecentlyPlayedTracks()
                .limit(1)
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
//        var initialState = new InitialState();
//        var authorizationCodeExtractor = new AuthorizationCodeExtractor();
//        authorizationCodeExtractor.StartHttpExchange();
//        initialState.authorizationCodeUriExtractor.MakeUriAuthorizationRequest();
//        initialState.GetMusicInfoPerDay();
        //refreshAuthorizationCodeManager.RefreshAuthorizationCode();
        var initialState = new InitialState();
        var authorizationCodeExtractor = new AuthorizationCodeExtractor();

        try {
            // Start the HTTP exchange and await the new authorization code
            CompletableFuture<String> authorizationCodeFuture = authorizationCodeExtractor.StartHttpExchange();

            // MakeUriAuthorizationRequest doesn't need to change
            initialState.authorizationCodeUriMaker.MakeUriAuthorizationRequest();

            // Await the future to complete, which means a new authorization code has been received
            String authorizationCode = authorizationCodeFuture.join(); // Blocks until the future is complete

            // Now you can safely proceed to GetMusicInfoPerDay or any other operations that depend on the authorization code
            initialState.GetMusicInfoPerDay();
            refreshAuthorizationCodeManager.RefreshAuthorizationCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
