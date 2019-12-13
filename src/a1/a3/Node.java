package a1.a3;

import static a1.A1.edges;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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
    public Node(String url) { this.url = url;}    // Construct

    private ArrayList<Edge> edges = new ArrayList<>(); //  create edges
    //private Edge[] edges = new Edge[5000];
    
    public double getTotalDistance() { return totalDistance; }
    public ArrayList<Edge> getEdges() { return edges; } 
    public int getEdgeSize() { return edges.size(); }
    
   // public Edge[] getEdges() { return edges; }
    
    /*public int getEdgeSize(){
      for (int i=0; i< edges.length;i++){
            if (edges[i] == null) {
                return i;
            }
      }
      return edges.length;
    }*/
    
    public boolean isVisited() { return visited; } 
        
    public void setTotalDistance(double totalDistance) {this.totalDistance = totalDistance;}  
    public void setVisited(boolean visited) { this.visited = visited; } 
    public void setEdges(ArrayList<Edge> edges) { this.edges = edges; }
   //public void setEdges(Edge[] edges) { this.edges = edges; }
    public void setUrl(String url){this.url = url;}
    
    public void addEdge(Edge edge) { 
        this.edges.add(edge);
    }
   /* public void addEdge(Edge edge){
        for (int i=0; i< edges.length;i++){
            if (edges[i] == null) {
                edges[i] = edge;
                break;
            }
        }
    }*/

   // public void setEdges(Edge[] edges) {
  //      this.edges = (ArrayList<Edge>) Arrays.asList(edges);
  //  }
    
}
    
  