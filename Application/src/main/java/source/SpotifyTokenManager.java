package source;


import java.io.*;

import models.Tokens;
import models.ResourcePaths;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
public class SpotifyTokenManager {
    private final ResourcePaths resourcePaths = new ResourcePaths();
    public Tokens getStoredJsonCache() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(resourcePaths.getCacheJsonPath()));
            JSONObject jsonObject =  (JSONObject) obj;

            String accessToken = (String) jsonObject.get("access_token");
            String tokenType = (String) jsonObject.get("token_type"); // usually Bearer
            Long expires_in = (Long) jsonObject.get("expires_in"); // usually 3600 seconds
            String scope = (String) jsonObject.get("scope");
            Long expires_at = (Long) jsonObject.get("expires_at");
            String refreshToken = (String) jsonObject.get("refresh_token");

            System.out.println("Json object was deconstructed successfully");

            var jsonModel = new Tokens();
            jsonModel.setAccessToken(accessToken);
            jsonModel.setTokenType(tokenType);
            jsonModel.setRefreshToken(refreshToken);
            jsonModel.setScope(scope);
            jsonModel.setExpiresAt(expires_at);
            jsonModel.setExpiresIn(expires_in);

            return jsonModel;
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

    public void storeNewCacheJson(Tokens newJsonCache) {
        try {
            var newJsonStoredCache = new JSONObject();

            newJsonStoredCache.put("access_token", newJsonCache.getAccessToken());
            newJsonStoredCache.put("token_type", newJsonCache.getTokenType());
            newJsonStoredCache.put("expires_in", newJsonCache.getExpiresIn());
            newJsonStoredCache.put("scope", newJsonCache.getScope());
            newJsonStoredCache.put("expires_at", newJsonCache.getExpiresAt());
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
