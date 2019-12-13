/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author CJ
 */
public class Node {
    public static final int degree = CustomBTree.degree;   
    public static final int halfDegree = degree / 2;
    public static final int entryBlockSize = BEntry.totalBlockSize;
    public static final int childrenMax = BEntry.childrenMax;
    public static final int TOTALBLOCKSIZE = BEntry.TOTALBLOCKSIZE;
    
    private static final int linkBlockSize = degree * 4; //4 bytes per link because int
    public static final int blockSize =
            linkBlockSize + ((degree - 1) * 8) + ((degree - 1) * entryBlockSize);
    
    static final int INT_SIZE = 4;
    static final int DOUBLE_SIZE = 8;
    static final int LONG_SIZE = 8;
    static final int CHAR_SIZE = 2;

    int NODENUMPOS = 0;
    int PARENTPOS = NODENUMPOS + INT_SIZE;
    int LEAFPOS = PARENTPOS + INT_SIZE;
    int LINKPOS = LEAFPOS + INT_SIZE;
    int LENGTHPOS = LINKPOS + (INT_SIZE*childrenMax);
    int TFPOS = LENGTHPOS + INT_SIZE;
    int KEYPOS = TFPOS + DOUBLE_SIZE;

        
    private CustomBTree tree;
    public HashMap<Integer,Node> loadedLinks;
    private BEntry[] keys;
    private int[] links;
    private int nodeNum;
    
    private int parent;
    
    
    int nodeHeight;
    boolean leaf;
    
    public int howManyLinks(){
        return links.length;
    }
    
    private boolean writeCheck;
    
   // public int totalWordsOnPage;
   // private int numPages;
    
    public Node(CustomBTree tree, boolean inc){
        this.tree = tree;
       // this.totalWordsOnPage = tree.totalWordsOnPage;
       // this.numPages = tree.numPages;
        
        loadedLinks = new HashMap<>();
       // keys = new BEntry[degree - 1];     // initialize size of keys/links     KEYS :  [3] [5] [7] 
        //links = new int[degree];           //                               ex. Links:[1] [4] [6] [8]
        keys = new BEntry[(2*degree) - 1];     // initialize size of keys/links     KEYS :  [3] [5] [7] 
        links = new int[2*degree];           //                               ex. Links:[1] [4] [6] [8]
        
        this.nodeHeight = tree.getHeight();
        this.nodeNum = tree.getNodeCount();
        if(inc) { tree.incNodeCount(); }
        
    }
    
    
    
