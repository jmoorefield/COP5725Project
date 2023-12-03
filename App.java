package IGS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

// include Table 1 statistics (out-degree statistics) in report? 

public class App {
    public static void main(String[] args) throws IOException {
        String dataPath = "/Users/jess/IGSProject/data/metadata_simplified.json";

        BufferedReader reader = new BufferedReader(new FileReader(dataPath));
        List<Entry> jsonEntries = new Gson().fromJson(reader, new TypeToken<List<Entry>>() {
        }.getType());
        reader.close();

        // default initialized with "root" as the root
        DFS myGraph = new DFS(jsonEntries.size());

        Iterator<Entry> jsonItr = jsonEntries.iterator();
        while (jsonItr.hasNext()) {
            Entry e = jsonItr.next();
            List<List<String>> categories = e.getCategories();

            // getting a title's list of every list of categories it is assigned to
            ListIterator<List<String>> outerItr = categories.listIterator();

            while (outerItr.hasNext()) {
                // getting each sub-list
                List<String> listofCats = outerItr.next();
                // starts at 1 to avoid the root
                ListIterator<String> innerItr = listofCats.listIterator(1);

                if (!"".equals(listofCats.get(0))) {
                    // check if edge already exists is done in addVertex
                    // don't want to duplicate a category in list of outgoing neighbors
                    if (!myGraph.hasPath("root", listofCats.get(0))) {
                        myGraph.addVertex("root", listofCats.get(0));
                        myGraph.addLeafDepth(0, listofCats.get(0));
                    }
                    // categories of length = 1
                    if (!innerItr.hasNext()) {
                        myGraph.addVertex(listofCats.get(0), e.getTitle());
                        myGraph.addLeafDepth(1, e.getTitle());
                    }
                    int counter = 1;
                    while (innerItr.hasNext()) {
                        String c = innerItr.next();

                        // checking if it is not a leaf node
                        if (innerItr.hasNext()) {
                            // don't want to duplicate a category in list of outgoing neighbors
                            if (!myGraph.hasPath(c, listofCats.get(innerItr.nextIndex()))) {
                                myGraph.addVertex(c, listofCats.get(innerItr.nextIndex()));
                                counter++;
                            }
                        }
                        // leaf of every entry is the title
                        else {
                            myGraph.addVertex(c, e.getTitle());
                            myGraph.addVertex(e.getTitle(), null);
                            counter++;
                            myGraph.addLeafDepth(counter, e.getTitle());
                        }
                    }
                }
            }
        }

        myGraph.printGraph();

    }

}
