import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain
{
    public static void main(String[] args)
    {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(4242);
            System.out.println("Server established");
        } catch (IOException e) {
            System.out.println("Error establishing server");
            e.printStackTrace();
        }

        System.out.println("Waiting for clients to connect...");
        Socket socket = null;

        try
        {
            while (true)
            {
                socket = serverSocket.accept();
                System.out.println("Client accepted");
                ServerThread newClient = new ServerThread(socket);
                new Thread(newClient).start();
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try {
            serverSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}



