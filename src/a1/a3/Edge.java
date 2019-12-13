/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1.a3;

/**
 *
 * @author CJ
 */
public class Edge {
    
    public Node src;
    public Node dest;
    
    int srcIndex;
    int destIndex;
    
    double weight;

    public Edge(int srcIndex, int destIndex){
        this.srcIndex = srcIndex;
        this.destIndex = destIndex;
        this.weight = 0;
    }
        
    public Edge(int srcIndex, int destIndex, double weight){
        this.srcIndex = srcIndex;
        this.destIndex = destIndex;
        this.weight = weight;
    }
    
      public Edge(Node src, Node dest){
        this.src = src;
        this.dest = dest;
        this.weight = 0;
    }
      
    public Edge(Node src, Node dest, double weight){
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
    
    
    public int getSrcIndex(){ return srcIndex; }
    public int getDestIndex() { return destIndex; }
    public double getWeight() { return weight; }
    
    public int getConnectIndex(int index) {
        if (this.srcIndex == index){
            return this.destIndex;                  // IF SRC GO TO NEXT CONNECT
        } else {
            return this.srcIndex;
        }
    }
    
}