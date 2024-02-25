package source;

import models.ResourcePaths;
import models.AuthorizationCode;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AuthorizationCodeManager {
    private final AuthorizationCode authorizationCode = new AuthorizationCode();
    private final ResourcePaths resourcePaths = new ResourcePaths();
    public void StoreAuthorizationCodeToJson(String authorizationCode) {
        var spotifyAuthorizationInfoJson = new JSONObject();

        spotifyAuthorizationInfoJson.put("auth_code", authorizationCode);

        try {
            var fileToWrite = new FileWriter(resourcePaths.getSpotifyAuthorizationInfoPath());
            fileToWrite.write(spotifyAuthorizationInfoJson.toJSONString());

            System.out.println("Successfully refresh spotify_auth.json file with new authorization code");

            fileToWrite.flush();
            fileToWrite.close();
        }

        catch (IOException e) {
            System.out.println("Error from WriteAuthorizationCode: " + e.getMessage());
        }
    }

    public AuthorizationCode GetAuthorizationCodeFromJson() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(resourcePaths.getSpotifyAuthorizationInfoPath()));
            JSONObject jsonObject =  (JSONObject) obj;

            String authorizationCode = (String) jsonObject.get("auth_code");

            System.out.println("Json 'spotify_auth.json' object was deconstructed successfully");

            var jsonModel = new AuthorizationCode();
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
