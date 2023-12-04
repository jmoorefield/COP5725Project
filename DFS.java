package IGS;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

public class DFS {
    Map<String, ArrayList<String>> map;
    int size;
    String root;
    Map<String, Boolean> visited;
    Map<Integer, ArrayList<String>> leavesByDepth;

    DFS(int size) {
        this.size = size;
        this.root = "*";
        map = new HashMap<String, ArrayList<String>>();
        leavesByDepth = new HashMap<Integer, ArrayList<String>>();

        // initially seeting all vertices to visited = false
        visited = new HashMap<String, Boolean>();
    }

    public void addVertex(String v, String newVertex) {
        if (!map.containsKey((v))) {
            ArrayList<String> adj = new ArrayList<String>();

            // remember to do the "".equals if checking for a leaf
            if (newVertex != null) {
                adj.add(newVertex);
            }
            map.put(v, adj);
            visited.put(v, false);
        } else {
            if (!isLeaf(v)) {
                map.get(v).add(newVertex);
            }
        }
    }

    public boolean isVertex(String v) {
        if (map.containsKey(v))
            return true;
        return false;
    }

    public boolean hasPath(String start, String dest) {

        if (map.containsKey(start)) {
            if (map.get(start).contains(dest)) {
                return true;
            }
        }
        return false;
    }

    public void addLeafDepth(int depth, String leaf) {
        if (!leavesByDepth.containsKey((depth))) {
            ArrayList<String> leaves = new ArrayList<String>();
            leaves.add(leaf);
            leavesByDepth.put(depth, leaves);
        }

        if (!leavesByDepth.get(depth).contains(leaf)) {
            leavesByDepth.get(depth).add(leaf);
        }
    }

    public boolean inLeafDepth(int depth, String leaf) {
        if (leavesByDepth.containsKey((depth)) && leavesByDepth.get(depth).contains(leaf)) {
            return true;
        }
        return false;
    }

    public void printGraph() {
        Iterator<Map.Entry<String, ArrayList<String>>> itr = map.entrySet().iterator();

        while (itr.hasNext()) {
            Map.Entry<String, ArrayList<String>> entry = itr.next();
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    public Map<Integer, ArrayList<String>> getLeavesDepth() {
        return leavesByDepth;
    }

    public ArrayList<String> getNodes() {
        return new ArrayList<String>(map.keySet());
    }

    boolean isLeaf(String vertex) {
        // if list of out neighbors is empty
        // System.out.println(map.get(vertex).isEmpty());
        if (map.get(vertex).isEmpty()) {
            return true;
        }
        return false;
    }

    // for DFS-interleave algorithm
    public ArrayList<String> findLeaf(String vertex, Map<String, Boolean> visited) {
        ArrayList<String> rootToLeafPath = new ArrayList<String>();

        Stack<String> stack = new Stack<>();
        stack.push(vertex);
        rootToLeafPath.add(vertex);
        System.out.println("pushing in findLeaf" + vertex);

        while (stack.empty() == false) {
            vertex = stack.pop();
            visited.put(vertex, true);
            rootToLeafPath.add(vertex);

            if (isLeaf(vertex)) {
                return rootToLeafPath;
            } else {
                if (!visited.get(vertex)) {
                    stack.push(vertex);
                }
            }
        }
        return null;
    }

    public void dfsInterleave(ArrayList<String> ordering) {
        System.out.println("hello world!");
    }

    // s = start vertex
    public ArrayList<String> buildHeavyPathDFSTree() {

        // represents the traversal order of heavy-path DFS tree
        ArrayList<String> finalOrdering = new ArrayList<String>();

        Stack<String> stack = new Stack<>();
        stack.push(this.root);

        while (stack.empty() == false) {
            String s = stack.pop();
            visited.put(s, true);
            finalOrdering.add(s);

            // Collections.sort(map.get(s), Collections.reverseOrder()); - used for first
            // example in original paper instead of using heavy-path sort criteria

            // if node at top of stack is not a leaf
            if (!map.get(s).isEmpty()) {
                ArrayList<String> outReachingOrder = chooseMostOutreaching(map.get(s).listIterator());
                Iterator<String> oItr = outReachingOrder.listIterator();
                while (oItr.hasNext()) {
                    String v = oItr.next();
                    stack.push(v); // don't need to test if visited bc that's already checked in
                                   // chooseMostOutreaching
                }
            }
        }
        return finalOrdering;
    }

    // iterating over all the out neighbors of a node to calculate number of white
    // edges
    // returns the list of children in order of count of white edges
    public ArrayList<String> chooseMostOutreaching(ListIterator<String> itr) {
        Map<String, Integer> nodeCount = new HashMap<String, Integer>();
        ArrayList<String> outreaching = new ArrayList<String>();

        while (itr.hasNext()) {
            String v = itr.next();
            if (!visited.get(v)) {
                int d = calculateDepth(v);
                nodeCount.put(v, d);
            }
        }
        // sort the map based on depth
        // sorting algorithm from
        // https://stackoverflow.com/questions/12184378/sorting-linkedhashmap
        List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(nodeCount.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> node1, Map.Entry<String, Integer> node2) {
                return node1.getValue().compareTo(node2.getValue());
            }
        });
        // append all neighbors in sorted order
        for (Map.Entry<String, Integer> entry : entries) {
            outreaching.add(entry.getKey());
        }
        return outreaching;
    }

    public int calculateDepth(String v) {
        int depth = 0;
        Iterator<String> itr = map.get(v).listIterator();
        while (itr.hasNext()) {
            String current = itr.next();
            if (!visited.get(current)) {
                Iterator<String> itr2 = map.get(current).listIterator();
                while (itr2.hasNext()) {
                    String current2 = itr2.next();
                    if (!visited.get(current2))
                        depth += map.get(current2).size();
                }
            }
        }
        return depth;
    }
}
// doesn't work on large scale (stack overflow)
/*
 * public int calculateDepth(String v) {
 * if (isLeaf(v)) {
 * return 0;
 * }
 * int depthSum = 0;
 * Iterator<String> itr = map.get(v).listIterator();
 * while (itr.hasNext()) {
 * String current = itr.next();
 * if (!visited.get(current)) {
 * depthSum += calculateDepth(current);
 * }
 * }
 * return 1 + depthSum;
 * }
 */