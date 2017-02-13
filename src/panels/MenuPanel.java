package panels;

import frames.MainWindow;
import graph.Graph;
import graph.NetGraph;

import javax.swing.*;
import java.awt.*;

public final class MenuPanel extends JPanel {

    public static JPanel getMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout(), true);

        Font font = new Font("Verdana", Font.PLAIN, 11);
        panel.setBackground(new Color(222, 222, 222));
        panel.setFont(font);

        JButton btnFlow = new JButton("Рассчитать макс. поток");
        JButton btnAddEdge = new JButton("Добавить");
        JButton btnDelEdge = new JButton("Удалить");

        JLabel lblFlow = new JLabel("Поток:");
        JLabel lblMaxFlow = new JLabel("Макс. поток:");
        JLabel lblEdges = new JLabel("Дуги:");
        JLabel lblFrom = new JLabel("От (Номер вершины):");
        JLabel lblTo = new JLabel("До (Номер вершины):");
        JLabel lblCapacity = new JLabel("Пропуск. способность:");

        JTextField txtFldFrom = new JTextField();
        JTextField txtFldTo = new JTextField();
        JTextField txtFldCapacity = new JTextField();
        JTextField txtFldFlow = new JTextField();
        // Панель "Поток".
        panel.add(lblFlow, new GridBagConstraints(0, 0, 2, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(lblMaxFlow, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(txtFldFlow, new GridBagConstraints(1, 1, 1, 1, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(btnFlow, new GridBagConstraints(0, 2, 2, 2, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        // Панель "Дуги".
        panel.add(lblEdges, new GridBagConstraints(3, 0, 3, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(lblFrom, new GridBagConstraints(3, 1, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(txtFldFrom, new GridBagConstraints(3, 2, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(lblTo, new GridBagConstraints(4, 1, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(txtFldTo, new GridBagConstraints(4, 2, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(lblCapacity, new GridBagConstraints(5, 1, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(txtFldCapacity, new GridBagConstraints(5, 2, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(btnAddEdge, new GridBagConstraints(3, 3, 2, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(btnDelEdge, new GridBagConstraints(5, 3, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        // Обработчик кнопки "Добавить".
        btnAddEdge.addActionListener(event -> new Thread(() -> {
            try {
                MainWindow.drawingPanel.addEdge(Integer.parseInt(txtFldFrom.getText()),
                        Integer.parseInt(txtFldTo.getText()),
                        Integer.parseInt(txtFldCapacity.getText()));
                MainWindow.drawingPanel.repaint();
            } catch (IndexOutOfBoundsException exc) {
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Отсутствует одна из вершин",
                        "Ошибка",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Ошибка при добавлении дуги",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }).start());
        // Обработчик кнопки "Удалить".
        btnDelEdge.addActionListener(event -> new Thread(() -> {
            try {
                MainWindow.drawingPanel.delEdge(Integer.parseInt(txtFldFrom.getText()),
                        Integer.parseInt(txtFldTo.getText()));
                MainWindow.drawingPanel.repaint();
            } catch (IndexOutOfBoundsException exc) {
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Отсутствует одна из вершин",
                        "Ошибка",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Ошибка при удалении дуги",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }).start());
        // Обработчик кнопки "Рассчитать макс. поток".
        btnFlow.addActionListener(event -> new Thread(() -> {
            try {
                Graph g = new NetGraph(DrawingPanel
                        .getGraph()
                        .toArray());
                int flow = g.search(1, DrawingPanel.getNodeCount());

                DrawingPanel.makeFlow(g.toArray());
                MainWindow.drawingPanel.repaint();
                txtFldFlow.setText(String.valueOf(flow));

                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        String.format("Максимальный поток в сети равен %d.", flow),
                        "Максимальный поток",
                        JOptionPane.PLAIN_MESSAGE);
            } catch (IndexOutOfBoundsException exc) {
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Отсутствуют вершины",
                        "Ошибка",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(MainWindow.drawingPanel,
                        "Ошибка при расчёте",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }).start());

        btnFlow.setFont(font);
        btnAddEdge.setFont(font);
        btnDelEdge.setFont(font);

        lblFlow.setFont(font);
        lblFlow.setFont(font);
        lblMaxFlow.setFont(font);
        lblEdges.setFont(font);
        lblFrom.setFont(font);
        lblTo.setFont(font);
        lblCapacity.setFont(font);

        txtFldFlow.setEditable(false);

        return panel;
    }
}
