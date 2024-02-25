package source;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class AuthorizationCodeExtractor {
    private static CompletableFuture<String> authorizationCodeFuture = new CompletableFuture<>();
    public CompletableFuture<String> StartHttpExchange() throws IOException {
        // Create HTTP server listening on localhost:5000
        HttpServer server = HttpServer.create(new InetSocketAddress(5000), 0);
        server.createContext("/redirect", new RedirectHandler());
        server.setExecutor(null); // Use default executor
        server.start();
        System.out.println("Server started. Listening on port 5000.");

        return authorizationCodeFuture;
    }

    static class RedirectHandler implements HttpHandler {
        private final AuthorizationCodeManager authorizationCodeManager = new AuthorizationCodeManager();

        @Override
        public void handle(HttpExchange exchange) {
            // Submit the request handling task to be executed asynchronously
            CompletableFuture.runAsync(() -> {
                try {
                    processRequest(exchange);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        private void processRequest(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            System.out.println("Query: " + query);
            String authorizationCode = extractAuthorizationCode(query);

            if (authorizationCode != null) {
                System.out.println("Received authorization code: " + authorizationCode);
                authorizationCodeManager.StoreAuthorizationCodeToJson(authorizationCode);
                authorizationCodeFuture.complete(authorizationCode); // Complete the future with the received code
            } else {
                System.err.println("No authorization code received.");
                authorizationCodeFuture.completeExceptionally(new RuntimeException("No authorization code received."));
            }

            String response = "<html><body><h1>Authorization code received successfully!</h1></body></html>";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private String extractAuthorizationCode(String query) {
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
            return authorizationCode;
        }
    }

    public static void main(String[] args) throws IOException {
        new AuthorizationCodeExtractor().StartHttpExchange();
    }
}
