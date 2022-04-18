import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                int option;
                do {
                    System.out.println("What would you like to do?\n" +
                            "1. Create quiz\n2. Delete quiz\n3. Modify quiz\n4. View student submission\n" +
                            "5. Edit question pool\n6. Modify account" +
                            "\n7. Delete account\n8. Exit");
                    option = tools.receiveValidInt(1, 8, scan);
                    writer.println(option);
                    writer.flush();

                    switch (option) {
                        case 1:
                            int randomQuiz;
                            System.out.println("Would you like to create a randomized quiz?\n1. Yes\n2. No");
                            randomQuiz = tools.receiveValidInt(1, 2, scan);
                            writer.println(randomQuiz);
                            writer.flush();

                            String flagError = "F";
                            int numberQuestions = 0;
                            LocalDateTime deadline = null;
                            int duration = 0;

                            switch (randomQuiz) {
                                case 1:
                                    System.out.println("How many questions would " +
                                            "you like this quiz to have?");
                                    numberQuestions = tools.receiveValidInt(0, scan);
                                    writer.println(numberQuestions);
                                    writer.flush();

                                    do {
                                        flagError = "F";
                                        System.out.println("When will be the deadline for this quiz" +
                                                " (YYYY-MM-DD HH:MM)?");
                                        String deadlineString = scan.nextLine();
                                        writer.println(deadlineString);
                                        writer.flush();

                                        flagError = reader.readLine();
                                        if (flagError.equals("T")) {
                                            System.out.println("Please enter a valid response!");
                                        }
                                    } while (flagError.equals("T"));

                                    System.out.println("What will be the duration of this quiz (minutes)?");
                                    duration = tools.receiveValidInt(0, scan);
                                    writer.println(duration);
                                    writer.flush();

                                    flagError = reader.readLine();
                                    if (flagError.equals("F")) {
                                        System.out.println("Random quiz created!");
                                    }
                                    break;
                                case 2:
                                    System.out.println("How many questions would you like this quiz to have?");
                                    numberQuestions = tools.receiveValidInt(0, scan);
                                    writer.println(numberQuestions);
                                    writer.flush();

                                    int[] questionIndex = new int[numberQuestions];
                                    for (int i = 0; i < numberQuestions; i++) {
                                        System.out.println("Which questions from" +
                                                " the pool would you like to add?");
                                        int index = tools.receiveValidInt(1, scan);
                                        writer.println(index);
                                        writer.flush();
                                    }
                                    do {
                                        flagError = "F";
                                        System.out.println("When will be the deadline for this quiz" +
                                                " (YYYY-MM-DD HH:MM)?");
                                        String deadlineString = scan.nextLine();
                                        writer.println(deadlineString);
                                        writer.flush();

                                        flagError = reader.readLine();
                                        if (flagError.equals("T")) {
                                            System.out.println("Please enter a valid response");
                                        }
                                    } while (flagError.equals("T"));

                                    System.out.println("What will be the duration of this quiz (minutes)?");
                                    duration = tools.receiveValidInt(0, scan);
                                    writer.println(duration);
                                    writer.flush();

                                    flagError = reader.readLine();
                                    if (flagError.equals("F")) {
                                        System.out.println("Custom quiz created!");
                                    }
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 2:
                            int quizID = 0;
                            System.out.println("Which quiz would you like to delete?");
                            quizID = tools.receiveValidInt(1, scan);
                            writer.println(quizID);
                            writer.flush();

                            System.out.println("TEST POINT 6");
                            if (reader.readLine().equals("Success")) {
                                System.out.println("Quiz successfully deleted!");
                            } else {
                                System.out.println("The quiz doesn't exist!");
                            }
                            break;
                        case 3:
                             break;
                        case 4:
                            break;
                        case 5:
                            System.out.println("What would you like to do?\n1. Add question\n2. Delete question\n" +
                                    "3. Modify question");
                            int questionPoolMod = tools.receiveValidInt(1, 3, scan);
                            writer.println(questionPoolMod);
                            writer.flush();

                            switch (questionPoolMod) {
                                case 1:
                                    int questionTypeOption = 0;
                                    System.out.println("What is the question type?\n1. Multiple choice\n" +
                                            "2. Response\n3. True/False");
                                    questionTypeOption = tools.receiveValidInt(1, 3, scan);
                                    writer.println(questionTypeOption);
                                    writer.flush();
                                    switch (questionTypeOption) {
                                        case 1:
                                            System.out.println("What is the question prompt?");
                                            String prompt = scan.nextLine();
                                            writer.println(prompt);
                                            writer.flush();
                                            int multipleChoiceOptions;
                                            System.out.println("How many options will this question have (min. 2)?");
                                            multipleChoiceOptions = tools.receiveValidInt(2, scan);
                                            writer.println(multipleChoiceOptions);
                                            writer.flush();
                                            for ( int i = 0 ; i < multipleChoiceOptions ; i++) {
                                                System.out.printf("What will be the option %d?\n", i + 1);
                                                String optionPrompt = scan.nextLine();
                                                writer.println(optionPrompt);
                                                writer.flush();
                                            }
                                            System.out.printf("What is the question answer (1 - %d)?\n",
                                                    multipleChoiceOptions);
                                            String answer = scan.nextLine();
                                            writer.println(answer);
                                            writer.flush();
                                            if (reader.readLine().equals("Success")) {
                                                System.out.println("Multiple choice question successfully created");
                                            }
                                            break;
                                        case 2:
                                            System.out.println("What is the question prompt?");
                                            prompt = scan.nextLine();
                                            writer.println(prompt);
                                            writer.flush();
                                            System.out.println("What is the question answer?");
                                            answer = scan.nextLine();
                                            writer.println(answer);
                                            writer.flush();
                                            if (reader.readLine().equals("Success")) {
                                                System.out.println("Response question successfully created");
                                            }
                                            break;
                                        case 3:
                                            System.out.println("What is the question prompt?");
                                            prompt = scan.nextLine();
                                            prompt = prompt + ": T or F";
                                            writer.println(prompt);
                                            writer.flush();
                                            System.out.println("What is the question answer (T/F)?");
                                            answer = tools.receiveValidString(new String[]{"T", "F"}, scan);
                                            writer.println(answer);
                                            writer.flush();
                                            if (reader.readLine().equals("Success")) {
                                                System.out.println("True or False question successfully created");
                                            }
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


            } else if (newAccount instanceof Student student) {
                System.out.println("What would you like to do?\n" +
                        "1. Take quiz\n2. View quiz submissions\n3. Modify account\n4. Delete account\n" +
                        "5. Exit");
            }
        }

    }
}
