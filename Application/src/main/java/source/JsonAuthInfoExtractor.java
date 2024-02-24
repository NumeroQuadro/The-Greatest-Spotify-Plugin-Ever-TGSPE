package source;


import java.io.*;

import models.JsonCacheModel;
import models.JsonCachePathInfo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
public class JsonAuthInfoExtractor {
    private final JsonCachePathInfo jsonCachePathInfo = new JsonCachePathInfo();
    public JsonCacheModel getStoredJsonCache() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(jsonCachePathInfo.getCacheJsonPath()));
            JSONObject jsonObject =  (JSONObject) obj;

            String accessToken = (String) jsonObject.get("access_token");
            String tokenType = (String) jsonObject.get("token_type"); // usually Bearer
            Long expires_in = (Long) jsonObject.get("expires_in"); // usually 3600 seconds
            String scope = (String) jsonObject.get("scope");
            Long expires_at = (Long) jsonObject.get("expires_at");
            String refreshToken = (String) jsonObject.get("refresh_token");

            System.out.println("Json object was deconstructed successfully");

            var jsonModel = new JsonCacheModel();
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
}
