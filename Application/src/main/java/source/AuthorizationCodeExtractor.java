package source;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import models.JsonCachePathInfo;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class AuthorizationCodeExtractor {
    public void StartHttpExchange() throws IOException {
        // Create HTTP server listening on localhost:5000
        HttpServer server = HttpServer.create(new InetSocketAddress(5000), 0);
        server.createContext("/redirect", new RedirectHandler());
        server.setExecutor(null); // Use default executor
        server.start();
        System.out.println("Server started. Listening on port 5000.");
    }

    static class RedirectHandler implements HttpHandler {
        private final AuthorizationCodeManager authorizationCodeManager = new AuthorizationCodeManager();
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Extract authorization code from query parameters
            String query = exchange.getRequestURI().getQuery();
            System.out.println("Query: " + query);
            String authorizationCode = null;
            if (query != null) {
                Scanner scanner = new Scanner(query);
                scanner.useDelimiter("&");
                while (scanner.hasNext()) {
                    String[] pair = scanner.next().split("=");
                    if (pair.length == 2 && pair[0].equals("code")) {
                        authorizationCode = pair[1];
                        break;
                    }
                }
                scanner.close();
            }

            // Handle authorization code (e.g., store it for later use)
            if (authorizationCode != null) {
                System.out.println("Received authorization code: " + authorizationCode);

                authorizationCodeManager.StoreAuthorizationCodeToJson(authorizationCode);


                // You can now use the authorization code to obtain an access token and refresh token
                // (This would typically involve making a request to the Spotify API's token endpoint)
            } else {
                System.err.println("No authorization code received.");
            }

            // Respond with a simple HTML page indicating success
            String response = "<html><body><h1>Authorization code received successfully!</h1></body></html>";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }
}
