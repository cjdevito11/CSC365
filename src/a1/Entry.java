/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

/**
 *
 * @author CJ
 */
public class Entry {
    
    //private K key;
    //private V value;
    private int key;
    private int count;
    private Entry root;
    private Entry next;
    private Entry prev;
    
    //public Entry(K key, V value){
    public Entry(int key, int count){
        this.key = key;
        this.count = count;
        this.root = null;
        this.next = null;
        this.prev = null;
       
    }

    Entry() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //public K getKey() { return key; }
    
    //public V getValue() { return value; }
    
    //public void setValue(V tempVal) { value = tempVal; }
    
    public int getKey() { return key; }
    
    public int getCount() { return count; }
    
    public int getLeast() { 
        Entry t = root;
        if (t == null) { return this.key; } // IDK
        while (t.prev != null) {
            t = t.prev;
        }
        return t.key;
    }
    
    public Entry getNext() { return next; }
    
    public void setKey(int tempKey){ key  = tempKey; }
    
    public void setCount(Integer tempCount) { count = tempCount; }
    
    public void addOne() { count++; }
    
    public void setNext(Entry tempNext){ next = tempNext; }
    
    
}
