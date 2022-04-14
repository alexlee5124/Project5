
/** Question class-----
 * Assigns point values and
 * holds fields to verify correct answers.
 *
 * CS1800 Spring 2022, Project 4
 * @author Taylor Graham, Alex Lee
 * @version 4/5/2022
 */

public class Question {
    private int pointValue;
    private String prompt;
    private String answer;

    /** Getter & Setter, Fields, Constructors
     * CS1800 Spring 2022, Project 4
     * @author Taylor Graham, Alex Lee
     * @version 4/5/2022
     */
    public Question(int pointValue, String prompt, String answer) {
        this.pointValue = pointValue;
        this.prompt = prompt;
        this.answer = answer;
    }
    Question() {
        this.pointValue = 0;
        this.prompt = null;
        this.answer = null;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public String getAnswer() {
        return this.answer;
    }

    public int getPointValue() {
        return this.pointValue;
    }

    public void setPrompt( String prompt ) {
        this.prompt = prompt;
    }
    public void setAnswer( String answer ) {
        this.answer = answer;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    /** Grade each question, returning the full point value if correct, 0 if not
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public int gradeQuestion( String response ) {
        int pointsEarned = 0;
        if (response.equals(this.answer.toLowerCase())) {
            pointsEarned = this.pointValue;
        }
        return pointsEarned;
    }

    /** toString method
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public String toString() {
        return String.format("%s,%s", this.prompt, this.answer);
    }

}