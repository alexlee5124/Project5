# Project 5 README.md
How to Compile and Run:
----------------------
Submissions:
----------------------

Alex Lee:

Quinn Bello:

Taylor Graham:

Implemented Classes:
----------------------
**Account.java:**

The Account class is the superclass for both the Teacher and Student classes. 
This class includes fields essential for the creation of an account. Teacher.java and Student.java then inherit these fields. 
This class has two fields: String username and boolean isLogged. Usernames are the only input taken to create and/or login to an account. 
There is no password field. isLogged boolean variable keeps track of the user’s log-in status. 
A user has to be logged in in order to perform any of its methods. 
To test that this class works, run the main.java program, follow through with the initial menu and see that it:
1) Creates an account with the input username and saves it out to the AccountInformation.txt file and 
2) Allows you to log in with any of the usernames currently existing in the AccountInformation.txt file.

**Teacher.java**

The Teacher class is the subclass of the Account class and inherits its fields and methods. A teacher object can create, edit, 
and delete questions from the question pool, create and delete quizzes, and view a student’s submission, along with any account methods 
inherited from the account superclass.

Following the main.java program, verify that adding a question properly stores the new question to the Question.txt file, 
modifying a question prompt or answer updates the Question.txt file accordingly, and deleting a question removes the question from the Question.txt 
file accordingly. A question is written out to the text file in the following format for each question, where each line in the text file is a new question: 
“Question prompt:Question answer.”

In a similar manner, verify that creating a quiz records the quiz out to the Quiz.txt file properly. Each quiz is recorded in the following format: 
“Quiz ID:Number of questions:[Array of question point values]: deadline YYYY-MM-DD HH:SS:duration in minutes:Total points:[Array of question indexes]. 
The array of question indexes is utilized when loading questions from the question pool. For example, a question index of [0,3,2] will load the first, fourth, and third question from the Question.txt file. 
This can be manually verified by taking this quiz through a student’s account. A teacher can either create a randomly produced quiz or a custom quiz. 
The only difference between the two is that a custom quiz will take a teacher’s input for determining the quiz’s question indexes.

To see a student’s past submissions, follow the main program to enter the student’s username and the quiz ID. The program will then retrieve this 
information from the Grades.txt file and output it to the terminal.

**Student.java**

The Student class is the subclass of the Account class and inherits its fields and methods. On top of being able to modify and delete its account, a student object, once logged in, 
can take any of the quizzes available and view any of the past attempt grades.

To test that a student object can take any quizzes, first log in through the main program and follow the subsequent menu to take a quiz. 
Note that the student can only take a quiz already created by a teacher account. Verify that the quiz has been loaded correctly 
by making sure that the quiz’s question index matches up with the question pool and that the correct questions have been loaded. 
Then take the quiz and make sure that the resulting grade, which will be printed out to the terminal, matches the expected grade, 
according to the quiz’s total grade and each question’s point values.

To test that a student can view their past attempts, log in as a student and enter a quiz ID to view the recorded score. Verify that this score matches 
the content from Grades.txt file and that trying to view a quiz grade that hasn’t been taken yet will return an error message.

**Quiz.java**

The Quiz class is used to handle the creation and manipulation of the quizzes. Each quiz has a unique ID that helps identify it after it gets stored in the Quiz.txt file. 
All quizzes will have numberQuestions field to store the number of questions, deadline field to store the deadline, duration field to store the duration for the quiz in minutes, 
totalPoints to store the total points value of this quiz, an integer array questionPoints to keep track of each question’s point value, and an integer array questionsIndex to keep track
of the question indexes. This index array is used when a student tries to take the particular quiz. The program will take this index array, go to the Question.txt file and retrieve 
the corresponding questions from the text file.

**Question.java**

The Question class's main purpose is to assign point values to questions and verify correct answers. 
To do so, three fields are used: pointValue, prompt, and answer. The gradeQuestion method takes the user's input and compares it to the answer for the question.
If the user's answer matches, all assigned points are rewarded, if they are incorrect, no points will be given for that specific question.
This class's methods are used in the Quiz class to grade taken quizzes.

**Main.java**

Main.java acts as the implementation of all of the created classes and can also be used as the testing program. 
Follow through with the given prompts to execute the program and verify that it works as it has been described above in the student.java and teacher.java sections. 
All responses will be tested for validity and an invalid response will print error messages and prompt the user again for a valid response so that the program 
can keep running without crashing. Feel free to test this feature at any point.

**AccountInformation.txt**

Holds all of the account usernames along with the account type (teacher/student). When an account is created, their username is written and stored within this file. 
When an account is deleted, its username is deleted. A username must appear on this text file in order for a login to be successful.

**Grades.txt**

Holds a student's recorded grade after taking a quiz and stores their attempt.

**Quiz.txt**

Holds created quizzes. Takes inputted quizzes and stores them in a text file. This stops the quizzes from being lost after the program is ended.
When a quiz is deleted, created, or modified, this is reflected through this file.
