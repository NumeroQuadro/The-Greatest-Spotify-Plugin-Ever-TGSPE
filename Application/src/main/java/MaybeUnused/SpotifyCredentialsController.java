package MaybeUnused;

import models.JsonCacheModel;
import models.JsonCachePathInfo;
import models.SpotifyClientManager;
import org.apache.hc.core5.http.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import source.AuthorizationCodeManager;
import source.JsonAuthInfoExtractor;
import source.SpotifyApiInitializer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SpotifyCredentialsController {
    private final SpotifyClientManager spotifyClientManager = new SpotifyClientManager();
    private final JsonCachePathInfo jsonCachePathInfo = new JsonCachePathInfo();
    private static final JsonAuthInfoExtractor jsonCredentialsExtractor = new JsonAuthInfoExtractor();

    private final AuthorizationCodeManager authorizationCodeManager = new AuthorizationCodeManager();

    private final SpotifyApiInitializer spotifyApiInitializer = new SpotifyApiInitializer();
    public SpotifyApi GetRelevantSpotifyApiManager() throws URISyntaxException {

        var spotifyApi = spotifyApiInitializer.GetFreshSpotifyApi();

        var storedJsonCache = jsonCredentialsExtractor.getStoredJsonCache();
        var expiresAt = storedJsonCache.getExpiresAt();
        long currentUnixTime = System.currentTimeMillis() / 1000L;

        if (currentUnixTime >= expiresAt) {
            return new SpotifyCredentialsController().authorizationCodeRefresh_Sync();
        }

        return new SpotifyApi.Builder()
                .setClientId(spotifyClientManager.getClientId())
                .setClientSecret(spotifyClientManager.getClientSecret())
                .setAccessToken(storedJsonCache.getAccessToken())
                .setRefreshToken(storedJsonCache.getRefreshToken())
                .setRedirectUri(new URI(spotifyClientManager.getRedirectUri()))
                .build();
    }

    public SpotifyApi authorizationCodeRefresh_Sync() {
        SpotifyApi spotifyApi = GetSpotifyApiWithCurrentFromJsonCache();
        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().refresh_token(spotifyApi.getRefreshToken()).build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();

            JSONObject newSpotifyAuthInfo = new JSONObject();
            newSpotifyAuthInfo.put("access_token", authorizationCodeCredentials.getAccessToken());
            newSpotifyAuthInfo.put("token_type", authorizationCodeCredentials.getTokenType());
            newSpotifyAuthInfo.put("expires_in", authorizationCodeCredentials.getExpiresIn());
            newSpotifyAuthInfo.put("scope", authorizationCodeCredentials.getScope());
            newSpotifyAuthInfo.put("expires_at", authorizationCodeCredentials.getExpiresIn());
            newSpotifyAuthInfo.put("refresh_token", authorizationCodeCredentials.getRefreshToken());

            return GetSpotifyApiWithRefreshedAuthInfo();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    public void refreshStoredCacheJson(JsonCacheModel newJsonCache) {
        try {
            var newJsonStoredCache = new JSONObject();

            newJsonStoredCache.put("access_token", newJsonCache.getAccessToken());
            newJsonStoredCache.put("token_type", newJsonCache.getTokenType());
            newJsonStoredCache.put("expires_in", newJsonCache.getExpiresIn());
            newJsonStoredCache.put("scope", newJsonCache.getScope());
            newJsonStoredCache.put("expires_at", newJsonCache.getExpiresAt());
            newJsonStoredCache.put("refresh_token", newJsonCache.getRefreshToken());

            var fileToWrite = new FileWriter(jsonCachePathInfo.getCacheJsonPath());
            fileToWrite.write(newJsonStoredCache.toJSONString());

            System.out.println("Successfully Copied JSON Object to File cache.json");

            fileToWrite.flush();
            fileToWrite.close();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static JsonCacheModel getJsonCacheModel(AuthorizationCodeCredentials authorizationCodeCredentials) {
        var newJsonCache = new JsonCacheModel();

        newJsonCache.setAccessToken(authorizationCodeCredentials.getAccessToken());
        newJsonCache.setTokenType(authorizationCodeCredentials.getTokenType());
        newJsonCache.setExpiresIn(Integer.toUnsignedLong(authorizationCodeCredentials.getExpiresIn()));
        newJsonCache.setScope(authorizationCodeCredentials.getScope());
        newJsonCache.setExpiresAt(Integer.toUnsignedLong(authorizationCodeCredentials.getExpiresIn()) + 3600 * 1000); // 3600 - 1 hour when token expires, 1000 - milliseconds
        newJsonCache.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

        return newJsonCache;
    }

    private SpotifyApi GetSpotifyApiWithCurrentFromJsonCache() {
        var storedJsonCache = jsonCredentialsExtractor.getStoredJsonCache();
        var spotifyApi = new SpotifyApi.Builder()
                .setClientId(spotifyClientManager.getClientId())
                .setClientSecret(spotifyClientManager.getClientSecret())
                .setAccessToken(storedJsonCache.getAccessToken())
                .setRefreshToken(storedJsonCache.getRefreshToken())
                .build();

        return spotifyApi;
    }

    private SpotifyApi GetSpotifyApiWithRefreshedAuthInfo() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(jsonCachePathInfo.getCacheJsonPath()));

            JSONObject jsonObject = (JSONObject) obj;

            SpotifyApi freshSpotifyApiInfo = new SpotifyApi.Builder()
                    .setClientId(spotifyClientManager.getClientId())
                    .setClientSecret(spotifyClientManager.getClientSecret())
                    .setAccessToken((String) jsonObject.get("access_token"))
                    .setRefreshToken((String) jsonObject.get("refresh_token"))
                    .build();

            System.out.println("Fresh SpotifyApi object was created successfully");

            return freshSpotifyApiInfo;
        } catch (org.json.simple.parser.ParseException | IOException e) {
            System.out.println("Error! Cannot refresh spotify api info file " + e.getMessage());
        }

        return null;
    }
}
