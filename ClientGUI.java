import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Collections;

public class ClientGUI extends JComponent implements Runnable {
    Tools tools = new Tools();
    Socket socket = null;
    BufferedReader reader = null;
    PrintWriter writer = null;
    JFrame frame;
    Container content;
    JFrame quizTakingFrame;
    Container quizContent;
    JComboBox<String> teacherStudent;
    Teacher teacher;
    Student student;

    /** QUIZ TAKING COMPONENTS */
    int currentPage;
    String currentAccountType = "";
    int currentNumberOptions = 0;



    /** PANELS */
    JPanel reloadPanel;
    JPanel initialPanel; //1
    JPanel createPanel; //2
    JPanel logInPanel; //3
    JPanel studentPanel; //4
    JPanel teacherPanel; //5
    JPanel modifyPanel; //6
    JPanel questionPoolPanel; //7
    JPanel addQuestionPanel; //8
    JPanel addMultipleChoicePanel; //9
    JPanel multipleChoiceOptionPanel; //10
    JPanel addFreeResponsePanel; //11
    JPanel createQuizPanel; //12
    JPanel addTrueFalsePanel; //13
    JPanel takeQuizPanel; //14
    JPanel quizTakingPanel; //15
    JPanel deleteQuestionPanel; //16
    JPanel createRandomQuizPanel; //17
    JPanel createCustomQuizPanel; //18

    /** reload button */
    JButton reloadButton;

    /** Initial Buttons and text fields */
    JButton createButton;
    JButton logInButton;
    JButton exitButton;

    /** Create account Buttons and text fields */
    JButton createButton2;
    JTextField usernameText;

    /** Log in Buttons*/
    JButton logInButton2;

    /** Teacher panel components */
    JLabel teacherMenuPrompt = new JLabel("What would you like to do?");
    JButton teacherMenuSelect;
    JComboBox<String> teacherMenuOptions;


    /** Student panel components */
    JLabel studentMenuPrompt = new JLabel("What would you like to do?");
    JButton studentMenuSelect;
    JComboBox<String> studentMenuOptions;

    /** Modify panel components*/
    JButton modifyButtonT;
    JButton modifyButtonS;
    JLabel modifyPrompt = new JLabel("Enter a new username");

    /** Question pool panel components*/
    JLabel questionPoolPrompt = new JLabel("What would you like to do?");
    JComboBox<String> questionPoolOptions;
    JButton questionPoolSelect;

    /** Create Random Quiz panel components*/
    /** Issue with question point values:
     * Program prints prompt to enter question point values to terminal from Teacher class
     *  - Should all questions be worth same amount in a random quiz?
     *  - Move from teacher to ServerThread?
     * */
    JLabel numberOfQuestionsLabel = new JLabel("Number of Questions");
    JComboBox<Integer> numberOfQuestionsOptions;
    JLabel questionValueLabel = new JLabel("How many points is each question worth?");
    JComboBox<String> questionValueOption;
    JLabel quizDeadlineLabel = new JLabel("Deadline (YYYY-MM-DD HH:MM)");
    JTextField quizDeadlineTextField;
    JLabel quizDurationLabel = new JLabel("Duration (minutes)");
    JTextField quizDurationTextField;
    JButton createRandomQuizButton = new JButton("Create Quiz");

    /** Create Custom Quiz panel components*/
    JComboBox<String> questionList;
    JButton createCustomQuizButton = new JButton("Create Quiz");
    JTextField customQuizQuestionIndexes;
    JTextField customQuizQuestionValues;

    /** Add question panel components*/
    JLabel addQuestionPrompt = new JLabel("What is the question type?");
    JComboBox<String> addQuestionOptions;
    JButton addQuestionSelect;

    /** Add multiple choice panel components*/
    JLabel addMultipleChoicePrompt = new JLabel("What is the question prompt?");
    JTextField multipleChoicePromptText;
    JLabel numberMultipleChoiceOptionsPrompt = new JLabel("How many options will this question have?");
    JComboBox<String> numberMultipleChoiceOptions;
    JButton addMultipleChoiceSelect;
    int multipleChoiceOptions;

    /** Add multiple choice options panel components*/
    JLabel multipleChoiceOptionPrompt;
    JButton multipleChoiceOptionPromptSubmit;
    JTextField[] optionInputs;
    JComboBox<String> correctAnswer;
    JLabel correctAnswerPrompt = new JLabel("What is the correct option?");


    /** Delete question panel components*/
    JLabel deleteQuestionPrompt = new JLabel("Which question would you like to remove?");
    JComboBox<String> deleteQuestionOptions;
    JButton deleteQuestionSelect;

    /** Create quiz panel components*/
    JLabel createQuizTypePrompt = new JLabel("What kind of quiz would you like to create?");
    JComboBox<String> createQuizType;
    JButton createQuizTypeSelect;

    /**Add free response panel components*/
    JLabel addFreeResponsePrompt = new JLabel("What is the question prompt?");
    JTextField freeResponsePromptText;
    JLabel addFreeResponseAnswer = new JLabel("What is the answer to your prompt?");
    JTextField freeResponseAnswerText;
    JButton addFreeResponseSelect;

