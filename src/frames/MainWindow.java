package frames;

import panels.DrawingPanel;
import panels.MenuBar;
import panels.MenuPanel;

import javax.swing.*;
import java.awt.*;

public final class MainWindow extends JFrame implements Runnable {
    public static final JMenuBar menuBar = MenuBar.getMenuBar();
    public static final JPanel menuPanel = MenuPanel.getMenuPanel();
    public static final DrawingPanel drawingPanel = DrawingPanel.getDrawingPanel();

    public MainWindow() {
        super("Максимальный поток в сети");
    }

    @Override
    public void run() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setLocation(200, 100);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(menuPanel, BorderLayout.NORTH);
        getContentPane().add(drawingPanel, BorderLayout.CENTER);

        setJMenuBar(menuBar);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}