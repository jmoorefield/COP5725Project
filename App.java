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

// TODO: change to 24,329 nodes 
public class App {

    public static String getRandomTarget(DFS graph) {
        List<String> listofNodes = new ArrayList<String>(graph.getNodes());
        listofNodes.remove("*"); // do not set root to target node
        Random r = new Random();
        return listofNodes.get(r.nextInt(listofNodes.size()));
    }

    // may be easier to do with sydney's implementation?
    public static void getOutDegreeStats(Map<Integer, ArrayList<String>> l) {
        System.out.println("Depth vs. Number of Nodes");
        System.out.println("----------------------------");

        Iterator<Map.Entry<Integer, ArrayList<String>>> itr = l.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Integer, ArrayList<String>> entry = itr.next();

            // gets all of the leaves at a depth
            ArrayList<String> n = entry.getValue();

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
        // String target = "Adult Ballet Tutu Cheetah Pink";
        String target = getRandomTarget(myGraph);
        System.out.println("The target is node " + target);
        List<List<String>> path = new ArrayList<List<String>>();

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

        // System.out.println(myGraph.getLeavesDepth());
        // System.out.println(jsonEntries.size());
        // getOutDegreeStats(myGraph.getLeavesDepth());

        // System.out.println(myGraph.buildHeavyPathDFSTree());

        // will need to call dfsinterleave for every node in order to get the node
        // depth vs questions asked result

        // List<String> listofNodes = new ArrayList<String>(graph.getNodes());
        // for every node in list, set as target
        // get depth of node
        // do dfs interleave = returns number of questions
        // store result as depth: total number of questions (summation)
        // then for every depth, get the size of its leaves list, and divide summation
        // by that size
    }

}