    public Node read(int blockNum){
        
        RandomAccessFile file = tree.getFile();
        
        int[] tempLinks = new int[2 *degree];
        BEntry[] tempKeys = new BEntry[(2*degree) -1];
        
        Node returnNode = new Node(tree,true);
        
        returnNode.setKeys(tempKeys);
        returnNode.setLinks(tempLinks);
        returnNode.setNodeNumber(blockNum);
        
        try {
            //System.out.println("Reading block num " + blockNum + " at location " + (blockSize + 1) * blockNum);
            
            file.seek((blockSize + 1) * blockNum);           //look for block
           
            for (int i=0; i < degree; i++) {  
               // tempLinks[i]
                tempLinks[i] = file.readInt();               //set temp links    
            }
            
            //System.out.println("1");
            
            for (int i=0; i < degree - 1; i++) {            //set entries
            //    int byteCount = file.readInt();
            //    //System.out.println("byteCount: " + byteCount);
               
            //    if(byteCount == 0) {                         //null entry
            //        //System.out.println("byteCount == 0 so continue");
            //        continue; 
            //    }               
                
        //        byte[] keyBytes = new byte[byteCount];
                
        //        //System.out.println("byteCount: " + byteCount);
                
        //        file.readFully(keyBytes);
                String key = file.readUTF();
                if (key.equals("")){
                    continue;
                }
        //        file.skipBytes(256 - byteCount);
                //String key = new String(keyBytes, Charset.forName("UTF-32BE"));
                //System.out.println("key: " + key);
                
                Word word = new Word(key);
                BEntry tempEntry = new BEntry(key,word);
                
                
                
         //           int wordKeyLength = file.readInt();
         //           //System.out.println("wordKeyLength: " + wordKeyLength);
                    
        //            if (wordKeyLength == 0) { 
        //                //System.out.println("$$ wordKeyLength == 0 so SKIP 256+8 Bytes $$");
        //                file.skipBytes(256+8);               // skip
        //                continue;
        //            }
        //            byte[] wordKeyBytes = new byte[wordKeyLength];
                    
                    String wordKey = file.readUTF();
                    //file.readFully(wordKeyBytes);                    //reads "wordKeyBytes".length from file
                    //file.skipBytes(256-wordKeyLength);               //skip to end of block
                    
                   //String wordKey = new String(wordKeyBytes, Charset.forName("UTF-32BE"));     //save bytes to wordKey
                    //System.out.println("wordKey: " + wordKey);
                    
                    double wordTfIdf = file.readDouble();
                    //word = new Word(fword, wordTfIdf);
                    
                    word = new Word(wordKey, wordTfIdf);
                    tempEntry.setWord(word);
                    tempKeys[i] = tempEntry;
                    //System.out.println("tempKeys[i].getWord().getWord(): " + tempKeys[i]);
            }
            
        } catch(Exception e) { System.out.println("Exception: " + e); }
        
        loadedLinks.put(blockNum, returnNode);
        /*
        if (!this.isLeaf()){
            for(int i=0;i<tempLinks.length;i++){

                Node tempNode = new Node(tree,true);
                tempNode.read(tempLinks[i]);
                
            }
        }
        */
        return returnNode;
    }
    
    
    private void readWhatIWrote(Node wroteNode) throws IOException{
        Node readNode = this.readNode(nodeNum);
       // if (readNode == wroteNode){ 
        if(readNode.links[0] == wroteNode.links[0] && (readNode.keys[0] == wroteNode.keys[0])){
            System.out.println("EQUAL");
        } else {System.out.println("NOT EQUAL");
        }
    }
    
    
    private Node readNode(int blockNum) throws IOException {
         
        BEntry[] tempKeys = new BEntry[(2*degree) -1];
        byte[] keyBytes = new byte[256];
        int[] tempLinks = new int[2*degree];
            
        RandomAccessFile file = tree.getFile();
        
        ByteBuffer b = ByteBuffer.allocate(TOTALBLOCKSIZE);
       
        Node returnNode = new Node(tree, true);
        
   
        
        try{
            file.seek((blockSize + 1) * blockNum);           //look for block

            FileChannel fc = file.getChannel();
            fc.read(b);
            

            int tempNodeNumber = b.getInt(NODENUMPOS);
            int tempParent = b.getInt(PARENTPOS);
            int tempLeaf = b.getInt(LEAFPOS);
            
            //UI.setOutput("tempNode(" + tempNodeNumber);
         
            double tempTF = 0;
            
            returnNode.setNodeNumber(blockNum);
          //returnNode.setNodeNumber(tempNodeNumber);
            returnNode.parent = tempParent;
            
            if (tempLeaf==1){returnNode.leaf = true;}
            else{returnNode.leaf = false;}


            for(int i=0;i<childrenMax;i++){
                tempLinks[i] = b.getInt(LINKPOS + (INT_SIZE*i));
            }

            for (int i=0; i<((degree*2)-1);i++){
                
                int wordLength = b.getInt((LENGTHPOS+(268*i)));
                if (wordLength != -1 && wordLength != 0 && wordLength < 9999999){
                    
                    tempTF = b.getDouble((TFPOS + (268*i)));

                        System.out.println("KEYPOS + 268 * i : " + (KEYPOS+(268*i)));

                    keyBytes = new byte[wordLength];

                    b.get(keyBytes,(KEYPOS+(268*i)),wordLength);

                    String tempKey = Arrays.toString(keyBytes);

                    Word word = new Word(tempKey,tempTF);
                    BEntry tempEntry = new BEntry(tempKey,word);
                    tempEntry.setWord(word);     
                    
                    tempKeys[i] = tempEntry;
                    tempKeys[i].setKey(tempKey);
                    //GUI.setOutput("tempKeys[i].setKey(" + tempKey);

                }
            }
            
            returnNode.setKeys(tempKeys);
            returnNode.setLinks(tempLinks);
            
            loadedLinks.put(returnNode.nodeNum,returnNode);
            b.clear();
            
            return returnNode;
        } catch(Exception e){System.out.println(e);}
        
        return returnNode;
    }
    
    
    private void writeNode() throws IOException{
        try {
            RandomAccessFile file = tree.getFile();
           // loadedLinks.put(nodeNum, this);
            
            ByteBuffer b = ByteBuffer.allocate(TOTALBLOCKSIZE);

            b.putInt(nodeNum);          // Multiply by TOTALBLOCKSIZE for POSITION
            b.putInt(parent);
            
          //  GUI.setOutput("nodeNum" + nodeNum);
            
            if (isLeaf()){ b.putInt(1);}
            else { b.putInt(0);}               // 1 is leaf 0 isn't
            
            for(int i : links) {
              //  if (i == 0){
              //      b.putInt(-1);
              //      continue;
              //  }
                b.putInt(i); }            //write links
            
            for(BEntry entry : keys) {    // for each entry in keys
                if(entry == null) {
                    for(int i=0;i<67;i++){
                        b.putInt(-1);
                    }
                    continue;
                }
                
                Word tempWord = entry.getWord();
                
                b.putInt(tempWord.getUtfLength());
               // GUI.setOutput("length" + tempWord.getUtfLength());
                b.putDouble(tempWord.getTf());          // add word length / word / tf
                b.put(tempWord.getUtfBytes());
               // GUI.setOutput("b.put(" + tempWord.getUtfBytes().toString());
                System.out.println("b.put(" + tempWord.getUtfBytes().toString());
            }
            

            
            FileChannel fc = file.getChannel();
            
            fc.write(b);

            //System.out.println(Arrays.toString(b.array()));

            readWhatIWrote(this);
            
        }catch (IllegalArgumentException e) { 
            //System.out.println("Exception thrown : " + e); 
        } 
  
     
    }
    
    
    
