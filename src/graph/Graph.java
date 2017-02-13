package graph;

public abstract class Graph {
    protected int[][] flows;
    protected int size;

    public Graph() {
        size = 1;
        flows = new int[size][size];
    }
    public abstract int search(int s, int t);

    public void addNode(int index) {
        int[][] newGr = new int[index + 1][index + 1];
        for (int i = 0; i < flows.length; i++)
            System.arraycopy(flows[i], 0, newGr[i], 0, flows.length);
        flows = newGr;
        this.size = index;
    }

    public void delNode(int index) {
        int[][] newGr = new int[size][size];
        size--;
        for (int i = 0; i < newGr.length; i++) {
            if (i < index) {
                System.arraycopy(flows[i], 0, newGr[i], 0, index);
                System.arraycopy(flows[i], index + 1, newGr[i], index, newGr.length - index);
            } else if (i > index) {
                System.arraycopy(flows[i], 0, newGr[i - 1], 0, index);
                System.arraycopy(flows[i], index + 1, newGr[i - 1], index, newGr.length - index);
            }
        }
        flows = newGr;
    }

    public void addEdge(int from, int to, int cost)
            throws IndexOutOfBoundsException {
        flows[from][to] = cost;
    }

    public void delEdge(int from, int to)
            throws IndexOutOfBoundsException {
        flows[from][to] = 0;
    }

    public void clear() {
        size = 1;
        flows = new int[size][size];
    }

    public void setGraph(int[][] array) {
        flows = array;
        size = array.length - 1;
    }

    public int[][] toArray() {
        int[][] copy = new int[flows.length][flows.length];
        for (int i = 0; i < flows.length; i++)
            System.arraycopy(flows[i], 0, copy[i], 0, flows.length);
        return copy;
    }
}