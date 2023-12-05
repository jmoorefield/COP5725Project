package IGS;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
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
    int numQuestions;
    String root;
    String target;
    Map<String, Boolean> visited;
    Map<String, Boolean> dfsInterleaveVisited;
    Map<Integer, ArrayList<String>> leavesByDepth;

    DFS(int size) {
        this.size = size;
        this.root = "*";
        this.numQuestions = 0;
        this.target = "";
        map = new HashMap<String, ArrayList<String>>();
        leavesByDepth = new HashMap<Integer, ArrayList<String>>();

        // initially seeting all vertices to visited = false
        visited = new HashMap<String, Boolean>();
        dfsInterleaveVisited = new HashMap<String, Boolean>();
    }

    public void setTarget(String v) {
        this.target = v;
    }

    public int getNumQuestions() {
        return this.numQuestions;
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
            dfsInterleaveVisited.put(v, false);
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

    public int getLeafDepth(String leaf) {
        Iterator<Map.Entry<Integer, ArrayList<String>>> itr = leavesByDepth.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Integer, ArrayList<String>> entry = itr.next();
            if (entry.getValue().contains(leaf)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public boolean inLeafDepth(int depth, String leaf) {
        if (leavesByDepth.containsKey((depth)) && leavesByDepth.get(depth).contains(leaf)) {
            return true;
        }
        return false;
    }

    public void printGraph() {
        // get all nodes : ArrayList<String>(map.keySet());
        Iterator<Map.Entry<String, ArrayList<String>>> itr = map.entrySet().iterator();

        while (itr.hasNext()) {
            Map.Entry<String, ArrayList<String>> entry = itr.next();
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    public Map<Integer, ArrayList<String>> getLeavesDepth() {
        return leavesByDepth;
    }

    public int getNumChildren(String leaf) {
        return map.get(leaf).size();
    }

    boolean isLeaf(String vertex) {
        // if list of out neighbors is empty
        if (map.get(vertex).isEmpty()) {
            return true;
        }
        return false;
    }

    // for DFS-interleave algorithm
    public ArrayList<String> findLeafPath(String root, ArrayList<String> ordering) {
        ArrayList<String> rootToLeafPath = new ArrayList<String>();

        // start looking in path at this current 'root'
        for (int index = ordering.indexOf(root); index < ordering.size(); index++) {
            String vertex = ordering.get(index); // current node
            rootToLeafPath.add(vertex);
            dfsInterleaveVisited.put(vertex, true);
            if (isLeaf(vertex)) {
                break;
            }
        }
        return rootToLeafPath;
    }

    public int dfsInterleave(ArrayList<String> ordering, String target) {
        boolean found = false;
        String currentNode = this.root;
        this.numQuestions = 0;

        while (!found) {
            ArrayList<String> path = findLeafPath(currentNode, ordering); // leftmost currentNode-to-leaf path
            String deepestChild = deepestChild(path);

            // this means our target node was a leaf
            if (deepestChild.equals("DNE")) {
                System.out.println("Found! Number of questions asked: " + getNumQuestions());
                return numQuestions; // terminate because we have found node
            }

            String node = leftMostChild(deepestChild);
            // this means there is no child of node that can reach the target
            // i.e., it must be the target
            if (node.equals("DNE")) {
                found = true; // terminate because we have found node
                System.out.println("Found! Number of questions asked: " + getNumQuestions());
            }
            currentNode = node;
        }
        return numQuestions;
    }

    public String deepestChild(ArrayList<String> path) {
        boolean keepGoing = false;
        String nextNode = "DNE";

        for (int i = 1; i < path.size(); i++) {
            keepGoing = reach(path.get(i), target);
            // backtrack if it reaches false
            if (keepGoing == false) {
                nextNode = path.get(i - 1);
                break;
            }
        }
        return nextNode;
    }

    public String leftMostChild(String node) {
        ArrayList<String> children = map.get(node);
        Iterator<String> itr = children.listIterator();

        while (itr.hasNext()) {
            String v = itr.next();
            if (!dfsInterleaveVisited.get(v)) {
                if (reach(v, target))
                    return v;
            }
        }
        return "DNE";
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

    // note that this has been modified to search only 2 levels of children down,
    // not all the way to the leaf nodes
    // recursive method to search down to the leaf caused stack overflow error due
    // to size of dataset
    public int calculateDepth(String v) {
        int depth = 0;
        Iterator<String> itr = map.get(v).listIterator(); // get children of current node
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

    public boolean reach(String node, String target) {
        System.out.println("Can " + node + " reach " + target + "?");
        System.out.println("Usage: YES or Y; NO or N");

        Scanner scn = new Scanner(System.in);
        String answer = scn.nextLine().toUpperCase();
        numQuestions++;

        if (("YES".equals(answer)) || ("Y".equals(answer))) {
            return true;
        } else
            return false;
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