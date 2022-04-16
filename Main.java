import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

/**
 * Main program for interacting with user and testing test cases
 *
 * CS1800 Spring 2022, Project 4
 *
 * @author Alex Lee
 * @version 4/5/2022
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Tools tools = new Tools();
        Scanner scan = new Scanner(System.in);
        int initialResponse = 0;
        Account newAccount = new Account();
        System.out.println("What would you like to do?\n1. Create account\n2. Log in\n3. Exit");
        initialResponse = tools.receiveValidInt(1, 3, scan);

        switch ( initialResponse ) {
            case 1:
                boolean created;
                do {
                    System.out.println("What do you want your username to be?");
                    String username = scan.nextLine();
                    newAccount = new Account(username, false);
                    // Ask user if they are a teacher or student
                    System.out.println("Are you a teacher or a student?\n1. Teacher\n2. Student");
                    String accountType = scan.nextLine();
                    if (accountType.equals("1")) {
                        newAccount = new Teacher(newAccount.getUsername(), false);
                    } else if (accountType.equals("2")) {
                        newAccount = new Student(newAccount.getUsername(), false);
                    }
                    created = newAccount.createAccount();
                } while (!created);
                System.out.println("Have a nice day!");
                break;
            case 2:
                String accountType;
                do {
                    System.out.println("What is your username?");
                    String username = scan.nextLine();
                    newAccount = new Account(username, false);
                    accountType = newAccount.logIn();
                } while (!newAccount.isLogged());
                if (accountType.equals("student")) {
                    newAccount = new Student(newAccount.getUsername(), true);
                } else if (accountType.equals("teacher")) {
                    newAccount = new Teacher(newAccount.getUsername(), true);
                }
                break;
            case 3:
                System.out.println("Have a nice day!");
                break;
            default:
                break;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////
        if ( newAccount.isLogged() ) {
            if (newAccount instanceof Teacher teacher) {
                int option;

                do {
                    System.out.println("What would you like to do?\n" +
                            "1. Create quiz\n2. Delete quiz\n3. Modify quiz\n4. View student submission\n" +
                            "5. Edit question pool\n6. Modify account" +
                            "\n7. Delete account\n8. Exit");
                    option = tools.receiveValidInt(1, 8, scan);
                    switch (option) {
                        case 1:
                            int randomQuiz;
                            System.out.println("Would you like to create a randomized quiz?\n1. Yes\n2. No");
                            randomQuiz = tools.receiveValidInt(1, 2, scan);

                            boolean flagError = false;
                            int numberQuestions = 0;
                            LocalDateTime deadline = null;
                            int duration = 0;

                            switch (randomQuiz) {
                                case 1:
                                    System.out.println("How many questions would " +
                                            "you like this quiz to have?");
                                    numberQuestions = tools.receiveValidInt(0, scan);
                                    do {
                                        flagError = false;
                                        System.out.println("When will be the deadline for this quiz" +
                                                " (YYYY-MM-DD HH:MM)?");
                                        String deadlineString = scan.nextLine();
                                        try {
                                            DateTimeFormatter deadlineFormat =
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                            deadline = LocalDateTime.parse(deadlineString, deadlineFormat);
                                        } catch (Exception e) {
                                            System.out.println("Please enter a valid response!");
                                            flagError = true;
                                        }
                                    } while (flagError);
                                    System.out.println("What will be the duration of this quiz (minutes)?");
                                    duration = tools.receiveValidInt(0, scan);

                                    teacher.createRandom(numberQuestions, deadline, duration);
                                    break;
                                case 2:
                                    System.out.println("How many questions would you like this quiz to have?");
                                    numberQuestions = tools.receiveValidInt(0, scan);

                                    int[] questionIndex = new int[numberQuestions];
                                    for (int i = 0; i < numberQuestions; i++) {
                                        System.out.println("Which questions from" +
                                                " the pool would you like to add?");
                                        int index = tools.receiveValidInt(1, scan);
                                        questionIndex[i] = index;
                                    }
                                    do {
                                        flagError = false;
                                        System.out.println("When will be the deadline for this quiz" +
                                                " (YYYY-MM-DD HH:MM)?");
                                        String deadlineString = scan.nextLine();
                                        try {
                                            DateTimeFormatter deadlineFormat =
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                            deadline = LocalDateTime.parse(deadlineString, deadlineFormat);
                                        } catch (Exception e) {
                                            System.out.println("Please enter valid responses!");
                                            flagError = true;
                                        }
                                    } while (flagError);
                                    System.out.println("What will be the duration" +
                                            " of this quiz (minutes)?");
                                    duration = tools.receiveValidInt(0, scan);
                                    teacher.createCustom(numberQuestions, deadline, duration, questionIndex);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 2:
                            int quizID = 0;
                            System.out.println("Which quiz would you like to delete?");
                            quizID = tools.receiveValidInt(1, scan);
                            teacher.deleteQuiz(quizID);
                            break;
                        case 3:
                            quizID = 0;
                            Quiz quiz;
                            do {
                                System.out.println("Which quiz would you like to modify?");
                                quizID = tools.receiveValidInt(1, scan);
                                quiz = teacher.findQuiz(quizID);
                                if (quiz != null) {
                                    int modifyQuizOption = 0;
                                    System.out.printf("Quiz %d:\n", quizID);
                                    System.out.println("What would you like to do?\n1. Modify questions\n" +
                                            "2.Modify point values\n3. Modify deadline\n" +
                                            "4. Modify duration");
                                    modifyQuizOption = tools.receiveValidInt(1, 5, scan);
                                    switch (modifyQuizOption) {
                                        case 1:
                                            int[] newQuestionIndexes = new int[quiz.getNumberQuestions()];
                                            String questionIndexString = "";
                                            for (int i = 0 ; i < quiz.getNumberQuestions() ; i++) {
                                                questionIndexString += String.valueOf(quiz.getQuestionsIndex()[i]);
                                            }
                                            System.out.printf("Current question indexes: %s\n",
                                                    questionIndexString);
                                            System.out.printf("Current question pool size: %d\n",
                                                    teacher.getQuestionPoolSize());
                                            System.out.println("What will the new questions indexes be?");
                                            for (int i = 0 ; i < quiz.getNumberQuestions() ; i++) {
                                                System.out.printf("New question %d index:", i + 1);
                                                int newIndex = tools.receiveValidInt(1,
                                                        teacher.getQuestionPoolSize(), scan);
                                                newQuestionIndexes[i] = newIndex;
                                            }
                                            quiz.setQuestionsIndex(newQuestionIndexes);
                                            System.out.println("Question indexes successfully updated!");
                                            break;
                                        case 2:
                                            int[] questionPoints = quiz.getQuestionPoints();
                                            for (int i = 0 ; i < questionPoints.length ; i++) {
                                                System.out.printf("The current point value for question %d: %d\n",
                                                        i + 1, questionPoints[i]);
                                                System.out.printf("What would you like the new point value for " +
                                                        "question %d to be?\n", i + 1);
                                                int newPoint = tools.receiveValidInt(1, scan);
                                                questionPoints[i] = newPoint;
                                            }
                                            quiz.setQuestionPoints(questionPoints);
                                            System.out.println("Question points modified!");
                                            break;
                                        case 3:
                                            System.out.printf("Current deadline: %s\n", quiz.getDeadline());
                                            do {
                                                flagError = false;
                                                System.out.println("What would you like the new deadline to be?" +
                                                        "(YYYY-MM-DD HH:SS");
                                                String newDeadline = scan.nextLine();
                                                try {
                                                    DateTimeFormatter deadlineFormat =
                                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                                    deadline = LocalDateTime.parse(newDeadline, deadlineFormat);
                                                    quiz.setDeadline(deadline);
                                                    System.out.println("Deadline modified");
                                                } catch (Exception e) {
                                                    System.out.println("Please enter valid responses!");
                                                    flagError = true;
                                                }
                                            } while (flagError);
                                            break;
                                        case 4:
                                            System.out.printf("Current duration: %d minutes\n", quiz.getDuration());
                                            System.out.println("What would you like the new duration to be?");
                                            duration = tools.receiveValidInt(0, scan);
                                            quiz.setDuration(duration);
                                            System.out.println("Duration modified!");
                                            break;
                                        default:
                                            break;
                                    }
                                } else {
                                    System.out.println("This quiz doesn't exist!");
                                }
                            } while (quiz == null);

                        case 4:
                            String studentUsername;
                            System.out.println("Enter the username of the student:");
                            studentUsername = scan.nextLine();
                            System.out.println("Which quiz would you like to see?");
                            int wantedID = tools.receiveValidInt(1, scan);

                            String[] studentGradeAndTime = teacher.loadGrade(studentUsername, wantedID);
                            if (studentGradeAndTime != null) {
                                System.out.printf("The quiz grade for this student is: %s\nTaken at: %s\n",
                                        studentGradeAndTime[0], studentGradeAndTime[1]);
                            }
                            break;
                        case 5:
                            System.out.println("What would you like to do?\n1. Add question\n2. Delete question\n" +
                                    "3. Modify question");
                            int questionPoolMod = tools.receiveValidInt(1, 3, scan);
                            switch (questionPoolMod) {
                                case 1:
                                    String questionType = "";
                                    int questionTypeOption = 0;
                                    System.out.println("What is the question type?\n1. Multiple choice\n" +
                                            "2. Response\n3. True/False");
                                    questionTypeOption = tools.receiveValidInt(1, 3, scan);
                                    switch (questionTypeOption) {
                                        case 1:
                                            System.out.println("What is the question prompt?");
                                            String prompt = scan.nextLine();
                                            int multipleChoiceOptions;
                                            System.out.println("How many options will this question have (min. 2)?");
                                            multipleChoiceOptions = tools.receiveValidInt(2, scan);
                                            String[] optionPrompts = new String[multipleChoiceOptions];
                                            for ( int i = 0 ; i < multipleChoiceOptions ; i++) {
                                                System.out.printf("What will be the option %d?\n", i + 1);
                                                String optionPrompt = scan.nextLine();
                                                optionPrompts[i] = optionPrompt;
                                            }
                                            System.out.printf("What is the question answer (1 - %d)?\n",
                                                    multipleChoiceOptions);
                                            String answer = scan.nextLine();
                                            teacher.addMultipleQuestion(prompt, answer, optionPrompts);
                                            break;
                                        case 2:
                                            System.out.println("What is the question prompt?");
                                            prompt = scan.nextLine();
                                            System.out.println("What is the question answer?");
                                            answer = scan.nextLine();
                                            teacher.addResponseQuestion(prompt, answer);
                                            break;
                                        case 3:
                                            System.out.println("What is the question prompt?");
                                            prompt = scan.nextLine();
                                            prompt = prompt + ": T or F";
                                            System.out.println("What is the question answer (T/F)?");
                                            answer = tools.receiveValidString(new String[]{"T", "F"}, scan);
                                            teacher.addResponseQuestion(prompt, answer);
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case 2:
                                    System.out.println("Which question number would you like to delete?");
                                    int questionNumber = tools.receiveValidInt(0, scan);
                                    ((Teacher) newAccount).deleteQuestion(questionNumber);
                                    break;
                                case 3:
                                    System.out.println("Which question number would you like to modify?");
                                    questionNumber = tools.receiveValidInt(0, scan);

                                    System.out.println("What would you like to do?\n" +
                                            "1. Modify prompt\n2. Modify answer\n");
                                    int modifyOption = tools.receiveValidInt(1, 2, scan);
                                    switch (modifyOption) {
                                        case 1:
                                            System.out.println("What is the new prompt?");
                                            String newPrompt = scan.nextLine();
                                            ((Teacher) newAccount).modifyQuestionPrompt(questionNumber, newPrompt);
                                            break;
                                        case 2:
                                            System.out.println("What is the new answer?");
                                            String newAnswer = scan.nextLine();
                                            ((Teacher) newAccount).modifyQuestionAnswer(questionNumber, newAnswer);
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
                            boolean modified;
                            do {
                                System.out.println("Enter a new username");
                                String newUsername = scan.nextLine();
                                modified = teacher.modifyAccount(newUsername);
                            } while (!modified);
                            break;
                        case 7:
                            newAccount.deleteAccount();
                            System.out.println("Have a nice day!");
                            break;
                        case 8:
                            System.out.println("Have a nice day!");
                            break;
                        default:
                            break;
                    }
                } while(option != 8);
                ////////////////////////////////////////////////////////////////////////////////////////////////////
            } else if ( newAccount instanceof Student ) {
                Student student = (Student) newAccount;
                int option = 0;
                boolean flagError = false;
                do {
                    System.out.println("What would you like to do?\n" +
                            "1. Take quiz\n2. View quiz submissions\n3. Modify account\n4. Delete account\n" +
                            "5. Exit");
                    option = tools.receiveValidInt(1, 5, scan);
                    switch(option) {
                        case 1:
                            int quizID = 0;
                            Quiz quiz;
                            System.out.println("Which quiz would you like to take?");
                            quizID = tools.receiveValidInt(1, scan);
                            quiz = student.loadQuiz(quizID);

                            if ( quiz != null) {
                                String[] responses = student.takeQuiz(quiz);
                                float grade = quiz.gradeQuiz(responses);
                                LocalDateTime timestamp = LocalDateTime.now();
                                student.recordGrade(grade, quizID, timestamp);
                            }
                            break;
                        case 2:
                            quizID = 0;
                            System.out.println("Which quiz grade would you like to view?");
                            quizID = tools.receiveValidInt(1, scan);
                            String quizGrade = student.loadGrade(quizID);
                            if ( quizGrade == null ) {
                                System.out.println("You have not taken this quiz yet!");
                            } else {
                                System.out.printf("Grade: %s\n", quizGrade);
                            }
                            break;
                        case 3:
                            boolean modified;
                            do {
                                System.out.println("Enter the new username");
                                String newUsername = scan.nextLine();
                                modified = newAccount.modifyAccount(newUsername);
                            } while (!modified);
                            break;
                        case 4:
                            newAccount.deleteAccount();
                            System.out.println("Have a nice day!");
                            option = 5;
                            break;
                        case 5:
                            System.out.println("Have a nice day!");
                            break;
                        default:
                            break;
                    }
                } while ( option != 5);
            }
        }
        }

    }

