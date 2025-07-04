import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {
    public Consumer<Socket> getConsumer() {
        return (clientSocket) -> {
            try (PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(), true)) {
                toSocket.println("Hello from server " + clientSocket.getInetAddress());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        };
    }
    
    public static void main(String args[]) {
        int port = 8010;
        Server server = new Server();
        
        try {

            //open server socket
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(70000); //close socket if no client connects within 70secs
            System.out.println("Server is listening on port " + port);
            while (true) {
                // a socket object is created when a client connects to the server
                Socket clientSocket = serverSocket.accept();
                
                // Create and start a new thread for each client
                //inside the thread it calls the function returned from getConsumer(), and passes the clientSocket to it
                Thread thread = new Thread(() -> server.getConsumer().accept(clientSocket));
                thread.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}