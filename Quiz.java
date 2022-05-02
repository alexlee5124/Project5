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

    Tools tools = new Tools();
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
        this.quizID = quizID;
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

    public int getDuration() {
        return this.duration;
    }

    public int getTotalPoints() {
        return this.totalPoints;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setQuestionPoints(int[] questionPoints) {
        this.questionPoints = questionPoints;
    }

    public void setQuestionsIndex(int[] questionsIndex) {
        this.questionsIndex = questionsIndex;
    }

    public String getDeadline() {
        return String.format("%d-%d-%d %d:%d",
                this.deadline.getYear(), this.deadline.getMonthValue(),
                this.deadline.getDayOfMonth(), this.deadline.getHour(),
                this.deadline.getMinute());
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    /** Retrieve the largest quiz ID value
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/11/2022
     */
    public int retrieveLargestID() {
        String quizFile = tools.loadTextFile("Quiz.txt");
        String[] lines = new String[0];
        if (!quizFile.equals("")) {
            lines = quizFile.split("/");
        }
        int largest = 0;
        if (lines.length > 0) {
            largest = Integer.parseInt(lines[lines.length - 1].split(":")[0]);
        }
        return largest;
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
        for (int i = 0; i < lines.length; i++) {
            String[] elements = lines[i].split(",");
            String prompt = elements[0];
            String answer = elements[1];
            String questionType = elements[2];
            Question question = new Question(prompt, answer, questionType);
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

    /** append this quiz to quiz text file
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public void writeNewQuiz() {
        try {
            PrintWriter out = new PrintWriter(new FileOutputStream("Quiz.txt", true));
            out.println(this);
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
        float grade = ((float) totalPointsEarned / this.totalPoints) * 100;
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
        String hourString = "";
        if (this.deadline.getHour() <10) {
            hourString = "0" + this.deadline.getHour();
        } else {
            hourString = String.valueOf(this.deadline.getHour());
        }
        String deadlineString = String.format("%d-%s-%s-%s-%d", this.deadline.getYear(),monthString,
                dayString, hourString, this.deadline.getMinute());
        String questionPointsString = "";
        for ( int i = 0 ; i < this.numberQuestions - 1 ; i++ ) {
            questionPointsString = questionPointsString +  this.questionPoints[i] + "," ;
        }
        questionPointsString = questionPointsString +  this.questionPoints[this.numberQuestions - 1];

        return String.format("%d:%d:%s:%s:%d:%d:%s/", this.quizID, this.numberQuestions, questionPointsString,
                deadlineString, this.duration,
                this.totalPoints, questionsIndexString);
    }
}
