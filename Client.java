import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;

        System.out.println("Enter the host name");
        String hostname = scan.nextLine();
        System.out.println("Enter the port number");
        int port = Integer.parseInt(scan.nextLine());

        try
        {
            socket = new Socket(hostname, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println("Enter your username");
        String username = scan.nextLine();
        String message = "";

        while (message.compareTo("exit") != 0)
        {
            System.out.println("\nEnter your message\n\n  Other Options:\n   - show history\n   - exit\n");
            message = scan.nextLine(); // get input

            if (message.compareTo("show history") == 0)
            {
                writer.write("show history");
                writer.println();
                writer.flush();
                String history = "";

                try
                {
                    System.out.println("\nMessage history to date:\n");

                    history = reader.readLine();
                    String[] messages = history.split("!@#");

                    for (int i = 0; i < messages.length; i++)
                        System.out.println(messages[i]);

                    System.out.println("\nEnd of message history");

                } catch (IOException e)
                {
                    e.printStackTrace();
                }

            } else if (message.compareTo("exit") == 0)
            {
                writer.write(message);
                writer.println();
                writer.flush();
                System.out.println("Exiting...");
            } else
            {
                writer.write(username + ": " + message);
                writer.println();
                writer.flush();
            }
        }

        System.out.println("Program exited, goodbye");
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
