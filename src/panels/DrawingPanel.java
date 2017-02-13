package panels;

import graph.Graph;
import graph.NetGraph;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Math.*;

public final class DrawingPanel extends JPanel {

    private static final DrawingPanel drawingPanel;
    private volatile static NetGraph graph;
    private volatile static ArrayList<Point> nodes;
    private volatile static ArrayList<Edge> edges;

    static {
        drawingPanel = new DrawingPanel(new GridBagLayout(), true);
    }

    private DrawingPanel(LayoutManager lm, boolean doubleBuffered) {
        graph = new NetGraph();
        nodes = new ArrayList<>();
        edges = new ArrayList<>();

        setBackground(new Color(214, 214, 214));
        setLayout(lm);
        setDoubleBuffered(doubleBuffered);
    }

    public static DrawingPanel getDrawingPanel() {
        return drawingPanel;
    }

    static Graph getGraph() {
        return graph;
    }

    static int getNodeCount() {
        return nodes.size();
    }

    static synchronized void makeFlow(int[][] flows)
            throws IndexOutOfBoundsException {
        int[][] gr = graph.toArray();

        for (int i = 0; i < flows.length - 1; i++) {
            for (int j = 0; j < flows.length - 1; j++) {
                Edge e = new Edge(nodes.get(i), nodes.get(j));
                for (Edge edge : edges)
                    if (edge.equals(e)) {
                        edge.flow = gr[i + 1][j + 1] - flows[i + 1][j + 1];
                    }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawGrid(g);
        g.setFont(new Font("Verdana", Font.BOLD, 11));

        for (Edge e : edges) {
            g.setColor(new Color(0, 32, 64));
            g.drawLine(e.from.x, e.from.y, e.to.x, e.to.y);

            int a = e.from.y - e.to.y,
                    b = e.from.x - e.to.x,
                    c = (int) sqrt(a * a + b * b);
            double angle = atan2(b, a);
            int dx = (int) round(e.to.x + 10 * (b * 1d / c)),
                    dy = (int) round(e.to.y + 10 * (a * 1d / c)),
                    dxLeft = (int) round(dx + 10 * sin(angle + PI / 6)),
                    dxRight = (int) round(dx + 10 * sin(angle - PI / 6)),
                    dyLeft = (int) round(dy + 10 * cos(angle + PI / 6)),
                    dyRight = (int) round(dy + 10 * cos(angle - PI / 6));

            Polygon arrow = new Polygon(new int[]{dx, dxLeft, dxRight, dx},
                    new int[]{dy, dyLeft, dyRight, dy},
                    4);
            g.fillPolygon(arrow);

            dx = (int) round(e.to.x + ((2 * c + 0.2 * c) / 3) * (b * 1d / c));
            dy = (int) round(e.to.y + ((2 * c + 0.2 * c) / 3) * (a * 1d / c));

            g.setColor(new Color(240, 0, 0));
            g.drawString(String.format("(%d,%d)", e.flow, e.capacity), dx, dy - 5);
        }

        int index = 1;

        for (Point v : nodes) {
            g.setColor(new Color(0, 100, 170));
            g.fillOval(v.x - 10, v.y - 10, 20, 20);
            g.setColor(new Color(255, 200, 0));
            g.drawString(String.valueOf(index), v.x - 5, v.y + 4);
            index++;
        }

    }

    synchronized void addEdge(int from, int to, int cost) throws
            IndexOutOfBoundsException {
        Point pFr = nodes.get(from - 1),
                pTo = nodes.get(to - 1);
        if (nodes.contains(pFr) && nodes.contains(pTo)) {
            for (Edge edge : edges) {
                if (edge.from.equals(pFr) && edge.to.equals(pTo)
                        || edge.to.equals(pFr) && edge.from.equals(pTo)) {
                    graph.addEdge(from, to, cost);
                    edge.capacity = cost;
                    return;
                }
            }
            graph.addEdge(from, to, cost);
            edges.add(new Edge(pFr, pTo, cost));
        }
    }

    synchronized void addNode(int x, int y) {
        if (x >= 0 && y >= 0) {
            Point p = new Point(x * 20 + 25, y * 20 + 25);
            int index = nodes.size() + 1;
            if (!nodes.contains(p)) {
                graph.addNode(index);
                nodes.add(p);
            }
        }
    }

    synchronized void delEdge(int from, int to)
            throws IndexOutOfBoundsException {
        Point p1 = nodes.get(from - 1),
                p2 = nodes.get(to - 1);
        Edge e = new Edge(p1, p2);

        int index = -1;
        for (Edge edge : edges) {
            if (edge.equals(e))
                index = edges.indexOf(edge);
        }

        if (index >= 0) {
            graph.delEdge(from, to);
            edges.remove(index);
        }
    }

    synchronized void delNode(int index) {
        Point p = nodes.get(index - 1);
        if (nodes.contains(p)) {
            nodes.remove(index - 1);
            graph.delNode(index);

            ArrayList<Edge> removableEdges = new ArrayList<>();
            for (Edge edge : edges) {
                if (edge.from.x == p.x && edge.from.y == p.y ||
                        edge.to.x == p.x && edge.to.y == p.y)
                    removableEdges.add(edge);
            }
            edges.removeAll(removableEdges);
        }
    }

    synchronized void placeNodes(int count) {
        Point p = new Point(2 * 20 + 25, 7 * 20 + 25);
        nodes.add(p);
        int offset = 5;
        for (int i = 2, step = 0; i <= count; i++, step *= offset) {
            if (i % 2 == 0) {
                if (count == 2 || count == 3) {
                    p = new Point(p.x + offset * 20, p.y);
                } else {
                    p = new Point(p.x + offset * 20,
                            p.y - (i == 2 || i == count ? offset * 20 : 3 * offset * 20));
                }
            } else {
                if (count == 2 || count == 3) {
                    p = new Point(p.x + offset * 20, p.y);
                } else {
                    p = new Point(p.x + offset * 20, p.y + 3 * offset * 20);
                }
            }
            nodes.add(p);
        }
        graph = new NetGraph(new int[count + 1][count + 1]);
    }

    void read(File file) throws FileNotFoundException {
        Scanner in = new Scanner(file);
        int size = in.nextInt();
        placeNodes(size);

        int[][] gr = new int[size + 1][size + 1];
        while (in.hasNext()) {
            int from = in.nextInt(),
                    to = in.nextInt(),
                    cost = in.nextInt();
            gr[from][to] = cost;
            edges.add(new Edge(nodes.get(from - 1), nodes.get(to - 1), cost));
        }

        in.close();
        graph.setGraph(gr);
    }

    void save(File file) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(file);
        pw.println(nodes.size());

        for (Edge edge : edges) {
            int from = nodes.indexOf(edge.from) + 1,
                    to = nodes.indexOf(edge.to) + 1;
            pw.printf("%d %d %d%n", from, to, edge.capacity);
        }
        pw.close();
    }

    void clear() {
        nodes.clear();
        edges.clear();
        graph.clear();
    }

    private void drawGrid(Graphics g) {
        int w = getWidth(), h = getHeight();

        g.setColor(new Color(159, 173, 209));
        for (int i = 25; i < w; i += 20) {
            for (int j = 25; j < h; j += 20) {
                g.drawLine(i, 25, i, h);
                g.drawLine(25, j, w, j);
            }
        }

        g.setColor(new Color(183, 142, 128));
        for (int i = 23; i < w; i += 20) {
            for (int j = 23; j < h; j += 20) {
                g.fillOval(i, j, 4, 4);
            }
        }

        g.setFont(new Font("Verdana", Font.PLAIN, 10));
        g.setColor(new Color(76, 76, 76));
        for (int i = 20, num = 0; i < w; i += 20, num++) {
            g.drawString(String.valueOf(num), i + 2, 10);
            g.drawString(String.valueOf(num), 2, i + 10);
        }
    }

    private static class Edge {
        Point from,
                to;
        int capacity,
                flow = 0;

        Edge(Point from, Point to) {
            this.from = from;
            this.to = to;
        }

        Edge(Point from, Point to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
        }

        boolean equals(Edge e) {
            return e.from.equals(from) && e.to.equals(to);
        }
    }
}
