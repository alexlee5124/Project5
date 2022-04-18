import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        try {
            serverSocket = new ServerSocket(4242);
            System.out.println("Server established");
        } catch (IOException e) {
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
        } catch (IOException e) {
            System.out.println("TEST POINT 1");
            e.printStackTrace();
        }
        //////////////////////////////////////////////////////////////////
        assert reader != null;
        assert writer != null;
        int initialResponse = Integer.parseInt(reader.readLine());
        Account newAccount = null;
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
                    newAccount = new Student(newAccount.getUsername(), true);
                    writer.println("student");
                    writer.flush();
                } else if (accountType.equals("teacher")) {
                    newAccount = new Teacher(newAccount.getUsername(), true);
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

        assert newAccount != null;
        if (newAccount.isLogged()) {
            System.out.println("TEST POINT 2");
            if (newAccount instanceof Teacher teacher) {
                System.out.println("TEST POINT 3");
                int option;
                do {
                    option = Integer.parseInt(reader.readLine());
                    switch (option) {
                        case 1:
                            int randomQuiz = Integer.parseInt(reader.readLine());
                            boolean flagError = false;
                            int numberQuestions = Integer.parseInt(reader.readLine());
                            LocalDateTime deadline = null;
                            int duration = 0;
                            switch (randomQuiz) {
                                case 1:
                                    do {
                                        String deadlineString = reader.readLine();
                                        try {
                                            DateTimeFormatter deadlineFormat =
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                            deadline = LocalDateTime.parse(deadlineString, deadlineFormat);
                                        } catch (Exception e) {
                                            System.out.println("Invalid deadline response.");
                                            System.out.println("Prompting client again.");
                                            flagError = true;
                                            writer.println("T");
                                            writer.flush();
                                        }
                                    } while (flagError);
                                    writer.println("F");
                                    writer.flush();

                                    duration = Integer.parseInt(reader.readLine());
                                    teacher.createRandom(numberQuestions, deadline, duration);
                                    writer.println("F");
                                    writer.flush();
                                    break;
                                case 2:
                                    numberQuestions = Integer.parseInt(reader.readLine());
                                    int[] questionIndex = new int[numberQuestions];
                                    for (int i = 0; i < numberQuestions; i++) {

                                        int index = Integer.parseInt(reader.readLine());
                                        questionIndex[i] = index;
                                    }
                                    do {
                                        String deadlineString = reader.readLine();
                                        try {
                                            DateTimeFormatter deadlineFormat =
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                            deadline = LocalDateTime.parse(deadlineString, deadlineFormat);
                                        } catch (Exception e) {
                                            System.out.println("Invalid deadline response.");
                                            System.out.println("Prompting client again.");
                                            flagError = true;
                                            writer.println("T");
                                            writer.flush();
                                        }
                                    } while (flagError);
                                    writer.println("F");
                                    writer.flush();

                                    duration = Integer.parseInt(reader.readLine());
                                    teacher.createCustom(numberQuestions, deadline, duration, questionIndex);
                                    writer.println("F");
                                    writer.flush();
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 2:
                            System.out.println("Deleting quiz...");
                            int quizID = Integer.parseInt(reader.readLine());
                            System.out.printf("TEST POINT 5 QUIZ ID %d\n", quizID);
                            boolean deleted = teacher.deleteQuiz(quizID);
                            if (deleted) {
                                System.out.println("Quiz deleted.");
                                writer.println("Success");
                                writer.flush();
                            } else {
                                System.out.println("Quiz doesn't exist.");
                                writer.println("Failure");
                                writer.flush();
                            }
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            System.out.println("Modifying question pool...");
                            int questionPoolMod = Integer.parseInt(reader.readLine());
                            switch (questionPoolMod) {
                                case 1:
                                    System.out.println("Adding a question...");
                                    int questionTypeOption = Integer.parseInt(reader.readLine());
                                    switch (questionTypeOption) {
                                        case 1:
                                            System.out.println("Adding a multiple choice question...");
                                            String prompt = reader.readLine();
                                            int multipleChoiceOptions = Integer.parseInt(reader.readLine());
                                            String[] optionPrompts = new String[multipleChoiceOptions];
                                            for (int i = 0; i < multipleChoiceOptions; i++) {
                                                String optionPrompt = reader.readLine();
                                                optionPrompts[i] = optionPrompt;
                                            }
                                            String answer = reader.readLine();
                                            teacher.addMultipleQuestion(prompt, answer, optionPrompts);
                                            writer.println("Success");
                                            writer.flush();
                                            break;
                                        case 2:
                                            System.out.println("Adding a response question...");
                                            prompt = reader.readLine();
                                            answer = reader.readLine();
                                            teacher.addResponseQuestion(prompt, answer);
                                            writer.println("Success");
                                            writer.flush();
                                            break;
                                        case 3:
                                            System.out.println("Adding a true/false question...");
                                            prompt = reader.readLine();
                                            answer = reader.readLine();
                                            teacher.addResponseQuestion(prompt, answer);
                                            writer.println("Success");
                                            writer.flush();
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 6:
                            break;
                        case 7:
                            break;
                        case 8:
                            break;
                        default:
                            break;
                    }
                } while (option != 8);

            }
        }
    }





    }
