package GraphEditor;

import javax.swing.SwingUtilities;
import javax.swing.*;

public class Main {

    private static void initUI() {

        JFrame frame = new JFrame("Graph Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Panel());
        frame.setSize(1000,600);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initUI();
            }
        });
    }
}