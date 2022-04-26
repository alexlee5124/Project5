import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
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
            e.printStackTrace();
        }
        //////////////////////////////////////////////////////////////////
        assert reader != null;
        assert writer != null;
        int initialResponse = 0;
        Account newAccount = new Account();
        boolean created = false;
        do {
            initialResponse = Integer.parseInt(reader.readLine());
            switch (initialResponse) {
                case 1:
                    System.out.println("Creating account...");
                    created = false;
                    String username = reader.readLine();
                    System.out.printf("TEST USERNAME : %s", username);
                    newAccount = new Account(username, false);

                    int accountType = Integer.parseInt(reader.readLine());
                    System.out.printf("TEST ACCOUNT TYPE %d", accountType);
                    if (accountType == 0) {
                        newAccount = new Teacher(newAccount.getUsername(), false);
                    } else if (accountType == 1) {
                        newAccount = new Student(newAccount.getUsername(), false);
                    }
                    created = newAccount.createAccount();
                    if (!created) {
                        writer.println("F");
                        writer.flush();
                    } else {
                        System.out.println("Account successfully created");
                        writer.println("T");
                        writer.flush();
                    }
                    break;
                case 2:
                    System.out.println("Logging in...");
                    String accountTypeStr = "";
                    username = reader.readLine();
                    System.out.printf("TEST LOG IN USERNAME %s", username);
                    newAccount = new Account(username, false);
                    accountTypeStr = newAccount.logIn();
                    if (!newAccount.isLogged()) {
                        writer.println("F");
                        writer.flush();
                    } else {
                        writer.println("T");
                        writer.flush();
                    }
                    if (accountTypeStr.equals("student")) {
                        newAccount = new Student(newAccount.getUsername(), true);
                        writer.println("student");
                        writer.flush();
                    } else if (accountTypeStr.equals("teacher")) {
                        newAccount = new Teacher(newAccount.getUsername(), true);
                        writer.println("teacher");
                        writer.flush();
                    } else if (accountTypeStr.equals("false")) {
                        writer.println("false");
                        writer.flush();
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    break;
            }
        } while (initialResponse != 3 && !Objects.requireNonNull(newAccount).isLogged());

        if (newAccount.isLogged()) {
            if (newAccount instanceof Teacher) {
                Teacher teacher = new Teacher(newAccount.getUsername(), true);
                int option;
                do {
                    option = Integer.parseInt(reader.readLine());
                    switch (option) {
                        case 1:
                            int randomQuiz = Integer.parseInt(reader.readLine());
                            boolean flagError = false;
                            int numberQuestions;
                            LocalDateTime deadline = null;
                            int duration = 0;
                            switch (randomQuiz) {
                                case 1:
                                    numberQuestions = Integer.parseInt(reader.readLine());
                                    do {
                                        flagError = false;
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
                                    writer.println("Success");
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
                                        flagError = false;
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
                                    writer.println("Success");
                                    writer.flush();
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 2:
                            System.out.println("Deleting quiz...");
                            int quizID = Integer.parseInt(reader.readLine());
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
                            Quiz quiz;
                            do {
                                flagError = false;
                                quizID = Integer.parseInt(reader.readLine());
                                quiz = teacher.findQuiz(quizID);
                                if (quiz == null) {
                                    writer.println("Failure");
                                    writer.flush();
                                } else {
                                    writer.println("Success");
                                    writer.flush();

                                    int modifyQuizOption = Integer.parseInt(reader.readLine());
                                    switch (modifyQuizOption) {
                                        case 1:
                                            System.out.println("Modifying question indexes...");
                                            int[] newQuestionIndexes = new int[quiz.getNumberQuestions()];
                                            for (int i = 0; i < quiz.getNumberQuestions(); i++) {
                                                int newIndex = Integer.parseInt(reader.readLine());
                                                newQuestionIndexes[i] = newIndex;
                                            }
                                            quiz.setQuestionsIndex(newQuestionIndexes);
                                            teacher.overwriteQuiz(quiz.getQuizID(), quiz);
                                            writer.println("Success");
                                            writer.flush();
                                            break;
                                        case 2:
                                            System.out.println("Modifying question point values...");
                                            int[] questionPoints = quiz.getQuestionPoints();
                                            for (int i = 0; i < questionPoints.length; i++) {
                                                int newPoint = Integer.parseInt(reader.readLine());
                                                questionPoints[i] = newPoint;
                                            }
                                            quiz.setQuestionPoints(questionPoints);
                                            teacher.overwriteQuiz(quiz.getQuizID(), quiz);
                                            writer.println("Success");
                                            writer.flush();
                                            break;
                                        case 3:
                                            do {
                                                flagError = false;
                                                String newDeadline = reader.readLine();
                                                try {
                                                    DateTimeFormatter deadlineFormat =
                                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                                    deadline = LocalDateTime.parse(newDeadline, deadlineFormat);
                                                    quiz.setDeadline(deadline);
                                                    teacher.overwriteQuiz(quiz.getQuizID(), quiz);
                                                    writer.println("Success");
                                                    writer.flush();
                                                } catch (Exception e) {
                                                    System.out.println("Invalid deadline response!");
                                                    System.out.println("Prompting user again.");
                                                    writer.println("Failure");
                                                    writer.flush();
                                                    flagError = true;
                                                }
                                            } while (flagError);
                                            break;
                                        case 4:
                                            duration = Integer.parseInt(reader.readLine());
                                            quiz.setDuration(duration);
                                            teacher.overwriteQuiz(quiz.getQuizID(), quiz);
                                            writer.println("Success");
                                            writer.flush();
                                            break;
                                        case 5:
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            } while (flagError);

                            break;
                        case 4:
                            String studentUsername = reader.readLine();
                            int wantedID = Integer.parseInt(reader.readLine());
                            String[] studentGradeAndTime = teacher.loadGrade(studentUsername, wantedID);
                            if (studentGradeAndTime == null) {
                                writer.println("Failure");
                                writer.flush();
                                writer.println("Failure");
                                writer.flush();
                            } else {
                                writer.println(studentGradeAndTime[0]);
                                writer.flush();
                                writer.println(studentGradeAndTime[1]);
                                writer.flush();
                            }
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
                                    int questionNumber = Integer.parseInt(reader.readLine());
                                    teacher.deleteQuestion(questionNumber);
                                    writer.println("Success");
                                    writer.flush();
                                    break;
                                case 3:
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 6:
                            boolean modified;
                            String newUsername = reader.readLine();
                            modified = teacher.modifyAccount(newUsername);
                            if (!modified) {
                                writer.println("Failure");
                                writer.flush();
                            } else {
                                writer.println("Success");
                                writer.flush();
                            }
                            break;
                        case 7:
                            teacher.deleteAccount();
                            writer.println("Success");
                            writer.flush();
                            System.out.println("Exiting...");
                            option = 8;
                            break;
                        case 8:
                            System.out.println("Exiting...");
                            break;
                        default:
                            break;
                    }
                } while (option != 8);

            } else if (newAccount instanceof Student) {
                Student student = new Student(newAccount.getUsername(), true);
                boolean flagError = false;

                int option;
                do {
                    System.out.println("TEST POINT 1");
                    option = Integer.parseInt(reader.readLine());
                    System.out.printf("TEST OPTION : %d", option);
                    switch (option) {
                        case 1:
                            break;
                        case 2:
                            int quizID = Integer.parseInt(reader.readLine());
                            String quizGrade = student.loadGrade(quizID);
                            writer.println(quizGrade);
                            writer.flush();
                            break;
                        case 3:
                            System.out.println("Modifying account...");
                            boolean modified;
                            String newUsername = reader.readLine();
                            modified = student.modifyAccount(newUsername);
                            if (!modified) {
                                writer.println("Failure");
                                writer.flush();
                            } else {
                                writer.println("Success");
                                writer.flush();
                            }
                            break;
                        case 4:
                            student.deleteAccount();
                            writer.println("Success");
                            writer.flush();
                            System.out.println("Exiting...");
                            option = 5;
                            break;
                        case 5:
                            System.out.println("Exiting...");
                            break;
                        default:
                            break;
                    }
                } while (option != 5);
            }
        }
    }





    }
