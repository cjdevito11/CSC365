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
    
    private final static int TABLE_SIZE = 16;
    private static int NEW_TABLE_SIZE = TABLE_SIZE;
    private static int sizeCounter = 0;
    
    
    Entry[] table;
    
    CustomHashMap() {
        sizeCounter = 0;
        NEW_TABLE_SIZE = TABLE_SIZE;
        table = new Entry[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++){
            table[i] = null;
        }
    }
    
    public int getSizeOfMap(){
        /*
        int size = 0;
        for (int i = 0; i < TABLE_SIZE; i++){
            if (table[i] != null) { size++; }
        }
        */
        return sizeCounter;
    }
    
    public int increment(String key){
        int hashKey = key.hashCode();
        int hash = (hashKey % table.length);
        if (hash < 0) { hash = (hash * -1 ) -1; }
        Entry entry = table[hash];
        entry.addOne();
        return entry.getCount();
    }    
    
    public int size(){
        return table.length;
    }
    
    public int get(String key) {
        if (sizeCounter >= table.length * .67){
            //table = resize();
            resize();
        }
        int hashKey = key.hashCode();
        int hash = (hashKey % table.length);
        if (hash < 0) { hash = (hash * -1 ) -1; }
        
        //while (hash >= NEW_TABLE_SIZE){ table = resize(); }
        
      //  System.out.println("\nsizeCounter : " + sizeCounter);
      //  System.out.println("TS : " + NEW_TABLE_SIZE);
      //  System.out.println("table.length     : " + table.length);
      //  System.out.println("hash        : " + hash);
        
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

    
        public void put(String key, int count) {
        if (sizeCounter >= table.length * .67){
            resize();
        }
        int hashKey = key.hashCode();
        int hash = (hashKey % table.length);
        if (hash < 0) { hash = (hash * -1 ) -1; }
        
        
        if (table[hash] == null){
            if (count == 0) { count = 1; }
             table[hash] = new Entry(hashKey, count);
             sizeCounter++;
             
        } else {
            Entry entry = table[hash];
            while (entry.getNext() != null && entry.getKey() != hashKey){
                entry = entry.getNext();
            }
            if (entry.getKey() == hashKey){
                entry.addOne();
            } else {
                entry.setNext(new Entry(hashKey, count));
                sizeCounter++;
            }
        }
    }
    public void resize() {
        int newSize = 2 * table.length;
        int oldSize = table.length;
        Entry[] oldHash = table;
        table = new Entry[newSize];
        
        //Entry[] oldHash = new Entry[oldSize];
        //oldHash = table;
        

        for (int i = 0; i < oldSize; i++) {
            
            while(oldHash[i] != null) {
                rehash(oldHash[i].getKey(), oldHash[i].getCount(), oldHash[i].getNext());
                oldHash[i] = oldHash[i].getNext();
            }
            
        }
    }
    
    public void rehash(int key, int count, Entry next) {
           
        int hash = (key % table.length);
        if (hash < 0) { hash = (hash * -1 ) -1; }

        table[hash] = new Entry(key, count);
        table[hash].setNext(next);
    }
                
	
 
        
        /*
         public Entry[] resize(){
            int i=0;
            
            NEW_TABLE_SIZE = NEW_TABLE_SIZE * 2;
            
            Entry[] newTable = new Entry[NEW_TABLE_SIZE];
            
            while (i < table.length) {
                newTable[i].setKey(table[i].getKey());
            }
            while (i < NEW_TABLE_SIZE) { 
                newTable[i] = null;
                i++;
            }
            return newTable;
            }
        
        */
        /*
        public Entry[] resize(){
            int i=0;
            
            NEW_TABLE_SIZE = NEW_TABLE_SIZE * 2;
            
            Entry[] newTable = new Entry[NEW_TABLE_SIZE];
            
            while (i < table.length) {
                Entry entry = table[i];
                
                while (entry.getNext() != null && entry.getKey() != i){
                    entry = entry.getNext();
                }
                if (entry.getKey() == i){
                    newTable[i].addOne();
                } else {
                    newTable[i].setNext(new Entry(i, 1));
                }
                
                newTable[i]=table[i];
                i++;
                
            }
            while (i < NEW_TABLE_SIZE) { 
                newTable[i] = null;
                i++;
            }
            return newTable;
            */
    
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
