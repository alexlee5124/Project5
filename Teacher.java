import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    Tools tools = new Tools();

    /** Retrieve quizzes from text file and save it as an array
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/10/2022
     */
    public Quiz[] retrieveQuizzes() {
        String quizFile = tools.loadTextFile("Quiz.txt");
        String[] quizzesString = quizFile.split("/");
        Quiz[] quizzes = new Quiz[quizzesString.length];
        for ( int i = 0 ; i < quizzes.length ; i++ ) {
            int quizID = Integer.parseInt((quizzesString[i].split(":"))[0]);
            int numberQuestions = Integer.parseInt((quizzesString[i].split(":"))[1]);
            int[] questionPoints = new int[numberQuestions];
            for ( int j = 0 ; j < numberQuestions ; j++ ) {
                questionPoints[j] = Integer.parseInt(((quizzesString[i].split(":"))[2]).split(",")[j]);
            }
            String deadlineString = (quizzesString[i].split(":")[3]);
            deadlineString = String.format("%s-%s-%s %s:%s", deadlineString.split("-")[0],
                    deadlineString.split("-")[1], deadlineString.split("-")[2],
                    deadlineString.split("-")[3], deadlineString.split("-")[4]);
            DateTimeFormatter deadlineFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime deadline = LocalDateTime.parse( deadlineString, deadlineFormat );
            int duration = Integer.parseInt((quizzesString[i].split(":"))[4]);
            int totalPoints = Integer.parseInt((quizzesString[i].split(":"))[5]);
            int[] questionsIndex = new int[numberQuestions];
            for ( int j = 0 ; j < numberQuestions ; j++ ) {
                questionsIndex[j] = Integer.parseInt((quizzesString[i].split(":"))[6].split(",")[j]);
            }
            Quiz quiz = new Quiz(quizID, numberQuestions, deadline,
                    duration, totalPoints, questionsIndex, questionPoints);
            quizzes[i] = quiz;
        }
        return quizzes;
    }

    /** Retrieve the questions from the text file and save it into a Question array
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public Question[] retrieveQuestions() {
        String questionFile = tools.loadTextFile("Questions.txt");
        String[] lines = questionFile.split("/");
        Question[] questions = new Question[lines.length];

        for (int i = 0 ; i < lines.length; i++) {
            String[] elements = lines[i].split(",");
            System.out.printf("TEST ELEMENTS LENGTH: %d\n", elements.length);
            String prompt = elements[0];
            String answer = elements[1];
            String questionType = elements[2];
            Question question = new Question(prompt, answer, questionType);
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
            System.out.println("The desired number of questions is greater than the number of questions" +
                    "in the question pool! Please update your question pool!");
        } else {
            int[] questionsIndex = tools.randomGenerator(questionPool.length, numberQuestions);
            // prompt teacher for the point value of each question and record each question point values
            int totalPoints = 0;
            int[] questionPoints = new int[numberQuestions];
            for ( int i = 0 ; i < numberQuestions ; i++ ) {
                System.out.printf("How many points is question %d worth?\n", i + 1);
                int point = tools.receiveValidInt(1, scan);
                questionPoints[i] = point;
                totalPoints = totalPoints + point;
            }
            // create randomized quiz and output it to quiz file
            Quiz randomQuiz = new Quiz( numberQuestions, deadline,
                    duration, totalPoints, questionsIndex, questionPoints );
            randomQuiz.writeNewQuiz();
            System.out.println("Random quiz successfully created!");
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
            System.out.println("The desired number of questions is greater than the number of questions" +
                    "in the question pool! Please update your question pool!");
        } else {
            // prompt teacher for the point value of each question and record each question point values
            int totalPoints = 0;
            int[] questionPoints = new int[numberQuestions];
            for ( int i = 0 ; i < numberQuestions ; i++ ) {
                System.out.printf("How many points is question %d worth?\n", i + 1);
                int point = tools.receiveValidInt(1, scan);
                questionPoints[i] = point;
                totalPoints = totalPoints + point;
            }
            // create randomized quiz and output it to quiz file
            Quiz customQuiz = new Quiz( numberQuestions, deadline, duration,
                    totalPoints, questionsIndex, questionPoints );
            customQuiz.writeNewQuiz();
            System.out.println("Custom quiz successfully created!");
        }
    }

    /** Delete desired quiz
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/11/2022
     */
    public boolean deleteQuiz( int wantedID ) {
        boolean deleted = false;
        String quizFile = tools.loadTextFile("Quiz.txt");
        String[] quizzes = quizFile.split("/");
        ArrayList<String> quizList = new ArrayList<>();
        Collections.addAll(quizList, quizzes);

        boolean exists = false;
        int quizIndex = 0;
        for ( int i = 0 ; i < quizzes.length ; i++ ) {
            int ID = Integer.parseInt(quizzes[i].split(":")[0]);
            if ( ID == wantedID ) {
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
                deleted = true;
            } catch (Exception e) {
                System.out.println("ERROR DELETING QUIZ!");
            }
        }
        return deleted;
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
        if (questionIndex > previousQuestions.length) {
            System.out.println("The question doesn't exist. Please check the question pool!");
        } else {
            ArrayList<Question> updatedQuestionsList = new ArrayList<>();
            Collections.addAll(updatedQuestionsList, previousQuestions);
            // remove the desired question and convert it back to array
            updatedQuestionsList.remove(questionIndex);
            Question[] updatedQuestions = new Question[updatedQuestionsList.size()];
            for (int i = 0; i < updatedQuestions.length; i++) {
                updatedQuestions[i] = updatedQuestionsList.get(i);
            }
            // rewrite the updated question pool out to the text file
            try {
                PrintWriter out = new PrintWriter(new FileOutputStream("Questions.txt", false));
                for (int i = 0; i < updatedQuestions.length; i++) {
                    out.println(updatedQuestions[i].toString());
                }
                out.close();
                System.out.println("Question deleted!");
            } catch (Exception e) {
                System.out.println("ERROR DELETING QUESTION!");
            }
        }
    }

    /** Retrieve the current size of quesiton pool
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/15/2022
     */
    public int getQuestionPoolSize() {
        int size = 0;
        String questionFile = tools.loadTextFile("Questions.txt");
        String[] lines = questionFile.split("/");
        return lines.length;
    }


    /** Receive question prompt and answer from the user and append it to the questions text file
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public void addResponseQuestion(String prompt, String answer) {
        Question question = new Question(prompt, answer, "R");
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream("Questions.txt", true));
            out.println(question);
            out.close();
            System.out.println("Question added!");
        } catch (Exception e) {
            System.out.println("ERROR RECORDING NEW QUESTION");
        }

    }


    public void addMultipleQuestion(String prompt, String answer, String[] optionPrompts) {
        Question question = new Question(prompt, answer, "M");
        for ( int i = 0 ; i < optionPrompts.length ; i++) {
            prompt += String.format(":%s", optionPrompts[i]);
        }
        question.setPrompt(prompt);
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream("Questions.txt", true));
            out.println(question);
            out.close();
            System.out.println("Question added!");
        } catch (Exception e) {
            System.out.println("ERROR RECORDING NEW QUESTION");
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
        if (questionNumber > questions.length) {
            System.out.println("The question doesn't exist. Please check the question pool!");
        } else {
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
                System.out.println("ERROR MODIFYING PROMPT!");
            }
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

        if (questionNumber > questions.length) {
            System.out.println("The question doesn't exist. Please check the question pool!");
        } else {
            // modify question answer
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
                System.out.println("ERROR MODIFYING ANSWER!");
            }
        }
    }

    /** Load the grade and timestamp of desired student & quiz
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/19/2022
     */
    public String[] loadGrade( String studentUsername, int wantedID ) {
        String grade = null;
        String timestamp = null;
        String[] gradeAndTime = null;
        boolean studentExists = false;

        String gradesFile = tools.loadTextFile("Grades.txt");
        String[] allGrades = gradesFile.split("/");
        String[] usernames = new String[allGrades.length];

        String[] studentsGrades = new String[allGrades.length];
        for ( int i = 0 ; i < allGrades.length ; i ++ ) {
            usernames[i] = allGrades[i].split("-")[0];
            studentsGrades[i] = allGrades[i].split("-")[1];
        }

        int studentIndex = 0;
        for ( int i = 0 ; i < usernames.length ; i++ ) {
            if ( usernames[i].equals(studentUsername)) {
                studentIndex = i;
                studentExists = true;
            }
        }
        boolean quizTaken = false;
        if (studentExists) {
            String[] thisStudentGrades = studentsGrades[studentIndex].split(",");
            for ( int i = 1 ; i < thisStudentGrades.length ; i++ ) {
                String takenQuizID = thisStudentGrades[i].split(":")[0];
                if ( Integer.parseInt(takenQuizID) == wantedID ) {
                    grade = (thisStudentGrades[i].split(":"))[1];
                    timestamp = (thisStudentGrades[i].split(":"))[2];
                    String [] elements = timestamp.split(" ");
                    timestamp = String.format("%s-%s-%s %s:%s", elements[0], elements[1], elements[2],
                            elements[3], elements[4]);
                    gradeAndTime = new String[2];
                    gradeAndTime[0] = grade;
                    gradeAndTime[1] = timestamp;
                    quizTaken = true;
                }
            }
        } else {
            System.out.println("The student username doesn't exist!");
        }
        if (!quizTaken) {
            System.out.println("This student hasn't taken this quiz yet or the quiz doesn't exist!");
        }


        return gradeAndTime;
    }

    /** Read the quiz text file and return an array of quizzes
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/19/2022
     */
    public Quiz findQuiz(int wantedID) {
        Quiz[] quizzes = retrieveQuizzes();
        Quiz returnQuiz = null;
        boolean exists = false;
        if (quizzes.length == 0) {
            System.out.println("There aren't any quizzes created. Please check the quiz file!");
        } else {
            for (int i = 0 ; i < quizzes.length ; i++) {
                if (wantedID == quizzes[i].getQuizID()) {
                    returnQuiz = quizzes[i];
                    exists = true;
                }
            }
        }
        if (!exists) {
            System.out.println("The quiz doesn't exist");
        }
        return returnQuiz;
    }

    /** Overwrite current quiz text file with current settings
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/19/2022
     */
    public void overwriteQuiz(int wantedID, Quiz updatedQuiz) {
        Quiz[] quizzes = retrieveQuizzes();
        for (int i = 0 ; i < quizzes.length ; i++) {
            if (quizzes[i].getQuizID() == wantedID) {
                quizzes[i] = updatedQuiz;
            }
        }
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream("Quiz.txt", false));
            for (Quiz q : quizzes) {
                out.println(q);
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR RECORDING QUIZ");
        }
    }
}
