import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JComponent implements Runnable {
    Socket socket = null;
    BufferedReader reader = null;
    PrintWriter writer = null;
    JFrame frame;
    Container content;
    JComboBox<String> teacherStudent;


    /** PANELS */
    JPanel initialPanel;
    JPanel createPanel;
    JPanel logInPanel;
    JPanel studentPanel;
    JPanel teacherPanel;
    JPanel modifyPanel;
    JPanel questionPoolPanel;
    JPanel addQuestionPanel;
    JPanel addMultipleChoicePanel;
    JPanel multipleChoiceOptionPanel;
    JPanel addFreeResponsePanel;
    JPanel createQuizPanel;
    JPanel addTrueFalsePanel;
    JPanel createRandomQuizPanel;
    JPanel createCustomQuizPanel;

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
    JComboBox<String> teacherMenuOptions= new JComboBox();


    /** Student panel components */
    JLabel studentMenuPrompt = new JLabel("What would you like to do?");
    JButton studentMenuSelect;
    JComboBox<String> studentMenuOptions = new JComboBox();

    /** Modify panel components*/
    JButton modifyButtonT;
    JButton modifyButtonS;
    JLabel modifyPrompt = new JLabel("Enter a new username");

    /** Question pool panel components*/
    JLabel questionPoolPrompt = new JLabel("What would you like to do?");
    JComboBox<String> questionPoolOptions = new JComboBox();
    JButton questionPoolSelect;

    /** Create Random Quiz panel components*/
    /** Issue with question point values:
     * Program prints prompt to enter question point values to terminal from Teacher class
     *  - Should all questions be worth same amount in a random quiz?
     *  - Move from teacher to ServerThread?
     * */
    JLabel numberOfQuestionsLabel = new JLabel("Number of Questions");
    JComboBox<Integer> numberOfQuestionsOptions = new JComboBox<>();
    JLabel questionValueLabel = new JLabel("How many points is each question worth?");
    JTextField questionValueTextField = new JTextField(30);
    JLabel quizDeadlineLabel = new JLabel("Deadline (YYYY-MM-DD HH:MM)");
    JTextField quizDeadlineTextField = new JTextField(30);
    JLabel quizDurationLabel = new JLabel("Duration (minutes)");
    JTextField quizDurationTextField = new JTextField(30);
    JButton createRandomQuizButton = new JButton("Create Quiz");

    /** Create Custom Quiz panel components*/
    JComboBox<String> questionList = new JComboBox<>();
    JButton createCustomQuizButton = new JButton("Create Quiz");
    JTextField customQuizQuestionIndexes = new JTextField(30);
    JTextField customQuizQuestionValues = new JTextField(30);

    /** Add question panel components*/
    JLabel addQuestionPrompt = new JLabel("What is the question type?");
    JComboBox<String> addQuestionOptions = new JComboBox();
    JButton addQuestionSelect;

    /** Add multiple choice panel components*/
    JLabel addMultipleChoicePrompt = new JLabel("What is the question prompt?");
    JTextField multipleChoicePromptText;
    JLabel numberMultipleChoiceOptionsPrompt = new JLabel("How many options will this question have?");
    JComboBox<String> numberMultipleChoiceOptions = new JComboBox();
    JButton addMultipleChoiceSelect;
    int multipleChoiceOptions;

    /** Add multiple choice options panel components*/
    JLabel multipleChoiceOptionPrompt;
    JButton multipleChoiceOptionPromptSubmit;
    JTextField[] optionInputs;
    JComboBox<String> correctAnswer = new JComboBox<>();
    JLabel correctAnswerPrompt = new JLabel("What is the correct option?");

    /** Create quiz panel components*/
    JLabel createQuizTypePrompt = new JLabel("What kind of quiz would you like to create?");
    JComboBox<String> createQuizType = new JComboBox<>();
    JButton createQuizTypeSelect;

    /**Add free response panel components*/
    JLabel addFreeResponsePrompt = new JLabel("What is the question prompt?");
    JTextField freeResponsePromptText;
    JLabel addFreeResponseAnswer = new JLabel("What is the answer to your prompt?");
    JTextField freeResponseAnswerText;
    JButton addFreeResponseSelect;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ClientGUI());
    }

    /** Add true false choice panel components*/
    JLabel addTrueFalsePrompt = new JLabel("What is the question prompt?");
    JTextField trueFalsePromptText;
    JLabel answerTrueFalseOptionsPrompt = new JLabel("What is the correct option?");
    JComboBox<String> trueFalseAnswerChoice = new JComboBox<>();
    JButton addTrueFalseSelect;

    /** ACTION LISTENERS */
    ActionListener initialListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createButton) {
                writer.println(1);
                writer.flush();
                initialPanel.setVisible(false);
                createAccount();
            } else if (e.getSource() == logInButton) {
                writer.println(2);
                writer.flush();
                initialPanel.setVisible(false);
                logIn();
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
                    System.out.println("Account created!");
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
                        loadStudentPanel();
                        studentPanel.setVisible(true);
                        JOptionPane.showMessageDialog(null, "Student: logged in!",
                                "Logged in",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else if (accountType.equals("teacher")) {
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
                        loadDeleteQuizPanel();
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

    ActionListener deleteQuizListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == quizDeleteSelect)
            {
                deleteQuizPanel.setVisible(false);
                String selectedQuiz = (String) quizSelectionBox.getSelectedItem();
                System.out.println(selectedQuiz.substring(0, selectedQuiz.indexOf(":")));
                writer.println(selectedQuiz.substring(0, selectedQuiz.indexOf(":")));
                writer.flush();

                loadTeacherPanel();
                JOptionPane.showMessageDialog(null, "Quiz Deleted!\n" +
                        "Returning to Teacher Main Menu", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    };

    ActionListener createRandomQuizListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == createRandomQuizButton)
            {
                writer.println(1);
                writer.flush();
                createRandomQuizPanel.setVisible(false);
                int numQuestions = numberOfQuestionsOptions.getSelectedIndex() + 1;
                int questionValue = Integer.parseInt(questionValueTextField.getText());
                writer.println(numQuestions);
                writer.flush();
                writer.println(questionValue);
                writer.flush();
                writer.println(quizDeadlineTextField.getText());
                writer.flush();
                writer.println(quizDurationTextField.getText());
                writer.flush();

                for (int i = 0; i < numQuestions; i++)
                {
                    writer.println(questionValue);
                    writer.flush();
                }

                loadTeacherPanel();
                JOptionPane.showMessageDialog(null, "Quiz Created!\n" +
                        "Returning to Teacher Main Menu", "Created!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    };

    ActionListener createCustomQuizListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == createCustomQuizButton)
            {
                writer.println(2);
                writer.flush();
                createCustomQuizPanel.setVisible(false);
                int numQuestions = numberOfQuestionsOptions.getSelectedIndex() + 1;
                writer.println(numQuestions);
                writer.flush();
                String[] questionIndexes = customQuizQuestionIndexes.getText().split(",");
                for (String questionIndex : questionIndexes)
                {
                    writer.println(questionIndex);
                    writer.flush();
                }
                writer.println(quizDeadlineTextField.getText());
                writer.flush();
                writer.println(quizDurationTextField.getText());
                writer.flush();
                writer.println(customQuizQuestionValues.getText());
                writer.flush();

                loadTeacherPanel();
                JOptionPane.showMessageDialog(null, "Quiz Created!\n" +
                        "Returning to Teacher Main Menu", "Created!", JOptionPane.INFORMATION_MESSAGE);
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
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
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
                int quizType = createQuizType.getSelectedIndex() + 1;
                createQuizPanel.setVisible(false);

                if (quizType == 1)
                    loadCreateRandomQuizPanel();
                if (quizType == 2)
                    loadCreateCustomQuizPanel();
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


    /** ACTION LISTENERS */



    /** LOAD PANELS */

    public void createAccount() {
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

    public void logIn() {
        logInPanel = new JPanel();
        JLabel logInLabel = new JLabel("Please enter your username.");
        logInButton2 = new JButton("Log in");
        usernameText = new JTextField(20);

        logInPanel.add(logInLabel);
        logInPanel.add(usernameText);
        logInPanel.add(logInButton2);
        logInButton2.addActionListener(logInListener);

        content.add(logInPanel, BorderLayout.CENTER);
        logInPanel.setVisible(true);
    }

    public void loadTeacherPanel() {
        teacherPanel = new JPanel();
        teacherMenuSelect = new JButton("Select");

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

    public void loadStudentPanel() {
        studentPanel = new JPanel();
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

    JComboBox<String> quizSelectionBox = new JComboBox<>();
    JPanel deleteQuizPanel;
    JButton quizDeleteSelect;

    public void loadDeleteQuizPanel()
    {
        deleteQuizPanel = new JPanel();

        Tools tools = new Tools();
        ArrayList<String> quizList = tools.getQuizList();

        for (String quiz : quizList)
            quizSelectionBox.addItem(quiz);

        quizDeleteSelect = new JButton("Select");
        deleteQuizPanel.add(new JLabel("Select a quiz to delete"));
        deleteQuizPanel.add(quizSelectionBox);
        quizDeleteSelect.addActionListener(deleteQuizListener);
        deleteQuizPanel.add(quizDeleteSelect);
        content.add(deleteQuizPanel, BorderLayout.CENTER);
        deleteQuizPanel.setVisible(true);
    }

    public void loadQuestionPoolPanel() {
        questionPoolPanel = new JPanel();
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
        addQuestionPanel = new JPanel();

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

    public void loadAddMultipleChoicePanel() {
        addMultipleChoicePanel = new JPanel();

        multipleChoicePromptText = new JTextField(30);
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
        multipleChoiceOptionPanel = new JPanel();
        multipleChoiceOptionPanel.setLayout(new GridLayout(numberOptions + 20, 2));
        optionInputs = new JTextField[numberOptions];

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
        createQuizPanel = new JPanel();

        createQuizTypeSelect = new JButton("Select");
        createQuizTypeSelect.addActionListener(createQuizListener);

        createQuizPanel.add(createQuizTypePrompt);
        createQuizPanel.add(createQuizType);
        createQuizPanel.add(createQuizTypeSelect);

        content.add(createQuizPanel, BorderLayout.CENTER);
        createQuizPanel.setVisible(true);
    }

    public void loadCreateRandomQuizPanel()
    {
        Tools tools = new Tools();
        createRandomQuizPanel = new JPanel();
        createRandomQuizPanel.setLayout(new GridLayout(0, 1));
        createRandomQuizButton.addActionListener(createRandomQuizListener);
        createRandomQuizPanel.add(numberOfQuestionsLabel);
        for (int i = 0; i < tools.getQuestionPoolSize(); i++)
            numberOfQuestionsOptions.addItem(i + 1);
        createRandomQuizPanel.add(numberOfQuestionsOptions);
        createRandomQuizPanel.add(questionValueLabel);
        createRandomQuizPanel.add(questionValueTextField);
        createRandomQuizPanel.add(quizDeadlineLabel);
        createRandomQuizPanel.add(quizDeadlineTextField);
        createRandomQuizPanel.add(quizDurationLabel);
        createRandomQuizPanel.add(quizDurationTextField);
        createRandomQuizPanel.add(createRandomQuizButton);

        content.add(createRandomQuizPanel);
        createRandomQuizPanel.setVisible(true);
    }

    public void loadCreateCustomQuizPanel()
    {
        Tools tools = new Tools();
        createCustomQuizPanel = new JPanel();
        createCustomQuizPanel.setLayout(new GridLayout(0, 1));
        createCustomQuizButton.addActionListener(createCustomQuizListener);
        createCustomQuizPanel.add(numberOfQuestionsLabel);
        for (int i = 0; i < tools.getQuestionPoolSize(); i++)
            numberOfQuestionsOptions.addItem(i + 1);
        createCustomQuizPanel.add(numberOfQuestionsOptions);
        createCustomQuizPanel.add(quizDeadlineLabel);
        createCustomQuizPanel.add(quizDeadlineTextField);
        createCustomQuizPanel.add(quizDurationLabel);
        createCustomQuizPanel.add(quizDurationTextField);
        ArrayList<String> questions = tools.getQuestionList();
        int qIndex = 0;

        for (String question : questions)
        {
            questionList.addItem(qIndex + ": " + question);
            qIndex++;
        }

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
        addFreeResponsePanel = new JPanel();

        freeResponsePromptText = new JTextField(30);
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
        addTrueFalsePanel = new JPanel();

        trueFalsePromptText = new JTextField(30);
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



    /** LOAD PANELS */

    public void run() {
        /** Combo box Items */


        /** teacher panel combo box */
        teacherMenuOptions.addItem("Create quiz");
        teacherMenuOptions.addItem("Delete quiz");
        teacherMenuOptions.addItem("Modify quiz");
        teacherMenuOptions.addItem("View student submissions");
        teacherMenuOptions.addItem("Edit question pool");
        teacherMenuOptions.addItem("Modify account");
        teacherMenuOptions.addItem("Delete account");

        /** create quiz panel combo box */
        createQuizType.addItem("Random quiz");
        createQuizType.addItem("Custom quiz");



        /** Combo box Items */


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

        /** Initial Panel */
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

        /** Create content and add paint object to it */
        content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(initialPanel, BorderLayout.CENTER);

        frame.setVisible(true);

    }
}
