import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;
public class Server 
{
    //executorService to manage the pool of worker threads created initially
    private final ExecutorService threadPool;
    public Server(int poolSize)
    {
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    //handle connection with one client
    public void handleClient(Socket clientSocket)
    {
        try(PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(),true))
        {
            toSocket.println("Hello from server "+clientSocket.getInetAddress());
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public static void main(String args[])
    {
        int port = 8010;
        int poolSize = 20;
        //create a Server object and set up the thread pool
        Server server = new Server(poolSize);
        try
        {
            //bind to port and start listening
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(70000); //socket throws SocketTimeoutException if no connections in 70seconds
            System.out.println("Server is listening on port: "+ port);
            while(true){
                Socket clientSocket = serverSocket.accept(); // wait for client connection

                //when client connects submit the task to the threadpool to handle this client
                server.threadPool.execute(()->server.handleClient(clientSocket));
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            server.threadPool.shutdown();
        }
    }
}
