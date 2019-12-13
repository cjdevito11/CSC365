/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1.a3;

import java.util.ArrayList;
import a1.a3.Triplet;
import java.io.*;
import java.util.*;

/**
 *
 * @author CJ
 */
public class Graph {
    private static int EDGE_DISTANCE;
    
    private Node[] nodes;
    private Edge[] edges;
    
    private int nodeCount;
    private int edgeCount;
    
    
    public Graph(Edge[] edges){ 
        this.edges = edges;
        nodeCount = calcNodeCount(edges);
        nodes = new Node[nodeCount];  
        
        for (int n = 0; n < this.nodeCount; n++) {       // create each node
            nodes[n] = new Node();
        }
                                                             //add edges
        edgeCount = edges.length;
        for (int add=0; add < edgeCount ;add++){
            ArrayList srcEdges = this.nodes[edges[add].getSrcIndex()].getEdges();
            ArrayList destEdges = this.nodes[edges[add].getDestIndex()].getEdges();
            
            srcEdges.add(edges[add]);
            destEdges.add(edges[add]);
        }
    }
    
    public int calcNodeCount(Edge[] edges){
        int nodes = 0;
        if(edges != null){
            for (Edge edge : edges) {
                if (edge.getDestIndex() > nodes){
                    nodes = edge.getDestIndex();
                }
                if (edge.getSrcIndex() > nodes) {
                    nodes = edge.getSrcIndex();
                }
            }
            nodes++;
            return nodes;
        }
        return 0;
    }
    
    // start from source // find dest
    public void dijkstra(){
        int nextNode = 0;
        
        this.nodes[0].setTotalDistance(0);
        
        //todo
        for (int i=0; i <this.nodes.length; i++){
            ArrayList<Edge> tempEdges = this.nodes[nextNode].getEdges();
            
            for (int connected = 0; connected < tempEdges.size(); connected++){
                int connectIndex = tempEdges.get(connected).getConnectIndex(nextNode);
                
                if (!this.nodes[connectIndex].isVisited()) {
                    double test = this.nodes[nextNode].getTotalDistance() + 
                            tempEdges.get(connected).getWeight();
                    
                    if (test < nodes[connectIndex].getTotalDistance()){
                        nodes[connectIndex].setTotalDistance(test);
                    }
                }
            }
            nodes[nextNode].setVisited(true);
            nextNode = getPath();
        }
        output();   // DISPLAY PATH
    }
    
    private int getPath() {
        int storedNodeIndex = 0;
        double storedDist = Integer.MAX_VALUE;
        
        for (int i = 0; i < this.nodes.length; i++) {               //loop each node
            double currentDist = this.nodes[i].getTotalDistance();
            if (!this.nodes[i].isVisited() && currentDist < storedDist) {
                storedDist = currentDist;                         
                storedNodeIndex = i;                  // If shorter update path
            }
        }    
        return storedNodeIndex;
    }
    
    public void output() {
        String output = "Node Count :" + nodeCount;
        output += "\nEdge Count : " + edgeCount;
        
        for(int i=0; i < nodes.length; i++){
            output += ("\nNode 0 ~~~> " + i + " : " + nodes[i].getTotalDistance());
        }
        System.out.println(output);
    }
    
    public Node[] getNodes() { return nodes; }
    public Edge[] getEdges() { return edges; }
    
    public int getNodeCount() { return nodeCount; }
    public int getEdgeCount() { return edgeCount; }
 
    
    public static void main(String[] args) {
        /*
        Edge[] edges = {
          new Edge(0, 2, 21.6), new Edge(0, 3, 18.2), new Edge(0, 4, 2.3),
          new Edge(0, 1, 3.5), new Edge(1, 3, 2.4), new Edge(1, 4, 3.5),
          new Edge(1, 5, 1.4), new Edge(2, 4, 10.1), new Edge(3, 5, 4),
          new Edge(4, 5, 42.2), new Edge(4, 6, 6.7), new Edge(4, 7, 2),
          new Edge(5, 6, 5.4), new Edge(6, 7, 5), new Edge(1,2,5)
        };
        
        
        Graph g = new Graph(edges);
        g.dijkstra();
        
        */
  }
    
}