    private void writeTree(){
        //System.out.println("Try and write Tree to file");
        
        RandomAccessFile file = tree.getFile();
        loadedLinks.put(nodeNum, this);
        
      
        try {
            
            //System.out.println("$Try write node #" + nodeNum);
            //System.out.println("writing @: " + (blockSize + 1) * nodeNum);
            
            file.seek((blockSize + 1) * nodeNum);   
            for(int i : links) { 
                //System.out.println("LINKS: " + i);
                file.writeInt(i); }            //write links
            
            for(BEntry entry : keys) {    // for each entry in keys
                
                if(entry == null) {
                    file.writeUTF("");
                   // file.write(new byte[entry.totalBlockSize]);
                    continue;
                }
                //file.writeInt(entry.getUtfLength());            // write key
                //file.write(entry.getKeyUtfBytes());
                file.writeUTF(entry.getKey()); 
                
              //for (Word word: entry.getWord()){
                Word word = entry.getWord();
                if (word == null){
                    file.writeInt(0);
                    //file.write(new byte[256]);              // write words
                    //file.write(new byte[8]);
                    continue;
                }
                ////System.out.println(word.getUtfLength());
                ////System.out.println(word.getUrlUtfBytes());
                ////System.out.println(word.getTfIdf());
                
                //file.writeInt(word.getUtfLength());         
                //file.write(word.getUrlUtfBytes());   // testing 2 utf's
                file.writeUTF(word.getWord());
                
                file.writeDouble(word.getTfIdf());          // write tf-idf

              //  }
            }
            readWhatIWrote(this);
        } catch (IOException e) { System.out.println(e); }
    }
    
        // splitRoot to new left and right nodes then insert new entry
    public boolean splitRoot(BEntry e) throws IOException {
        //System.out.println("splitRoot to new left and right nodes then insert new entry!");
        
        Node left = new Node(tree,true);            // Branch to Left and Right
        Node right = new Node(tree,true);
        
        for (int x=0; x < halfDegree; x++){
            //System.out.println(this.keys[x]);
            //System.out.println(this.keys[x + halfDegree]);
            
            left.keys[x]=this.keys[x];           // Set first half of Node to new left node 0/1
            right.keys[x]=this.keys[x + halfDegree]; // Set back half of Node to new right node 2/3
        }
        
        for(int x = 0; x < halfDegree; x++){      // Set new links for left and right
            left.links[x] = this.links[x];
            right.links[x] = this.links[x + halfDegree];
	}
        
        keys[0] = keys[((degree - 1) / 2)];     //set new single root to middle left key
        for(int x = 1; x < keys.length; x++){
            keys[x] = null;                         //Empty Root Keys
        }
        
        int leftNodeNumber = left.getNodeNumber();      //get Node locations
        int rightNodeNumber = right.getNodeNumber();
        
        links[0] = leftNodeNumber;              // set left & right links of new root to the new 
        links[1] = rightNodeNumber;             // left and right nodes w/ the old root values
      
        left.parent = this.nodeNum;
        right.parent = this.nodeNum;
        
        left.writeNode();
        right.writeNode();
        
        for(int x = 2; x < links.length; x++){
            links[x] = 0;                       //Delete the rest of the links because i only have one value in root and two links to l and right nodes
	}
        
        writeNode();
        return insert(e);
    }
    
