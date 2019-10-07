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
public class CustomHashMap<String,Integer> {
    
    private final static int TABLE_SIZE = 500000;
    
    Entry[] table;
    
    CustomHashMap() {
        table = new Entry[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++){
            table[i] = null;
        }
    }
    
    public int getSizeOfMap(){
        int size = 0;
        for (int i = 0; i < TABLE_SIZE; i++){
            if (table[i] != null) { size++; }
        }
        
        return size;
    }
    
    public int increment(String key){
        int hashKey = key.hashCode();
        int hash = (hashKey % TABLE_SIZE);
        if (hash < 0) { hash = (hash * -1 ) -1; }
        Entry entry = table[hash];
        entry.addOne();
        return entry.getCount();
    }    
    
    public int size(){
        return table.length;
    }
    
    public int get(String key) {
        int hashKey = key.hashCode();
        int hash = (hashKey % TABLE_SIZE);
        if (hash < 0) { hash = (hash * -1 ) -1; }
        
     //   System.out.println("hashKey     : " + hashKey);
    //   System.out.println("hash        : " + hash);
//        System.out.println("\n\ntable[hashKey] : " + table[hashKey]);
        if (table[hash] == null){
            return 0;
        } else {
            Entry entry = table[hash];
            while (entry != null && entry.getKey() != hashKey){
                entry = entry.getNext();
                if (entry == null){
                    return 0;
                } else {
                    return entry.getCount();
                }
            }
        }
        /*
        if (table[hash] == null && hashKey > 0){
            return -1;
        } else {
            while (table[hash] != null && table[hash].getKey() != hashKey){
                hash = (hash + 1) % TABLE_SIZE;
            }
        
        }
        */
        return table[hash].getCount();
    }
   /* 
     public int get(String key) {
        int hashKey = key.hashCode();
        int hash = (hashKey % TABLE_SIZE);
       // System.out.println(table[hash].getKey());
   
        while (table[hash] != null){
            if (table[hash].equals(key)){
                return table[hash].getCount();
            }
            hash = table[hash].getNext().getKey();
            
          //  hash = (hash + 1) % TABLE_SIZE;
            
            
        }
        return 0;       // 0 null
        
    }
    */
        public void put(String key, int count) {
        int hashKey = key.hashCode();
        int hash = (hashKey % TABLE_SIZE);
        if (hash < 0) { hash = (hash * -1 ) -1; }
        
        if (table[hash] == null){
            if (count == 0) { count = 1; }
             table[hash] = new Entry(hashKey, count);
        } else {
            Entry entry = table[hash];
            while (entry.getNext() != null && entry.getKey() != hashKey){
                entry = entry.getNext();
            }
            if (entry.getKey() == hashKey){
                entry.addOne();
            } else {
                entry.setNext(new Entry(hashKey, count));
            }
        }
    }
    
    /*
    public void put(String key, int count) {
        int hashKey = key.hashCode();
        int hash = (hashKey % TABLE_SIZE);
        if (hash < 0) { hash = (hash * -1 ) -1; }
        
        if (table[hash] == null){
             table[hash] = new Entry(hashKey, count);
        } else {
            Entry entry = table[hash];
            while (entry.getNext() != null && entry.getKey() != hashKey){
                entry = entry.getNext();
            }
            if (entry.getKey() == hashKey){
                entry.setCount(count);
            } else {
                entry.setNext(new Entry(hashKey, count));
            }
        }
    }
    */
    /*
      public void put(String key, int count) {
        int hashKey = key.hashCode();
        int hash = (hashKey % TABLE_SIZE);
        if (table[hash] != null){
             table[hash] = new Entry(hashKey, count);
        } else {
            Entry entry = table[hash];
            while (entry.getNext() != null || entry.getKey() != hashKey){
                entry = entry.getNext();
            }
            if (entry.getKey() == hashKey){
                entry.setCount(count);
            } else {
                entry.setNext(new Entry(hashKey, count));
            }
        }
    }
    */
}
