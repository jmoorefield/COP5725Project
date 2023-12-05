import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;

public class DFS {
    Map<Integer, ArrayList> map;

    DFS() {
        map = new HashMap<Integer, ArrayList>();
    }

    void addVertex(int v, int newVertex) {
        if (!map.containsKey((v))) {
            ArrayList<Integer> adj = new ArrayList<Integer>();

            if (newVertex != -1) {
                adj.add(newVertex);
            }
            map.put(v, adj);

        } else {
            map.get(v).add(newVertex);
        }
    }

    // s = 0 (start vertex)
    public void dfs_algo(int s) {
        Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>();

        Iterator<Map.Entry<Integer, ArrayList>> visitedItr = map.entrySet().iterator();
        while (visitedItr.hasNext()) {
            Map.Entry<Integer, ArrayList> entry = visitedItr.next();
            visited.put(entry.getKey(), false);
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(s);
        System.out.println("pushing " + s);

        while (stack.empty() == false) {
            s = stack.pop();
            visited.put(s, true);
            System.out.println("top of stack is " + s);

            Collections.sort(map.get(s), Collections.reverseOrder());

            Iterator<Integer> colorItr = map.get(s).iterator();
            while (colorItr.hasNext()) {
                int v = colorItr.next();
                if (!visited.get(v)) {
                    stack.push(v);
                    // System.out.println("pushing " + v);
                }
            }
        }
    }

    // iterating over all the out neighbors of a node to calculate number of white
    // edges
    // put in error checking for length = 0 of out neighbors
    // remember sorting algo will do the tie breaking for you

    public static void main(String[] args) {
        DFS graph = new DFS();
        graph.addVertex(1, 2);
        graph.addVertex(1, 3);
        graph.addVertex(2, 3);
        graph.addVertex(2, 4);
        graph.addVertex(2, 5);
        graph.addVertex(3, 8);
        graph.addVertex(3, 13);
        graph.addVertex(4, 6);
        graph.addVertex(4, 7);
        graph.addVertex(4, 8);
        graph.addVertex(5, 9);
        graph.addVertex(9, 12);
        graph.addVertex(9, 13);
        graph.addVertex(12, 14);
        graph.addVertex(8, 10);
        graph.addVertex(8, 11);
        graph.addVertex(6, -1);
        graph.addVertex(7, -1);
        graph.addVertex(13, -1);
        graph.addVertex(14, -1);
        graph.addVertex(10, -1);
        graph.addVertex(11, -1);

        graph.dfs_algo(1);
        System.out.println("********");
        System.out.println("DFS Search on a DAG should be 1, 2, 4, 8, 10, 11, 6, 7, 5, 9, 12, 14, 13, 3");
    }
}