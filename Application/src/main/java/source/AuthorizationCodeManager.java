package source;

import models.JsonCacheModel;
import models.JsonCachePathInfo;
import models.SpotifyAuthorizationInfo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AuthorizationCodeManager {
    private final SpotifyAuthorizationInfo spotifyAuthorizationInfo = new SpotifyAuthorizationInfo();
    private final JsonCachePathInfo jsonCachePathInfo = new JsonCachePathInfo();
    public void StoreAuthorizationCodeToJson(String authorizationCode) {
        var spotifyAuthorizationInfoJson = new JSONObject();

        spotifyAuthorizationInfoJson.put("auth_code", authorizationCode);

        try {
            var fileToWrite = new FileWriter(jsonCachePathInfo.getSpotifyAuthorizationInfoPath());
            fileToWrite.write(spotifyAuthorizationInfoJson.toJSONString());

            System.out.println("Successfully refresh spotify_auth.json file with new authorization code");

            fileToWrite.flush();
            fileToWrite.close();
        }

        catch (IOException e) {
            System.out.println("Error from WriteAuthorizationCode: " + e.getMessage());
        }
    }

    public SpotifyAuthorizationInfo GetAuthorizationCodeFromJson() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(jsonCachePathInfo.getSpotifyAuthorizationInfoPath()));
            JSONObject jsonObject =  (JSONObject) obj;

            String authorizationCode = (String) jsonObject.get("auth_code");

            System.out.println("Json 'spotify_auth.json' object was deconstructed successfully");

            var jsonModel = new SpotifyAuthorizationInfo();
            jsonModel.setAuthorizationCode(authorizationCode);

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
}
