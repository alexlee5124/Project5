import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
/** Quiz class for creating quizzes
 *
 * CS1800 Spring 2022, Project 4
 * @author Alex Lee
 * @version 4/5/2022
 */

public class Quiz {
    private int quizID;     // Quiz number
    private int numberQuestions;    // Number of questions in this quiz
    private LocalDateTime deadline; // Deadline for the quiz
    private int duration;   // How many minutes the quiz should take
    private int totalPoints;    // How many points the quiz is worth
    private int[] questionPoints;   // a record of what each question is worth
    private int[] questionsIndex;  // An array for question pool


    /** Quiz constructors
     * CS1800 Spring 2022, Project 4
     * @author Taylor Graham, Alex lee
     * @version 4/11/2022
     */
    public Quiz( int numberQuestions, LocalDateTime deadline, int duration, int totalPoints,
                 int[] questionsIndex, int[] questionPoints) {
        this.quizID = retrieveLargestID() + 1;   // The new quiz ID will be one greater than the largest one
        this.numberQuestions = numberQuestions;
        this.deadline = deadline;
        this.duration = duration;
        this.totalPoints = totalPoints;
        this.questionsIndex = questionsIndex;
        this.questionPoints = questionPoints;
    }

    public Quiz( int quizID, int numberQuestions, LocalDateTime deadline, int duration, int totalPoints,
                 int[] questionsIndex, int[] questionPoints) {
        this.quizID = quizID;   // The new quiz ID will be one greater than the last one
        this.numberQuestions = numberQuestions;
        this.deadline = deadline;
        this.duration = duration;
        this.totalPoints = totalPoints;
        this.questionsIndex = questionsIndex;
        this.questionPoints = questionPoints;
    }

    public int getQuizID() {
        return this.quizID;
    }

    public int[] getQuestionsIndex() {
        return this.questionsIndex;
    }

    public int[] getQuestionPoints() {
        return this.questionPoints;
    }

    public int getNumberQuestions() {
        return this.numberQuestions;
    }

    /** Retrieve the largest quiz ID value
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/11/2022
     */
    public int retrieveLargestID() {
        ArrayList<String> quizList = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Quiz.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                quizList.add(line);
            }
        } catch (IOException ie) {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }
        int largest = Integer.parseInt((quizList.get(quizList.size() - 1)).split(":")[0]);
        return largest;
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

    /** Retrieve the questions for this specific quiz
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/10/2022
     */
    public Question[] loadQuizQuestions() {
        Question[] questionPool = retrieveQuestions();
        int[] questionIndex = getQuestionsIndex();
        int[] questionPoints = getQuestionPoints();
        int numberQuestions = getNumberQuestions();
        Question[] quizQuestions = new Question[numberQuestions];

        for ( int i = 0 ; i < numberQuestions ; i++ ) {
            quizQuestions[i] = questionPool[questionIndex[i]];
            quizQuestions[i].setPointValue(questionPoints[i]);
        }
        return quizQuestions;
    }

    /** Retrieve number of questions existing in the quiz file
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public int getNumberQuizzes() {
        ArrayList<String> quizList = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Quiz.txt"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                quizList.add(line);
            }
        } catch (IOException ie) {
            System.out.println("Either the file doesn't exist or the file is in the wrong format!");
        }
        return quizList.size();
    }

    /** append this quiz to quiz text file
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public void writeQuiz() {
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream("Quiz.txt", true));
            out.println(this.toString());
            out.close();
            System.out.println("Quiz recorded!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR RECORDING QUIZ");
        }
    }

    /** grade the quiz and save it out to grades text file
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public float gradeQuiz( String[] responses ) {
        Question[] questions = loadQuizQuestions();
        int totalPointsEarned = 0;
        for ( int i = 0 ; i < this.numberQuestions ; i++ ) {
            int questionGrade = questions[i].gradeQuestion(responses[i]);
            totalPointsEarned = totalPointsEarned + questionGrade;
        }
        float grade = (float) ( (float) totalPointsEarned / this.totalPoints) * 100;
        System.out.printf("GRADE : %d/%d = %.2f\n", totalPointsEarned, this.totalPoints, grade);

        return grade;
    }

    /** toString method
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public String toString() {
        String questionsIndexString = "";
        String monthString;
        String dayString;
        if ( this.deadline.getMonthValue() < 10 ) {
            monthString = String.format("0%d", this.deadline.getMonthValue());
        } else {
            monthString = String.valueOf(this.deadline.getMonthValue());
        }
        if ( this.deadline.getDayOfMonth() < 10 ) {
            dayString = String.format("0%d", this.deadline.getDayOfMonth());
        } else {
            dayString = String.valueOf(this.deadline.getDayOfMonth());
        }
        for (int i = 0 ; i < this.numberQuestions - 1 ; i++ ) {
            questionsIndexString = questionsIndexString + this.questionsIndex[i] + ",";
        }
        questionsIndexString = questionsIndexString + this.questionsIndex[this.numberQuestions - 1];
        String deadlineString = String.format("%d-%s-%s-%d-%d", this.deadline.getYear(),monthString,
                dayString, this.deadline.getHour(), this.deadline.getMinute());
        String questionPointsString = "";
        for ( int i = 0 ; i < this.numberQuestions - 1 ; i++ ) {
            questionPointsString = questionPointsString +  this.questionPoints[i] + "," ;
        }
        questionPointsString = questionPointsString +  this.questionPoints[this.numberQuestions - 1];

        return String.format("%d:%d:%s:%s:%d:%d:%s", this.quizID, this.numberQuestions, questionPointsString,
                deadlineString, this.duration,
                this.totalPoints, questionsIndexString);
    }
}
