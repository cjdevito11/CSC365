/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author CJ
 */
public class CustomBTree {
    
    public static final int degree = 4;   // 
    
    public Node root;
    String category;
    private String parentURL;
    private int nodeCount;
    private int height;
    private RandomAccessFile file;
    private File parentFile;
    
    public int totalWordsOnPage;
    public int numOfPages;
    
    public CustomBTree(String tempParentURL) {
        try {
            this.parentURL = tempParentURL;
            
            parentFile = new File(System.getProperty("user.dir") + "//treeFiles//" + parentURL + ".raf"); 
            file = new RandomAccessFile(parentFile, "rw");
            
            root = new Node(this, true);
            if (file.length() > 0){
                root = root.read(0);
            }
            
            
        } catch (Exception e) {e.printStackTrace();}
    }
    
    public CustomBTree(int num) {
        try {
            parentFile = new File(System.getProperty("user.dir") + "//treeFiles//urlTree" + num + ".raf"); 
            file = new RandomAccessFile(parentFile, "rw");
            
            root = new Node(this, true);
            if (file.length() > 0){
                //root = root.read(0);
               readTree(root);
            }
            //readTree(root);
            
            } catch (Exception e) {e.printStackTrace();}
        }
    
    public static void readTree(Node node){
        if (!node.isLeaf()){
            for(int i=0;i<node.howManyLinks();i++){
                Node tempLink = node.read(i);
                readTree(tempLink);
            }
        }
    }
    
    
    
    public CustomBTree(String tempParentURL, int num) {     // Label Tree File by num
        try {
            this.parentURL = tempParentURL;
            
            parentFile = new File(System.getProperty("user.dir") + "//treeFiles//urlTree" + num + ".raf"); 
            file = new RandomAccessFile(parentFile, "rw");
            
            root = new Node(this, true);
            if (file.length() > 0){
                readTree(root);
                //root = root.read(0);
            }
        } catch (Exception e) {e.printStackTrace();}
    }
    
    public CustomBTree(String tempParentURL, int treeNum, int ttlWordsOnPage, int numOPages) {
        try {
            this.parentURL = tempParentURL;
            this.totalWordsOnPage = ttlWordsOnPage;
            this.numOfPages = numOPages;
            
            parentFile = new File(System.getProperty("user.dir") + "//treeFiles//urlTree" + treeNum + ".raf"); 
            file = new RandomAccessFile(parentFile, "rw");
            
            root = new Node(this, true);
            
            if (file.length() > 0){
                readTree(root);
                //root = root.read(0);
            }
            
        } catch (Exception e) {e.printStackTrace();}
    }
    /*
    public boolean insert(String key, Word word){ 
        return insert(new BEntry(key, word));
    } // incase called with key / word
    */
    
    public boolean insert(BEntry entry) throws IOException{
    //    System.out.println("Try and Insert entry " + entry);
    //    System.out.println("Key:  " + entry.getKey());
        //System.out.println("value: " + entry.getWords()[0].getWord() + " - tf - " + entry.getWords()[0].getTf());
        
        if(root.isFull()){     //if root is full
            if(root.splitRoot(entry)){ //Split root and increment height
                height++;
     //           System.out.println("Split Root & INSERTED " + entry.getKey() + "\n\n");
                return true;
            }
        }
        else if(root.insert(entry)){
     //       System.out.println("INSERTED " + entry.getKey() + "\n\n");
            return true; } //Insert the entry down the tree

        return false;	
    }
    
    public Node getRoot() { return root; }
    
    public int getDegree() { return degree; }
    
    public int getHeight() { return height; }
	
    public int getNodeCount() { return nodeCount; }   
    
    public String getParentURL() { return parentURL; }
    
    public RandomAccessFile getFile() { return file; }
    
    public void incNodeCount() { this.nodeCount++; }
    
    
}
