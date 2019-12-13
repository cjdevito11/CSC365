package a1.a3;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;

public class Node {
    
    public String url;
   // HashSet<Edge> edges = new HashSet<>();

    double totalDistance = Integer.MAX_VALUE;
    boolean visited;
    int index;
    double cost;

    public Node() {}    // Construct
    public Node(String id) { this.url = url;}    // Construct

    private ArrayList<Edge> edges = new ArrayList<>(); // now we must create edges
    
    public double getTotalDistance() { return totalDistance; }
    public ArrayList<Edge> getEdges() { return edges; } 
    public int getEdgeSize(){ return edges.size(); }
    public boolean isVisited() { return visited; } 
        
    public void setTotalDistance(double totalDistance) {this.totalDistance = totalDistance;}  
    public void setVisited(boolean visited) { this.visited = visited; } 
    public void setEdges(ArrayList<Edge> edges) { this.edges = edges; }
    public void setUrl(String url){this.url = url;}
    public void addEdge(Edge edge){this.edges.add(edge);}
    
}
    
  