package graph;

import java.util.ArrayDeque;
import java.util.Arrays;

import static java.lang.Math.abs;

public final class NetGraph extends Graph {
    public NetGraph() {
        super();
    }

    public NetGraph(int gr[][]) {
        size = gr.length - 1;
        this.flows = gr;
    }

    @Override
    public synchronized int search(int s, int t) {
        int flow = 0, minFlow = 0;
        int[] path; // Кратчайший путь <s, t>.

        while ((path = breadthFirstSearch(s, t, minFlow)) != null) {
            minFlow = Integer.MAX_VALUE;
            for (int i = 1; i < path.length; i++) {
                int u = abs(path[i - 1]), v = abs(path[i]);
                minFlow = abs(flows[u][v]) < minFlow ? abs(flows[u][v]) : minFlow;
            }

            for (int i = 1; i < path.length; i++) {
                int u = abs(path[i - 1]), v = abs(path[i]);
                flows[u][v] -= minFlow;
                flows[v][u] = -flows[u][v];
            }
            flow += minFlow;
        }
        return flow;
    }

    private int[] breadthFirstSearch(int start, int end, int flow) {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        int[] path = new int[flows.length];
        boolean[] visited = new boolean[flows.length];
        visited[start] = true;
        deque.addLast(start);
        while (!deque.isEmpty()) {
            int u = deque.removeFirst();
            if (u == end) break;
            for (int i = 1; i < flows.length; i++) {
                if (flows[u][i] != 0 && !visited[i]) {
                    visited[i] = true;
                    if (!(flows[u][i] < 0 && flows[i][u] - flow >= 0)) {
                        path[i] = flows[u][i] > 0 ? u : -u;
                        deque.addLast(i);
                    }
                }
            }
        }

        int[] way = new int[path.length];
        if (path[end] == 0) {
            return null;
        } else {
            int j = way.length - 1;
            for (int i = end; i != 0; i = path[abs(i)], j--) {
                if (i < 0) way[j + 1] = -way[j + 1];
                way[j] = abs(i);
            }
            return Arrays.copyOfRange(way, j + 1, way.length);
        }
    }
}
