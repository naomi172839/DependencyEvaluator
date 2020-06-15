import java.util.*;

/*
 *      *****Graph Class*****
 *
 * The graph class contains the logic for creating and traversing a graph
 *
 * The graph class has two instance variables, a Map containing the adjacency mappings
 *      and graph representing the actual graph.
 *
 * The graph class has one constructor that takes a single argument
 *      The construction requires a 2 dimensional array containing the class order
 *
 * The graph class contains an inner class, vertex.
 *      Vertex has 2 instance variables, data of type T and adjacent which represents the adjacency list
 *      Vertex has 1 constructor that takes 1 argument, a T representing the nodes value
 *      Vertex has an overridden toString method.
 *
 * The graph class has 5 methods: 4 private, 1 public
 *      buildGraph builds the graph.  Takes 1 argument, a 2 dimensional array from the constructor.
 *      topologicalOrder returns the topological order.  Takes 1 argument, a String representing the starting class
 *          Returns a string with the final ordering
 *      find returns the vertex based on a key.  Takes 1 argument type T representing the key
 *          returns a Vertex representing the found vertex
 *      getMapping finds the mapping of a vertex.  Takes 1 argument, a vertex and returns a integer representing the mapping
 *      topologicalRecursive recursively generate a reverse topological order.  Takes 2 arguments
 *          a Vertex v representing the starting point, and a Stack used to store the found order.
 *
 */
public class Graph<T> {

    /*
     *      **Instance Variables**
     *
     * adjacencyMappings            -           Type Integer, Vertex containing the integer mappings
     * graph                        -           Type Vertex containing the graph
     */
    private final Map<Integer, Vertex> adjacencyMappings;
    private final List<Vertex> graph;

    /*
     * 1 argument constructor that calls the build graph method
     */
    public Graph(T[][] rows) {
        adjacencyMappings = new HashMap<>();
        graph = new ArrayList<>();
        buildGraph(rows);
    }

    /*
     * Takes the provided array and builds the graph
     */
    private void buildGraph(T[][] rows) {
        int mappingNumber = 0;  //To track the mappings and prevent duplicates
        Vertex current;     //Used to prevent duplicate vertices
        Vertex first = null;        //Used to track if the vertex is the first on a row
        int i;  //used to tell if vertex is first
        int key = -1;   //Key is used to insert into list rather than the whole vertex
        for (T[] row : rows) {      //Iterate through each row
            i = 0;  //  initialize as 0, also reset on subsequent loops
            for (T element : row) { //Iterate through items in each row
                current = find(element);    //get the vertex that corresponds to that vertex, if any
                if (current == null) {      //If the vertex is not yet mapped, map it
                    current = new Vertex(element);  //Create the new vertex
                    adjacencyMappings.put(mappingNumber, current); //Add it to tha matrix
                    mappingNumber++;  //Increment the mapping number
                }
                if (i == 0) {   //If the vertex is the first in a row
                    first = current;    //Set the vertex as the first in the row
                } else {
                    for (Map.Entry<Integer, Vertex> entry : adjacencyMappings.entrySet()) {     //Get the int representation
                        if (current.equals(entry.getValue())) {
                            key = entry.getKey();
                        }
                    }
                    first.adjacent.add(key);    //Add the vertex to the adjacency list
                }
                if (!graph.contains(current)) {
                    graph.add(current);  //Add it to the graph
                }
                i++;    //Increment i
            }
        }
    }

    /*
     * Wrapper method for finding the topological order recursively
     * Throws cycledetectedcycle and invalidclassname exceptions
     */
    public String topologicalOrder(T data) throws CycleDetectedException, InvalidClassNameException {
        Vertex v = find(data);  //Find the vertex corresponding to the data
        Stack<Vertex> stack = new Stack<>();    //Create a new stack to store reverse order
        StringBuilder sb = new StringBuilder(); //NEw string builder to prevent concatenation
        topologicalRecursive(v, stack); //Call the recursive method to populate the stack
        while (!stack.empty()) {    //While the stack is not empty
            sb.append(stack.pop().toString()).append(" ");  //Append the string
        }
        return sb.toString();   //Returns the string representation of the compilation order
    }

    /*
     * Finds the vertex based off of the data field
     */
    private Vertex find(T element) {
        for (Map.Entry<Integer, Vertex> entry : adjacencyMappings.entrySet()) {        //Iterates through mappings
            if (entry.getValue().data.equals(element)) {   //If found
                return entry.getValue();
            }
        }
        return null;    //Default return
    }

    /*
     * Gets the int representation of the vertex
     */
    private int getMapping(Vertex v) {
        for (Map.Entry<Integer, Vertex> entry : adjacencyMappings.entrySet()) { //Iterates through the mappings
            if (v != null && v.equals(entry.getValue())) { //If not null and the vertices match
                return entry.getKey();
            }
        }
        return -1; //Default return
    }

    /*
     * Recursively populates a stack representing the reverse topological order
     * Uses the algorithm given in class (with slight modifications)
     * Throws exceptions below for cycles or for invalid names
     */
    public void topologicalRecursive(Vertex v, Stack<Vertex> stack)
            throws CycleDetectedException, InvalidClassNameException {
        int key = getMapping(v);    //Get the mapping for the vertex
        if (key == -1) { //If the starting class is not mapped i.e. it is not in the graph
            throw new InvalidClassNameException("The provided vertex is not part of this graph");
        }
        if (stack.contains(v)) { //If there is a cycle
            throw new CycleDetectedException("Cycle Detected");
        }
        for (int i : v.adjacent) { //For all adjacent vertices
            topologicalRecursive(adjacencyMappings.get(i), stack);  //Recurse through the list
        }
        stack.push(v);  //push the result onto the stack
    }

    /*
     * Inner class vertex.  Defines any vertex.
     */
    private class Vertex {
        private final T data;   //Type T representing the data
        private final List<Integer> adjacent;       //Adjaceny list

        /*
         * Single argument constructor
         */
        public Vertex(T data) {
            this.data = data;
            adjacent = new LinkedList<>();
        }

        //Override Object.toString().
        public String toString() {
            return data.toString();
        }
    }
}