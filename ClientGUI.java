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

    Account newAccount;
    Socket socket = null;
    BufferedReader reader = null;
    PrintWriter writer = null;
    JFrame frame;
    Container content;
    JComboBox<String> teacherStudent;
    Tools tools = new Tools();


    /** PANELS */
    JPanel initialPanel;
    JPanel createPanel;
    JPanel logInPanel;
    JPanel studentPanel;
    JPanel teacherPanel;
    
    
    /** Initial Buttons */
    JButton createButton;
    JButton createButton2;
    JButton logInButton;
    JButton logInButton2;
    JButton exitButton;
    
    /** create account buttons and text fields */
    JTextField usernameText;

    public static void main(String[] args) {
        //////////////////////////////////////////////////////////////////

        SwingUtilities.invokeLater(new ClientGUI());
    }


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
                    System.out.println("TEST POINT 8");
                    logged = reader.readLine();
                    accountType = reader.readLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println(logged);
                logInPanel.setVisible(false);
                if (logged.equals("F")) {
                    JOptionPane.showMessageDialog(null, "This account doesn't exist!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    initialPanel.setVisible(true);
                } else if (logged.equals("T")) {
                    if (accountType.equals("student")) {
                        JOptionPane.showMessageDialog(null, "Logged in!",
                                "Logged in",
                                JOptionPane.INFORMATION_MESSAGE);
                        studentPanel.setVisible(true);
                    } else if (accountType.equals("teacher")) {
                        JOptionPane.showMessageDialog(null, "Logged in!",
                                "Logged in",
                                JOptionPane.INFORMATION_MESSAGE);
                        teacherPanel.setVisible(true);
                    }
                }
            }
        }
    };

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
