/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

//import java.util.HashMap;

import java.util.ArrayList;
import java.util.Map;


/**
 *
 * 
 * @author CJ
 * 
 * use corpus to save all words to a map w/ count of pages
 */
public class Corpus {
    public CHashMap map;
    ArrayList<String> words;
    
    
    public Corpus() { map = new CHashMap();
                        words = new ArrayList();}
   
    public Corpus(CHashMap inputMap){ map = inputMap;
                                        words = new ArrayList();}
    
    /*
    public int containedIn(String word) {
        int total = 0;
        
        for (int i=0; i < map.size(); i++) {
            
            map.get(word);
        }
        
        return total;
    }
    */
    
    public void addWord(String word) { 
        
        if (!words.contains(word)){ words.add(word); }
        map.put(word); }
    
    public ArrayList<String> getWords(){
        return words;
    }
    
    public int getWordCount(String word) { return map.get(word); }
    
    public void setMap(CHashMap iMap) { this.map = iMap; }
    public CHashMap getMap() { return map; }
    
}
