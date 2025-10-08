package view;

import javax.swing.*;

public class Window extends JFrame {

    private Panel panel;

    public Window(int width, int height) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("PGRF1 2024/2025");

        panel = new Panel(width, height);
        add(panel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        panel.setFocusable(true);
        panel.grabFocus();
    }


}
