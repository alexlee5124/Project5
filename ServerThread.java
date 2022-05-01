import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Server program responsible for receiving input from client and processing data
 *
 * CS1800 Spring 2022, Project 4
 *
 * @author Alex Lee, Quinn Bello
 * @version 4/17/2022
 */

public class ServerThread implements Runnable
{
    private Socket socket;

    public ServerThread(Socket socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        BufferedReader reader = null;
        PrintWriter writer = null;

        try {
            System.out.println("Client connected!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert reader != null;
        assert writer != null;
        int initialResponse = 0;
        Account newAccount = new Account();
        boolean created = false;

        do {
            try {
                initialResponse = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch (initialResponse) {
                case 1:
                    System.out.println("Creating account...");
                    created = false;
                    String username = null;
                    try {
                        username = reader.readLine();
                        System.out.printf("TEST USERNAME : %s", username);
                        newAccount = new Account(username, false);
                        int accountType = Integer.parseInt(reader.readLine());
                        System.out.printf("TEST ACCOUNT TYPE %d", accountType);

                        if (accountType == 0) {
                            newAccount = new Teacher(newAccount.getUsername(), false);
                        } else if (accountType == 1) {
                            newAccount = new Student(newAccount.getUsername(), false);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
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
                    try {
                        username = reader.readLine();
                        System.out.printf("TEST LOG IN USERNAME %s", username);
                        newAccount = new Account(username, false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    accountTypeStr = newAccount.logIn();
                    if (!newAccount.isLogged()) {
                        writer.println("F");
                        writer.flush();
                    } else {
                        writer.println("T");
                        writer.flush();
                    }
                    if (accountTypeStr.equals("student")) {
                        try {
                            newAccount = new Student(newAccount.getUsername(), true);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        writer.println("student");
                        writer.flush();
                    } else if (accountTypeStr.equals("teacher")) {
                        try {
                            newAccount = new Teacher(newAccount.getUsername(), true);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
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
                Teacher teacher = null;
                try {
                    teacher = new Teacher(newAccount.getUsername(), true);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                int option = 0;
                do {
                    try {
                        option = Integer.parseInt(reader.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    switch (option) {
                        case 1:
                            int randomQuiz = 0;
                            try {
                                randomQuiz = Integer.parseInt(reader.readLine());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            boolean flagError = false;
                            int numberQuestions = 0;
                            int questionValue = 0;
                            LocalDateTime deadline = null;
                            int duration = 0;
                            switch (randomQuiz) {
                                case 1:
                                    System.out.println("TEST POINT 7");
                                    do {
                                        flagError = false;
                                        String deadlineString = null;
                                        String durationString = null;
                                        String numberQuestionsString = null;
                                        String questionValueString = null;
                                        try {
                                            numberQuestionsString = reader.readLine();
                                            questionValueString = reader.readLine();
                                            deadlineString = reader.readLine();
                                            durationString = reader.readLine();
                                            System.out.println(numberQuestionsString + questionValueString + deadlineString + durationString);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            numberQuestions = Integer.parseInt(numberQuestionsString);
                                            questionValue = Integer.parseInt(questionValueString);
                                            DateTimeFormatter deadlineFormat =
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                            deadline = LocalDateTime.parse(deadlineString, deadlineFormat);
                                            duration = Integer.parseInt(durationString);
                                        } catch (Exception e) {
                                            System.out.println("Invalid response.");
                                            System.out.println("Prompting client again.");
                                            flagError = true;
                                            writer.println("T");
                                            writer.flush();
                                        }
                                    } while (flagError);
                                    writer.println("F");
                                    writer.flush();

                                    teacher.createRandom(numberQuestions, deadline, duration, questionValue);
                                    break;
                                case 2:
                                    int[] questionIndexes = new int[0];
                                    int[] questionValues = new int[0];
                                    do {
                                        flagError = false;
                                        System.out.println("TEST POINT 8");
                                        String questionIndexString = null;
                                        String deadlineString = null;
                                        String durationString = null;
                                        String customQuizQuestionValuesString = null;
                                        try {
                                            numberQuestions = Integer.parseInt(reader.readLine());
                                            questionIndexString = reader.readLine();
                                            deadlineString = reader.readLine();
                                            durationString = reader.readLine();
                                            customQuizQuestionValuesString = reader.readLine();
                                            System.out.println(numberQuestions + questionIndexString +deadlineString+
                                                    durationString + customQuizQuestionValuesString);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        String[] questionIndexesString;
                                        String[] questionValuesString;
                                        try {
                                            questionIndexesString = questionIndexString.split(",");
                                            questionValuesString = customQuizQuestionValuesString.split(",");
                                            questionIndexes = new int[questionIndexesString.length];
                                            questionValues = new int[questionIndexesString.length];
                                            for (int i = 0; i < questionIndexesString.length; i++) {
                                                questionIndexes[i] = Integer.parseInt(questionIndexesString[i]);
                                                questionValues[i] = Integer.parseInt(questionValuesString[i]);
                                            }
                                            DateTimeFormatter deadlineFormat =
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                            deadline = LocalDateTime.parse(deadlineString, deadlineFormat);
                                            duration = Integer.parseInt(durationString);
                                            teacher.createCustom(numberQuestions, deadline, duration,
                                                    questionIndexes, questionValues);
                                        } catch (Exception e) {
                                            System.out.println("Invalid response.");
                                            System.out.println("Prompting client again.");
                                            flagError = true;
                                            writer.println("T");
                                            writer.flush();
                                        }
                                    } while (flagError);
                                    writer.println("F");
                                    writer.flush();
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 2:
                            System.out.println("Deleting quiz...");
                            int quizID = 0;
                            try {
                                quizID = Integer.parseInt(reader.readLine());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
                            quizID = 0;
                            do {
                                flagError = false;
                                try {
                                    quizID = Integer.parseInt(reader.readLine());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                quiz = teacher.findQuiz(quizID);
                                if (quiz == null) {
                                    writer.println("Failure");
                                    writer.flush();
                                } else {
                                    writer.println("Success");
                                    writer.flush();

                                    int modifyQuizOption = 0;
                                    try {
                                        modifyQuizOption = Integer.parseInt(reader.readLine());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    switch (modifyQuizOption) {
                                        case 1:
                                            System.out.println("Modifying question indexes...");
                                            int[] newQuestionIndexes = new int[quiz.getNumberQuestions()];
                                            for (int i = 0; i < quiz.getNumberQuestions(); i++) {
                                                int newIndex = 0;
                                                try {
                                                    newIndex = Integer.parseInt(reader.readLine());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
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
                                                int newPoint = 0;
                                                try {
                                                    newPoint = Integer.parseInt(reader.readLine());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
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
                                                String newDeadline = null;
                                                try {
                                                    newDeadline = reader.readLine();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
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
                                            duration = 0;
                                            try {
                                                duration = Integer.parseInt(reader.readLine());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
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
                            String studentUsername = null;
                            int wantedID = 0;
                            try {
                                studentUsername = reader.readLine();
                                wantedID = Integer.parseInt(reader.readLine());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
                            int questionPoolMod = 0;
                            try {
                                questionPoolMod = Integer.parseInt(reader.readLine());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            switch (questionPoolMod) {
                                case 1:
                                    System.out.println("Adding a question...");
                                    int questionTypeOption = 0;
                                    try {
                                        questionTypeOption = Integer.parseInt(reader.readLine());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    switch (questionTypeOption) {
                                        case 1:
                                            System.out.println("Adding a multiple choice question...");
                                            String prompt = null;
                                            int multipleChoiceOptions = 0;
                                            try {
                                                prompt = reader.readLine();
                                                multipleChoiceOptions = Integer.parseInt(reader.readLine());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            String[] optionPrompts = new String[multipleChoiceOptions];
                                            for (int i = 0; i < multipleChoiceOptions; i++) {
                                                String optionPrompt = null;
                                                try {
                                                    optionPrompt = reader.readLine();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                optionPrompts[i] = optionPrompt;
                                            }
                                            String answer = null;
                                            try {
                                                answer = reader.readLine();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            teacher.addMultipleQuestion(prompt, answer, optionPrompts);
                                            writer.println("Success");
                                            writer.flush();
                                            break;
                                        case 2:
                                            System.out.println("Adding a response question...");
                                            prompt = null;
                                            answer = null;
                                            try {
                                                prompt = reader.readLine();
                                                answer = reader.readLine();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            teacher.addResponseQuestion(prompt, answer);
                                            writer.println("Success");
                                            writer.flush();
                                            break;
                                        case 3:
                                            System.out.println("Adding a true/false question...");
                                            prompt = null;
                                            answer = null;
                                            try {
                                                prompt = reader.readLine();
                                                answer = reader.readLine();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            teacher.addTrueFalseQuestion(prompt, answer);
                                            writer.println("Success");
                                            writer.flush();
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                case 2:
                                    int questionNumber = 0;
                                    try {
                                        questionNumber = Integer.parseInt(reader.readLine());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    teacher.deleteQuestion(questionNumber);
                                    break;
                                case 3:
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 6:
                            boolean modified;
                            String newUsername = null;
                            try {
                                newUsername = reader.readLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
                Student student = null;
                try {
                    student = new Student(newAccount.getUsername(), true);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                boolean flagError = false;

                int option;
                do {
                    System.out.println("TEST POINT 1");
                    option = 0;
                    try {
                        option = Integer.parseInt(reader.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.printf("TEST OPTION : %d", option);
                    switch (option) {
                        case 1:
                            int quizID = 0;
                            Quiz quiz;
                            try {
                                quizID = Integer.parseInt(reader.readLine());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            quiz = student.loadQuiz(quizID);
                            String[] responses = new String[quiz.getNumberQuestions()];
                            for (int i = 0 ; i < responses.length ; i++) {
                                try {
                                    responses[i] = reader.readLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            float grade = quiz.gradeQuiz(responses);
                            LocalDateTime timestamp = LocalDateTime.now();
                            student.recordGrade(grade, quizID, timestamp);
                            break;
                        case 2:
                            quizID = 0;
                            try {
                                quizID = Integer.parseInt(reader.readLine());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String quizGrade = student.loadGrade(quizID);
                            writer.println(quizGrade);
                            writer.flush();
                            break;
                        case 3:
                            System.out.println("Modifying account...");
                            boolean modified;
                            String newUsername = null;
                            try {
                                newUsername = reader.readLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
