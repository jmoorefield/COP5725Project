package org.jgrapht.alg.decomposition;

import org.jgrapht.*;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
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


public class SampleTree {
    
    public static List<Integer> rootOfTree;
    public static Integer userInput = 1;
    public static Map<Integer, ArrayList> map= new HashMap<Integer, ArrayList>();
    public static boolean targetNodeFound = false;
    public static List<Integer> currentVertex = new ArrayList<>();
    public static Integer numQuestions = 0;

    public static void main(String[] args) {  
        Graph<Integer, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        //map.get(index of key).add(value for the key to the list);
        int r = 1;
        int child2 = 2;
        int child3 = 3;
        int child4 = 4;
        int child5 = 5;
        int child6 = 6;
        int child7 = 7;
        int child8 = 8;
        int child9 = 9;
        int child10 = 10;
        int child11 = 11;
        int child12 = 12;
        int child13 =13;
        int child14 = 14;

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

        Set<Integer> allVertices = graph.vertexSet();
        Iterator<Integer> itr = allVertices.iterator();
        ArrayList<Integer> vertexList = new ArrayList<>();

        while(itr.hasNext())
        {
            Integer node = itr.next();
            vertexList.add(node);
        }


        for(int i =0; i < vertexList.size(); i++)
        {
            List<Integer> childrenOfV = new ArrayList<>();
            Set<DefaultEdge> allOutgoingEdges = graph.outgoingEdgesOf(vertexList.get(i));
            Iterator<DefaultEdge> itr2 = allOutgoingEdges.iterator();
            List<DefaultEdge> listOfEdges = new ArrayList<>();
            ArrayList<Integer> children = new ArrayList<>();
            
            listOfEdges.clear();
            while(itr2.hasNext())
            {
                DefaultEdge e = itr2.next();
                listOfEdges.add(e);
            }
            for(int j=0; j < listOfEdges.size(); j++)
            {
                int child = graph.getEdgeTarget(listOfEdges.get(j));
                children.add(child);
            }
            map.put(vertexList.get(i), children);
        }

        System.out.println("Map is " + map);

        System.out.println("Here is the graph, please enter the node you would like to find: " + graph);
        Scanner scn = new Scanner(System.in);
        //userInput = scn.nextInt();
        userInput = 9;
  
        Graph<Integer, DefaultEdge> originalGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        originalGraph = graph;
        ArrayList<List<Integer>> result = Decomposition(graph, r);
        Graph<List<Integer>, DefaultEdge> result2 = buildPathTree(result, originalGraph, r, map);
        interleave(result2, originalGraph);
    }

        public static ArrayList<List<Integer>> Decomposition(Graph<Integer, DefaultEdge> graph, int root)
        {
            //Perform heavy path decomposition and put the result in h
            //The result is a set of paths
            HeavyPathDecomposition h = new HeavyPathDecomposition<>(graph, root);
            TreeToPathDecompositionAlgorithm.PathDecomposition decompPaths = h.getPathDecomposition();

            //The specific paths are stored in the paths variable and iterated through
            //with the itr iterator
            Set<GraphWalk<Integer, DefaultEdge>> paths = decompPaths.getPaths();
            System.out.print("The result of heavy path decompositon are these paths");
            System.out.println(". Each path is seperated by a comma and an individual path is distinguished by opening and enclosing brackets " + paths);
            Iterator<GraphWalk<Integer, DefaultEdge>> itr = paths.iterator();

            //This 2D array holds all the heavy paths
            ArrayList<List<Integer>> eachPath = new ArrayList<>(paths.size());
            
            int i = 0;
            while(itr.hasNext())
            {
                //Taking just the paths from one row of the iterator
                //and modifying it into a list
                GraphWalk<Integer, DefaultEdge> subGraph = itr.next();
                List<Integer> nodes = subGraph.getVertexList();

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

        
        static Graph<List<Integer>, DefaultEdge> buildPathTree(ArrayList<List<Integer>> eachPath, 
        Graph<Integer, DefaultEdge> originalGraph, int root, Map<Integer, ArrayList>map)
        {
            Graph<List<Integer>, DefaultEdge> pathTree = new DefaultDirectedGraph<>(DefaultEdge.class);
            for(int i=0; i < eachPath.size(); i++)
            {
                pathTree.addVertex(eachPath.get(i));
            }

            System.out.println("Building the heavy path tree");
            for (List<Integer> vertex : pathTree.vertexSet()) {
                //one path
                for(int i =0; i < vertex.size(); i++) {
                    //v = a node in the path
                    int v = vertex.get(i);
                    //temp = all the children of node v
                    List<Integer> temp = map.get(v);
                    for(int j =0; j < temp.size(); j++){   
                        //look through all the children
                        //if a child is not in the vertex v
                        //add an edge between v and child in the pathTree
                        if(!(vertex.contains(temp.get(j))))
                        {
                            for(int k=0; k < eachPath.size(); k++){
                                List<Integer> path = eachPath.get(k);
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

        public static void interleave(Graph<List<Integer>, DefaultEdge> pathTree, Graph<Integer, 
        DefaultEdge> graph)
        {
            //navigate in Path tree to find the vertex that contians the rooot
            Integer nextNode =0;
            Integer currentNode = 0;
            for(List<Integer> isRoot : pathTree.vertexSet())
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

        public static Integer pathTreeDeepestChild()
        {
            boolean keepGoing = false;
            Integer nextNode = 0;
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

        public static List<Integer> searchPathTree(Integer currentNode, List<Integer> currentVertex, 
        Graph<List<Integer>, DefaultEdge> pathTree)
        {
            System.out.println("We are now back to searching in the path tree.");
            System.out.println("Starting with the vertex containing node " + currentNode + " lets begin searching");
            //search until you find the child of the currentVertex that includes the currentNode
            Set<DefaultEdge> outEdges = pathTree.outgoingEdgesOf(currentVertex);
            List<Integer> childrenOfV = new ArrayList<>();
            Iterator<DefaultEdge> itr = outEdges.iterator();
            List<DefaultEdge> listOfEdges = new ArrayList<>();
            ArrayList<Integer> children = new ArrayList<>();
            
            listOfEdges.clear();
            while(itr.hasNext())
            {
                DefaultEdge e = itr.next();
                listOfEdges.add(e);
            }

            for(int j=0; j < listOfEdges.size(); j++)
            {
                List<Integer> l = pathTree.getEdgeTarget(listOfEdges.get(j));
                //within the list of edges iterate through the edges to get its target
                //within the target list see if this target is equal to the node we are looking for. 
                //If so -> the vertex that has this node becomes the next current vertex
                for(int k =0; k < l.size(); k++)
                {
                    if(l.get(k) == currentNode)
                    {
                        for(List<Integer> specificVertex : pathTree.vertexSet())
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

        public static Integer searchRegTree(Integer currentNode, Graph<Integer, 
        DefaultEdge> graph, Graph<List<Integer>, DefaultEdge> pathTree, List<Integer> currentVertex)
        {
            System.out.println("In the original graph");
            List<Integer> children = new ArrayList<>();
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

        public static boolean reach(Integer node)
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

