import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        Tools tools = new Tools();

        String hostname = "localhost";
        int port = 4242;

        try {
            socket = new Socket(hostname, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("TEST POINT 2");
            e.printStackTrace();
        }
        //////////////////////////////////////////////////////////////////
        int initialResponse = 0;
        Account newAccount;
        do {
            newAccount = new Account();
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
                    initialResponse = 3;
                    break;
                case 3:
                    writer.println("3");
                    System.out.println("Have a nice day!");
                    break;
                default:
                    break;
            }
        } while (initialResponse != 3);

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

                                    if (reader.readLine().equals("Success")) {
                                        System.out.println("Random quiz created!");
                                    }
                                    break;
                                case 2:
                                    System.out.println("How many questions would you like this quiz to have?");
                                    numberQuestions = tools.receiveValidInt(0, scan);
                                    writer.println(numberQuestions);
                                    writer.flush();

                                    for (int i = 0; i < numberQuestions; i++) {
                                        System.out.printf("Question %d:\n", i + 1);
                                        System.out.println("Which question from" +
                                                " the pool would you like to add?");
                                        int index = tools.receiveValidInt(1, scan);
                                        writer.println(index);
                                        writer.flush();
                                    }

                                    do {
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

                                    if (reader.readLine().equals("Success")) {
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
                            Quiz quiz;
                            do {
                                System.out.println("Which quiz would you like to modify?");
                                quizID = tools.receiveValidInt(1, scan);
                                writer.println(quizID);
                                writer.flush();

                                quiz = teacher.findQuiz(quizID);

                                flagError = reader.readLine();

                                if (flagError.equals("Success")) {
                                    System.out.printf("Quiz %d:\n", quizID);
                                    System.out.println("What would you like to do?\n1. Modify questions\n" +
                                            "2.Modify point values\n3. Modify deadline\n" +
                                            "4. Modify duration\n5. Exit");
                                    int modifyQuizOption = tools.receiveValidInt(1, 5, scan);
                                    writer.println(modifyQuizOption);
                                    writer.flush();

                                    switch (modifyQuizOption) {
                                        case 1:
                                            int[] newQuestionIndexes = new int[quiz.getNumberQuestions()];
                                            String questionIndexString = "";
                                            for (int i = 0 ; i < quiz.getNumberQuestions() ; i++) {
                                                questionIndexString += " " +
                                                        String.valueOf(quiz.getQuestionsIndex()[i]);
                                            }
                                            System.out.printf("Current question indexes: %s\n",
                                                    questionIndexString);
                                            System.out.printf("Current question pool size: %d\n",
                                                    teacher.getQuestionPoolSize());
                                            System.out.println("What will the new question indexes be?");
                                            for (int i = 0 ; i < quiz.getNumberQuestions() ; i++) {
                                                System.out.printf("Question %d new index:", i + 1);
                                                int newIndex = tools.receiveValidInt(1,
                                                        teacher.getQuestionPoolSize(), scan);
                                                writer.println(newIndex);
                                                writer.flush();

                                                if (reader.readLine().equals("Success")) {
                                                    System.out.println("Question indexes successfully updated!");
                                                }
                                            }
                                        case 2:
                                            int[] questionPoints = quiz.getQuestionPoints();
                                            for (int i = 0 ; i < questionPoints.length ; i++) {
                                                System.out.printf("The current point value for question %d: %d\n",
                                                        i + 1, questionPoints[i]);
                                                System.out.printf("What would you like the new point value for " +
                                                        "question %d to be?\n", i + 1);
                                                int newPoint = tools.receiveValidInt(1, scan);
                                                writer.println(newPoint);
                                                writer.flush();
                                            }
                                            if (reader.readLine().equals("Success")) {
                                                System.out.println("Question points successfully modified!");
                                            }
                                            break;
                                        case 3:
                                            System.out.printf("Current deadline: %s\n", quiz.getDeadline());
                                            do {
                                                System.out.println("What would you like the new deadline to be?" +
                                                        "(YYYY-MM-DD HH:SS");
                                                String newDeadline = scan.nextLine();
                                                writer.println(newDeadline);
                                                writer.flush();
                                                flagError = reader.readLine();
                                                if (flagError.equals("Failure")) {
                                                    System.out.println("Please enter a valid deadline!");
                                                }
                                            } while (flagError.equals("Failure"));
                                            break;
                                        case 4:
                                            System.out.printf("Current duration: %d minutes\n", quiz.getDuration());
                                            System.out.println("What would you like the new duration to be?");
                                            duration = tools.receiveValidInt(0, scan);
                                            writer.println(duration);
                                            writer.flush();

                                            flagError = reader.readLine();

                                            if (flagError.equals("Success")) {
                                                System.out.println("Duration successfully modified");
                                            }
                                            break;
                                        case 5:
                                            flagError = "Success";
                                            break;
                                        default:
                                            break;
                                    }
                                } else if (flagError.equals("Failure")) {
                                    System.out.println("The quiz doesn't exist");
                                }
                            } while (flagError.equals("Failure"));
                            break;
                        case 4:
                            String studentUsername;
                            System.out.println("Enter the username of the student:");
                            studentUsername = scan.nextLine();
                            writer.println(studentUsername);
                            writer.flush();

                            System.out.println("Which quiz would you like to see?");
                            int wantedID = tools.receiveValidInt(1, scan);
                            writer.println(wantedID);
                            writer.flush();

                            String studentGrade = reader.readLine();
                            String studentTimestamp = reader.readLine();
                            if (studentGrade == null) {
                                System.out.println("This student hasn't taken this quiz yet.");
                            } else {
                                System.out.printf("The quiz grade for this student is: %s\nTaken at: %s\n",
                                        studentGrade, studentTimestamp);
                            }
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
                                            for (int i = 0; i < multipleChoiceOptions; i++) {
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
                                    System.out.println("Which question number would you like to delete?");
                                    int questionNumber = tools.receiveValidInt(0, scan);
                                    writer.println(questionNumber);
                                    writer.flush();

                                    if (reader.readLine().equals("Success")) {
                                        System.out.println("Question successfully deleted.");
                                    }
                                    break;
                                case 3:
                                    System.out.println("Which question number would you like to modify?");
                                    questionNumber = tools.receiveValidInt(0, scan);
                                    writer.println(questionNumber);
                                    writer.flush();

                                    System.out.println("What would you like to do?\n" +
                                            "1. Modify prompt\n2. Modify answer\n");
                                    int modifyOption = tools.receiveValidInt(1, 2, scan);
                                    writer.println(modifyOption);
                                    writer.flush();
                                    switch (modifyOption) {
                                        case 1:
                                            System.out.println("What is the new prompt?");
                                            String newPrompt = scan.nextLine();
                                            writer.println(newPrompt);
                                            writer.flush();
                                            break;
                                        case 2:
                                            System.out.println("What is the new answer?");
                                            String newAnswer = scan.nextLine();
                                            writer.println(newAnswer);
                                            writer.flush();
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 6:
                            String flag = "";
                            do {
                                System.out.println("Enter the new username: ");
                                String newUsername = scan.nextLine();
                                writer.println(newUsername);
                                writer.flush();

                                flag = reader.readLine();
                            } while (flag.equals("Failure"));
                            break;
                        case 7:
                            if (reader.readLine().equals("Success")) {
                                System.out.println("Account deleted.");
                                System.out.println("Have a nice day!");
                            }
                            option = 8;
                            break;
                        case 8:
                            System.out.println("Have a nice day!");
                            break;
                        default:
                            break;
                    }
                } while (option != 8);


            } else if (newAccount instanceof Student student) {
                int option = 0;
                String flag = "";
                do {
                    System.out.println("What would you like to do?\n" +
                            "1. Take quiz\n2. View quiz submissions\n3. Modify account\n4. Delete account\n" +
                            "5. Exit");
                    option = tools.receiveValidInt(1, 5, scan);
                    writer.println(option);
                    writer.flush();

                    switch (option) {
                        case 1:
                            int quizID = 0;
                            Quiz quiz;
                            System.out.println("Which quiz would you like to take?");
                            quizID = tools.receiveValidInt(1, scan);
                            writer.println(quizID);
                            writer.flush();

                            String quizGrade = reader.readLine();
                            if (quizGrade == null) {
                                System.out.println("You have not taken this quiz yet!");
                            } else {
                                System.out.printf("Grade: %s\n", quizGrade);
                            }
                            break;
                        case 2:
                            quizID = 0;
                            System.out.println("Which quiz grade would you like to view?");
                            quizID = tools.receiveValidInt(1, scan);
                            writer.println(quizID);
                            writer.flush();

                            break;
                        case 3:
                            do {
                                System.out.println("Enter the new username: ");
                                String newUsername = scan.nextLine();
                                writer.println(newUsername);
                                writer.flush();

                                flag = reader.readLine();
                            } while (flag.equals("Failure"));
                            break;
                        case 4:

                            if (reader.readLine().equals("Success")) {
                                System.out.println("Account deleted.");
                                System.out.println("Have a nice day!");
                            }
                            option = 5;
                            break;
                        case 5:
                            System.out.println("Have a nice day!");
                            break;
                        default:
                            break;
                    }
                } while (option != 5);
            }
        }

    }
}
