# Interactive Graph Search
Our term project for COP5725: Advanced Database Systems  
Group Members: Sydney McGinnis and Jess Moorefield  

Required Files: 
---------------------  
App.Java - driver file   
Entry.Java - class to represent a JSON entry in the dataset    
DFS.Java - DFS Heavy Path Tree and DFS Interleave functionality        
OrderedInterleave.Java - Heavy path decomposition and Ordered Interleave functionality    
metadata.json - Amazon dataset, with modifications detailed in our final report     

Prerequisites: 
---------------------  
```
Working version of Java, Visual Studio Code, and JGraphT and GSON libraries:  
JGraphT is needed for creating the graphs, traversing them, and heavy path decomposition. GSON is needed for JSON processing.
Both are available in this repository in .jar format, and you may also download them with these links:
GSON : https://mvnrepository.com/artifact/com.google.code.gson/gson/2.10.1
JGraphT : https://sourceforge.net/projects/jgrapht/

To add the libraries, follow the steps below.  
2. Add the jars to your desired directory (and unzip the JGraphT zip file) 
3. Open Visual Studio Code
4. Navigate to the "Extensions" tab within Visual Studio Code
5. Type in "Project manager for Java" in the search bar
6. Install "Project Manager for Java"
7. Download the list of required files (see above) from our repository
8. Open the files in Visual Studio Code
9. Once the editor with our downloaded code is open, navigate to the "JAVA PROJECTS" tab
10. Expand the "JAVA PROJECTS" tab to reveal the "Referenced Libraries" tab
11. Click the plus "+" button next to "Referenced Libraries"
12. Navigate to the directory where the zipped jar file was unzipped
13. Within the unzipped folder navigate to jgrapht-1.5.2 > lib > jgrapht-core-1.5.2
14. Add this file to the "Referenced Libraries" folder
15. Again, click the plus "+" button in Referenced Libraries
16. Navigate to where your GSON jar is stored
17. Add this to your referenced libraries
15. Add the GSON jar to the "Referenced Libraries" folder
18. Now, the jars file jgrapht-core-1.5.2 and gson-2.10.1 will be added to the runtime settings
```

To Run the Code:
-------------------
```
Download all files from the GitHub
Ensure that the prerequisites are met
Run App.java, SampleOrderedInterleave.java, InterleaveWithData.java

To generate the metadata_simplified.json file run the preprocess_json.py
```

Distribution of Work:
---------------------    
Jess - DFS Heavy Path tree and DFS Interleave algorithm, parsing dataset JSON input, experimental results, collaborated with Jess on all papers related to the project

Sydney - Heavy Path Decomposition and Ordered Interleave algorithm, created the bar graph, collaborated with Jess on all papers related to the project  

External Resources:
---------------------
[GSON](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.10.1)  
[JGraphT](https://github.com/jgrapht/jgrapht/wiki/Users%3A-How-to-use-JGraphT-as-a-dependency-in-your-projects)  
preprocess_json.py was modified using [this](https://colab.research.google.com/drive/1Zv6MARGQcrBbLHyjPVVMZVnRWsRnVMpV) code  
[Amazon dataset](https://cseweb.ucsd.edu/~jmcauley/datasets/amazon/links.html)  


