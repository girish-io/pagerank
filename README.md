# PageRank
An implementation of the [PageRank (PR)](https://en.wikipedia.org/wiki/PageRank) algorithm that is used by Google to rank web pages in their search engine results.

### Generated Neo4j graph from an adjacency matrix:
![Generated Neo4j graph from adjacency matrix](https://i.imgur.com/npn769P.png)
Each node has a `pagerank` property which can be used to find the most important nodes in the network.

## PageRank Formula
The program uses the following formula for computing the `PageRank` of nodes:

![PageRank formula](https://i.imgur.com/lfp6DzV.png)

The above is a modified version of the `PageRank` formula mentioned in the original paper. The original is commonly agreed upon to be [incorrect](https://en.wikipedia.org/wiki/PageRank#Damping_factor).

## Build Instructions
Step 1) Build JAR file
```
mvn package
```

Step 2) Run the program
```
java -jar target/pagerank-<version>.jar --help
```
### Example run:
```
java -jar pagerank-<version>.jar --verbose --damping 0.85 --iterations 0 --reset-db /path/to/network.graph
```

## Original Paper
You can find the original paper for `PageRank` at the following link:

[https://snap.stanford.edu/class/cs224w-readings/Brin98Anatomy.pdf](https://snap.stanford.edu/class/cs224w-readings/Brin98Anatomy.pdf)

## License
This project is licensed under the terms of the MIT License.
