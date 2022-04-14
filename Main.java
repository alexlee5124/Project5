import java.io.FileNotFoundException;
import java.time.LocalDateTime;
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

        Scanner scan = new Scanner(System.in);
        int initialResponse = 0;
        Account newAccount = new Account();
        do {
            System.out.println("What would you like to do?\n1. Create account\n2. Log in\n3. Exit");
            String initialResponseString = scan.nextLine();
            try {
                initialResponse = Integer.parseInt( initialResponseString );
            } catch (NumberFormatException nfe ) {
                initialResponse = 4;
            }

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
                    //break loop
                    initialResponse = 3;
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
                    //break loop
                    initialResponse = 3;
                    break;
                case 3:
                    System.out.println("Have a nice day!");
                    break;
                default:
                    System.out.println("Please enter a valid option!");
                    break;
            }
        } while ( initialResponse != 3);

        if ( newAccount.isLogged() ) {
            if ( newAccount instanceof Teacher ) {
                Teacher teacher = (Teacher) newAccount;
                int option;
                do {
                    System.out.println("What would you like to do?\n" +
                            "1. Create quiz\n2. Delete quiz\n3. View student submission\n" +
                            "4. Edit question pool\n5. Modify account" +
                            "\n6. Delete account\n7. Exit");
                    String optionString = scan.nextLine();
                    try {
                        option = Integer.parseInt(optionString);
                    } catch (NumberFormatException nfe) {
                        option = 10;
                    }
                    switch(option) {
                        case 1:
                            int randomQuiz;
                            do {
                                System.out.println("Would you like to create a randomized quiz?\n1. Yes\n2. No");
                                String randomQuizString = scan.nextLine();
                                try {
                                    randomQuiz = Integer.parseInt( randomQuizString );
                                } catch ( NumberFormatException nfe ) {
                                    randomQuiz = 3;
                                }
                                switch (randomQuiz) {
                                    case 1:
                                        boolean flagError = false;
                                        int numberQuestions = 0;
                                        LocalDateTime deadline = null;
                                        int duration = 0;
                                        do {
                                            flagError = false;
                                            System.out.println("How many questions would " +
                                                    "you like this quiz to have?");
                                            String numberQuestionsString = scan.nextLine();
                                            System.out.println("When will be the deadline for this quiz" +
                                                    " (YYYY-MM-DD HH:MM)?");
                                            String deadlineString = scan.nextLine();
                                            System.out.println("What will be the duration of this quiz (minutes)?");
                                            String durationString = scan.nextLine();
                                            try {
                                                numberQuestions =
                                                        Integer.parseInt(numberQuestionsString);
                                                DateTimeFormatter deadlineFormat =
                                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                                deadline = LocalDateTime.parse(deadlineString, deadlineFormat);
                                                duration = Integer.parseInt(durationString);
                                            } catch ( Exception e) {
                                                System.out.println("Please enter a valid response!");
                                                flagError = true;
                                            }
                                        } while (flagError);

                                        try {
                                            teacher.createRandom(numberQuestions,
                                                    deadline, duration);
                                        } catch ( Exception e ) {
                                            System.out.println("Error creating quiz," +
                                                    " please check your question pool!");
                                        }
                                        break;
                                    case 2:
                                        numberQuestions = 0;
                                        deadline = null;
                                        duration = 0;

                                        do {
                                            flagError = false;
                                            System.out.println("How many questions would" +
                                                    " you like this quiz to have?");
                                            String numberQuestionsString = scan.nextLine();
                                            try {
                                                numberQuestions = Integer.parseInt(numberQuestionsString);
                                            } catch ( Exception e ) {
                                                System.out.println("Please enter a valid number!");
                                                flagError = true;
                                            }
                                        } while ( flagError );

                                        int[] questionIndex;
                                        do {
                                            flagError = false;
                                            System.out.println("When will be the deadline for this quiz" +
                                                    " (YYYY-MM-DD HH:MM)?");
                                            String deadlineString = scan.nextLine();
                                            System.out.println("What will be the duration" +
                                                    " of this quiz (minutes)?");
                                            String durationString = scan.nextLine();
                                            questionIndex = new int[numberQuestions];
                                            try {
                                                for ( int i = 0 ; i < numberQuestions ; i ++ ) {
                                                    System.out.println("Which questions from" +
                                                            " the pool would you like to add?");
                                                    String indexString = scan.nextLine();
                                                    int index = Integer.parseInt(indexString);
                                                    questionIndex[i] = index;
                                                }
                                            } catch ( Exception e ) {
                                                System.out.println("Please enter a valid response!");
                                                flagError = true;
                                            }
                                            if ( !flagError ) {
                                                try {
                                                    DateTimeFormatter deadlineFormat =
                                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                                    deadline = LocalDateTime.parse(deadlineString, deadlineFormat);
                                                    duration = Integer.parseInt(durationString);
                                                } catch ( Exception e ) {
                                                    System.out.println("Please enter valid responses!");
                                                    flagError = true;
                                                }
                                            }
                                        } while ( flagError );
                                        try {
                                            teacher.createCustom( numberQuestions, deadline, duration,
                                                    questionIndex);
                                        } catch ( Exception e ) {
                                            System.out.println("Error creating quiz," +
                                                    " please check your question pool!");
                                        }
                                        break;
                                    default:
                                        System.out.println("Please enter a valid option!");
                                        break;
                                }
                            } while (randomQuiz != 1 && randomQuiz != 2);
                            break;
                        case 2:
                            boolean flagError = false;
                            int quizID = 0;
                            do {
                                System.out.println("Which quiz would you like to delete?");
                                String quizIDString = scan.nextLine();
                                try {
                                    quizID = Integer.parseInt(quizIDString);
                                } catch ( Exception e ) {
                                    System.out.println("Please enter a valid number!");
                                    flagError = true;
                                }
                            } while ( flagError );
                            teacher.deleteQuiz( quizID );
                            break;
                        case 3:
                            String studentUsername;
                            quizID = 0;
                            do {
                                flagError = false;
                                System.out.println("Enter the username of the student:");
                                studentUsername = scan.nextLine();
                                System.out.println("Which quiz would you like to see?");
                                String quizIDString = scan.nextLine();
                                try {
                                    quizID = Integer.parseInt( quizIDString);

                                } catch ( Exception e ) {
                                    System.out.println("Please enter a valid response!");
                                    flagError = true;
                                }
                            } while ( flagError );
                            String studentGrade = teacher.loadGrade( studentUsername, quizID );
                            if ( studentGrade == null ) {
                                System.out.println("The student hasn't taken this quiz yet!");
                            } else {
                                System.out.printf("The quiz grade for this student is: %s\n", studentGrade);
                            }
                            break;
                        case 4:
                            System.out.println("What would you like to do?\n1. Add question\n2. Delete question\n" +
                                    "3. Modify question");
                            String questionPoolMod = scan.nextLine();
                            if (questionPoolMod.equals("1")) {
                                System.out.println("What is the question prompt?");
                                String prompt = scan.nextLine();
                                System.out.println("What is the question answer?");
                                String answer = scan.nextLine();
                                System.out.println("What is the question point value?");
                                int pointValue = scan.nextInt();
                                scan.nextLine();
                                ((Teacher) newAccount).addQuestion(pointValue, prompt, answer);
                            } else if (questionPoolMod.equals("2")) {
                                System.out.println("Which question number would you like to delete?");
                                int questionNumber = scan.nextInt();
                                scan.nextLine();
                                ((Teacher) newAccount).deleteQuestion(questionNumber);
                            } else if (questionPoolMod.equals("3")) {
                                System.out.println("Which question number would you like to modify?");
                                int questionNumber = scan.nextInt();
                                scan.nextLine();

                                System.out.println("What would you like to do?\n" +
                                        "1. Modify prompt\n2. Modify answer\n");
                                String modifyOption = scan.nextLine();
                                switch (modifyOption) {
                                    case "1":
                                        System.out.println("What is the new prompt?");
                                        String newPrompt = scan.nextLine();
                                        ((Teacher) newAccount).modifyQuestionPrompt(questionNumber, newPrompt);
                                        break;
                                    case "2":
                                        System.out.println("What is the new answer?");
                                        String newAnswer = scan.nextLine();
                                        ((Teacher) newAccount).modifyQuestionAnswer(questionNumber, newAnswer);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                        case 5:
                            boolean modified;
                            do {
                                System.out.println("Enter the new username");
                                String newUsername = scan.nextLine();
                                modified = newAccount.modifyAccount(newUsername);
                            } while (!modified);
                            break;
                        case 6:
                            newAccount.deleteAccount();
                            System.out.println("Have a nice day!");
                            option = 7;
                            break;
                        case 7:
                            System.out.println("Have a nice day!");
                            break;
                        default:
                            System.out.println("Please enter a valid option!");
                            break;
                    }
                } while ( option != 7);
                ////////////////////////////////////////////////////////////////////////////////////////////////////
            } else if ( newAccount instanceof Student ) {
                Student student = (Student) newAccount;
                int option = 0;
                boolean flagError = false;
                do {
                    do {
                        System.out.println("What would you like to do?\n" +
                                "1. Take quiz\n2. View quiz submissions\n3. Modify account\n4. Delete account\n" +
                                "5. Exit");
                        String optionString = scan.nextLine();
                        try {
                            option = Integer.parseInt( optionString );
                        } catch ( Exception e ) {
                            System.out.println("Please enter a valid option!");
                            flagError = true;
                        }
                    } while ( flagError );

                    switch(option) {
                        case 1:
                            int quizID = 0;
                            Quiz quiz;
                            String quizIDString;
                            do {
                                System.out.println("Which quiz would you like to take?\nEnter 'exit' to exit.");
                                quizIDString = scan.nextLine();
                                if (quizIDString.equals("exit")) {
                                    break;
                                }
                                try {
                                    quizID = Integer.parseInt(quizIDString);
                                } catch (Exception e) {
                                    System.out.println("Please enter a valid number!");
                                    flagError = true;
                                }
                            } while (flagError);
                            quiz = student.loadQuiz(quizID);
                            if ( !quizIDString.equals("exit") && quiz != null) {
                                String[] responses = student.takeQuiz(quiz);
                                float grade = quiz.gradeQuiz( responses );
                                student.recordGrade( grade, student.getUsername(), quizID);
                            }
                            break;
                        case 2:
                            quizID = 0;
                            do {
                                flagError = false;
                                System.out.println("Which quiz grade would you like to view?");
                                quizIDString = scan.nextLine();
                                try {
                                    quizID = Integer.parseInt( quizIDString);
                                } catch ( Exception e ) {
                                    System.out.println("Please enter a valid response!");
                                    flagError = true;
                                }
                            } while ( flagError );
                            String quizGrade = student.loadGrade ( quizID );
                            if ( quizGrade == null ) {
                                System.out.println("You have not taken this quiz yet!");
                            } else {
                                System.out.printf("Your grade is %s\n", quizGrade);
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
                            System.out.println("Please enter a valid option!");
                            break;
                    }
                } while ( option != 5);
            }
        }
        }

    }

