package src.managers;

import models.ConnectionCredentials;
import models.ConnectionResourcePaths;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConnectionCredentialsManager {
    private final ConnectionResourcePaths resourcePaths = new ConnectionResourcePaths();
    public ConnectionCredentials GetConnectionCredentialsFromJson() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(resourcePaths.getDatabaseConnectionCredentialsPath()));
            JSONObject jsonObject =  (JSONObject) obj;

            String host = (String) jsonObject.get("POSTGRESQL_HOST");
            String port = (String) jsonObject.get("POSTGRESQL_PORT");
            String user = (String) jsonObject.get("POSTGRESQL_USER");
            String password = (String) jsonObject.get("POSTGRESQL_PASSWORD");
            String databaseName = (String) jsonObject.get("POSTGRESQL_DBNAME");

            System.out.println("Json 'database_connection.json' object was deconstructed successfully");

            var jsonModel = new ConnectionCredentials();
            jsonModel.setPOSTGRESQL_DATABASE_NAME(databaseName);
            jsonModel.setPOSTGRESQL_HOST(host);
            jsonModel.setPOSTGRESQL_PASSWORD(password);
            jsonModel.setPOSTGRESQL_PORT(port);
            jsonModel.setPOSTGRESQL_USER(user);

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
