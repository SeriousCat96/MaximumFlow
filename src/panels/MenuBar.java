package panels;

import frames.MainWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.NoSuchElementException;

public final class MenuBar extends JMenuBar {
    private static final MenuBar menu;
    private static JMenuBar menuBar;

    static {
        menu = new MenuBar();
    }

    private MenuBar() {
        Font font = new Font("Verdana", Font.PLAIN, 12);

        JFileChooser chooser = new JFileChooser(new File("").getAbsolutePath());
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "GRAPH file", "graph");
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);

        JMenuItem openItem = new JMenuItem("Открыть");
        openItem.setFont(font);
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                InputEvent.CTRL_MASK));
        JMenuItem saveAsItem = new JMenuItem("Сохранить как");
        saveAsItem.setFont(font);
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.CTRL_MASK));

        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.setFont(font);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                InputEvent.ALT_MASK));
        exitItem.addActionListener(event -> System.exit(0));

        JMenuItem eraseItem = new JMenuItem("Очистить");
        eraseItem.setFont(font);
        eraseItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                InputEvent.CTRL_MASK));
        eraseItem.addActionListener(event -> {
            MainWindow.drawingPanel.clear();
            MainWindow.drawingPanel.repaint();
        });

        JMenuItem addNodeItem = new JMenuItem("Добавить вершину");
        addNodeItem.setFont(font);
        addNodeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                InputEvent.CTRL_MASK));
        addNodeItem.addActionListener(event -> new Thread(() -> {
            try {
                String resultX = JOptionPane.showInputDialog(MainWindow.drawingPanel,
                        "Введите координату X:",
                        "Добавление вершины",
                        JOptionPane.PLAIN_MESSAGE);
                if (resultX != null) {
                    int x = Integer.parseInt(resultX);
                    String resultY = JOptionPane.showInputDialog(MainWindow.drawingPanel,
                            "Введите координату Y:",
                            "Добавление вершины",
                            JOptionPane.PLAIN_MESSAGE);
                    if (resultY != null) {
                        int y = Integer.parseInt(resultY);
                        MainWindow.drawingPanel.addNode(x, y);
                        MainWindow.drawingPanel.repaint();
                    }
                }
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Неверный формат числа",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Ошибка при добавлении вершины",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }).start());

        JMenuItem delNodeItem = new JMenuItem("Удалить вершину");
        delNodeItem.setFont(font);
        delNodeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
                InputEvent.CTRL_MASK));
        delNodeItem.addActionListener(event -> new Thread(() -> {
            try {
                String res = JOptionPane.showInputDialog(MainWindow.drawingPanel,
                        "Введите номер:",
                        "Удаление вершины",
                        JOptionPane.PLAIN_MESSAGE);

                if (res != null) {
                    int index = Integer.parseInt(res);
                    MainWindow.drawingPanel.delNode(index);
                    MainWindow.drawingPanel.repaint();
                }
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Неверный формат числа",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Ошибка при удалении вершины",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }).start());

        JMenuItem placeNodeItem = new JMenuItem("Разместить вершины");
        placeNodeItem.setFont(font);
        placeNodeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                InputEvent.CTRL_MASK));
        placeNodeItem.addActionListener(event -> new Thread(() -> {
            try {
                String res = JOptionPane.showInputDialog(MainWindow.drawingPanel,
                        "Введите количество вершин:",
                        "Размещение вершин",
                        JOptionPane.PLAIN_MESSAGE);

                if (res != null) {
                    int index = Integer.parseInt(res);
                    if (index > 0 && index < 14) {
                        MainWindow.drawingPanel.clear();
                        MainWindow.drawingPanel.placeNodes(index);
                        MainWindow.drawingPanel.repaint();
                    } else {
                        JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                                "Количество вершин должно быть от 0 до 13",
                                "Вершины не добавлены",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Неверный формат числа",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Ошибка при удалении вершины",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }).start());

        openItem.addActionListener(event -> new Thread(() -> {
            try {
                int returnVal = chooser.showOpenDialog(MainWindow.drawingPanel);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    MainWindow.drawingPanel.clear();

                    File file = chooser.getSelectedFile();
                    MainWindow.drawingPanel.read(file);
                    MainWindow.drawingPanel.repaint();
                }
            } catch (NoSuchElementException exc) {
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Неверный формат файла",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception exc) {
                MainWindow.drawingPanel.clear();
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Файл имеет неверные данные",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }).start());

        saveAsItem.addActionListener(event -> new Thread(() -> {
            try {
                int returnVal = chooser.showSaveDialog(MainWindow.drawingPanel);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    if (!file.getPath().contains(".graph"))
                        file = new File(file.getAbsolutePath() + ".graph");
                    MainWindow.drawingPanel.save(file);

                    JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                            String.format("Граф сохранен в файл %s", file.getAbsolutePath()),
                            "Сохранено",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception exc) {
                MainWindow.drawingPanel.clear();
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Файл не сохранен",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }).start());

        JMenu fileMenu = new JMenu("Файл");
        fileMenu.setFont(font);
        fileMenu.add(openItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu editMenu = new JMenu("Правка");
        editMenu.setFont(font);
        editMenu.add(eraseItem);
        editMenu.add(addNodeItem);
        editMenu.add(delNodeItem);
        editMenu.addSeparator();
        editMenu.add(placeNodeItem);

        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
    }

    public static JMenuBar getMenuBar() {
        return menuBar;
    }
}
