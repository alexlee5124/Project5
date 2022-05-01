import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Threading extends JComponent implements Runnable{

    /** INITIAL THREADING FRAME */
    JFrame initialFrame;
    Container threadingContent;
    JPanel threadingPanel;
    JButton newWindow;

    ActionListener threadingListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == newWindow) {
                SwingUtilities.invokeLater(new ClientGUI());
            }
        }
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Threading());
    }

    public void run() {
        /** CREATE INITIAL FRAME FOR THREADING CLIENT GUI */
        initialFrame = new JFrame();
        initialFrame.setTitle("Welcome");
        initialFrame.setSize(200, 100);
        initialFrame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        initialFrame.setLocationRelativeTo(null);

        threadingContent = initialFrame.getContentPane();
        threadingContent.setLayout(new BorderLayout());

        threadingPanel = new JPanel();
        newWindow = new JButton("New Window");
        newWindow.addActionListener(threadingListener);
        threadingPanel.add(newWindow, BorderLayout.CENTER);
        threadingPanel.setVisible(true);

        threadingContent.add(threadingPanel, BorderLayout.CENTER);

        initialFrame.setVisible(true);
    }
}
