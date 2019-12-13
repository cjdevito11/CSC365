/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

import static a1.CustomBTree.degree;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CJ
 */
public class BEntry implements Comparable<BEntry> {
    
    private static final int keyBlockSize = 256; // 
    public static final int childrenMax = degree*2;
    public static final int wordsMax = (degree * 2) - 1;
    private static final int wordBlockSize = ((childrenMax * 256) + (childrenMax * 8) + (childrenMax * 4)); // word block contains word int and double
    public static final int totalBlockSize = keyBlockSize + wordBlockSize;
    
    public static final int ENTRYBLOCKSIZE = keyBlockSize + 8 + 4;
    public static final int LINKBLOCKSIZE = 4;
    
    public static final int TOTALBLOCKSIZE = (ENTRYBLOCKSIZE * wordsMax) + (LINKBLOCKSIZE * childrenMax) +
            4 + 4 + 4;
    
    
    
    private String key;
   // private Word[] words = new Word[10];
    
    public Word word;
    
    private int count;
    
    int skipped = 0;
  
    
    
    public BEntry(String inputWord) {
        this.word = new Word(inputWord);
    }
    
    public BEntry(String key, Word word){
        this.key = key;
        this.word = word;
    }
    
    
   // public BEntry(String key, Word[] words){
	//this.key = key;
	//this.words = words;
    //}
    
    
    public BEntry(BEntry e){
	this.key = e.getKey();
	this.word = e.getWord();
    }

    BEntry() { throw new UnsupportedOperationException("Missing Param"); }
    
    public int getUtfLength(){
        try {
            return key.getBytes("UTF-32BE").length;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BEntry.class.getName()).log(Level.SEVERE, null, ex);
        }
            return 256;
        }
    
    
    public byte[] getKeyUtfBytes() throws UnsupportedEncodingException {
        try {
            
            byte[] bytes = new byte[256];
            byte[] keyBytes = key.getBytes("UTF-32BE");
            int byteLength = keyBytes.length;
            
            for (int i = 0; i < byteLength; i++){
                if(byteLength >= 256){                      // Skip any bytes > 256
                    skipped++;
                    System.out.println(skipped + " skipped " + key + " ( > 256 )\n\n\n");
                    continue;
		} else {
                bytes[i] = keyBytes[i];
                }
            }
            return bytes;
            
        } catch(UnsupportedEncodingException ex){ 
            ex.printStackTrace();
        }
        return null;
    }
    
   
    @Override
    public int compareTo(BEntry e){ 
       //System.out.println(key + " comparedTo " + e.getKey() + " = " + key.compareTo(e.getKey()));
      
           return key.compareTo(e.word.getWord());
        
    }

    public void incCount() { count++; }
    public int getCount() { return count; }

    public void setKey(String tempKey){ key  = tempKey; }
    public String getKey() { return key; }
    
    public Word getWord() { return word; }
    public void setWord(Word inputWord) { word = inputWord; }
    
    
    
    
  //  public void setWords(Word[] words) { this.words = words; }
  //  public Word[] getWords() { return words; }
    
    public void incWord() { word.incRawFreq(); }
    //public void incWord(int spot) { words[spot].incRawFreq(); }
    
    //public String getWord(int spot) { return words[spot].getWord(); }
    //public int getWordFreq(int spot) { return words[spot].getRawFreq(); }
    
    //public String getWord() { return word.getWord(); }
    //public int getWordFreq() { return word.getRawFreq(); }
    
    
    
    
   // public void calcIdf() { idf = Math.log(numberOfPages / rawFreq); }
    //public void calcTfIdf() { tfIdf = tf * idf; }
    /*
    public void calcTfIdf() { 
        if (numberWordsOnPage > 0){
            tf = count / totalWords;
        } else { tf = 0; }
        
        if (urlsContainedCounter > 0) {
            idf = Math.log(numberOfPages / urlsContainedCounter);
        } else { idf = 0; }
        
        tfIdf = tf * idf; 
    }
*/
    
    
    
    
    
}
