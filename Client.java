import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        Tools tools = new Tools();

        String hostname = "localhost";
        int port = 4242;

        try
        {
            socket = new Socket(hostname, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e)
        {
            System.out.println("TEST POINT 2");
            e.printStackTrace();
        }
        //////////////////////////////////////////////////////////////////
        int initialResponse = 0;
        Account newAccount = new Account();
        System.out.println("What would you like to do?\n1. Create account\n2. Log in\n3. Exit");
        initialResponse = tools.receiveValidInt(1, 3, scan);

        assert writer != null;
        switch (initialResponse) {
            case 1:
                writer.println("1");
                writer.flush();
                String created = "";
                do {
                    System.out.println("What do you want your username to be?");
                    String username = scan.nextLine();
                    writer.println(username);
                    writer.flush();

                    System.out.println("Are you a teacher or a student?\n1. Teacher\n2. Student");
                    int accountType = tools.receiveValidInt(1, 2, scan);
                    writer.println(accountType);
                    writer.flush();

                    created = reader.readLine();
                    if (created.equals("F")) {
                        System.out.println("This account already exists!");
                    }
                } while (created.equals("F"));
                System.out.println("Account created!");
                System.out.println("Have a nice day!");
                break;
            case 2:
                writer.println("2");
                writer.flush();
                String accountType;
                String loggedIn;
                do {
                    System.out.println("What is your username?");
                    String username = scan.nextLine();
                    writer.println(username);
                    writer.flush();

                    loggedIn = reader.readLine();
                    if (loggedIn.equals("F")) {
                        System.out.println("This username doesn't exist!");
                    }
                } while (loggedIn.equals("F"));

                accountType = reader.readLine();
                if (accountType.equals("student")) {
                    newAccount = new Student(newAccount.getUsername(), true);
                } else if (accountType.equals("teacher")) {
                    newAccount = new Teacher(newAccount.getUsername(), true);
                }
                break;
            case 3:
                writer.println("3");
                System.out.println("Have a nice day!");
                break;
            default:
                break;
        }
        if (newAccount.isLogged()) {
            if (newAccount instanceof Teacher teacher) {
                System.out.println("What would you like to do?\n" +
                        "1. Create quiz\n2. Delete quiz\n3. Modify quiz\n4. View student submission\n" +
                        "5. Edit question pool\n6. Modify account" +
                        "\n7. Delete account\n8. Exit");
            } else if (newAccount instanceof Student student) {
                System.out.println("What would you like to do?\n" +
                        "1. Take quiz\n2. View quiz submissions\n3. Modify account\n4. Delete account\n" +
                        "5. Exit");
            }
        }

    }
}