    public void splitFullNode(Node fullNode) throws IOException {
        
        int midIndex = 0;
        BEntry mid = fullNode.keys[(degree - 1)/2];
        
        for(int x = 0; x < keys.length; x++) {
            if(keys[x] == null){                
                keys[x] = mid;
                midIndex = x;
                break;
            } else if(mid.compareTo(keys[x]) < 0) {
                for(int i = keys.length-1; i > x; i--) {
                    keys[i] = keys[i-1];
                    links[i+1] = links[i];
                }
            keys[x] = mid;
            midIndex = x;
            break;
            }
        }
        Node right = new Node(tree, true);
        
        for(int x = 0; x < halfDegree - 1; x++){
            right.keys[x] = fullNode.keys[x + halfDegree];
            right.links[x] = fullNode.links[x + halfDegree];
        }
        
        right.links[halfDegree - 1] = fullNode.links[degree - 1];
        int rightNum = right.getNodeNumber();
        links[midIndex + 1] = rightNum;
        
        right.parent = fullNode.parent;
        right.writeNode();
        
        for(int x = halfDegree - 1; x < degree - 1; x++){
            fullNode.keys[x] = null;
            fullNode.links[x+1] = 0;
        }
        links[midIndex] = fullNode.getNodeNumber();
        fullNode.writeNode();
    }
 
    public boolean insert(BEntry entry) throws IOException{
        
        for(int x = 0; x < keys.length - 1; x++){
            
            if(keys[x] == null){
                if (isLeaf()){                  // if leaf add entry
                    keys[x] = entry;
                    //System.out.println("Added " + entry.getWord() + " at Leaf");
                    writeCheck = true;
                    return true;
                } else {
                    Node xLink = getLink(x);
                    
                    if(xLink.isFull()){
                        //System.out.println("\n\n\n\n\n\n\nFull so Split\n\n\n\n\n\n\n\n");
                        
                        splitFullNode(xLink);           // split & save
                        
                  //      writeNode();
                        
                        writeCheck = true;
                        return(insert(entry));          //insert after split                        
                    } else {
                        boolean insert = xLink.insert(entry);  
                        //System.out.println("Added " + entry.getWord() + " at link " + x);
                   
                        if (xLink.writeCheck) {         //If checked Write then reset check
                            xLink.writeNode();
                            xLink.writeCheck = false;
                        }
                        return insert;                        
                    }
                }
            } else if(entry.compareTo(keys[x]) == 0){
                    if (keys[x].getWord() == null){
                        //keys[x].setWord(entry.getWord());
                        //keys[x].setKey(entry.getKey());
                        
                        keys[x].incWord();
                        ////System.out.println("Add entry " + entry.getKey());
                        writeNode();
                        return true; 
                    }
                
            } else if(entry.compareTo(keys[x]) < 0) {
                
                if (links[x] != 0) {
                    Node xLink = getLink(x);
                    
                    if(xLink.isFull()){
                        splitFullNode(xLink);
                        writeCheck = true;
                        return insert(entry);
                    } else {
                        boolean insert = xLink.insert(entry);
                        //System.out.println("Added " + entry.getWord() + " at xLink[" + x + "]");
                   
                        if(xLink.writeCheck){
                            xLink.writeNode();
                            xLink.writeCheck = false;
                        }
                        return insert;
                    }  
                } else {
                    for(int i = 0; i >= keys.length; i++){
                        if(entry.compareTo(keys[i]) == 0) { return false; }             // ??!?!?!?
                    }
                    int i2 = keys.length - 2;
                    while(i2 >= x){
                        keys[i2+1] = keys[i2];
                        i2--;
                    }
                    keys[x] = entry;
                    writeCheck = true;
                    return true;
                }
            }/* else if (x == degree - 2) {
                //System.out.println("x = " + x);
                //System.out.println("entry = " + entry.getKey());
                Node xLink1 = getLink(x+1);
		boolean r = xLink1.insert(entry);//save success
		if(xLink1.writeCheck){  // if node changed
                    xLink1.writeCheck = false;//reset save
                    xLink1.writeTree();//re-write it
		}
                    return r;//return success
            }*/
        }
        return true;
    }
 
