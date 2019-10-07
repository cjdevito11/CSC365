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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static jdk.nashorn.internal.objects.Global.print;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author CJ
 */
class words {
    String word;
    Integer number;
}

public class A1 {
    //CustomHashMap<String, Integer> map = new CustomHashMap<>();  // Initialize with (Int) to start w/ default size
    
    public static void checkTF(Map<String, Integer> map){
            Integer a = map.get("a");
        if(a != null){
            Double aTF = a.doubleValue() / map.size();
            System.out.println("\n" + "a : " + a.longValue());
            System.out.println("aTF(a term frequency) : " + aTF);
        }
    }
    
    public static boolean checkForURL(String url, String file) throws FileNotFoundException{
        int counter = 0;
        File realFile = new File(file);
        Scanner scanner = new Scanner(realFile);
        //now read the file line by line...
        int lineNum = 0;
        System.out.println("URL LOOKING FOR : " + url);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lineNum++;
            System.out.println("line testing : " + line + "   |    line.compareTo(url) : " + line.compareTo(url));
            if(line.compareTo(url) == 0) { 
                counter++;
            }
        }
        scanner.close();
        if (counter == 0){ 
            System.out.println("url DOESNT exist");
            return false;
        } else {
            System.out.println("url DOES exist");
            return true;
        }
    }
    
    // ADD STRING
    public static void addToFile(String file, String url) throws IOException {
    BufferedWriter writer = new BufferedWriter(
                                new FileWriter(file, true)  //Set true for append mode
                            ); 
    writer.newLine();   //Add new line
    writer.write(url);
    writer.close();
}
    
    // ADD LIST
    public static void addListToFile(String file, List urlList) throws IOException {
        int counter = 0;
        int addCounter = 0; 
        String tempURL;
        List <String> addList = new ArrayList();
        
        // Get lines in file
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        
        if (lines < 30){
        
                                
            while (counter < urlList.size()){
                tempURL = urlList.get(counter).toString();
                if (checkForURL(tempURL,file) == false){
                    System.out.println("Add to list");
                    addList.add(tempURL);
                    addCounter++;
                } else { System.out.println("\n\n\nAlready In"); }
           
            counter++;
        }
        
        if (addCounter > 0){
            addCounter--;    // TO MOVE DOWN ONE FOR SIZE OF ARRAY 0-X not 1-X
            BufferedWriter writer = new BufferedWriter( //Set true for append mode
                                        new FileWriter(file, true));  
            while (addCounter > 0){

                writer.newLine();   //Add new line
                writer.write(addList.get(addCounter));

                addCounter--;
            }
            writer.close();
        }
        }
        
    }
    
    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }


    
    /**                urlToMap Function
     * @param url
     * @param urlFile
     * @return Map
     * @throws java.io.IOException =
     */
    
    public static CustomHashMap urlToMap(String url,String urlFile) throws IOException{
        CustomHashMap map = new CustomHashMap<>();
        int splitCounter = 0;  
        int loopCounter = 0;
        int linkCounter = 0;
        
        String tempWord;
        
        String hrefRegex = 
                "<a[^>]*>(.*?)</a>";
        Pattern pattern = Pattern.compile(hrefRegex);
        
        try{
            
            File input = new File(url);

            //Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");

            Document doc = Jsoup.connect(url).get();
            String title = doc.title();
            String body = doc.body().text();
            String[] splitInput = body.split("\\s+");

            Elements links = doc.select("a[href]");
            List linksList = new ArrayList();

            //System.out.println(links.size());
            for (Element link : links) {
               //System.out.println("link.attr(\"abs:href\") : " + link.attr("abs:href"));
               linksList.add(link.attr("abs:href"));
            }

            addListToFile(urlFile, linksList);

            while(splitCounter < splitInput.length){
                tempWord = splitInput[splitCounter];
                tempWord = tempWord.replaceAll("\\p{Punct}",""); 
                int l = map.get(tempWord);
                
                /*
                if (checkForURL(tempWord.toLowerCase(),"words.txt") == false) {
                    addToFile("words.txt", tempWord.toLowerCase());
                }
                */
                
                if (l == 0){
                    map.put(tempWord.toLowerCase(), l);
                } else {
                    map.increment(tempWord);
                }
              
                //System.out.println(tempWord + " : " + l);
                
                splitCounter++;
            }
      /* 
        while (loopCounter < links.size()){
           
            Matcher m = pattern.matcher(links.get(loopCounter).toString());
            System.out.println(links.get(loopCounter));
            //System.out.println(m.find());
            while(m.find()){
               // System.out.println(m);
               // System.out.println("m.group(0) " + m.group(0));
                //System.out.println("links : " + m.group(1));
            }
            loopCounter++;
        }
        */
       // System.out.println("links  :  " + links);
        
        
        // Output
        System.out.println("\n\n"+title);
        } catch(IOException e){
        }
        //
        return map;
    }
 
    public static void output(Map<String, Integer> map){
        Integer a = map.get("a");
        if(a != null){
            Double aTF = a.doubleValue() / map.size();
            System.out.print("\n" + "a : " + a.longValue());
            System.out.print("  |   TF : " + aTF);
        }
        
        Integer to = map.get("to");
        if(to != null){
            Double toTF = to.doubleValue() / map.size();
            System.out.print("\nto : " + to.longValue());
            System.out.print("  |   TF : " + toTF);
        }
        Integer the = map.get("the");
        if(the != null){
            Double theTF = the.doubleValue() / map.size();
            System.out.print("\nthe : " + the.longValue());
            System.out.print("  |   TF : " + theTF);
        }
        Integer so = map.get("so");
        if(so != null){
            Double soTF = the.doubleValue() / map.size();
            System.out.print("\nso : " + so.longValue());
            System.out.print("  |   TF : " + soTF);
        } else {
            System.out.println("\nno so's");
        }
        System.out.println("\n# words in doc): " + map.size());
    }
    
       
    public static ArrayList<String> readWordsToList(String filename) throws IOException{
        
        ArrayList<String> words = new ArrayList<>();
 
        try (FileReader file = new FileReader(filename)) {
            StringBuffer sb = new StringBuffer();
            while (file.ready()) {
                char c = (char) file.read();
                if (c == '\n') {
                    words.add(sb.toString());      // Add string to list @ \n
                    sb = new StringBuffer();
                } else {
                    sb.append(c);           //Add char to String
                }
            }
            if (sb.length() > 0) {
                words.add(sb.toString());
            }
        }
        return words;
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
        Double IDF = 0.0;
        System.out.println(listOfMaps.size());
        while (pageNumber < listOfMaps.size()){
            Map<String,Integer> tempMap;
            tempMap = (Map<String, Integer>) listOfMaps.get(pageNumber);
        
            Integer so = tempMap.get("so");
            
            if(so != null){
                soTF = so.doubleValue() / tempMap.size();
                System.out.print("\nso : " + so.longValue());
                System.out.print("  |  TF : " + soTF);
                contains++;
               }
            pageNumber++;
        }
        System.out.println("\n\n" + contains + " page(s) contain so");
        /*
        if (contains == 0) {
            IDF = Math.log(listOfMaps.size());
        }
        
        IDF = Math.log(listOfMaps.size() / contains);
        Double tfidf = soTF * IDF;
        
        System.out.println("  | IDF : " + IDF);
        System.out.println("  | tfidf : " + tfidf);
        */
        
    }
    
        public static void tfidfOfWord(List listOfMaps,String wordIn){
        int pageNumber = 0;
        int contains = 0;
        Double wordTF = 0.0;
        Double IDF = 0.0;
        Double tfidf = 0.0;
        
        System.out.println(listOfMaps.size());
        while (pageNumber < listOfMaps.size()){
            Map<String,Integer> tempMap;
            tempMap = (Map<String, Integer>) listOfMaps.get(pageNumber);
        
            Integer word = tempMap.get(wordIn);
            
            if(word != null){
                wordTF = word.doubleValue() / tempMap.size();
                System.out.print("\n" + wordIn + " : " + word.longValue());
               
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
    public static Double tfOfWord(CustomHashMap<String, Integer> map,String wordIn){
        Double wordTF = 0.0;
        Integer wordCount = map.get(wordIn);

        if(wordCount != null){
            wordTF = wordCount.doubleValue() / map.size();
            System.out.print("\nthe : " + wordCount.longValue());
            System.out.print("  |  TF : " + wordTF);
        }
        return wordTF;
    }
      
    
    public static int compareMaps(CustomHashMap<String,Integer> map1,
                                CustomHashMap<String,Integer> map2,
                                ArrayList<String> words, int sensitivity){
        
        int counter = 0;
        int similarity = 0;
        Double map1TF = 0.0;
        Double map2TF = 0.0;
        
        Integer wordCountMap1;
        Integer wordCountMap2;
        
        
        
        //CustomHashMap<String, Double> compareMap = new CustomHashMap();
        
        while (counter < words.size()){
            
            wordCountMap1 = map1.get(words.get(counter));
           // if(wordCountMap1 != null){
            map1TF = wordCountMap1.doubleValue() / map1.getSizeOfMap();
           //     System.out.print("\n" + words.get(counter) + " in map1 : " + wordCountMap1.longValue());
            //    System.out.print("  |   TF : " + map1TF);
                
           // }
            
            wordCountMap2 = map2.get(words.get(counter));
           // if(wordCountMap2 != null){
            map2TF = wordCountMap2.doubleValue() / map2.getSizeOfMap();
            //    System.out.print("\n" + words.get(counter) + " in map2 : " + wordCountMap2.longValue());
            //    System.out.print("  |   TF : " + map2TF);
           // }
            if (map1TF != 0 && map2TF != 0){
           
                switch (sensitivity) {
                    case(1):
                       if ((map1TF > (map2TF - .005)) && (map1TF < (map2TF + .005))){
                            similarity++;           // INCREMENT IF WITHIN RANGE
                       }
                    case(2):
                        if ((map1TF > (map2TF - .001)) && (map1TF < (map2TF + .001))){
                            similarity++;           // INCREMENT IF WITHIN RANGE
                        }
                    case(3):
                        if ((map1TF > (map2TF - .0005)) && (map1TF < (map2TF + .0005))){
                            similarity++;           // INCREMENT IF WITHIN RANGE
                        }     

               }
           }
              
                   /*
            if (sensitivity == 1){
                if ((map1TF > (map2TF - .005)) && (map1TF < (map2TF + .005))){
                    similarity++;           // INCREMENT IF WITHIN RANGE
                }
            }
            if (sensitivity == 2){
                if ((map1TF > (map2TF - .001)) && (map1TF < (map2TF + .001))){
                    similarity++;           // INCREMENT IF WITHIN RANGE
                }
            }
            if (sensitivity == 3){
                if ((map1TF > (map2TF - .0005)) && (map1TF < (map2TF + .0005))){
                    similarity++;           // INCREMENT IF WITHIN RANGE
                }
            }
           */
            counter++;
            //compareMap.putIfAbsent(words.get(counter).toLowerCase(), map1TF);
        }
        return similarity;
    }
      
    
    public static void main(String input) throws Exception {
        
        int pageNumber = 0;
        int closest = 0;
        int secondClosest = 0;
        List<Integer> similarity = new ArrayList<Integer>();
        String inputUrl = GUI.getURL();
        String urlFile = "url.txt";
        String wordsFile = "words.txt";
        String mostSimilarLink = "IDK YET SORRY";
        
        
        int sensitivity = 1;            // 1->3 3 = most sensitive
        
        
        ArrayList<String> wordList = readWordsToList(wordsFile);
        
        CustomHashMap<String, Integer> inputMap;
        CustomHashMap<String, Integer> tempMap;
         
        inputMap = urlToMap(inputUrl,urlFile);
        
        //System.out.println(inputUrl);
       
       
        List<String> urlList = readFile(urlFile);
        List<CustomHashMap<String,Integer>> listOfMaps = new ArrayList<>();
        
        while (pageNumber < urlList.size()){
            System.out.println(urlList.get(pageNumber));
            
           
            tempMap = urlToMap(urlList.get(pageNumber),urlFile);
            similarity.add(pageNumber, compareMaps(inputMap,tempMap,wordList,sensitivity));
            System.out.println("\n\nSimilarity between inputMap & map @ page # : " + pageNumber + 
                    " = " + similarity.get(pageNumber));
           
            
            if (Collections.max(similarity) > similarity.get(closest)){
                closest = pageNumber;
            }
           // if(similarity.get(pageNumber) > similarity.get(secondClosest)
           //         && similarity.get(pageNumber) != closest) {
           //     secondClosest = pageNumber;
           // }
            
            listOfMaps.add(tempMap);
            
       //    CustomHashMap<Integer, words> map = urlToMap(urlList.get(pageNumber),pageNumber);
            
            pageNumber++;
        }
        
        
       /*         
        for (int i = 1; i < similarity.size(); i++) {
            if(similarity.get(i) > closest) {
                secondClosest = closest;
                closest = similarity.get(i);
            }
            if(similarity.get(i) > secondClosest && similarity.get(i) != closest) {
                secondClosest = similarity.get(i);
                }
        }
          
        */
       
        System.out.println("\n\n---------------------------------");
        System.out.println("|     1st Page # :" + closest
                + " = " + Collections.max(similarity) + "     |");
       // System.out.println("|     2nd Page # : " + secondClosest
       //         + " = " + similarity.get(secondClosest)+ "     |");
        

        
        
       // tfidfOfWord(listOfMaps,"so".toLowerCase());       
        
        
        
        
        //similar / total number of words  * tf5
        
        
        GUI.setTextField(urlList.get(closest));
        // addToFile(urlFile, "test");
        //checkSym(listOfMaps);  // Improved with tfidfOfWord
        //CustomHashMap<String, Integer> map = urlToMap("https://en.wikipedia.org/wiki/Java_ConcurrentMap");
        //CustomHashMap<String, Integer> map2 = urlToMap("https://en.wikipedia.org/wiki/Doug_Lea");
      
        
        
    }
}







/*


        while ((inputLine = in.readLine()) != null){        // Add input to List
            inputList.add(inputLine);
        }

        

private final ConcurrentMap<String,AtomicInteger> map "
    new CustomHashMap<String,AtomicInteger>();

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
       Integer l = map.get(word);
       if(l == null){
           l = new Integer(1);
           l = map.putIfAbsent(word, l);
           if(l != null){
               l.incrementAndGet();
           }
       }else{
           l.incrementAndGet();
       }
   }
    
    public long getCount(String word){
       Integer l = map.get(word);
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
            
            Integer l = map.get(splitInput[splitCounter]);
            if(l == null){
                l = new Integer(1);
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

    
    public static CustomHashMap<Integer, words> urlToMap(String url, int pageNumber) throws IOException{
        CustomHashMap<Integer, words> map = new CustomHashMap<>();
        int splitCounter = 0;       
        Document doc = Jsoup.connect(url).get();
        String title = doc.title();
        String body = doc.body().text();
        String[] splitInput = body.split("\\s+");
        
        words wordTest = new words();
       // wordTest.word = "f";
       // wordTest.number = new Integer(splitCounter);
        
        while(splitCounter < splitInput.length){
            if(map.get(splitInput[splitCounter]) == null){
                
                wordTest.number = new Integer(1);
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