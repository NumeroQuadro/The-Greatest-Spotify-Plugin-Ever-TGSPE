package source.Managers;


import java.io.*;

import models.ResourcePaths;
import models.SpotifyCredentials;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import se.michaelthelin.spotify.SpotifyApi;

import java.io.IOException;
public class SpotifyTokenManager {
    private final SpotifyCredentials spotifyCredentials = new SpotifyCredentials();
    private final ResourcePaths resourcePaths = new ResourcePaths();
    public SpotifyApi getStoredJsonCache() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(resourcePaths.getCacheJsonPath()));
            JSONObject jsonObject =  (JSONObject) obj;

            String accessToken = (String) jsonObject.get("access_token");
            String refreshToken = (String) jsonObject.get("refresh_token");

            System.out.println("Json object was deconstructed successfully");

            var spotifyApi = new SpotifyApi.Builder()
                    .setClientId(spotifyCredentials.getClientId())
                    .setClientSecret(spotifyCredentials.getClientSecret())
                    .setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .build();

            return spotifyApi;
        }

        catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception! " + e.getCause() + e.getMessage());
        }

        return null;
    }

    public void storeNewCacheJson(SpotifyApi newJsonCache) {
        try {
            var newJsonStoredCache = new JSONObject();

            newJsonStoredCache.put("access_token", newJsonCache.getAccessToken());
            newJsonStoredCache.put("refresh_token", newJsonCache.getRefreshToken());

            var fileToWrite = new FileWriter(resourcePaths.getCacheJsonPath());
            fileToWrite.write(newJsonStoredCache.toJSONString());

            System.out.println("Successfully Copied JSON Object to File cache.json");

            fileToWrite.flush();
            fileToWrite.close();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