    // from book 
    //splitNode = x, index = i, .n = nodeNum degree = t
    // . = links[]
    /*
    public boolean splitChild(Node splitNode, int index){
        Node z = new Node(tree, true);
        Node y = splitNode.getLink(index);
        z.leaf = y.leaf;
        z.nodeNum = degree - 1;
        for (int j = 1; j < degree - 1; j++){
            z.keys[j] = y.keys[j+degree];
        }
        if (!y.leaf){
            for (int j = 1; j < degree; j++){
                z.links[j] = y.links[j+degree];
            } 
        }
        y.nodeNum = degree - 1;
        for (int j = splitNode.nodeNum; j >= index + 1 ; j--){
            splitNode.links[j+1] = splitNode.links[j];
        }
 ?? splitNode.links[index+1] = z.nodeNum;
        for (int j = splitNode.nodeNum; j >= index; j--){
            splitNode.keys[j+1] = splitNode.keys[j];
        }
        splitNode.keys[index] = y.keys[degree];
        splitNode.nodeNum = splitNode.nodeNum + 1;
        writeTree();
        return false;
    }
    
    public boolean insertBook(BEntry entry){
        Node r = tree.root;
        
        if (r.isFull()){
           Node s = new Node(tree,true);
            tree.root = s;
            s.leaf = false;
            s.nodeNum = 0;
            Node sc1 = s.getLink(1);
            sc1 = r;
            // split Child
            // insert NonFull
            return true;
        } else {
            // insert NonFull
            return true;
        }
    }
    /*
    public boolean insertNonFull(Node x,int k){
        int i = x.nodeNum;
        if (x.leaf){
            while(i >= 1 && k < x.keys[i].word.){
                
                i++;
            }
        }
        
        
        return false;
    }
    */
    
    
    
    
    
    public Word search(String key) throws IOException {
        //System.out.println("Search for key " + key);
        
        for(int x = 0; x < keys.length; x++) {
            //System.out.println("Search for " + key + " @ " + "keys[" + x + "]");
            //System.out.println("\nx:  " + x + " | node #" + nodeNum);
            
            if(keys[x] == null) {
                //System.out.println("keys[x] at " + x + " == null");
                
                if(isLeaf()) {
                    //System.out.println("\nFound leaf");
                    return null;
                }
                return getLink(x).search(key);
                
            } else if(key.compareTo(keys[x].getKey()) == 0){
                //System.out.println("\nFound Word"+keys[x].getWord());
                    return keys[x].getWord();
                    
            } else if(key.compareTo(keys[x].getKey()) < 0){
                if (isLeaf()) {
                    //System.out.println("\nFound leaf");
                    return null;
                } else { return getLink(x).search(key); }
                
            } else if(x == keys.length - 1) {
                 if (isLeaf()) {
                    //System.out.println("\nFound leaf");
                    return null;
                } else { return getLink(x + 1).search(key); }
            }
        }
        return null;
    }
    
 
    public boolean isFull() { return getNumKeys() == keys.length; }

    //public boolean isLeaf() { return  nodeHeight == tree.getHeight(); } // If no links it's a leaf

    public boolean isLeaf() { return links[0] == 0; }
    
    public void setKeys(BEntry[] e) { keys = e; }
    public int getNumKeys() {
        int counter = 0;
        
        for(BEntry e : keys){
            if(e != null){ 
                counter++;
            }
        }
        //System.out.println(counter + " keys");
        return counter;
    }
    
    public void setLinks(int[] l) { this.links = l; }
    
    /*public Node getLink(int i){
	Node inRam = loadedLinks.get(links[i]);
	//System.out.println("inRam: " + (inRam != null));
        return inRam != null ? inRam : read(links[i]);
    }*/
    
    
    public Node getLink(int i) throws IOException{
        try{
            //System.out.println("Please Dont loop.. i= "+ i);
            Node temp = loadedLinks.get(links[i]);
        
            if (temp != null){
                return temp;
            } else {return readNode(links[i]);}
            
            } catch(Exception e) {
                return readNode(links[i]);
            }
        
        /*
        if (temp != null ) {
            return temp;
        } else {
            return read(links[i]);
        }*/
    }
        
    public void setNodeNumber(int i) { nodeNum = i; }
    public int getNodeNumber() { return nodeNum; }
    
   
    public BEntry[] getKeys() { return keys; }
     
   // See if Left string is less than right string
    private boolean lessThan(String left, String right){    
        return left.compareTo(right) < 0;
    }
    
}
