import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * A student subclass for student accounts
 *
 * CS1800 Spring 2022, Project 4
 *
 * @author Alex Lee
 * @version 4/5/2022
 */
public class Student extends Account {
    Tools tools = new Tools();

    public Student( String username, boolean isLogged) throws FileNotFoundException {
        super( username, isLogged );
    }

    /** Retrieve quizzes from text file and save it as an array
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/10/2022
     */
    public Quiz[] retrieveQuizzes() {
        String quizFile = tools.loadTextFile("Quiz.txt");
        String[] lines = quizFile.split("/");
        Quiz[] quizzes = new Quiz[lines.length];
        for ( int i = 0 ; i < lines.length ; i++ ) {
            String[] elements = lines[i].split(":");
            int quizID = Integer.parseInt((elements[0]));
            int numberQuestions = Integer.parseInt(elements[1]);
            int[] questionPoints = new int[numberQuestions];
            for ( int j = 0 ; j < numberQuestions ; j++ ) {
                questionPoints[j] = Integer.parseInt((elements[2]).split(",")[j]);
            }
            String deadlineString = (elements[3]);
            deadlineString = String.format("%s-%s-%s %s:%s", deadlineString.split("-")[0],
                    deadlineString.split("-")[1], deadlineString.split("-")[2],
                    deadlineString.split("-")[3], deadlineString.split("-")[4]);
            DateTimeFormatter deadlineFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime deadline = LocalDateTime.parse( deadlineString, deadlineFormat );
            int duration = Integer.parseInt(elements[4]);
            int totalPoints = Integer.parseInt(elements[5]);
            int[] questionsIndex = new int[numberQuestions];
            for ( int j = 0 ; j < numberQuestions ; j++ ) {
                questionsIndex[j] = Integer.parseInt(elements[6].split(",")[j]);
            }
            Quiz quiz = new Quiz(quizID, numberQuestions, deadline,
                    duration, totalPoints, questionsIndex, questionPoints);
            quizzes[i] = quiz;
        }
        return quizzes;
    }

    /** Take desired quiz and save user response in string array
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public String[] takeQuiz( Quiz quiz ) {
        String[] responses = new String[quiz.getNumberQuestions()];
        Question[] quizQuestions = quiz.loadQuizQuestions();
        for (int i = 0; i < quizQuestions.length; i++) {
            System.out.println(quizQuestions[i].getPrompt());
            String response = scan.nextLine();
            responses[i] = response;
        }
        return responses;
    }

    /** Load desired quiz based on quiz ID
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public Quiz loadQuiz( int wantedID ) {
        Quiz[] quizzes = retrieveQuizzes();
        Quiz quiz = null;
        for ( int i = 0 ; i < quizzes.length ; i++ ) {
            if ( quizzes[i].getQuizID() == wantedID ) {
                quiz = quizzes[i];
            }
        }
        if ( quiz == null ) {
            System.out.println("The quiz doesn't exist!");
        } else {
            System.out.printf("Quiz %d:\n", wantedID);
        }
        return quiz;
    }

    /** Load the current grades from grade file and return it in the format "grade,timestamp(YYYY-MM-DD HH:SS)"
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/11/2022
     */
    public String loadGrade(int wantedID) {
        String grade = null;
        String[] timestampElements;
        String gradeFile = tools.loadTextFile("Grades.txt");
        String[] lines = gradeFile.split("/");

        boolean exists = false;
        String[] usernames = new String[lines.length];
        for ( int i = 0 ; i < lines.length ; i ++ ) {
            usernames[i] = lines[i].split("-")[0];
        }
        int studentIndex = 0;
        for ( int i = 0 ; i < usernames.length ; i++ ) {
            if ( usernames[i].equals(this.getUsername())) {
                studentIndex = i;
                exists = true;
            }
        }
        if (exists) {
            int quizIndex = 0;
            boolean quizTaken = false;
            String[] thisStudentGrades = lines[studentIndex].split("-")[1].split(",");
            for ( int i = 1 ; i < thisStudentGrades.length ; i++ ) {
                String quizID = thisStudentGrades[i].split(":")[0];
                if ( Integer.parseInt(quizID) == wantedID ) {
                    quizIndex = i;
                    quizTaken = true;
                }
            }
            if ( quizTaken ) {
                grade = (thisStudentGrades[quizIndex].split(":"))[1];
                timestampElements = ((thisStudentGrades[quizIndex].split(":"))[2]).split(" ");
                String deadlineString = String.format("%s-%s-%s %s:%s", timestampElements[0], timestampElements[1],
                        timestampElements[2], timestampElements[3], timestampElements[4]);
                System.out.printf("Quiz %d submitted %s\n", wantedID, deadlineString);
            }
        }
        return grade;
    }


