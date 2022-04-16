
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
    private String questionType;    // Question type: M for multiple choice, R for response or True/False

    /** Getter & Setter, Fields, Constructors
     * CS1800 Spring 2022, Project 4
     * @author Taylor Graham, Alex Lee
     * @version 4/5/2022
     */
    public Question(int pointValue, String prompt, String answer, String questionType) {
        this.pointValue = pointValue;
        this.prompt = prompt;
        this.answer = answer;
        this.questionType = questionType;
    }
    public Question(String prompt, String answer, String questionType) {
        this.pointValue = 0;
        this.prompt = prompt;
        this.answer = answer;
        this.questionType = questionType;
    }

    public String getPrompt() {
        if (this.questionType.equals("R")) {
            return this.prompt;
        } else {
            String[] promptElements = this.prompt.split(":");
            String prompt = promptElements[0];
            for (int i = 1 ; i < promptElements.length ; i++) {
                prompt += String.format("\n%d. %s", i, promptElements[i]);
            }
            return prompt;
        }
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
        if (response.equalsIgnoreCase(getAnswer())) {
            pointsEarned = getPointValue();
        }
        return pointsEarned;
    }

    /** toString method
     * CS1800 Spring 2022, Project 4
     * @author Alex Lee
     * @version 4/9/2022
     */
    public String toString(){
        return String.format("%s,%s,%s/", this.prompt, this.answer, this.questionType);
    }

}
