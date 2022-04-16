import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server
{
    public static void main(String[] args)
    {
        ServerSocket serverSocket = null;
        Scanner fromKeyboard = new Scanner(System.in);
        System.out.println("Enter port number");
        int portNumber = Integer.parseInt(fromKeyboard.nextLine());

        try
        {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server established");
        } catch (IOException e)
        {
            System.out.println("Error establishing server");
            e.printStackTrace();
        }

        System.out.println("Waiting for the client to connect...");
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;

        try
        {
            socket = serverSocket.accept();
            System.out.println("Client connected!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        String message = "";
        ArrayList<String> messages = new ArrayList<>();

        while (message.compareTo("exit") != 0)
        {
            try
            {
                // program waits here for client to send something
                message = reader.readLine();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            if (message.compareTo("exit") == 0) // for case exit
            {
                // do nothing, loop ends next iteration
            }
            else if (message.compareTo("show history") == 0) // for show history
            {
                String history = "";
                for (int i = 0; i < messages.size(); i++)
                {
                    // write all the stored messages to the writer
                    history += messages.get(i) + "!@#";
                }
                writer.write(history);
                writer.println();
                writer.flush();
            }
            else // all other cases:
                messages.add(message);
        }

        writer.close();

        try
        {
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
