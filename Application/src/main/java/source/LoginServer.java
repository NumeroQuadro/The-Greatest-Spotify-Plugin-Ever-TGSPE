package source;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class LoginServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            Socket socket = serverSocket.accept();
        }
        catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
