package IGS;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Collections;

public class DFS {
    Map<String, ArrayList> map;
    int size;
    String root;

    Map<Integer, ArrayList<String>> leavesByDepth;

    DFS(int size) {
        this.size = size;
        this.root = "root";
        map = new HashMap<String, ArrayList>();
        leavesByDepth = new HashMap<Integer, ArrayList<String>>();
    }

    void addVertex(String v, String newVertex) {
        if (!map.containsKey((v))) {
            ArrayList<String> adj = new ArrayList<String>();

            if (newVertex != null) {
                adj.add(newVertex);
            }
            map.put(v, adj);

        } else {
            map.get(v).add(newVertex);
        }
    }

    void addLeaf(int depth, String leaf) {
        if (!leavesByDepth.containsKey((depth))) {
            ArrayList<String> leaves = new ArrayList<String>();
            leaves.add(leaf);
            leavesByDepth.put(depth, leaves);
        }
        leavesByDepth.get(depth).add(leaf);
    }

    // TODO: bug here
    void printGraph() {
        Iterator<Map.Entry<String, ArrayList>> itr = map.entrySet().iterator();

        while (itr.hasNext()) {
            Map.Entry<String, ArrayList> entry = itr.next();
            System.out.println(entry.getKey());
        }
    }

    // s = start vertex
    public ArrayList<String> dfs_algo(String s) {

        ArrayList<String> finalOrdering = new ArrayList<String>();

        Map<String, Boolean> visited = new HashMap<String, Boolean>();
        Iterator<Map.Entry<String, ArrayList>> visitedItr = map.entrySet().iterator();
        while (visitedItr.hasNext()) {
            Map.Entry<String, ArrayList> entry = visitedItr.next();
            visited.put(entry.getKey(), false);
        }

        Stack<String> stack = new Stack<>();
        stack.push(s);
        System.out.println("pushing " + s);

        while (stack.empty() == false) {
            s = stack.pop();
            visited.put(s, true);
            System.out.println("top of stack is " + s);
            finalOrdering.add(s);
        }

        // TODO: calculating the count of unvisited nodes for every out neighbor of node
        // at top of stack
        // Collections.sort(map.get(s), Collections.reverseOrder()); - this needs to be
        // used for example in original paper
        Iterator<Integer> countItr = map.get(s).iterator();
        ArrayList<Integer> or = chooseMostOutreaching(countItr);

        Iterator<Integer> colorItr = or.iterator();
        while (colorItr.hasNext()) {
            int v = colorItr.next();
            if (!visited.get(v)) {
                stack.push(v);
                System.out.println("pushing " + v);
            }
        }
        return finalOrdering;
    }

    // iterating over all the out neighbors of a node to calculate number of white
    // edges
    // put in error checking for length = 0 of out neighbors
    // remember sorting algo will do the tie breaking for you
    public ArrayList<Integer> chooseMostOutreaching(Iterator itr) {
        // include the current node itself
        int outreaching = 1;
        while (itr.hasNext()) {
            int v2 = itr.next();
            if (!visited.get(v2)) {
                outreaching++;
            }
            return outreaching;
        }
    }

    public int recursiveTry(Iterator itr) {
        if (Iterator.length == 0) {
            return 1;
        } else {
            int total = chooseMostOutreaching(itr);
            return 1 + total;
        }
    }
}