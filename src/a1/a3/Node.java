package a1.a3;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;

public class Node {
    String id;
   // HashSet<Edge> edges = new HashSet<>();

    double totalDistance = Integer.MAX_VALUE;
    boolean visited;
    int index;
    double cost;

    //Node(String id) { this.id = id;}    // Construct

    private ArrayList<Edge> edges = new ArrayList<>(); // now we must create edges
    
    public double getTotalDistance() { return totalDistance; }
    public ArrayList<Edge> getEdges() { return edges; } 
    public boolean isVisited() { return visited; } 
        
    public void setTotalDistance(double totalDistance) {this.totalDistance = totalDistance;}  
    public void setVisited(boolean visited) { this.visited = visited; } 
    public void setEdges(ArrayList<Edge> edges) { this.edges = edges; }
    
}
    
    
    /*

    void addEdge(Node dest, double w) {     //Add edge w/ Weight = w 
            Edge edge = new Edge(this,dest,w);
            edge.weight = w;

            edge.src = this;                //connect this w/ next node
            edge.dest = dest;
            edges.add(edge);
    }

    public double findEdge(Node dst) {             // find & return edges weight
        for (Edge e : edges) {
            if (e.dest.equals(dst)) {
                return e.weight;
            }
        }
        throw new NoSuchElementException();
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
            out.writeObject(id);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
            edges = new HashSet<>();
            children = new HashSet<>();
            id = (String) in.readObject();
    }
    
    @Override
    public boolean equals(Object obj) {
            return obj instanceof Node && id.equals(((Node) obj).id);
    }

    @Override
    public String toString() { return id; }

    @Override
    public int hashCode() { return id.hashCode(); }

*/


