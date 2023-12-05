package IGS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class App {

    public static void avgOutDegree(DFS myGraph) {
        System.out.println("Depth vs. Average Out-Degree (Number of Children)");
        System.out.println("------------------------------------------------");

        Iterator<Map.Entry<Integer, ArrayList<String>>> itr = myGraph.getLeavesDepth().entrySet().iterator();
        while (itr.hasNext()) {
            double numNodes = 0.0;
            Map.Entry<Integer, ArrayList<String>> entry = itr.next(); // current depth
            Iterator<String> leaves = entry.getValue().iterator(); // leaves of the current depth

            while (leaves.hasNext()) {
                String lf = leaves.next();
                // need the number of children
                numNodes += myGraph.getNumChildren(lf);
            }
            double avg = (numNodes / entry.getValue().size());
            System.out.println(entry.getKey() + " | " + avg);
        }
    }

    public static void numNodesByDepth(Map<Integer, ArrayList<String>> l) {
        System.out.println("Depth vs. Number of Nodes");
        System.out.println("----------------------------");

        Iterator<Map.Entry<Integer, ArrayList<String>>> itr = l.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Integer, ArrayList<String>> entry = itr.next();
            System.out.println(entry.getKey() + " | " + entry.getValue().size());
        }
    }

    public static void runForAllDepths(DFS myGraph, ArrayList<String> ordering, List<Entry> jsonEntries) {
        // array to hold the average number of questions for leaves at each depth
        double[] avgNumQuestions = new double[myGraph.getLeavesDepth().size()];

        // traverse over all depths
        Iterator<Map.Entry<Integer, ArrayList<String>>> itr = myGraph.getLeavesDepth().entrySet().iterator();
        int index = 0;
        while (itr.hasNext()) {
            int numQuestions = 0;
            Map.Entry<Integer, ArrayList<String>> entry = itr.next();
            // if the list of leaves is small enough, just iterate over every leaf at the
            // current depth

            if (entry.getKey() != 0) {
                if (entry.getValue().size() < 10) {
                    Iterator<String> leafItr = entry.getValue().iterator();
                    while (leafItr.hasNext()) {
                        String target = leafItr.next();
                        myGraph.setTarget(target);

                        System.out.println("Depth : " + entry.getKey());
                        System.out.println("The target is node " + target);
                        showTargetPath(jsonEntries, target);

                        numQuestions += myGraph.dfsInterleave(ordering, target);
                    }
                }

                else {
                    // randomly select 10 leaves
                    for (int i = 0; i < 10; i++) {
                        Random r = new Random();
                        List<String> listofNodes = new ArrayList<String>(entry.getValue());

                        String randomTarget = listofNodes.get(r.nextInt(listofNodes.size()));
                        myGraph.setTarget(randomTarget);

                        System.out.println("The target is node " + randomTarget);
                        showTargetPath(jsonEntries, randomTarget);

                        myGraph.dfsInterleave(ordering, randomTarget);

                    }
                }
                avgNumQuestions[index] = (numQuestions / entry.getValue().size());
                index++;
            }
        }

        System.out.println("Depth vs. Avg. Number of Questions");
        System.out.println("----------------------------");

        for (int j = 0; j < avgNumQuestions.length; j++) {
            System.out.println(j + " | " + avgNumQuestions[j]);
        }
    }

    public static DFS buildGraph(DFS myGraph, Iterator<Entry> jsonItr) {
        while (jsonItr.hasNext()) {
            Entry e = jsonItr.next();
            List<List<String>> categories = e.getCategories();

            // getting a product's list of every list of categories it is assigned to
            ListIterator<List<String>> outerItr = categories.listIterator();

            while (outerItr.hasNext()) {
                // getting each sub-list
                List<String> listofCats = outerItr.next();
                ListIterator<String> innerItr = listofCats.listIterator(0);

                // if not an empty trailing list (due to some kind of json parsing error)
                if (!"".equals(listofCats.get(0))) {

                    // don't want to duplicate a category in list of outgoing neighbors
                    if (!myGraph.hasPath("*", listofCats.get(0))) {
                        // adding an edge from the root
                        myGraph.addVertex("*", listofCats.get(0));
                        myGraph.addLeafDepth(0, listofCats.get(0)); // store the depth of every leaf
                    }
                    if (listofCats.size() > 1) {
                        int counter = 0; // stores level of tree

                        // going through all of the categories
                        while (innerItr.hasNext()) {
                            String c = innerItr.next();
                            // checking if it is not a leaf node
                            if (innerItr.hasNext()) {
                                // don't want to duplicate a category in list of outgoing neighbors
                                if (!myGraph.hasPath(c, listofCats.get(innerItr.nextIndex()))) {
                                    myGraph.addVertex(c, listofCats.get(innerItr.nextIndex()));

                                    if (!myGraph.inLeafDepth(0, c)) {
                                        myGraph.addLeafDepth(counter, c);
                                    }
                                }
                            }
                            // leaf of every entry is the title
                            // the destination vertex of a leaf edge is null
                            else {
                                myGraph.addVertex(c, e.getTitle());
                                if (!myGraph.inLeafDepth(counter, c)) {
                                    myGraph.addLeafDepth(counter, c);
                                }
                                // error checking so that no leaf is repeated twice
                                if (!myGraph.isVertex(e.getTitle())) {
                                    myGraph.addVertex(e.getTitle(), null);
                                    myGraph.addLeafDepth(counter + 1, e.getTitle());
                                }
                            }
                            counter++;
                        }
                    } else {
                        // add category
                        myGraph.addVertex(listofCats.get(0), e.getTitle());

                        // add leaf node (title)
                        if (!myGraph.isVertex(e.getTitle())) {
                            myGraph.addVertex(e.getTitle(), null);
                            myGraph.addLeafDepth(1, e.getTitle());
                        }
                    }
                }
            }
        }
        return myGraph;
    }

    public static void showTargetPath(List<Entry> jsonEntries, String target) {
        List<List<String>> path = new ArrayList<List<String>>();

        // get path to target node
        Iterator<Entry> jsonItr2 = jsonEntries.iterator();
        while (jsonItr2.hasNext()) {
            Entry e = jsonItr2.next();
            if (e.getTitle().equals(target)) {
                path = e.getCategories();
                break;
            }
        }
        System.out.println("The path(s) to the target node (from the root) are : ");
        System.out.println(path);
    }

    public static void main(String[] args) throws IOException {
        // read in .JSON dataset file
        String dataPath = "/Users/jess/IGSProject/data/metadata.json";
        BufferedReader reader = new BufferedReader(new FileReader(dataPath));
        List<Entry> jsonEntries = new Gson().fromJson(reader, new TypeToken<List<Entry>>() {
        }.getType());
        reader.close();

        // iterate over every JSON entry and add to graph
        // each entry has two keys: title and categories
        // title is the name of the product (the leaf)
        // and categories is the path of product categories from the root to the leaf
        Iterator<Entry> jsonItr = jsonEntries.iterator();

        // default initialized with "*" as the root
        DFS myGraph = buildGraph(new DFS(jsonEntries.size()), jsonItr);

        // get heavy path tree traversal order
        ArrayList<String> ordering = myGraph.buildHeavyPathDFSTree();

        // runForAllDepths(myGraph, ordering, jsonEntries);
        String target = "Adult Ballet Tutu Yellow";
        System.out.println("The target is node " + target);
        showTargetPath(jsonEntries, target);

        myGraph.setTarget(target);
        int numQuestions = myGraph.dfsInterleave(ordering, target);

        numNodesByDepth(myGraph.getLeavesDepth());
        avgOutDegree(myGraph);
    }
}