    /** Add true false choice panel components*/
    JLabel addTrueFalsePrompt = new JLabel("What is the question prompt?");
    JTextField trueFalsePromptText;
    JLabel answerTrueFalseOptionsPrompt = new JLabel("What is the correct option?");
    JComboBox<String> trueFalseAnswerChoice;
    JButton addTrueFalseSelect;

    /** Take quiz- choose quiz panel components*/
    JComboBox<String> chooseQuizOptions;
    JLabel chooseQuiz = new JLabel("Choose quiz to take");
    JButton chooseQuizSelect;

    /** Take quiz - quiz taking panel components*/
    JButton submitButton;
    Quiz currentQuiz;
    Question[] quizQuestions;
    int quizResponseCounter = 0;
    Object[] quizResponses =  new Object[40];

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ClientGUI());
    }

    /** ACTION LISTENERS */

    ActionListener reloadListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == reloadButton) {
                System.out.println("TEST 1");
                reload();
            }
        }
    };


    ActionListener initialListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createButton) {
                writer.println(1);
                writer.flush();
                initialPanel.setVisible(false);
                loadCreateAccountPanel();
            } else if (e.getSource() == logInButton) {
                writer.println(2);
                writer.flush();
                initialPanel.setVisible(false);
                loadLogInPanel();
            } else if (e.getSource() == exitButton) {
                writer.println(3);
                writer.flush();
                JOptionPane.showMessageDialog(null, "Have a nice day!",
                        "Exiting",
                        JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            }
        }
    };

    ActionListener createListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createButton2) {
                String username = usernameText.getText();
                int accountType = teacherStudent.getSelectedIndex();
                writer.println(username);
                writer.flush();
                writer.println(accountType);
                writer.flush();
                String created = null;
                createPanel.setVisible(false);
                initialPanel.setVisible(true);
                try {
                    created = reader.readLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (created.equals("F")) {
                    JOptionPane.showMessageDialog(null, "This account already exists!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Account created!",
                            "Account created",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    };

    ActionListener logInListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == logInButton2) {
                String username = usernameText.getText();
                writer.println(username);
                writer.flush();
                String logged = null;
                String accountType = null;
                try {
                    logged = reader.readLine();
                    accountType = reader.readLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                logInPanel.setVisible(false);
                if (logged.equals("F")) {
                    JOptionPane.showMessageDialog(null, "This account doesn't exist!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    initialPanel.setVisible(true);
                } else if (logged.equals("T")) {
                    if (accountType.equals("student")) {
                        currentAccountType = "S";
                        try {
                            student = new Student(username, true);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        loadStudentPanel();
                        studentPanel.setVisible(true);
                        JOptionPane.showMessageDialog(null, "Student: logged in!",
                                "Logged in",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else if (accountType.equals("teacher")) {
                        currentAccountType = "T";
                        try {
                            teacher = new Teacher(username, true);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        loadTeacherPanel();
                        teacherPanel.setVisible(true);
                        JOptionPane.showMessageDialog(null, "Teacher: logged in!",
                                "Logged in",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }
    };

    ActionListener teacherListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == teacherMenuSelect) {
                int teacherSelectedOption = teacherMenuOptions.getSelectedIndex() + 1;
                writer.println(teacherSelectedOption);
                writer.flush();
                teacherPanel.setVisible(false);
                switch (teacherSelectedOption) {
                    case 1:
                        loadCreateQuizPanel();
                        break;
                    case 2:
                         break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        loadQuestionPoolPanel();
                        break;
                    case 6:
                        loadModifyPanel("T");
                        modifyPanel.setVisible(true);
                        break;
                    case 7:
                        try {
                            String deleted = reader.readLine();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(null, "Account deleted!\n" +
                                        "Have a nice day!",
                                "Account deleted",
                                JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        break;
                    default:
                        break;
                }
            } else if (e.getSource() == exitButton) {
                writer.println(8);
                writer.flush();
                JOptionPane.showMessageDialog(null, "Have a nice day!",
                        "Exiting",
                        JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            }
        }
    };

    ActionListener studentListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == studentMenuSelect) {
                int studentSelectedOption = studentMenuOptions.getSelectedIndex() + 1;
                writer.println(studentSelectedOption);
                writer.flush();
                studentPanel.setVisible(false);
                switch (studentSelectedOption) {
                    case 1:
                        loadTakeQuizPanel();
                        break;
                    case 2:
                        break;
                    case 3:
                        loadModifyPanel("S");
                        modifyPanel.setVisible(true);
                        break;
                    case 4:
                        JOptionPane.showMessageDialog(null, "Account deleted!\n" +
                                        "Have a nice day!",
                                "Account deleted",
                                JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        break;
                    default:
                        break;
                }
            } else if (e.getSource() == exitButton) {
                writer.println(5);
                writer.flush();
                JOptionPane.showMessageDialog(null, "Have a nice day!",
                        "Exiting",
                        JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            }
        }
    };

    ActionListener modifyListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == modifyButtonT) {
                modifyPanel.setVisible(false);
                String newUsername = usernameText.getText();
                writer.println(newUsername);
                writer.flush();

                String modified = "";
                try {
                    modified = reader.readLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                teacherPanel.setVisible(true);
                if (modified.equals("Failure")) {
                    JOptionPane.showMessageDialog(null, "This username already exits!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Username modified!",
                            "Modified",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (e.getSource() == modifyButtonS) {
                modifyPanel.setVisible(false);
                String newUsername = usernameText.getText();
                writer.println(newUsername);
                writer.flush();

                String modified = "";
                try {
                    modified = reader.readLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                studentPanel.setVisible(true);
                if (modified.equals("Failure")) {
                    JOptionPane.showMessageDialog(null, "This username already exits!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Username modified!",
                            "Modified",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    };

    ActionListener questionPoolListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == questionPoolSelect) {
                questionPoolPanel.setVisible(false);
                int questionPoolMod = questionPoolOptions.getSelectedIndex() + 1;
                writer.println(questionPoolMod);
                writer.flush();

                switch (questionPoolMod) {
                    case 1:
                        loadAddQuestionPanel();
                        break;
                    case 2:
                        loadDeleteQuestionPanel();
                        break;
                    case 3:
                         break;
                    default:
                         break;
                }
            }
        }
    };

    ActionListener deleteQuestionListener = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == deleteQuestionSelect) {
                deleteQuestionPanel.setVisible(false);
                int deleteOpts = deleteQuestionOptions.getSelectedIndex() + 1;
                writer.println(deleteOpts);
                writer.flush();

                loadTeacherPanel();
                JOptionPane.showMessageDialog(null, "Question " +
                                "deleted!",
                        "Question deleted",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    };

    ActionListener addQuestionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addQuestionSelect) {
                addQuestionPanel.setVisible(false);
                int questionTypeOption = addQuestionOptions.getSelectedIndex() + 1;
                writer.println(questionTypeOption);
                writer.flush();

                switch (questionTypeOption) {
                    case 1:
                        loadAddMultipleChoicePanel();
                         break;
                    case 2:
                        loadAddFreeResponsePanel();
                        break;
                    case 3:
                        loadTrueFalsePanel();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    ActionListener addMultipleChoiceListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addMultipleChoiceSelect) {
                addMultipleChoicePanel.setVisible(false);
                String prompt = multipleChoicePromptText.getText();
                writer.println(prompt);
                writer.flush();

                multipleChoiceOptions = numberMultipleChoiceOptions.getSelectedIndex() + 1;
                writer.println(multipleChoiceOptions);
                writer.flush();

                loadMultipleChoiceOptionPanel(multipleChoiceOptions);
                multipleChoiceOptionPanel.setVisible(true);
            }
        }
    };

    ActionListener multipleChoiceOptionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == multipleChoiceOptionPromptSubmit) {
                multipleChoiceOptionPanel.setVisible(false);
                for (int i = 0 ; i < multipleChoiceOptions ; i++) {
                    String multipleChoiceOption = optionInputs[i].getText();
                    if (!multipleChoiceOption.isEmpty()) {
                        writer.println(multipleChoiceOption);
                        writer.flush();
                    } else {
                        writer.println(" ");
                        writer.flush();
                    }
                }
                String answer = String.valueOf(correctAnswer.getSelectedIndex() + 1);
                writer.println(answer);
                writer.flush();
                loadTeacherPanel();
                JOptionPane.showMessageDialog(null, "Multiple choice question " +
                                "added!",
                        "Question added",
                        JOptionPane.INFORMATION_MESSAGE);


            }
        }
    };

    ActionListener createQuizListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createQuizTypeSelect) {
                createQuizPanel.setVisible(false);
                int quizType = createQuizType.getSelectedIndex() + 1;
                writer.println(quizType);
                writer.flush();
                switch (quizType) {
                    case 1:
                        loadCreateRandomQuizPanel();
                         break;
                    case 2:
                        loadCreateCustomQuizPanel();
                        break;
                    default:
                        break;
                }

            }
        }
    };

    ActionListener createRandomQuizListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == createRandomQuizButton) {
                createRandomQuizPanel.setVisible(false);

                int numQuestions = numberOfQuestionsOptions.getSelectedIndex() + 1;
                int questionValue = questionValueOption.getSelectedIndex() + 1;
                writer.println(numQuestions);
                writer.flush();
                writer.println(questionValue);
                writer.flush();
                writer.println(quizDeadlineTextField.getText());
                writer.flush();
                writer.println(quizDurationTextField.getText());
                writer.flush();

                String flagError = null;
                try {
                    flagError = reader.readLine();
                    System.out.println(flagError);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (flagError.equals("T")) {
                    loadCreateRandomQuizPanel();
                    JOptionPane.showMessageDialog(null,
                            "Please enter valid responses!\n"
                            , "Error", JOptionPane.ERROR_MESSAGE);
                } else if (flagError.equals("F")) {
                    loadTeacherPanel();
                    JOptionPane.showMessageDialog(null, "Quiz Created!\n" +
                            "Returning to Teacher Main Menu",
                            "Created!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    };

    ActionListener createCustomQuizListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == createCustomQuizButton)
            {
                createCustomQuizPanel.setVisible(false);
                int numQuestions = numberOfQuestionsOptions.getSelectedIndex() + 1;
                String questionIndexesString = customQuizQuestionIndexes.getText();
                String[] questionIndexes = questionIndexesString.split(",");
                String customQuizQuestionValuesString = customQuizQuestionValues.getText();
                String[] customQuizQuestionValues =
                        customQuizQuestionValuesString.split(",");
                String deadlineString = quizDeadlineTextField.getText();
                String durationString = quizDurationTextField.getText();

                if (numQuestions != questionIndexes.length ||
                        numQuestions != customQuizQuestionValues.length) {
                    loadCreateCustomQuizPanel();
                    JOptionPane.showMessageDialog(null,
                            "Please make sure the number of question indexes" +
                                    "match the number of questions!\n"
                            , "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    writer.println(numQuestions);
                    writer.flush();
                    writer.println(questionIndexesString);
                    writer.flush();
                    writer.println(deadlineString);
                    writer.flush();
                    writer.println(durationString);
                    writer.flush();
                    writer.println(customQuizQuestionValuesString);
                    writer.flush();

                    String flagError = null;
                    try {
                        flagError = reader.readLine();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (flagError.equals("T")) {
                        loadCreateCustomQuizPanel();
                        JOptionPane.showMessageDialog(null,
                                "Please enter valid responses!\n"
                                , "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (flagError.equals("F")) {
                        loadTeacherPanel();
                        JOptionPane.showMessageDialog(null,
                                "Quiz Created!\n" +
                                        "Returning to Teacher Main Menu",
                                "Created!", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }
    };

    ActionListener addFreeResponseListener = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource() == addFreeResponseSelect) {
                addFreeResponsePanel.setVisible(false);
                String prompt = freeResponsePromptText.getText();
                String answer = freeResponseAnswerText.getText();

                writer.println(prompt);
                writer.flush();
                writer.println(answer);
                writer.flush();

                loadTeacherPanel();
                JOptionPane.showMessageDialog(null, "Free response question added!"
                        , "Question added", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    };

    ActionListener addTrueFalseListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addTrueFalseSelect) {
                addTrueFalsePanel.setVisible(false);
                String prompt = trueFalsePromptText.getText();
                String answer = trueFalseAnswerChoice.getItemAt(trueFalseAnswerChoice.getSelectedIndex());
                writer.println(prompt);
                writer.flush();
                writer.println(answer);
                writer.flush();
                loadTeacherPanel();
                JOptionPane.showMessageDialog(null, "Question added!\n" +
                        "Returning to Teacher Main Menu", "Added!", JOptionPane.INFORMATION_MESSAGE);
            }
    }
};

    ActionListener takeQuizListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == chooseQuizSelect) {
                takeQuizPanel.setVisible(false);
                int quizID = Integer.parseInt((String) chooseQuizOptions.getSelectedItem());
                System.out.printf("TEST QUIZ %d\n", quizID);
                writer.println(quizID);
                writer.flush();
                loadQuizTakingFrame(quizID);
            }
        }
    };

    ActionListener quizTakingListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == submitButton) {
                String[] responses = new String[currentQuiz.getNumberQuestions()];
                for (int i = 0 ; i < responses.length ; i++) {
                    if (quizQuestions[i].getType().equals("M")) {
                        responses[i] = String.valueOf(
                                ((JComboBox<String>) quizResponses[quizResponseCounter]).getSelectedIndex() + 1);
                        quizResponseCounter++;
                    } else if (quizQuestions[i].getType().equals("TF")) {
                        responses[i] = String.valueOf(
                                ((JComboBox<String>) quizResponses[quizResponseCounter]).getSelectedItem());
                        if (responses[i].equals("True")) {
                            responses[i] = "T";
                        } else if (responses[i].equals("false")) {
                            responses[i] = "F";
                        }
                        quizResponseCounter++;
                    } else if (quizQuestions[i].getType().equals("R")) {
                        responses[i] = String.valueOf(
                                ((JTextField) quizResponses[quizResponseCounter]).getText());
                        quizResponseCounter++;
                    }
                }
                quizResponseCounter = 0;
                for (int i = 0 ; i < responses.length; i++) {
                    System.out.println(responses[i]);
                    writer.println(responses[i]);
                    writer.flush();
                }
                quizTakingFrame.dispose();
                JOptionPane.showMessageDialog(null, "Quiz submitted!"
                        , "Quiz submitted", JOptionPane.INFORMATION_MESSAGE);

            }
        }
    };




    /** ACTION LISTENERS */

    /** LOAD PANELS */
    public void reload() {
        System.out.println(currentPage);
        switch (currentPage) {
            case 1:
                loadInitialPanel();
                break;
            case 2:
                loadCreateAccountPanel();
                break;
            case 3:
                loadLogInPanel();
                break;
            case 4:
                loadStudentPanel();
                break;
            case 5:
                loadTeacherPanel();
                break;
            case 6:
                loadModifyPanel(currentAccountType);
                break;
            case 7:
                loadQuestionPoolPanel();
                break;
            case 8:
                loadAddQuestionPanel();
                break;
            case 9:
                loadAddMultipleChoicePanel();
                break;
            case 10:
                loadMultipleChoiceOptionPanel(currentNumberOptions);
                break;
            case 11:
                loadAddFreeResponsePanel();
                break;
            case 12:
                loadCreateQuizPanel();
                break;
            case 13:
                loadTrueFalsePanel();
                break;
            case 14:
                takeQuizPanel.setVisible(false);
                reloadPanel.setVisible(false);
                loadTakeQuizPanel();
                break;
            default:
                break;
        }
    }

    public void loadInitialPanel() {
        currentPage = 1;

        initialPanel = new JPanel();
        JLabel initialPrompt = new JLabel("Please select an option.");
        createButton = new JButton("Create Account");
        logInButton = new JButton("Log In");
        exitButton = new JButton("Exit");
        initialPanel.add(initialPrompt);
        initialPanel.add(createButton);
        initialPanel.add(logInButton);
        initialPanel.add(exitButton);
        createButton.addActionListener(initialListener);
        logInButton.addActionListener(initialListener);
        exitButton.addActionListener(initialListener);

        initialPanel.setVisible(true);
    }

    public void loadCreateAccountPanel() {
        currentPage = 2;

        createPanel = new JPanel();
        JLabel createLabel = new JLabel("Please enter a new username.");
        createButton2 = new JButton("Create account");
        createPanel.add(createLabel);
        teacherStudent = new JComboBox();
        teacherStudent.addItem("Teacher");
        teacherStudent.addItem("Student");
        usernameText = new JTextField(20);
        createPanel.add(usernameText);
        createPanel.add(teacherStudent);
        createPanel.add(createButton2);
        createButton2.addActionListener(createListener);

        content.add(createPanel, BorderLayout.CENTER);
        createPanel.setVisible(true);
    }

    public void loadLogInPanel() {
        currentPage = 3;

        logInPanel = new JPanel();
        JLabel logInLabel = new JLabel("Please enter your username.");
        logInButton2 = new JButton("Log in");
        usernameText = new JTextField(20);

        logInPanel.add(logInLabel);
        logInPanel.add(usernameText);
        logInPanel.add(logInButton2);
        logInButton2.addActionListener(logInListener);

        /** go back & reload panel
        reloadPanel = new JPanel();
        JButton reloadButton = new JButton("Reload");
        reloadPanel.add(reloadButton);
        reloadButton.addActionListener(reloadListener);
        content.add(reloadPanel, BorderLayout.SOUTH);
        reloadPanel.setVisible(true);
         */

        content.add(logInPanel, BorderLayout.CENTER);
        logInPanel.setVisible(true);
    }

    public void loadStudentPanel() {
        currentPage = 4;

        studentPanel = new JPanel();
        studentMenuOptions = new JComboBox();
        studentMenuOptions.addItem("Take quiz");
        studentMenuOptions.addItem("View submissions");
        studentMenuOptions.addItem("Modify account");
        studentMenuOptions.addItem("Delete account");
        studentMenuSelect = new JButton("Select");

        studentPanel.add(studentMenuPrompt);
        studentPanel.add(studentMenuOptions);
        studentPanel.add(studentMenuSelect);
        studentPanel.add(exitButton);

        studentMenuSelect.addActionListener(studentListener);
        content.add(studentPanel, BorderLayout.CENTER);
        studentPanel.setVisible(true);

    }

    public void loadTeacherPanel() {
        currentPage = 5;

        teacherPanel = new JPanel();
        teacherMenuSelect = new JButton("Select");

        teacherMenuOptions = new JComboBox();
        teacherMenuOptions.addItem("Create quiz");
        teacherMenuOptions.addItem("Delete quiz");
        teacherMenuOptions.addItem("Modify quiz");
        teacherMenuOptions.addItem("View student submissions");
        teacherMenuOptions.addItem("Edit question pool");
        teacherMenuOptions.addItem("Modify account");
        teacherMenuOptions.addItem("Delete account");

        teacherPanel.add(teacherMenuPrompt);
        teacherPanel.add(teacherMenuOptions);
        teacherPanel.add(teacherMenuSelect);
        teacherPanel.add(exitButton);
        exitButton.addActionListener(teacherListener);
        teacherMenuSelect.addActionListener(teacherListener);

        content.add(teacherPanel, BorderLayout.CENTER);
        teacherPanel.setVisible(true);
    }

    public void loadModifyPanel(String type) {
        currentPage = 6;

        modifyPanel = new JPanel();
        usernameText = new JTextField(20);

        modifyPanel.add(modifyPrompt);
        modifyPanel.add(usernameText);
        if (type.equals("T")) {
            modifyButtonT = new JButton("Modify");
            modifyButtonT.addActionListener(modifyListener);
            modifyPanel.add(modifyButtonT);
        } else {
            modifyButtonS = new JButton("Modify");
            modifyButtonS.addActionListener(modifyListener);
            modifyPanel.add(modifyButtonS);
        }

        content.add(modifyPanel, BorderLayout.CENTER);
        modifyPanel.setVisible(true);
    }


    public void loadQuestionPoolPanel() {
        currentPage = 7;

        questionPoolPanel = new JPanel();
        questionPoolOptions = new JComboBox();
        questionPoolOptions.addItem("Add question");
        questionPoolOptions.addItem("Delete question");
        questionPoolOptions.addItem("Modify question");
        questionPoolSelect = new JButton("Select");

        questionPoolPanel.add(questionPoolPrompt);
        questionPoolPanel.add(questionPoolOptions);
        questionPoolPanel.add(questionPoolSelect);

        questionPoolSelect.addActionListener(questionPoolListener);

        content.add(questionPoolPanel, BorderLayout.CENTER);
        questionPoolPanel.setVisible(true);
    }

    public void loadAddQuestionPanel() {
        currentPage = 8;

        addQuestionPanel = new JPanel();

        addQuestionOptions = new JComboBox();
        addQuestionOptions.addItem("Multiple choice");
        addQuestionOptions.addItem("Free response");
        addQuestionOptions.addItem("True/False");
        addQuestionSelect = new JButton("Select");
        addQuestionSelect.addActionListener(addQuestionListener);

        addQuestionPanel.add(addQuestionPrompt);
        addQuestionPanel.add(addQuestionOptions);
        addQuestionPanel.add(addQuestionSelect);

        content.add(addQuestionPanel, BorderLayout.CENTER);
        addQuestionPanel.setVisible(true);
    }

    public void loadDeleteQuestionPanel() {
        deleteQuestionPanel = new JPanel();

        ArrayList<String> questions = tools.getQuestionList();

        deleteQuestionOptions = new JComboBox();
        for(String question: questions)
            if (question.split(",")[2].equals("M")) {
                System.out.println("TEST POINT 6");
                deleteQuestionOptions.addItem(question.split(",")[0].split(":")[0]);
            } else {
                deleteQuestionOptions.addItem(question.split(",")[0]);
            }


        deleteQuestionSelect = new JButton("Delete");
        deleteQuestionSelect.addActionListener(deleteQuestionListener);

        deleteQuestionPanel.add(deleteQuestionPrompt);
        deleteQuestionPanel.add(deleteQuestionOptions);
        deleteQuestionPanel.add(deleteQuestionSelect);

        content.add(deleteQuestionPanel, BorderLayout.CENTER);
        deleteQuestionPanel.setVisible(true);
    }

    public void loadAddMultipleChoicePanel() {
        currentPage = 9;

        addMultipleChoicePanel = new JPanel();

        multipleChoicePromptText = new JTextField(30);
        numberMultipleChoiceOptions = new JComboBox();
        numberMultipleChoiceOptions.addItem("1");
        numberMultipleChoiceOptions.addItem("2");
        numberMultipleChoiceOptions.addItem("3");
        numberMultipleChoiceOptions.addItem("4");
        addMultipleChoiceSelect = new JButton("Add");
        addMultipleChoiceSelect.addActionListener(addMultipleChoiceListener);

        addMultipleChoicePanel.add(addMultipleChoicePrompt);
        addMultipleChoicePanel.add(multipleChoicePromptText);
        addMultipleChoicePanel.add(numberMultipleChoiceOptionsPrompt);
        addMultipleChoicePanel.add(numberMultipleChoiceOptions);
        addMultipleChoicePanel.add(addMultipleChoiceSelect);

        content.add(addMultipleChoicePanel, BorderLayout.CENTER);
        addMultipleChoicePanel.setVisible(true);
    }

    public void loadMultipleChoiceOptionPanel(int numberOptions) {
        currentPage = 10;

        multipleChoiceOptionPanel = new JPanel();
        multipleChoiceOptionPanel.setLayout(new GridLayout(numberOptions + 20, 2));
        optionInputs = new JTextField[numberOptions];
        correctAnswer = new JComboBox<>();

        for (int i = 0 ; i < numberOptions ; i++) {
            String multipleChoiceOptionPanelPrompt = String.format("What will be the option prompt for option %d?",
                    i + 1);
            multipleChoiceOptionPrompt = new JLabel(multipleChoiceOptionPanelPrompt);
            optionInputs[i] = new JTextField(20);
            multipleChoiceOptionPanel.add(multipleChoiceOptionPrompt);
            multipleChoiceOptionPanel.add(optionInputs[i]);
            correctAnswer.addItem(String.valueOf(i + 1));
        }
        multipleChoiceOptionPromptSubmit = new JButton("Submit");
        multipleChoiceOptionPromptSubmit.addActionListener(multipleChoiceOptionListener);


        multipleChoiceOptionPanel.add(correctAnswerPrompt);
        multipleChoiceOptionPanel.add(correctAnswer);
        multipleChoiceOptionPanel.add(multipleChoiceOptionPromptSubmit);

        content.add(multipleChoiceOptionPanel, BorderLayout.CENTER);
        multipleChoiceOptionPanel.setVisible(true);
    }

    public void loadCreateQuizPanel() {
        currentPage = 12;

        createQuizPanel = new JPanel();

        createQuizType = new JComboBox<>();
        createQuizType.addItem("Random quiz");
        createQuizType.addItem("Custom quiz");

        createQuizTypeSelect = new JButton("Select");
        createQuizTypeSelect.addActionListener(createQuizListener);

        createQuizPanel.add(createQuizTypePrompt);
        createQuizPanel.add(createQuizType);
        createQuizPanel.add(createQuizTypeSelect);

        content.add(createQuizPanel, BorderLayout.CENTER);
        createQuizPanel.setVisible(true);
    }

    public void loadCreateRandomQuizPanel() {
        createRandomQuizPanel = new JPanel();
        createRandomQuizPanel.setLayout(new GridLayout(36, 1));

        questionValueOption = new JComboBox<>();
        questionValueOption.addItem("1");
        questionValueOption.addItem("2");
        questionValueOption.addItem("3");
        questionValueOption.addItem("4");
        questionValueOption.addItem("5");
        createRandomQuizPanel.add(numberOfQuestionsLabel);

        numberOfQuestionsOptions = new JComboBox<>();
        for (int i = 0; i < tools.getQuestionPoolSize(); i++)
            numberOfQuestionsOptions.addItem(i + 1);
        createRandomQuizPanel.add(numberOfQuestionsOptions);
        createRandomQuizPanel.add(questionValueLabel);
        createRandomQuizPanel.add(questionValueOption);
        createRandomQuizPanel.add(quizDeadlineLabel);
        quizDeadlineTextField = new JTextField(30);
        createRandomQuizPanel.add(quizDeadlineTextField);
        createRandomQuizPanel.add(quizDurationLabel);
        quizDurationTextField = new JTextField(30);
        createRandomQuizPanel.add(quizDurationTextField);
        createRandomQuizPanel.add(createRandomQuizButton);

        content.add(createRandomQuizPanel);
        createRandomQuizPanel.setVisible(true);
    }

    public void loadCreateCustomQuizPanel() {
        createCustomQuizPanel = new JPanel();
        createCustomQuizPanel.setLayout(new GridLayout(36, 1));

        createCustomQuizPanel.add(numberOfQuestionsLabel);
        numberOfQuestionsOptions = new JComboBox<>();
        for (int i = 0; i < tools.getQuestionPoolSize(); i++)
            numberOfQuestionsOptions.addItem(i + 1);
        createCustomQuizPanel.add(numberOfQuestionsOptions);
        createCustomQuizPanel.add(quizDeadlineLabel);
        quizDeadlineTextField = new JTextField(30);
        createCustomQuizPanel.add(quizDeadlineTextField);
        createCustomQuizPanel.add(quizDurationLabel);
        quizDurationTextField = new JTextField(30);
        createCustomQuizPanel.add(quizDurationTextField);
        questionList = new JComboBox<>();
        ArrayList<String> questions = tools.getQuestionList();

        int qIndex = 1;
        for(String question: questions)
            if (question.split(",")[2].equals("M")) {
                questionList.addItem(qIndex + ": " +
                        question.split(",")[0].split(":")[0]);
                qIndex++;
            } else {
                questionList.addItem(qIndex + ": " +
                        question.split(",")[0]);
                qIndex++;
            }
        customQuizQuestionIndexes = new JTextField(30);
        customQuizQuestionValues = new JTextField(30);

        createCustomQuizPanel.add(new JLabel("Full list of Questions for reference: "));
        createCustomQuizPanel.add(questionList);
        createCustomQuizPanel.add(new JLabel("Enter question indexes 1 by 1, separated by commas"));
        createCustomQuizPanel.add(customQuizQuestionIndexes);
        createCustomQuizPanel.add(new JLabel("Enter question point values 1 by 1, separated by commas"));
        createCustomQuizPanel.add(customQuizQuestionValues);
        createCustomQuizPanel.add(createCustomQuizButton);

        content.add(createCustomQuizPanel);
        createCustomQuizPanel.setVisible(true);
    }



    public void loadAddFreeResponsePanel() {
        currentPage = 11;

        addFreeResponsePanel = new JPanel();
        addFreeResponsePanel.setLayout(new GridLayout(28, 1));

        freeResponsePromptText = new JTextField(40);
        freeResponseAnswerText = new JTextField(30);
        addFreeResponseSelect = new JButton("Add");
        addFreeResponseSelect.addActionListener(addFreeResponseListener);

        addFreeResponsePanel.add(addFreeResponsePrompt);
        addFreeResponsePanel.add(freeResponsePromptText);
        addFreeResponsePanel.add(addFreeResponseAnswer);
        addFreeResponsePanel.add(freeResponseAnswerText);
        addFreeResponsePanel.add(addFreeResponseSelect);

        content.add(addFreeResponsePanel, BorderLayout.CENTER);
        addFreeResponsePanel.setVisible(true);
    }

    public void loadTrueFalsePanel() {
        currentPage = 13;

        addTrueFalsePanel = new JPanel();

        trueFalsePromptText = new JTextField(30);
        trueFalseAnswerChoice = new JComboBox<>();
        trueFalseAnswerChoice.addItem("T");
        trueFalseAnswerChoice.addItem("F");
        addTrueFalseSelect = new JButton("Add");
        addTrueFalseSelect.addActionListener(addTrueFalseListener);

        addTrueFalsePanel.add(addTrueFalsePrompt);
        addTrueFalsePanel.add(trueFalsePromptText);
        addTrueFalsePanel.add(answerTrueFalseOptionsPrompt);
        addTrueFalsePanel.add(trueFalseAnswerChoice);
        addTrueFalsePanel.add(addTrueFalseSelect);

        content.add(addTrueFalsePanel, BorderLayout.CENTER);
        addTrueFalsePanel.setVisible(true);
    }

    public void loadTakeQuizPanel() {
        currentPage = 14;

        takeQuizPanel = new JPanel();

        chooseQuizSelect = new JButton("Select");
        chooseQuizSelect.addActionListener(takeQuizListener);

        chooseQuizOptions = new JComboBox<>();
        int[] quizIDs = tools.retrieveQuizIDs();
        for (int i = 0 ; i < quizIDs.length ; i++) {
            chooseQuizOptions.addItem(String.valueOf(quizIDs[i]));
        }

        takeQuizPanel.add(chooseQuiz);
        takeQuizPanel.add(chooseQuizOptions);
        takeQuizPanel.add(chooseQuizSelect);

        reloadPanel = new JPanel();
        reloadPanel.add(reloadButton);
        content.add(reloadPanel, BorderLayout.SOUTH);
        reloadPanel.setVisible(true);

        content.add(takeQuizPanel, BorderLayout.CENTER);
        takeQuizPanel.setVisible(true);
    }


        public void loadQuizTakingFrame(int quizID) {
            /** create JFrame and title */
            quizTakingFrame = new JFrame();
            quizTakingFrame.setTitle("QUIZ");
            quizTakingFrame.setSize(1000, 1000);
            quizTakingFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
            quizTakingFrame.setLocationRelativeTo(null);

            /** Create frame and set it visible*/
            quizContent = quizTakingFrame.getContentPane();
            quizContent.setLayout(new BorderLayout());

            quizTakingFrame.setVisible(true);
            quizTakingPanel = new JPanel();
            quizTakingPanel.setLayout(new GridLayout(30, 2));

            currentQuiz = student.loadQuiz(quizID);
            quizQuestions = currentQuiz.loadQuizQuestions();

            /** Quiz Info Panel */
            JPanel quizInfoPanel = new JPanel();
            quizInfoPanel.setLayout(new GridLayout(5, 1));
            JLabel quizIDInfo = new JLabel("Quiz number: " + currentQuiz.getQuizID());
            JLabel numberQuestionsInfo = new JLabel("Number of questions: " +
                    currentQuiz.getNumberQuestions());
            JLabel deadlineInfo = new JLabel("Quiz deadline: " +
                    currentQuiz.getDeadline());
            JLabel durationInfo = new JLabel("Quiz duration: " +
                    currentQuiz.getDuration());
            JLabel totalPointsInfo = new JLabel("Quiz total points: " +
                    currentQuiz.getTotalPoints());
            quizInfoPanel.add(quizIDInfo);
            quizInfoPanel.add(numberQuestionsInfo);
            quizInfoPanel.add(deadlineInfo);
            quizInfoPanel.add(durationInfo);
            quizInfoPanel.add(totalPointsInfo);

            quizContent.add(quizInfoPanel, BorderLayout.NORTH);
            quizInfoPanel.setVisible(true);

            /** Quiz Questions Panel */
            JPanel quizTakingQuestionsPanel = new JPanel();
            quizTakingQuestionsPanel.setLayout(new GridLayout(20, 2));
            for (int i = 0 ; i < quizQuestions.length ; i++) {
                JLabel questionPrompt = new JLabel(quizQuestions[i].getPrompt());
                quizTakingQuestionsPanel.add(questionPrompt);
                if (quizQuestions[i].getType().equals("R")) {
                    JTextField response = new JTextField(30);
                    quizResponses[quizResponseCounter] = response;
                    quizResponseCounter++;
                    quizTakingQuestionsPanel.add(response);
                } else if (quizQuestions[i].getType().equals("TF")) {
                    JComboBox<String> response = new JComboBox<>();
                    response.addItem("True");
                    response.addItem("False");
                    quizResponses[quizResponseCounter] = response;
                    quizResponseCounter++;
                    quizTakingQuestionsPanel.add(response);
                } else if (quizQuestions[i].getType().equals("M")) {
                    String[] options = quizQuestions[i].getMultipleOptions();
                    JComboBox<String> response = new JComboBox<>();
                    for (int j = 0 ; j < options.length ; j++) {
                        response.addItem(options[j]);
                    }
                    quizResponses[quizResponseCounter] = response;
                    quizResponseCounter++;
                    quizTakingQuestionsPanel.add(response);
                }
            }
            quizResponseCounter = 0;
            submitButton = new JButton("Submit quiz");
            quizTakingQuestionsPanel.add(submitButton);
            submitButton.addActionListener(quizTakingListener);
            quizContent.add(quizTakingQuestionsPanel, BorderLayout.SOUTH);
            quizTakingQuestionsPanel.setVisible(true);

        }
        /** LOAD PANELS */

    public void run() {

        String hostname = "localhost";
        int port = 4242;

        try {
            socket = new Socket(hostname, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }





        /** create JFrame and title */
        frame = new JFrame();
        frame.setTitle("Quiz");
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        /** Create frame and set it visible*/
        content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        loadInitialPanel();
        content.add(initialPanel, BorderLayout.CENTER);

        /** reload buttons */
        reloadButton = new JButton("Reload");
        reloadButton.addActionListener(reloadListener);

        frame.setVisible(true);

        /** RANDOM AND CUSTOM QUIZ BUTTON ACTION LISTENER*/
        createRandomQuizButton.addActionListener(createRandomQuizListener);
        createCustomQuizButton.addActionListener(createCustomQuizListener);
    }
}
