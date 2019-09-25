/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author CJ
 */
class words {
    String word;
    AtomicLong number;
}


public class A1 {

    //ConcurrentHashMap<String, AtomicLong> map = new ConcurrentHashMap<>();  // Initialize with (Int) to start w/ default size
    
    public static void checkTF(Map<String, AtomicLong> map){
            AtomicLong a = map.get("a");
        if(a != null){
            Double aTF = a.doubleValue() / map.size();
            System.out.println("\n" + "a : " + a.longValue());
            System.out.println("aTF(a term frequency) : " + aTF);
        }
    }

    
    /**                urlToMap Function
     * @param url
     * @return Map
     * @throws java.io.IOException =
     */
    
    public static ConcurrentHashMap<String, AtomicLong> urlToMap(String url) throws IOException{
        ConcurrentHashMap<String, AtomicLong> map = new ConcurrentHashMap<>();
        int splitCounter = 0;        
        Document doc = Jsoup.connect(url).get();
        String title = doc.title();
        String body = doc.body().text();
        String[] splitInput = body.split("\\s+");
    
        while(splitCounter < splitInput.length){
            AtomicLong l = map.get(splitInput[splitCounter]);
            if(l == null){
                l = new AtomicLong(1);
                l = map.putIfAbsent(splitInput[splitCounter].toLowerCase(), l);
                if(l != null){
                    l.incrementAndGet();
                }
            }else{
                l.incrementAndGet();
            }
            splitCounter++;
        }
        
        // Output
        System.out.println("\n\n"+title);
        
        //
        return map;
    }
    

    
    public static void output(Map<String, AtomicLong> map){
        AtomicLong a = map.get("a");
        if(a != null){
            Double aTF = a.doubleValue() / map.size();
            System.out.print("\n" + "a : " + a.longValue());
            System.out.print("  |   TF : " + aTF);
        }
        
        AtomicLong to = map.get("to");
        if(to != null){
            Double toTF = to.doubleValue() / map.size();
            System.out.print("\nto : " + to.longValue());
            System.out.print("  |   TF : " + toTF);
        }
        AtomicLong the = map.get("the");
        if(the != null){
            Double theTF = the.doubleValue() / map.size();
            System.out.print("\nthe : " + the.longValue());
            System.out.print("  |   TF : " + theTF);
        }
        AtomicLong so = map.get("so");
        if(so != null){
            Double soTF = the.doubleValue() / map.size();
            System.out.print("\nso : " + so.longValue());
            System.out.print("  |   TF : " + soTF);
        } else {
            System.out.println("\nno so's");
        }
        System.out.println("\n# words in doc): " + map.size());
    }
    
    public static ArrayList<String> readFile(String filename) throws IOException{
        
        ArrayList<String> urlList = new ArrayList<>();
 
        try (FileReader file = new FileReader(filename)) {
            StringBuffer sb = new StringBuffer();
            while (file.ready()) {
                char c = (char) file.read();
                if (c == '\n') {
                    urlList.add(sb.toString());
                    sb = new StringBuffer();
                } else {
                    sb.append(c);
                }
            }
            if (sb.length() > 0) {
                urlList.add(sb.toString());
            }
        }      
        
        return urlList;
    }
    
    public static void checkSym(List listOfMaps){
        int pageNumber = 0;
        int contains = 0;
        Double soTF = 0.0;
        System.out.println(listOfMaps.size());
        while (pageNumber < listOfMaps.size()){
            Map<String,AtomicLong> tempMap;
            tempMap = (Map<String, AtomicLong>) listOfMaps.get(pageNumber);
        
            AtomicLong so = tempMap.get("so");
            
            if(so != null){
                soTF = so.doubleValue() / tempMap.size();
                System.out.print("\nso : " + so.longValue());
                System.out.print("  |  TF : " + soTF);
                contains++;
               }
            pageNumber++;
        }
        System.out.println("\n" + contains + " page(s) contain so");
        
        Double IDF = Math.log(listOfMaps.size() / contains);
        Double tfidf = soTF * IDF;
        
        System.out.println("  | IDF : " + IDF);
        System.out.println("  | tfidf : " + tfidf);
        
    }
    
        public static void tfidfOfWord(List listOfMaps,String wordIn){
        int pageNumber = 0;
        int contains = 0;
        Double wordTF = 0.0;
        Double IDF = 0.0;
        Double tfidf = 0.0;
        
        System.out.println(listOfMaps.size());
        while (pageNumber < listOfMaps.size()){
            Map<String,AtomicLong> tempMap;
            tempMap = (Map<String, AtomicLong>) listOfMaps.get(pageNumber);
        
            AtomicLong word = tempMap.get(wordIn);
            
            if(word != null){
                wordTF = word.doubleValue() / tempMap.size();
                System.out.print("\nthe : " + word.longValue());
                System.out.print("  |  TF : " + wordTF);
                contains++;
               }
            pageNumber++;
        }
        System.out.println("\n" + contains + " page(s) contains " + wordIn);
        
        if (contains > 0) {
            IDF = Math.log(listOfMaps.size() / contains);
        } else {
            IDF = 0.0;
        }
        tfidf = wordTF * IDF;
        
        System.out.println(listOfMaps.size());
        System.out.println(contains);
        
        System.out.println("  | IDF : " + IDF);
        System.out.println("  | tfidf : " + tfidf);
        
    }
    
    
    