    /** Output student grade for the quiz
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public void recordGrade(float grade, int wantedID, LocalDateTime timestamp) {
        String gradeFile = tools.loadTextFile("Grades.txt");
        String[] lines = gradeFile.split("/");
        boolean hasTaken = hasTaken(wantedID);
        boolean exists = false;
        if (hasTaken) {
            System.out.println("ERROR: You have already taken this quiz before!");
        } else {
            String[] usernames = new String[lines.length];
            int lineIndex = 0;
            for ( int i = 0 ; i < usernames.length ; i++) {
                usernames[i] = lines[i].split("-")[0];
            }
            for (int i = 0 ; i < usernames.length; i++) {
                if (getUsername().equals(usernames[i])) {
                    lineIndex = i;
                    exists = true;
                }
            }
            if (exists) {
                lines[lineIndex] = lines[lineIndex] + "," + String.format("%d:%.2f:%d %d %d %d %d", wantedID, grade,
                        timestamp.getYear(), timestamp.getMonthValue(), timestamp.getDayOfMonth(),
                        timestamp.getHour(), timestamp.getMinute());
                try {
                    out = new PrintWriter(new FileOutputStream("Grades.txt", false));
                    for (int i = 0; i < lines.length; i++) {
                        out.println(lines[i] + "/");
                    }
                    out.close();
                    System.out.println("Grade recorded!");
                } catch (Exception e) {
                    System.out.println("ERROR RECORDING GRADE");
                }
                System.out.println("TEST POINT 2");
            } else {
                System.out.println("TEST POINT 3");
                String[] newLines = new String[lines.length + 1];
                for (int i = 1 ; i < lines.length ; i++) {
                    newLines[i] = lines[i];
                }
                newLines[newLines.length - 1] = String.format("%s-,%d:%.2f:%d %d %d %d %d", getUsername(), wantedID,
                        grade, timestamp.getYear(), timestamp.getMonthValue(), timestamp.getDayOfMonth(),
                        timestamp.getHour(), timestamp.getMinute());
                try {
                    out = new PrintWriter(new FileOutputStream("Grades.txt", false));
                    for (int i = 0; i < newLines.length; i++) {
                        if (newLines[i] != null) {
                            out.println(newLines[i] + "/");
                        }
                    }
                    out.close();
                    System.out.println("Grade recorded!");
                } catch (Exception e) {
                    System.out.println("ERROR RECORDING GRADE");
                }
            }
        }
    }

    /** Checks to see if the student has taken the quiz before
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/16/2022
     */
    public boolean hasTaken(int wantedID) {
        boolean hasTaken = false;
        boolean userExists = false;
        String gradeFile = tools.loadTextFile("Grades.txt");
        String[] lines = gradeFile.split("/");
        System.out.printf("TEST LINES LENGTH: %d\n", lines.length);
        if (lines.length > 1) {
            String[] usernames = new String[lines.length];
            int lineIndex = 0;
            for (int i = 0 ; i < usernames.length ; i++) {
                usernames[i] = lines[i].split("-")[1];
            }
            for (int i = 0 ; i < usernames.length ; i++) {
                if (this.getUsername().equals(usernames[i])) {
                    userExists = true;
                    lineIndex = i;
                    break;
                }
            }
            if (userExists) {
                String[] quizzes = (lines[lineIndex].split("-"))[1].split(",");
                String[] takenIDs = new String[quizzes.length];
                for ( int i= 0 ; i < quizzes.length ; i++) {
                    takenIDs[i] = quizzes[i].split(":")[0];
                }
                for (int i = 0 ; i < takenIDs.length ; i++) {
                    if (wantedID == Integer.parseInt(takenIDs[i])) {
                        hasTaken = true;
                    }
                }
        }
        }

        return hasTaken;
    }
}
