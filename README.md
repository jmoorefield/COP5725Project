# Interactive Graph Search
Our term project for COP5725: Advanced Database Systems  
Group Members: Sydney McGinnis and Jess Moorefield  

Required Files: 
---------------------  
App.java - driver file   
Entry.java - class to represent a JSON entry in the dataset    
DFS.java - DFS Heavy Path Tree and DFS Interleave functionality        
SampleOrderedInterleave.java - Heavy path decomposition and Ordered Interleave functionality with an example graph    
InterleaveWithData.java - Heavy path decomposition and Ordered Interleave with the Amazon dataset  
metadata.json - Amazon dataset, with modifications detailed in our final report    
gson-2.10.1.jar  
jgrapht-core-1.5.2.jar  
preprocess_json.py  


Additional Files: 
---------------------  
DAGDFS.java - Working example of DFS on example DAG in paper  

Prerequisites: 
---------------------  
```
A working version of Java, Visual Studio Code, and JGraphT and GSON libraries:  
JGraphT is needed for creating the graphs, traversing them, and heavy path decomposition. GSON is needed for JSON processing.
Both are available in this repository in .jar format and you may also download them with these links:
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
12. Navigate to the directory where the files are stored
13. Within the directory navigate to jgrapht-core-1.5.2.jar and gson-2.10.1.jar
14. Add these files to the "Referenced Libraries" folder
15. Now, the jars file jgrapht-core-1.5.2 and gson-2.10.1 will be added to the runtime settings
```

To Run the Code:
-------------------
```
Download all files from the GitHub
Ensure that the prerequisites are met
Update the path to the metadata file on your machine
Run App.java, SampleOrderedInterleave.java, InterleaveWithData.java

```

Distribution of Work:
---------------------    
Jess - DFS Heavy Path tree and DFS Interleave algorithm, parsing dataset JSON input, experimental results, collaborated with Sydney on all papers related to the project

Sydney - Heavy Path Decomposition and Ordered Interleave algorithm, created the visualizations of the results, repository maintenance, collaborated with Jess on all papers related to the project  

External Resources:
---------------------
[GSON](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.10.1)  
[JGraphT](https://github.com/jgrapht/jgrapht/wiki/Users%3A-How-to-use-JGraphT-as-a-dependency-in-your-projects)  
preprocess_json.py was modified using [this](https://colab.research.google.com/drive/1Zv6MARGQcrBbLHyjPVVMZVnRWsRnVMpV) code  
[Amazon dataset](https://cseweb.ucsd.edu/~jmcauley/datasets/amazon/links.html)  


