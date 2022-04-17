import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Server program responsible for receiving input from client and processing data
 *
 * CS1800 Spring 2022, Project 4
 *
 * @author Alex Lee, Quinn Bello
 * @version 4/17/2022
 */

public class Server {
    static Tools tools = new Tools();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Scanner scan = new Scanner(System.in);
        try
        {
            serverSocket = new ServerSocket(4242);
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
        try {
            assert serverSocket != null;
            socket = serverSocket.accept();
            System.out.println("Client connected!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e)
        {
            System.out.println("TEST POINT 1");
            e.printStackTrace();
        }
        //////////////////////////////////////////////////////////////////
        assert reader != null;
        assert writer != null;
        int initialResponse = Integer.parseInt(reader.readLine());
        Account newAccount;
        switch (initialResponse) {
            case 1:
                System.out.println("Creating account...");
                boolean created = false;
                do {
                    String username = reader.readLine();
                    newAccount = new Account(username, false);

                    int accountType = Integer.parseInt(reader.readLine());
                    if (accountType == 1) {
                        newAccount = new Teacher(newAccount.getUsername(), false);
                    } else if (accountType == 2) {
                        newAccount = new Student(newAccount.getUsername(), false);
                    }
                    created = newAccount.createAccount();
                    if (!created) {
                        writer.println("F");
                        writer.flush();
                    }
                } while (!created);
                System.out.println("Account successfully created");
                writer.println("T");
                writer.flush();
                break;
            case 2:
                System.out.println("Logging in...");
                String accountType = "";
                do {
                    String username = reader.readLine();
                    newAccount = new Account(username, false);
                    accountType = newAccount.logIn();
                    if (!newAccount.isLogged()) {
                        writer.println("F");
                        writer.flush();
                    }
                } while (!newAccount.isLogged());
                writer.println("T");
                writer.flush();
                if (accountType.equals("student")) {
                    writer.println("student");
                    writer.flush();
                } else if (accountType.equals("teacher")) {
                    writer.println("teacher");
                    writer.flush();
                }
                break;
            case 3:
                System.out.println("Exiting...");
                break;
            default:
                 break;
        }




    }
}
