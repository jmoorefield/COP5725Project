/* This java file performs ordered interleave according to the 2019 paper titled
 * Interactive Graph Search. The program first creates a graph of the Amazon
 * dataset through the buildGraph function. Then, then the program performs
 * HeavyPathDecomposition using JGraphT's function. This is where the program
 * produces an IndexOutOfBoundsException. Therefore, we have created a new version
 * of Ordered Interleave titled "SampleOrderedInterleave". This algorithm performs
 * exactly as described in the paper, using the example from Figure 3A as our 
 * original input data.
 */

package IGS;


import org.jgrapht.*;
import org.jgrapht.alg.decomposition.HeavyPathDecomposition;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphWalk;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class InterleaveWithData {
    
    public static List<String> rootOfTree;
    public static String userInput = "";
    public static Map<String, ArrayList> map= new HashMap<String, ArrayList>();
    public static boolean targetNodeFound = false;
    public static List<String> currentVertex = new ArrayList<>();
    public static Integer numQuestions = 0;

    public static void main(String[] args) throws IOException {  
        String dataPath = "/Users/sydneym/Desktop/metadata_tiny.json";
        BufferedReader reader = new BufferedReader(new FileReader(dataPath));
        List<Entry> jsonEntries = new Gson().fromJson(reader, new TypeToken<List<Entry>>() {
        }.getType());
        try {
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // iterate over every JSON entry and add to graph
        // each entry has two keys: title and categories
        // title is the name of the product (the leaf)
        // and categories is the path of product categories from the root to the leaf
        Iterator<Entry> jsonItr = jsonEntries.iterator();

        // default initialized with "*" as the root
        Graph<String, DefaultEdge> graph = buildGraph(new DFS(jsonEntries.size()), jsonItr);
        
        // randomly selecting a target node
        String target = "Adult Ballet Tutu Cheetah Pink";
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

        
        Set<DefaultEdge> edge = graph.edgeSet();
        ArrayList<DefaultEdge> edgeList = new ArrayList<>();
        Iterator<DefaultEdge> itr = edge.iterator();

        while(itr.hasNext())
        {
            DefaultEdge ee = itr.next();
            edgeList.add(ee);
        }

         DefaultEdge output;
         String k = "";
         String text = "";
        for(int i =0; i < edgeList.size(); i++)
        {
            output = edgeList.get(i);
            text += output.toString();
        }
        Path fileName = Path.of("/Users/sydneym/Desktop/output.txt");
 
        // Write all the edges to the file. This shows
        //all of the relations between parent and child
        //i.e. (parent : child)
        Files.writeString(fileName, text);

        System.out.println("The path(s) to the target node are : ");
        System.out.println(path);
        userInput = "Adult Ballet Tutu Cheetah Pink";
  
        Graph<String, DefaultEdge> originalGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        originalGraph = graph;
        ArrayList<List<String>> result = Decomposition(graph, "*");
        Graph<List<String>, DefaultEdge> result2 = buildPathTree(result, originalGraph, "*", map);
        interleave(result2, originalGraph);
    }

        public static ArrayList<List<String>> Decomposition(Graph<String, DefaultEdge> graph, String root)
        {
            //Perform heavy path decomposition and put the result in h
            //The result is a set of paths
            HeavyPathDecomposition h = new HeavyPathDecomposition<>(graph, root);
            TreeToPathDecompositionAlgorithm.PathDecomposition decompPaths = h.getPathDecomposition();

            //The specific paths are stored in the paths variable and iterated through
            //with the itr iterator
            System.out.println("The paths are " + decompPaths);

            Set<GraphWalk<String, DefaultEdge>> paths = decompPaths.getPaths();
            System.out.print("The result of heavy path decompositon are these paths");
            System.out.println(". Each path is seperated by a comma and an individual path is distinguished by opening and enclosing brackets " + paths);
            Iterator<GraphWalk<String, DefaultEdge>> itr = paths.iterator();

            //This 2D array holds all the heavy paths
            ArrayList<List<String>> eachPath = new ArrayList<>(paths.size());
            
            int i = 0;
            while(itr.hasNext())
            {
                //Taking just the paths from one row of the iterator
                //and modifying it into a list
                GraphWalk<String, DefaultEdge> subGraph = itr.next();
                List<String> nodes = subGraph.getVertexList();

                //The list of a singular heavy path is added as an 
                //element to the 2D eachPath array
                if(nodes != null)
                {eachPath.add(i, nodes);}
                else {System.out.println("Found a null path");}
                i += 1;
            }
            /*eachPath is a list of lists(integer)
            eachPath = [ [x..x], [x..x], [x..x]]
            to get a whole path print(eachPath.get(index))
            to get a specific element in the path
            loop through index via for (Integer element : eachPath.get(1))
            */
            return eachPath;
        }

        static Graph<List<String>, DefaultEdge> buildPathTree(ArrayList<List<String>> eachPath, 
        Graph<String, DefaultEdge> originalGraph, String root, Map<String, ArrayList>map)
        {
            Graph<List<String>, DefaultEdge> pathTree = new DefaultDirectedGraph<>(DefaultEdge.class);
            for(int i=0; i < eachPath.size(); i++)
            {
                pathTree.addVertex(eachPath.get(i));
            }

            System.out.println("Building the heavy path tree");
            for (List<String> vertex : pathTree.vertexSet()) {
                //for every heavy path look at all of its nodes
                for(int i =0; i < vertex.size(); i++) {
                    String v = vertex.get(i);
                    List<String> temp = map.get(v);
                    if(temp == null)
                    { continue; }
                    for(int j =0; j < temp.size(); j++){   
                        //look through all the children
                        //if a child is not in the vertex v
                        //add an edge between v and child in the pathTree
                        if(!(vertex.contains(temp.get(j))))
                        {
                            for(int k=0; k < eachPath.size(); k++){
                                List<String> path = eachPath.get(k);
                                if(path.contains(temp.get(j)) && (!pathTree.containsEdge(vertex, path)))
                                { pathTree.addEdge(vertex, path);}
                                }
                            }

                        }

                    }
                }
                System.out.println("The path tree is " + pathTree);
                return pathTree;
            }

        public static void interleave(Graph<List<String>, DefaultEdge> pathTree, Graph<String, 
        DefaultEdge> graph)
        {
            //navigate in Path tree to find the vertex that contians the rooot
            String nextNode = "";
            String currentNode = "";
            for(List<String> isRoot : pathTree.vertexSet())
            {
                if(pathTree.inDegreeOf(isRoot) == 0)
                {
                    rootOfTree = isRoot;
                    System.out.println("Root of tree is " + isRoot);
                    break;
                }
            }
            currentVertex = rootOfTree; //root
            
            while(targetNodeFound == false)
            {
                nextNode = pathTreeDeepestChild();
                //Search in the regular tree for the children of the "nextNode"
                //that can reach the target node (exlcuding all children that are in the vertex of nextNode)
                currentNode = searchRegTree(nextNode, graph, pathTree, currentVertex);
                if(targetNodeFound == true)
                { break;}
                //once we have this current node, go back to the path tree
                currentVertex = searchPathTree(currentNode, currentVertex, pathTree);
            }
        }

        public static String pathTreeDeepestChild()
        {
            boolean keepGoing = false;
            String nextNode = "";
            //Within the current vertex, find the deepest node
            //that can reach the target node
            for(int i=0; i < currentVertex.size(); i++)
            {
                //if false use the previous node (currentVertex.get(i-1)) as the node
                //that can reach the target node
                keepGoing = reach(currentVertex.get(i));
                if(keepGoing == false)
                {
                    nextNode = currentVertex.get(i-1);
                    System.out.println("We will now go to node " + nextNode + " in the original tree");
                    break;
                }
            }
            return nextNode;
        }

        public static List<String> searchPathTree(String currentNode, List<String> currentVertex, 
        Graph<List<String>, DefaultEdge> pathTree)
        {
            System.out.println("We are now back to searching in the path tree.");
            System.out.println("Starting with the vertex containing node " + currentNode + " lets begin searching");
            //search until you find the child of the currentVertex that includes the currentNode
            Set<DefaultEdge> outEdges = pathTree.outgoingEdgesOf(currentVertex);
            List<String> childrenOfV = new ArrayList<>();
            Iterator<DefaultEdge> itr = outEdges.iterator();
            List<DefaultEdge> listOfEdges = new ArrayList<>();
            ArrayList<String> children = new ArrayList<>();
            
            listOfEdges.clear();
            while(itr.hasNext())
            {
                DefaultEdge e = itr.next();
                listOfEdges.add(e);
            }

            for(int j=0; j < listOfEdges.size(); j++)
            {
                List<String> l = pathTree.getEdgeTarget(listOfEdges.get(j));
                //within the list of edges iterate through the edges to get its target
                //within the target list see if this target is equal to the node we are looking for. 
                //If so -> the vertex that has this node becomes the next current vertex
                for(int k =0; k < l.size(); k++)
                {
                    if(l.get(k) == currentNode)
                    {
                        for(List<String> specificVertex : pathTree.vertexSet())
                        {
                            for(int i= 0; i < specificVertex.size(); i++)
                            {
                                if(specificVertex.get(i).equals(currentNode))
                                {
                                    currentVertex = specificVertex;
                                }
                            }
                        }

                    }
                }
            }
            return currentVertex;
        }

        public static String searchRegTree(String currentNode, Graph<String, 
        DefaultEdge> graph, Graph<List<String>, DefaultEdge> pathTree, List<String> currentVertex)
        {
            System.out.println("In the original graph");
            List<String> children = new ArrayList<>();
            //look for all children of 2, excluding the ones in the vertex 2 is in

            boolean result = false;

            //Find all the children of the current node
            children = map.get(currentNode);
            System.out.println("This node has no children :( ");
            if(children == null)
            {
                targetNodeFound = true;
                System.out.println("Target node found ! " + userInput);
                System.out.println("Number of questions asked " + numQuestions);
                return currentNode;
            }
            for(int i =0; i < children.size(); i++)
            {
                //If the child is not already in the same
                //vertex of the parent in the path tree
                //see if the child can reach the target node
                if(!currentVertex.contains(children.get(i)))
                {
                    result = reach(children.get(i));
                    if(result == true)
                    {
                        currentNode = children.get(i);
                        break;
                        
                        //we have found the child of the current
                        //node that can reach the target node
                        //reach(children.get(i));
                    }
                }

            }
            if(result != true)
            {
                //the target node can not be reached from the current node
                //which means the target node must be the current node
                targetNodeFound = true;
                System.out.println("Target node found ! " + userInput);
                System.out.println("Number of questions asked " + numQuestions);
            }
            return currentNode;
        }

        public static boolean reach(String node)
        {
            String answer = "no";
            System.out.println("Can " + node + " reach " + userInput + " ?");
            Scanner scn = new Scanner(System.in);
            answer = scn.nextLine();
            answer = answer.toUpperCase();

            numQuestions += 1;

            if(("YES".equals(answer)) || ("Y".equals(answer)))
            {
                return true;
            }
            else return false;
        }


        //This function takes in the JSON, parsed by preprocess_json.py in our GitHub
        //and produces a grap with verteices, edges, parents, and children
        public static Graph<String, DefaultEdge> buildGraph(DFS myGraph, Iterator<Entry> jsonItr) 
        {
            String root = "*";
            Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
            Map<String, ArrayList> map = new HashMap<String, ArrayList>();
            if(graph.containsVertex(root) == false)
            {
                graph.addVertex(root);
            }
            ArrayList<String> childrenOfRoot = new ArrayList<String>();
            map.put("*", childrenOfRoot);
    
            while (jsonItr.hasNext()) {
                Entry e = jsonItr.next();
                List<List<String>> categories = e.getCategories();
    
                // getting a product's list of every list of categories it is assigned to
                ListIterator<List<String>> outerItr = categories.listIterator();
    
                while (outerItr.hasNext()) {
                    // getting each sub-list
                    List<String> listofCats = outerItr.next();
                    ListIterator<String> innerItr = listofCats.listIterator(0);
                    if(graph.containsVertex(listofCats.get(0)) == false)
                    {
                        ArrayList<String> children= new ArrayList<>();
                        graph.addVertex(listofCats.get(0));
                        if(map.get(listofCats.get(0)) == null)
                        {
                            map.put(listofCats.get(0), children);
                        }
                    }
                    if(graph.containsEdge("*", listofCats.get(0)) == false)
                    {
                        if(graph.containsEdge(root, listofCats.get(0)) == false)
                        {
                            graph.addEdge(root, listofCats.get(0));
                        }
                        map.get("*").add(listofCats.get(0));
                    }
                        if (listofCats.size() > 1) {
                            int counter = 0; // stores level of tree
    
                            // going through all of the categories
                            while (innerItr.hasNext()) {
                                ArrayList<String> child = new ArrayList<>();
                                String c = innerItr.next();
                                if(graph.containsVertex(c) == false)
                                {    
                                    graph.addVertex(c);
                                }
    
                                // checking if it is not a leaf node
                                if (innerItr.hasNext()) {
                                    // don't want to duplicate a category in list of outgoing neighbors
                                    if(graph.containsEdge(c, listofCats.get(innerItr.nextIndex())) == false)
                                    {
                                        String nextNode = listofCats.get(innerItr.nextIndex());

                                        if(graph.containsVertex(nextNode) == false)
                                        {
                                            graph.addVertex(nextNode);
                                        }
                                        
                                        graph.addEdge(c, nextNode);

                                        if(map.get(c) == null)
                                        {
                                            map.put(c, child);
                                        }
                                        map.get(c).add(nextNode);

                                    }
                                }
                                // leaf of every entry is the title
                                // the destination vertex of a leaf edge is null
                                else {
                                    // error checking so that no leaf is repeated twice
                                    if (!graph.containsVertex(e.getTitle())) 
                                    {
                                        graph.addVertex(e.getTitle());
                                    }

                                    if(graph.containsEdge(c, e.getTitle()) == false)
                                    {
                                        graph.addEdge(c, e.getTitle());
                                    }

                                    if(map.get(c) == null)
                                    {
                                        map.put(c, child);
                                    }
                                    map.get(c).add(e.getTitle());
                                    if(map.get(e.getTitle()) == null)
                                    {
                                        map.put(e.getTitle(), null);
                                    }
                                }
                                counter++;
                            }
                        } else {
                            // add category
                            //cateogry and leaf edge added
                            ArrayList<String> child = new ArrayList<>();
                            if(graph.containsVertex(listofCats.get(0)) == false)
                            {
                                graph.addVertex(listofCats.get(0));
                            }
    
                            // add leaf node (title)
                            if (!graph.containsVertex(e.getTitle())) 
                            {
                                graph.addVertex(e.getTitle());
                            }
                            if(graph.containsEdge(listofCats.get(0), e.getTitle()) == false)
                            {
                                graph.addEdge(listofCats.get(0), e.getTitle());
                            }

                            if(map.get(listofCats.get(0)) == null)
                            {
                                map.put(listofCats.get(0), child);
                            }
                            map.get(listofCats.get(0)).add(e.getTitle());
                            if(map.get(e.getTitle()) == null)
                            {
                                map.put(e.getTitle(), null);
                            }
                        }
                    }
                }
            return graph;
        }


}

