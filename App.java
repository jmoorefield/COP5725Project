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

    public static void avgOutDegree(DFS dfsGraph, SampleTree oiGraph) {
        System.out.println("Depth vs. Average Out-Degree");
        System.out.println("----------------------------");

        Iterator<Map.Entry<Integer, ArrayList<String>>> itr = dfsGraph.getLeavesDepth().entrySet().iterator();
        while (itr.hasNext()) {
            double numNodes = 0.0;
            Map.Entry<Integer, ArrayList<String>> entry = itr.next();

            Iterator<String> leaves = entry.getValue().iterator();
            while (leaves.hasNext()) {
                String l = leaves.next();
                numNodes += oiGraph.map.get(l).size();
            }
            double avg = (numNodes / entry.getKey());
            System.out.println(entry.getKey() + " | " + avg);
        }
    }

    public static String getRandomTarget(DFS graph) {
        List<String> listofNodes = new ArrayList<String>(graph.getNodes());
        listofNodes.remove("*"); // do not set root to target node
        Random r = new Random();
        return listofNodes.get(r.nextInt(listofNodes.size()));
    }

    public static void getNumNodesStats(Map<Integer, ArrayList<String>> l) {
        System.out.println("Depth vs. Number of Nodes");
        System.out.println("----------------------------");

        Iterator<Map.Entry<Integer, ArrayList<String>>> itr = l.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Integer, ArrayList<String>> entry = itr.next();
            System.out.println(entry.getKey() + " | " + entry.getValue().size());
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

    public static void main(String[] args) throws IOException {

        SampleTree s;

        // read in .JSON dataset file
        String dataPath = "/Users/jess/IGSProject/data/metadata_simplified.json";
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

        // randomly selecting a target node
        String target = "Adult Ballet Tutu Cheetah Pink";
        // String target = getRandomTarget(myGraph);
        myGraph.setTarget(target);
        System.out.println("The target is node " + target);
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
        System.out.println("The path(s) to the target node are : ");
        System.out.println(path);

        ArrayList<String> ordering = myGraph.buildHeavyPathDFSTree();

        // change getNodes to be list of randomly selected nodes
        // List<String> listofNodes = new ArrayList<String>(myGraph.getNodes());
        // Iterator<String> nItr = listofNodes.iterator();

        myGraph.dfsInterleave(ordering, target);

        /*
         * while (nItr.hasNext()) {
         * String t = nItr.next();
         * int depth = myGraph.getLeafDepth(t);
         * 
         * System.out.println("number of questions is " + myGraph.getNumQuestions());
         * myGraph.dfsInterleave(ordering, t);
         * 
         * // then for every depth, get the size of its leaves list, and divide
         * summation
         * // by that size
         * 
         * }
         */
        // Print results statistics
        // getNumNodesStats(myGraph.getLeavesDepth());
        // avgOutDegree(myGraph, s);
    }
}