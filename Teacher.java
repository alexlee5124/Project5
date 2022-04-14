import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A class for teachers' accounts
 *
 * CS1800 Spring 2022, Project 4
 *
 * @author Quinn Bello, Alex Lee
 * @version 4/5/2022
 */

public class Teacher extends Account {

    public Teacher(String username, boolean isLogged) throws FileNotFoundException {
        super(username, isLogged);
    }

    /** Retrieve the questions from the text file and save it into a Question array
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public Question[] retrieveQuestions() {
        ArrayList<String> questionsList = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Questions.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                questionsList.add(line);
            }
        } catch (IOException ie) {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }

        Question[] questions = new Question[questionsList.size()];
        for (int i = 0; i < questionsList.size(); i++) {
            String prompt = (questionsList.get(i).split(","))[0];
            String answer = (questionsList.get(i).split(","))[1];
            Question question = new Question(0, prompt, answer);
            questions[i] = question;
        }
        return questions;
    }

    /** Create randomized quiz from the question pool
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public void createRandom(int numberQuestions, LocalDateTime deadline, int duration) {
        // create a random questions array from question pool
        Question[] questionPool = retrieveQuestions();
        if ( questionPool.length < numberQuestions ) {
            System.out.println("Error creating quiz. Please check the question pool!");
        } else {
            int[] questionsIndex = new int[ numberQuestions ];
            ArrayList<Integer> randomGenerator = new ArrayList<>();
            for ( int i = 0 ; i < questionPool.length ; i++ ) {
                randomGenerator.add(i);
            }
            Collections.shuffle(randomGenerator);
            for ( int i = 0 ; i < numberQuestions ; i++ ) {
                questionsIndex[i] = randomGenerator.get(i);
            }
            // prompt teacher for the point value of each question and record each question point values
            int totalPoints = 0;
            int[] questionPoints = new int[numberQuestions];
            for ( int i = 0 ; i < numberQuestions ; i++ ) {
                System.out.printf("How many points is question %d worth?\n", i + 1);
                int point = scan.nextInt();
                scan.nextLine();
                questionPoints[i] = point;
                totalPoints = totalPoints + point;
            }
            // create randomized quiz and output it to quiz file
            Quiz randomQuiz = new Quiz( numberQuestions, deadline, duration, totalPoints, questionsIndex, questionPoints );
            randomQuiz.writeQuiz();
            System.out.println("Quiz created!");
        }
    }

    /** Create custom quiz based on teach input
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public void createCustom(int numberQuestions, LocalDateTime deadline, int duration, int[] questionsIndex) {
        // create a random questions array from question pool
        Question[] questionPool = retrieveQuestions();
        for ( int i = 0 ; i < questionsIndex.length ; i++ ) {
            questionsIndex[i]--;
        }
        if ( questionPool.length < numberQuestions ) {
            System.out.println("Error creating quiz. Please check the question pool!");
        } else {
            // prompt teacher for the point value of each question and record each question point values
            int totalPoints = 0;
            int[] questionPoints = new int[numberQuestions];
            for ( int i = 0 ; i < numberQuestions ; i++ ) {
                System.out.printf("How many points is question %d worth?\n", i + 1);
                int point = scan.nextInt();
                scan.nextLine();
                questionPoints[i] = point;
                totalPoints = totalPoints + point;
            }
            // create randomized quiz and output it to quiz file
            Quiz customQuiz = new Quiz( numberQuestions, deadline, duration, totalPoints, questionsIndex, questionPoints );
            customQuiz.writeQuiz();
            System.out.println("Quiz created!");
        }
    }

    /** Delete deisred quiz
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/11/2022
     */
    public void deleteQuiz( int quizID ) {
        ArrayList<String> quizList = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Quiz.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                quizList.add(line);
            }
        } catch (IOException ie) {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }
        String[] quizzes = new String[quizList.size()];
        boolean exists = false;
        int quizIndex = 0;
        for ( int i = 0 ; i < quizzes.length ; i++ ) {
            int ID = Integer.parseInt(quizList.get(i).split(":")[0]);
            if ( ID == quizID ) {
                exists = true;
                quizIndex = i;
            }
        }
        if ( exists ) {
            quizList.remove(quizIndex);
            try {
                out = new PrintWriter(new FileOutputStream("Quiz.txt", false));
                for (String s : quizList) {
                    out.println(s);
                }
                out.close();
            } catch (Exception e) {
                System.out.println("ERROR DELETING QUIZ");
            }
            System.out.println("Quiz deleted!");
        } else {
            System.out.println("This quiz doesn't exist.");
        }
    }

    /** Receive the question index to be deleted and write over the question file with it deleted
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public void deleteQuestion(int questionIndex) {
        questionIndex--;
        // retrieve question pool and convert it to arraylist
        Question[] previousQuestions = retrieveQuestions();
        ArrayList<Question> updatedQuestionsList = new ArrayList<>();
        for (int i = 0; i < previousQuestions.length; i++) {
            updatedQuestionsList.add(previousQuestions[i]);
        }
        // remove the desired question and convert it back to array
        updatedQuestionsList.remove(questionIndex);
        Question[] updatedQuestions = new Question[updatedQuestionsList.size()];
        for (int i = 0; i < updatedQuestions.length; i++) {
            updatedQuestions[i] = updatedQuestionsList.get(i);
        }

        // rewrite the updated question pool out to the text file
        PrintWriter out;
        try {
            out = new PrintWriter(new FileOutputStream("Questions.txt", false));
            for (int i = 0; i < updatedQuestions.length; i++) {
                out.println(updatedQuestions[i].toString());
            }
            out.close();
            System.out.println("Question deleted!");
        } catch (Exception e) {
            System.out.println("ERROR CREATING NEW ACCOUNT");
        }
    }
    /** Receive question prompt, answer, and point value from the user and append it to the questions text file
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public void addQuestion(int pointValue, String prompt, String answer) {
        Question question = new Question(pointValue, prompt, answer);
        PrintWriter out;
        try {
            out = new PrintWriter(new FileOutputStream("Questions.txt", true));
            out.println(question.toString());
            out.close();
            System.out.println("Question added!");
        } catch (Exception e) {
            System.out.println("ERROR OUTPUTTING NEW QUESTION");
        }

    }
    /** Modify question prompt
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public void modifyQuestionPrompt( int questionNumber, String newPrompt) {
        questionNumber--;
        // retrieve question pool
        Question[] questions = retrieveQuestions();

        // modify question prompt
        questions[questionNumber].setPrompt(newPrompt);

        // output modified question pool
        PrintWriter out;
        try {
            out = new PrintWriter(new FileOutputStream("Questions.txt", false));
            for (int i = 0; i < questions.length; i++) {
                out.println(questions[i].toString());
            }
            out.close();
            System.out.println("Prompt modified!");
        } catch (Exception e) {
            System.out.println("ERROR CREATING NEW ACCOUNT");
        }


    }
    /** Modify question answer
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public void modifyQuestionAnswer(int questionNumber, String newAnswer) {
        questionNumber--;
        // retrieve question pool
        Question[] questions = retrieveQuestions();

        // modify question prompt
        questions[questionNumber].setAnswer(newAnswer);

        // output modified question pool
        PrintWriter out;
        try {
            out = new PrintWriter(new FileOutputStream("Questions.txt", false));
            for (int i = 0; i < questions.length; i++) {
                out.println(questions[i].toString());
            }
            out.close();
            System.out.println("Answer modified!");
        } catch (Exception e) {
            System.out.println("ERROR CREATING NEW ACCOUNT");
        }
    }
    /** Modify question point value
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public void modifyQuestionPoint(int questionNumber, int newPoint) {
        questionNumber--;
        // retrieve question pool
        Question[] questions = retrieveQuestions();

        // modify question prompt
        questions[questionNumber].setPointValue(newPoint);

        // output modified question pool
        PrintWriter out;
        try {
            out = new PrintWriter(new FileOutputStream("Questions.txt", false));
            for (int i = 0; i < questions.length; i++) {
                out.println(questions[i].toString());
            }
            out.close();
            System.out.println("Point value modified!");
        } catch (Exception e) {
            System.out.println("ERROR CREATING NEW ACCOUNT");
        }
    }

    public String loadGrade( String username, int QuizID ) {
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
            if ( usernames[i].equals(username)) {
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
}