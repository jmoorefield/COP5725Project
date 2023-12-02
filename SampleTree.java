package org.jgrapht.alg.decomposition;

import org.jgrapht.*;
import org.jgrapht.alg.decomposition.HeavyPathDecomposition.InternalState;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.interfaces.TreeToPathDecompositionAlgorithm.PathDecomposition;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphWalk;
import org.jgrapht.util.*;
import org.apache.commons.lang3.tuple.Pair;
import java.util.*;


public class SampleTree {
    

    public static void main(String[] args) {
        //Creating a sample tree
        // Create a directed graph
        Graph<Integer, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        List<Pair> childList = new ArrayList<>();

        //Add vertices and edges
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
        Pair<Integer, Integer> t1 = Pair.of(child2, child3);
        childList.add(t1);
        graph.addEdge(child2, child4);
        Pair<Integer, Integer> t2 = Pair.of(child2, child4);
        childList.add(t2);
        graph.addEdge(child2, child5);
        Pair<Integer, Integer> t3 = Pair.of(child2, child5);
        childList.add(t3);
        graph.addEdge(child4, child6);
        Pair<Integer, Integer> t4 = Pair.of(child4, child6);
        childList.add(t4);
        graph.addEdge(child4, child7);
        Pair<Integer, Integer> t5 = Pair.of(child4, child7);
        childList.add(t5);
        graph.addEdge(child4, child8);
        Pair<Integer, Integer> t6 = Pair.of(child4, child8);
        childList.add(t6);
        graph.addEdge(child8, child10);
        Pair<Integer, Integer> t7 = Pair.of(child8, child10);
        childList.add(t7);
        graph.addEdge(child8,child11);
        Pair<Integer, Integer> t8 = Pair.of(child8, child11);
        childList.add(t8);
        graph.addEdge(child5, child9);
        Pair<Integer, Integer> t9 = Pair.of(child5, child9);
        childList.add(t9);
        graph.addEdge(child9, child12);
        Pair<Integer, Integer> t10 = Pair.of(child9, child12);
        childList.add(t10);
        graph.addEdge(child9, child13);
        Pair<Integer, Integer> t11 = Pair.of(child9, child13);
        childList.add(t11);
        graph.addEdge(child12, child14);
        Pair<Integer, Integer> t12 = Pair.of(child12, child14);
        childList.add(t12);

        System.out.println("Graph: " + graph);
        Graph<Integer, DefaultEdge> originalGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        originalGraph = graph;
        ArrayList<List<Integer>> result = Decomposition(graph, r);
        Decomposition(graph, r);
        buildPathTree(result, originalGraph);
        //interleave(result, originalGraph);
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
            


                List<Integer> element = eachPath.get(1);
                //System.out.println("Here" + element);
                //System.out.println(element.get(1));

            return eachPath;
        }

        static void buildPathTree(ArrayList<List<Integer>> eachPath, Graph<Integer, DefaultEdge> originalGraph)
        {
            //Find root
            for(int i=0; i < eachPath.size(); i++)
            {
                for (Integer element : eachPath.get(i))
                {
                    
                }

            }

        }






            //Graph<Object, DefaultEdge> pathTree = new DefaultDirectedGraph<>(DefaultEdge.class);
            /*pathTree.addVertex(objarr[0]);
            pathTree.addVertex(objarr[1]);
            pathTree.addVertex(objarr[2]);
            pathTree.addVertex(objarr[3]);
            pathTree.addVertex(objarr[4]);
            pathTree.addVertex(objarr[5]);
            pathTree.addVertex(objarr[6]);

            pathTree.addEdge(objarr[3], objarr[4]);
            pathTree.addEdge(objarr[3], objarr[5]);
            pathTree.addEdge(objarr[3], objarr[6]);
            pathTree.addEdge(objarr[3], objarr[2]);
            pathTree.addEdge(objarr[2], objarr[1]);
            pathTree.addEdge(objarr[1], objarr[0]);

            System.out.println("Path tree: " + pathTree);
            return pathTree;*/


        public static void interleave(Graph<Object, DefaultEdge> pathTree, Graph<Integer, 
        DefaultEdge> graph)
        {
            System.out.println("Original graph" + graph);
        }
}

