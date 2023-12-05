/* This java file performs heavy path decompositon using the graph
 * from Figure 3A of Interactive Graph Search as input into the algorithm. The program first 
 * creates a graph according to the graph in Figure 3A. Next, heavy path decompositioin is
 * performed by using the HeavyPathDecomposition class provided by JGraphT. After this,
 * the path tree is built using the decomposition. The algorithm then begins at the root of 
 * the path tree (function: interleave). It then looks for the deepest child of the path
 * vertex that can reach the target node (pathTreeDeepestChild). Based on the result of 
 * pathTreeDeepest child, the algorithm looks in the original tree to find a child of the 
 * node that can reach the target node (excluding ones in the same vertex as the node in
 * the path tree) (function: searchRegTree). Based on this result, the algorithm goes back to
 * the path tree and finds the vertex that covers this node (searchPathTree). The algorithm 
 * repeats until the target node is found.
 */

package IGS;

import org.jgrapht.*;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.decomposition.HeavyPathDecomposition;
import org.jgrapht.alg.decomposition.HeavyPathDecomposition.InternalState;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.interfaces.TreeToPathDecompositionAlgorithm.PathDecomposition;
import org.jgrapht.alg.interfaces.VertexCoverAlgorithm.VertexCover;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphWalk;
import org.jgrapht.util.*;
import org.apache.commons.lang3.tuple.Pair;
import java.util.*;


public class SampleOrderedInterleave {
    
    public static List<String> rootOfTree;
    public static String userInput = "";
    public static Map<String, ArrayList> map= new HashMap<String, ArrayList>();
    public static boolean targetNodeFound = false;
    public static List<String> currentVertex = new ArrayList<>();
    public static Integer numQuestions = 0;

    public static void main(String[] args) {  
        Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        //Create the graph
        String r = "*";
        String child2 = "Two";
        String child3 = "Three";
        String child4 = "Four";
        String child5 = "Five";
        String child6 = "Six";
        String child7 = "Seven";
        String child8 = "Eight";
        String child9 = "Nine";
        String child10 = "Ten";
        String child11 = "Eleven";
        String child12 = "Twelve";
        String child13 = "Thirteen";
        String child14 = "Fourteen";

        graph.addVertex(r);
        graph.addVertex(child2);
        graph.addVertex(child3);
        graph.addVertex(child4);
        graph.addVertex(child5);
        graph.addVertex(child6);
        graph.addVertex(child7);
        graph.addVertex(child8);
        graph.addVertex(child9);
        graph.addVertex(child10);
        graph.addVertex(child11);
        graph.addVertex(child12);
        graph.addVertex(child13);
        graph.addVertex(child14);

        graph.addEdge(r, child2);
        graph.addEdge(child2, child3);
        graph.addEdge(child2, child4);
        graph.addEdge(child2, child5);
        graph.addEdge(child4, child6);
        graph.addEdge(child4, child7);
        graph.addEdge(child4, child8);
        graph.addEdge(child8, child10);
        graph.addEdge(child8,child11);
        graph.addEdge(child5, child9);
        graph.addEdge(child9, child12);
        graph.addEdge(child9, child13);
        graph.addEdge(child12, child14);

        Set<String> allVertices = graph.vertexSet();
        Iterator<String> itr = allVertices.iterator();
        ArrayList<String> vertexList = new ArrayList<>();

        //Find all children of the graph and add them to the
        //hash map where the value is the parent and the key
        //is the list of children
        while(itr.hasNext())
        {
            String node = itr.next();
            vertexList.add(node);
        }

        for(int i =0; i < vertexList.size(); i++)
        {
            List<String> childrenOfV = new ArrayList<>();
            Set<DefaultEdge> allOutgoingEdges = graph.outgoingEdgesOf(vertexList.get(i));
            Iterator<DefaultEdge> itr2 = allOutgoingEdges.iterator();
            List<DefaultEdge> listOfEdges = new ArrayList<>();
            ArrayList<String> children = new ArrayList<>();
            
            listOfEdges.clear();
            while(itr2.hasNext())
            {
                DefaultEdge e = itr2.next();
                listOfEdges.add(e);
            }
            for(int j=0; j < listOfEdges.size(); j++)
            {
                String child = graph.getEdgeTarget(listOfEdges.get(j));
                children.add(child);
            }
            map.put(vertexList.get(i), children);
        }

        System.out.println("Here is the graph, please enter the node you would like to find: " + graph);
        Scanner scn = new Scanner(System.in);
        userInput = scn.nextLine();
        if(userInput == null)
        {
            userInput = "Nine";
        }
  
        Graph<String, DefaultEdge> originalGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        originalGraph = graph;
        //Perform heavy path decomposition, build the path tree, perform ordered interleave
        ArrayList<List<String>> result = Decomposition(graph, r);
        Graph<List<String>, DefaultEdge> result2 = buildPathTree(result, originalGraph, r, map);
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
                eachPath.add(i, nodes);
                i += 1;
            }
            //Notes
            /*eachPath Is a list of lists(integer)
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
                //one path
                for(int i =0; i < vertex.size(); i++) {
                    //v = a node in the path
                    String v = vertex.get(i);
                    //temp = all the children of node v
                    List<String> temp = map.get(v);
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
            //navigate in path tree to find the vertex that contians the rooot
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
                                if(specificVertex.get(i) == currentNode)
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
}