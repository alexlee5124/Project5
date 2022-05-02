**Test 1: Threading, creating account, logging in, and exiting**
1. Run ServerMain.java
2. Run Threading.java
3. Click the “New Window” button on Threading.java to create a new window or multiple new windows.
4. On any one of the windows, click the “Exit” button to close the window.
5. Click the “Log in” button and enter an arbitrary username to try to log in. Because you haven’t created any accounts yet, it will show an error message. 
6. Click the “Create Account” button and create a new account. Make sure to distinguish between a teacher account and a student account, which can be chosen from the option bar. 
7. After you’ve done so, log in again using the account you just created. To test concurrency, try creating an account from one window and logging in on another window.

Expected Result: Each window is fully functional meaning the server can host multiple clients simultaneously.

Test Status: Passed

**Test 2: Modifying and deleting accounts**
1. Once you’ve logged in, either as a student or a teacher, use the option bar to navigate to “Modify account.” Try entering an already existing username and check that the program throws an error message.
2. After you’ve modified your username, exit out of that window, create a new window, and log in through that window to check that the username has been modified appropriately. 
3. Once you’ve logged back in, now choose the “Delete account” option. This will delete your account, log you out, and close the current window. 
4. Open a new window and check that the account has been deleted.

Expected Result: Users can modify their username to edit their account. They can also delete their account completely.

Test Status: Passed

**Test 3: Editing question pool**
1. Once you’re logged in as a teacher, choose “Edit question pool” from the option.
2. There are three options here: add question, delete question, and modify question.
3. Choose “Add question” first. There should be three types of questions you can add: multiple choice, free response, and true/false. Let’s start with multiple choices. 
4. In the first text box, enter the question prompt, including a question mark if you wish to display it on a quiz. In the option box, choose the number of options this question will have. There can be 1 to 4 options. Click the “Add” button to proceed.
5. In each text box, enter the appropriate option prompt. If you leave a text box blank, it will simply record as a space (“ “).
6. In the option box below, choose the correct option and click the “Submit” button.
7. Navigate back to the same “Add question” window following steps 1-3.
8. Now choose the “Free response” option.
9. Enter the question prompt and question answer and click “Add.”
10. Navigate back to the “Add questions” window and choose true/false this time.
11. Enter the question prompt and choose the correct option (T or F) from the option box. 
12. Add any arbitrary question at this point to test the “Delete question” option later.
13. Under the “Edit question pool” window, choose the delete question option this time. 
14. In the option box, which displays the current list of questions, choose the one you’d like to delete and click the “Delete” button.

Expected Result: Teachers can edit their question pool by adding new question (either free response, multiple choice, or true/false) or deleting a preexisting one. 

Test Status: Passed

**Test 4: Creating quizzes**
1. From the teacher menu, choose the “Create quiz” option. There should be two options to do this: random quiz and custom quiz.
2. Choose the random quiz option.
3. Choose the number of questions from the option bar. This number should be maxed out at the current size of the question pool.
4. In the next option box, choose the point value for the questions. Each question will hold the same point value and it can be worth 1-5 points.
5. Enter a deadline for this quiz following the format provided in the prompt.
6. Enter a duration in minutes.
7. Try entering an invalid response for either the deadline or the duration and check to see that it throws an error message.
8. Now enter a valid response and click “Create quiz.”
9. Navigate back to the create quiz window and choose custom quiz this time.
10. In the first option box, choose how many questions this quiz will have.
11. Enter the deadline and duration either with an appropriate format or not. We will test this in a moment.
12. Where the prompt asks you to enter the question indexes and the question point values, use the list of questions provided just above to enter the questions indexes. For example, if you wish to have 3 questions on the quiz and you’d like the questions 1, 5, and 3 on this quiz, you would enter “1,5,3”. However, we will test the error cases first.
13. Enter more questions than the number of questions chosen (e.g. if you choose to have 2 questions on the quiz, enter “1,5,3” which would be three questions) and check to see that you get an error message.
14. Next, enter an appropriate index and question values, but an invalid response for deadline and/or duration. Check that you get an error message.
15. Now enter a valid response for deadline, duration, index, and question values. Each question value will determine the point value for the corresponding question. For example, if you chose “1,5,3” as your question index and “2,2,3” for question values, the quiz will have questions 1, 5, and 3 and question 1 will be worth 2 points, question 5 worth 2, and question 3 worth 3 points.

Expected Result: Teachers can create either custom or random quizzes. These quizzes can have different question types, varying point values for question, and dealines.

Test Status: Passed

**Test 5: Taking quiz**
1. Now, log in as a student and choose the “Take quiz” option from the main menu.
2. There should be two quizzes there: the first is the random quiz and the second is the custom quiz since we created them in that order.
3. Choose either quiz and answer to your best abilities.
4. If you have taken the quiz before, the program will throw an error message when you press submit.

Expected Result: Students can take quizzes created by teacher accounts. These quizzes can only be taken once.

Test Status: Passed

**Test 6: Viewing submissions as a student**
1. From the student menu, choose “View submissions”
2. Enter the quiz ID that you want to view. Try entering an invalid quiz ID to see that it throws an error message.
3. Then enter a valid quiz ID, one you’ve taken before to view the grade.

Expected Result: Students can view their submissions and grades from quizzes by entering the quiz's ID. 

Test Status: Passed

**Test 7: View student’s submission as a teacher**
1. Having logged in as a teacher, choose the “View student submissions.” 
2. In the option box, select the student that you’d like to see the grade of. Enter an invalid quiz ID to check that it throws an error message.
3. Now enter a valid quiz ID to see the student’s submission, both the timestamp and the quiz grade.

Expected Result: Teachers can view student submissions and grades of a specific student by selecting a student and entering a valid quiz ID. 

Test Status: Passed
