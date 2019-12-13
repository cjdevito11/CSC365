/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author CJ
 */
public class Word {
    
    public static Corpus corpus = A1.corpus;
    private String word;
    private long hash;
    private int rawFreq = 0;
    private int totalWords;
    
    private double tf;
    private double idf;
    private double tfIdf;
    
    private String[] urlsContainedIn;
    private int urlsContainedCounter;
    
    private int numberOfPages;
    
    private int skipped = 0;
    
    public Word(String inputWord){
        //this.word = inputWord;
        this.hash = hash(inputWord);
        this.rawFreq = 1;
    }
    public int hash(String key){
        int hashKey = key.hashCode();
        
        if (hashKey < 0) { hashKey = (hashKey * -1 ) -1; }
        return hashKey;
    }
/*
    public Word(String url, String inputWord, int total, int numOfPages) { 
        this.url = url;
        this.word = inputWord;
        this.totalWords = total;
        this.numberOfPages = numOfPages;
        
        for (int i=0; i < urlsContainedIn.length; i++){
            if (urlsContainedIn[i] == null) {
                urlsContainedIn[i] = url;
                urlsContainedCounter++;
                
                calcTfIdf();
                
            } else if (urlsContainedIn[i].compareTo(url) == 0){
                    break;   
            }
        }
    }
    */
    /*
    public Word(String word, double tfIdf) { 
        this.word = word;
        this.tfIdf = tfIdf;
    }
    */
     public Word(String word, double tf) { 
        this.word = word;
        this.tf = tf;
    }
    public int getUtfLength(){
        try { System.out.println("UTFLENGTH : " + word.getBytes().length);
            return word.getBytes().length;
        } catch (Exception e){
            e.printStackTrace();
        }
        return 256;
    }

    public byte[] getUtfBytes(){
        try {
            byte[] bytes = new byte[256];           //256 length
            byte[] keyBytes = word.getBytes();
            int byteLength = keyBytes.length;
            
            for(int i = 0; i < keyBytes.length; i++) {
                  if(byteLength >= 256){                      // Skip any bytes > 256
                    skipped++;
                    System.out.println(skipped + " skipped " + word + " ( > 256 )\n\n\n");
                    continue;
		} else {
                bytes[i] = keyBytes[i];
                }
            }
            return bytes;
        } catch (Exception e) {}
        return null;
    }
    
    public void incRawFreq() { rawFreq++; calcTf();}
    public int getRawFreq()  { return rawFreq; }
    
    public void setTf(double inputTf) { tf = inputTf; }
    public double getTf() { calcTf();
        return tf; }
    
    public void setTfIdf(double inputTfIdf) { tfIdf = inputTfIdf; }
    public double getTfIdf() { return tfIdf; }
    
    public void calcTf() { setTf(rawFreq / corpus.words.size()); }
    
    public void calcTf(double totalWords){ setTf(rawFreq / totalWords); }
    public void calcIdf() { idf = Math.log(numberOfPages / rawFreq); }
    //public void calcTfIdf() { tfIdf = tf * idf; }
    
    public void calcTfIdf() { 
        if (totalWords > 0){
            tf = rawFreq / totalWords;
        } else { tf = 0; }
        
        if (urlsContainedCounter > 0) {
            idf = Math.log(numberOfPages / corpus.getWordCount(word));
        } else { idf = 0; }
        
        tfIdf = tf * idf; 
    }
    
    public void setWord(String inputWord) { this.word = inputWord; }
    public String getWord() { return word; }
    
    public void setTotalWords(int total) { totalWords = total; }
    public int getTotalWords(){ return totalWords; }
}