    public static void main(String[] args) throws Exception {
        int pageNumber = 0;
        List<String> urlList = readFile("urls.txt");
        List<Map<String,AtomicLong>> listOfMaps = new ArrayList<>();
        
        while (pageNumber < urlList.size()){
            System.out.println(urlList.get(pageNumber));
            
            ConcurrentHashMap<String, AtomicLong> tempMap = urlToMap(urlList.get(pageNumber));
            
            output(tempMap);
            
            listOfMaps.add(tempMap);
            
       //    ConcurrentHashMap<Integer, words> map = urlToMap(urlList.get(pageNumber),pageNumber);
            
            pageNumber++;
        }
        //checkSym(listOfMaps);
        tfidfOfWord(listOfMaps,"Doug".toLowerCase());
        
        //ConcurrentHashMap<String, AtomicLong> map = urlToMap("https://en.wikipedia.org/wiki/Java_ConcurrentMap");
        //ConcurrentHashMap<String, AtomicLong> map2 = urlToMap("https://en.wikipedia.org/wiki/Doug_Lea");
      
        
        
        
    }
}








/*


        while ((inputLine = in.readLine()) != null){        // Add input to List
            inputList.add(inputLine);
        }

        

private final ConcurrentMap<String,AtomicInteger> map "
    new ConcurrentHashMap<String,AtomicInteger>();

public void increment(String key) {
    AtomicInteger value = new AtomicInteger(0);
    AtomicInteger old = map.putIfAbsent(key,value);
    if (old != new) { value = old; }
    value.incrementAndGet(); // increment the value atomically
}

public Integer getCount(String key) {
    AtomicInteger value = map.get(key);
    return (value == null) ? null : value.get();
}



    public void addWord(String word){
       AtomicLong l = map.get(word);
       if(l == null){
           l = new AtomicLong(1);
           l = map.putIfAbsent(word, l);
           if(l != null){
               l.incrementAndGet();
           }
       }else{
           l.incrementAndGet();
       }
   }
    
    public long getCount(String word){
       AtomicLong l = map.get(word);
       if(l != null){
           return l.longValue();
       }
       return 0;
   }




        while ((inputLine = in.readLine()) != null){        // Add input to map
            
            splitInput = inputLine.split("\\s+");
       
            while(splitCounter < splitInput.length){
                
           // String nohtml = splitInput[splitCounter].replaceAll("<[^>]*>.*", " ");
            //System.out.println("nohtml :" + nohtml);
            
            AtomicLong l = map.get(splitInput[splitCounter]);
            if(l == null){
                l = new AtomicLong(1);
                l = map.putIfAbsent(splitInput[splitCounter], l);
                if(l != null){
                    l.incrementAndGet();
                }
            }else{
                l.incrementAndGet();
                }
            
                splitCounter++;
                
                
                // TESTS
                //System.out.println(splitCounter);
               
            }
            splitCounter = 0;
        }

        
        
        in.close();



  inputList.add(null);   // Add null to end so I can check for end of list
        
        while (inputList.get(printCounter) != null){
        //System.out.println(inputList.get(printCounter));
        printCounter++;
        }
       
*/

/*
TRIED TO MAKE OWN CLASS TO KEEP TRACK OF PAGE NUMBERS

    
    public static ConcurrentHashMap<Integer, words> urlToMap(String url, int pageNumber) throws IOException{
        ConcurrentHashMap<Integer, words> map = new ConcurrentHashMap<>();
        int splitCounter = 0;       
        Document doc = Jsoup.connect(url).get();
        String title = doc.title();
        String body = doc.body().text();
        String[] splitInput = body.split("\\s+");
        
        words wordTest = new words();
       // wordTest.word = "f";
       // wordTest.number = new AtomicLong(splitCounter);
        
        while(splitCounter < splitInput.length){
            if(map.get(splitInput[splitCounter]) == null){
                
                wordTest.number = new AtomicLong(1);
                wordTest.word = splitInput[splitCounter];
                
                map.putIfAbsent(pageNumber, wordTest);
                
                wordTest.number.incrementAndGet();
            }
            splitCounter++;
        }
        
        // Output
        System.out.println("\n\n"+title);
        //output(map);
        
        words a = map.get(1);
        if(a != null){
            Double aTF = a.doubleValue() / map.size();
            System.out.print("\n" + "a : " + a.longValue());
            System.out.print("  |   TF : " + aTF);
        }
        
        
        
        //
        return map;
    }


*/