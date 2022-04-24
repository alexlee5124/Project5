import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
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




    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ClientGUI());
    }

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
                        break;
                    case 2:
                         break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
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


    /** ACTION LISTENERS */

    /** LOAD PANELS */

    public void loadInitialPanel() {
        initialPanel.add(exitButton);

    }

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

        /** Log in panel */

        /** Create content and add paint object to it */
        content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(initialPanel, BorderLayout.CENTER);

        frame.setVisible(true);

    }
}
