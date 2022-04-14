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

    public Student( String username, boolean isLogged) throws FileNotFoundException {
        super( username, isLogged );
    }

    /** Retrieve quizzes from text file and save it as an array
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/10/2022
     */
    public Quiz[] retrieveQuizzes() {
        ArrayList<String> quizList = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Quiz.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                quizList.add(line);
            }
        } catch (IOException ie) {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }
        Quiz[] quizzes = new Quiz[quizList.size()];
        for ( int i = 0 ; i < quizList.size() ; i++ ) {
            int quizID = Integer.parseInt((quizList.get(i).split(":"))[0]);
            int numberQuestions = Integer.parseInt((quizList.get(i).split(":"))[1]);
            int[] questionPoints = new int[numberQuestions];
            for ( int j = 0 ; j < numberQuestions ; j++ ) {
                questionPoints[j] = Integer.parseInt(((quizList.get(i).split(":"))[2]).split(",")[j]);
            }
            String deadlineString = (quizList.get(i).split(":")[3]);
            deadlineString = String.format("%s-%s-%s %s:%s", deadlineString.split("-")[0],
                    deadlineString.split("-")[1], deadlineString.split("-")[2],
                    deadlineString.split("-")[3], deadlineString.split("-")[4]);
            DateTimeFormatter deadlineFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime deadline = LocalDateTime.parse( deadlineString, deadlineFormat );
            int duration = Integer.parseInt((quizList.get(i).split(":"))[4]);
            int totalPoints = Integer.parseInt((quizList.get(i).split(":"))[5]);
            int[] questionsIndex = new int[numberQuestions];
            for ( int j = 0 ; j < numberQuestions ; j++ ) {
                questionsIndex[j] = Integer.parseInt((quizList.get(i).split(":"))[6].split(",")[j]);
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
    public Quiz loadQuiz( int QuizID ) {
        Quiz[] quizzes = retrieveQuizzes();
        Quiz quiz = null;
        for ( int i = 0 ; i < quizzes.length ; i++ ) {
            if ( quizzes[i].getQuizID() == QuizID ) {
                quiz = quizzes[i];
            }
        }
        if ( quiz == null ) {
            System.out.println("The quiz doesn't exist!");
        } else {
            System.out.printf("Quiz %d loaded!\n", QuizID);
        }
        return quiz;
    }

    /** Load the current grades from grade file and return it in the format "QUIZ ID: Grade"
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/11/2022
     */
    public String loadGrade( int QuizID) {
        String grade = null;
        ArrayList<String> grades= new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Grades.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                grades.add(line);
            }
        } catch (IOException ie) {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }

        String entireFile = "";
        for ( int i = 0 ; i < grades.size() ; i++ ) {
            entireFile += grades.get(i);
        }

        String[] allGrades = entireFile.split("/");

        String[] usernames = new String[allGrades.length];
        String[] studentsGrades = new String[allGrades.length];
        for ( int i = 0 ; i < allGrades.length ; i ++ ) {
            usernames[i] = allGrades[i].split("-")[0];
            studentsGrades[i] = allGrades[i].split("-")[1];
        }

        int studentIndex = 0;
        for ( int i = 0 ; i < usernames.length ; i++ ) {
            if ( usernames[i].equals(this.getUsername())) {
                studentIndex = i;
            }
        }

        int quizIndex = 0;
        boolean quizTaken = false;
        String[] thisStudentGrades = studentsGrades[studentIndex].split(",");
        for ( int i = 0 ; i < thisStudentGrades.length ; i++ ) {
            String quizID = thisStudentGrades[i].split(":")[0];
            if ( Integer.parseInt(quizID) == QuizID ) {
                quizIndex = i;
                quizTaken = true;
            }
        }
        if ( quizTaken ) {
            grade = (thisStudentGrades[quizIndex].split(":"))[1];
        }
        return grade;
    }

    /** Load past grades by this student
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/11/2022
     */
    public String loadPastGrade() {
        String pastGrades = null;
        ArrayList<String> grades= new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Grades.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                grades.add(line);
            }
        } catch (IOException ie) {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }

        String entireFile = "";
        for ( int i = 0 ; i < grades.size() ; i++ ) {
            entireFile += grades.get(i);
        }

        String[] allGrades = entireFile.split("/");

        String[] usernames = new String[allGrades.length];
        String[] studentsGrades = new String[allGrades.length];
        for ( int i = 0 ; i < allGrades.length ; i ++ ) {
            usernames[i] = allGrades[i].split("-")[0];
            studentsGrades[i] = allGrades[i].split("-")[1];
        }

        int studentIndex = 0;
        for ( int i = 0 ; i < usernames.length ; i++ ) {
            if ( usernames[i].equals(this.getUsername())) {
                studentIndex = i;
            }
        }
        pastGrades = studentsGrades[studentIndex];
        return pastGrades;
    }


    /** Output student grade for the quiz
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public void recordGrade( float grade, String username, int quizID) {
        ArrayList<String> grades = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Grades.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                grades.add(line);
            }
        } catch (IOException ie) {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }

        String entireFile = null;
        String[] usernames = new String[0];
        boolean studentExists = false;
        String[] allGrades = new String[0];
        if (grades.size() > 0) {
            entireFile = "";
            for (int i = 0; i < grades.size(); i++) {
                entireFile += grades.get(i);
            }
            allGrades = entireFile.split("/");
            usernames = new String[allGrades.length];
            for (int i = 0; i < allGrades.length; i++) {
                usernames[i] = allGrades[i].split("-")[0];
            }
            int studentIndex = 0;
            studentExists = false;
            for (int i = 0; i < usernames.length; i++) {
                if (usernames[i].equals(this.getUsername())) {
                    studentIndex = i;
                    studentExists = true;
                }
            }

        }
        String[] updatedGrades;
        if (studentExists) {
            String pastGrades = loadPastGrade();
            String[] pastQuizGrades = pastGrades.split(",");
            int[] pastQuizID = new int[pastQuizGrades.length];
            for (int i = 0; i < pastQuizGrades.length; i++) {
                pastQuizID[i] = Integer.parseInt((pastQuizGrades[i].split(":"))[0]);
            }
            int quizIndex;
            boolean taken = false;
            for (int i = 0; i < pastQuizID.length; i++) {
                if (pastQuizID[i] == quizID) {
                    quizIndex = i;
                    taken = true;
                }
            }
            String updatedGrade = "";
            if (taken) {
                System.out.println("You have already taken this quiz before. Please contact your teacher!");
            } else {
                updatedGrade = pastGrades.substring(0, pastGrades.length() - 1);
                updatedGrade += String.format(",%d:%.2f/", quizID, grade);
            }
            int studentIndex = 0;
            allGrades[studentIndex] = getUsername() + "-" + updatedGrade;
            updatedGrades = allGrades;
        } else {
            updatedGrades = new String[allGrades.length + 1];
            for (int i = 0; i < allGrades.length; i++) {
                updatedGrades[i] = allGrades[i];
            }
            updatedGrades[updatedGrades.length - 1] = String.format("%s-%d:%.2f", username, quizID, grade);
        }

        try {
            out = new PrintWriter(new FileOutputStream("Grades.txt", false));
            for (int i = 0; i < updatedGrades.length; i++) {
                out.println(updatedGrades[i]);
            }
            out.close();
            System.out.println("Grade recorded!");
        } catch (Exception e) {
            System.out.println("ERROR RECORDING GRADE");
        }
    }
}
