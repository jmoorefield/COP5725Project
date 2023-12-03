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

        Iterator<Entry> test = jsonEntries.iterator();
        while (test.hasNext()) {
            Entry e = test.next();
            List<List<String>> categories = e.getCategories();

            ListIterator<List<String>> outerItr = categories.listIterator();
            while (outerItr.hasNext()) {
                List<String> listofCats = outerItr.next();
                ListIterator<String> innerItr = listofCats.listIterator();

                // need to check if edge already exists every time before adding
                myGraph.addVertex("root", listofCats.get(0));
                int counter = 1;
                while (innerItr.hasNext()) {
                    String c = innerItr.next();
                    int nextIndex = innerItr.nextIndex();

                    if (nextIndex != listofCats.size()) {
                        myGraph.addVertex(c, listofCats.get(nextIndex));
                        counter++;
                    }
                    // leaf of every entry is the title ?
                    else {
                        myGraph.addVertex(c, e.getTitle());
                        counter++;
                        myGraph.addLeaf(counter, e.getTitle());
                    }
                }
            }
        }

        myGraph.printGraph();

    }

}
