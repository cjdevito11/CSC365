/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

import static a1.A2.corpus;
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

public class A1 {
    
    
    //CustomHashMap<String, Integer> map = new CustomHashMap<>();  // Initialize with (Int) to start w/ default size
    
    public static Corpus corpus = new Corpus();
    public static List<CustomHashMap<String,Integer>> categoryList = new ArrayList<>();
    public static ArrayList<String> smallWordList;
    
    Map<String,Integer> sportList;
    Map<String,Integer> weatherList;
    Map<String,Integer> newsList;
    Map<String,Integer> bookList;
    Map<String,Integer> businessList;
    Map<String,Integer> educationList;
    Map<String,Integer> gamingList;
    
    
    
      public static void urlToTree(String url,int urlNum,int numberOfPages) throws FileNotFoundException, IOException {

        int splitCounter = 0;
        String tempWord;
       
//        CustomBTree tree = new CustomBTree(url);
        
      //  try{
     
            Document doc = Jsoup.connect(url).get();
            String title = doc.title();
            String body = doc.body().text();
            String[] splitInput = body.split("\\s+");

            CustomBTree tree = new CustomBTree(url,urlNum);
            
            Elements links = doc.select("a[href]");
            List linksList = new ArrayList();

            File file = new File(System.getProperty("user.dir") + "//pagefiles//" + title + ".txt");
            System.out.println(file.getPath());
            
            for (Element link : links) {                    // Links on page
               linksList.add(link.attr("abs:href"));
            }

            //addListToFile(urlFile, linksList);
            
            while(splitCounter < splitInput.length){
                tempWord = splitInput[splitCounter];
                tempWord = tempWord.replaceAll("\\p{Punct}",""); 
                
                
                Word tempWordObject = new Word(tempWord);
                //Word tempWordObject = new Word(url,tempWord,splitInput.length,numberOfPages);
                BEntry tempEntry = new BEntry(tempWord,tempWordObject);
                       
                
                tree.insert(tempEntry);
                
                
              //  System.out.println(tempWordObject.getWord());
              //  System.out.println(tree.getRoot());
                
                splitCounter++;
            }
            
            
        // Output
        System.out.println("\n\n"+title);
        //} catch(IOException e){
        //}
        //
    }
    
    
    
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



    public static void urlToFile(String url,String urlFile) throws FileNotFoundException, IOException {

        int splitCounter = 0;
        String tempWord;
       
      //  try{
     
            Document doc = Jsoup.connect(url).get();
            String title = doc.title();
            String body = doc.body().text();
            String[] splitInput = body.split("\\s+");

            Elements links = doc.select("a[href]");
            List linksList = new ArrayList();

            
            
            File file = new File(System.getProperty("user.dir") + "//pagefiles//" + title + ".txt");
            System.out.println(file.getPath());
            
            
            //System.out.println(links.size());
            for (Element link : links) {
               //System.out.println("link.attr(\"abs:href\") : " + link.attr("abs:href"));
               linksList.add(link.attr("abs:href"));
            }

            //addListToFile(urlFile, linksList);
            
            if (file.createNewFile()){
                System.out.println("CREATED FILE: " + file.getPath());
            }   // CREATE FILE TO ADD WORDS TOO
            else { 
                System.out.println("FAILED TO CREATE FILE...");
            }
            //BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            FileOutputStream out = new FileOutputStream(
                    System.getProperty("user.dir") + "//pagefiles//" + title + ".txt");
            
            while(splitCounter < splitInput.length){
                tempWord = splitInput[splitCounter];
                tempWord = tempWord.replaceAll("\\p{Punct}",""); 
                out.write(tempWord.getBytes());
                
                //System.out.println(tempWord + " : " + l);
                
                splitCounter++;
            }
            
            out.close();

       // System.out.println("links  :  " + links);
        
        
        // Output
        System.out.println("\n\n"+title);
        //} catch(IOException e){
        //}
        //
    }
        
    /**                urlToMap Function
     * @param url
     * @param urlFile
     * @return Map
     * @throws java.io.IOException =
     */
    /* new one? */
        public static CustomHashMap urlToMap(String url,String urlFile, String cat) throws IOException{
        CustomHashMap map = new CustomHashMap<>(url, cat);
        int splitCounter = 0;  
        int loopCounter = 0;
        int linkCounter = 0;
        String alreadyAdded[] = new String[5000];
        int addedCounter = 0;
        
        boolean needsAdd;
        
        
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

            // **
              File file = new File(System.getProperty("user.dir") + "//pagefiles//" + title + ".txt");
              FileOutputStream out = new FileOutputStream(
                    System.getProperty("user.dir") + "//pagefiles//" + title + ".txt");
            out.write(title.getBytes());
            out.close();
            FileWriter fr = new FileWriter(file);
            BufferedWriter  br = new BufferedWriter(fr);
            // **
            
            
            while(splitCounter < splitInput.length){
                tempWord = splitInput[splitCounter];
                tempWord = tempWord.replaceAll("\\p{Punct}",""); 
                int l = map.get(tempWord);
                needsAdd = true;
                
                if (addedCounter == 0) {
                    
                    corpus.addWord(tempWord);
                    alreadyAdded[addedCounter] = tempWord;
                    System.out.println(tempWord + " word count in corpus : " + corpus.getWordCount(tempWord));
                    addedCounter++;
                    needsAdd = false;
                    
                } else {
                    
                    System.out.println(": " + alreadyAdded[0]);
                    for (int i=0; i < addedCounter - 1; i++){               // loop through added words to check if equal
                      //  System.out.println(i);
                      //  System.out.println(alreadyAdded[i]);
                      
                        if (alreadyAdded[i].compareTo(tempWord) == 0){
                            needsAdd = false;
                            System.out.println(tempWord + " word count in corpus : " + corpus.getWordCount(tempWord));
                        
                        }
                    }
                    
                    if (needsAdd) {
                        corpus.addWord(tempWord);
                        alreadyAdded[addedCounter] = tempWord;
                        System.out.println(tempWord + " word count in corpus : " + corpus.getWordCount(tempWord));
                        addedCounter++;
                        needsAdd = false;
                    }
                }
                
                
                /*
                if (checkForURL(tempWord.toLowerCase(),urlFile) == false) {
                    addToFile(urlFile, tempWord.toLowerCase());
                }
                */
                // ***************
                br.write(tempWord);
                br.newLine();
                // ******************
            
                
                if (l == 0){
                    map.put(tempWord.toLowerCase(), l);
                } else {
                    map.increment(tempWord);
                }
              
                //System.out.println(tempWord + " : " + l);
                
                splitCounter++;
            }
            
            br.close();  // **
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
        // old
    public static CustomHashMap urlToMap(String url,String urlFile) throws IOException{
        CustomHashMap map = new CustomHashMap<>(url);
        int splitCounter = 0;  
        int loopCounter = 0;
        int linkCounter = 0;
        String alreadyAdded[] = new String[5000];
        int addedCounter = 0;
        
        boolean needsAdd;
        
        
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

            System.out.println(links.size());
            for (Element link : links) {
               //System.out.println("link.attr(\"abs:href\") : " + link.attr("abs:href"));
               linksList.add(link.attr("abs:href"));
            }

            addListToFile(urlFile, linksList);

            // **
              File file = new File(System.getProperty("user.dir") + "//pagefiles//" + title + ".txt");
              FileOutputStream out = new FileOutputStream(
                    System.getProperty("user.dir") + "//pagefiles//" + title + ".txt");
            out.write(title.getBytes());
            out.close();
            FileWriter fr = new FileWriter(file);
            BufferedWriter  br = new BufferedWriter(fr);
            // **
            
            
            while(splitCounter < splitInput.length){
                tempWord = splitInput[splitCounter];
                tempWord = tempWord.replaceAll("\\p{Punct}",""); 
                int l = map.get(tempWord);
                needsAdd = true;
                
                if (addedCounter == 0) {
                    
                    corpus.addWord(tempWord);
                    alreadyAdded[addedCounter] = tempWord;
                    System.out.println(tempWord + " word count in corpus : " + corpus.getWordCount(tempWord));
                    addedCounter++;
                    needsAdd = false;
                    
                } else {
                    
                    System.out.println(": " + alreadyAdded[0]);
                    for (int i=0; i < addedCounter - 1; i++){               // loop through added words to check if equal
                      //  System.out.println(i);
                      //  System.out.println(alreadyAdded[i]);
                      
                        if (alreadyAdded[i].compareTo(tempWord) == 0){
                            needsAdd = false;
                            System.out.println(tempWord + " word count in corpus : " + corpus.getWordCount(tempWord));
                        
                        }
                    }
                    
                    if (needsAdd) {
                        corpus.addWord(tempWord);
                        alreadyAdded[addedCounter] = tempWord;
                        System.out.println(tempWord + " word count in corpus : " + corpus.getWordCount(tempWord));
                        addedCounter++;
                        needsAdd = false;
                    }
                }
                
                
                /*
                if (checkForURL(tempWord.toLowerCase(),"words.txt") == false) {
                    addToFile("words.txt", tempWord.toLowerCase());
                }
                */
                // ***************
                br.write(tempWord);
                br.newLine();
                // ******************
            
                
                if (l == 0){
                    map.put(tempWord.toLowerCase(), l);
                } else {
                    map.increment(tempWord);
                }
              
                //System.out.println(tempWord + " : " + l);
                
                splitCounter++;
            }
            
            br.close();  // **
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
    public static Double tfOfWord(CustomHashMap map,String wordIn){
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
                System.out.print("\n" + words.get(counter) + " in map1 : " + wordCountMap1.longValue());
                System.out.print("  |   TF : " + map1TF);
                
           // }
            
            wordCountMap2 = map2.get(words.get(counter));
           // if(wordCountMap2 != null){
            map2TF = wordCountMap2.doubleValue() / map2.getSizeOfMap();
                System.out.print("\n" + words.get(counter) + " in map2 : " + wordCountMap2.longValue());
                System.out.print("  |   TF : " + map2TF);
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
    
    
      
    
    public static CustomBTree mapToTree(CustomHashMap map, String url,int urlNum,int numberOfPages) throws FileNotFoundException, IOException {

        int splitCounter = 0;
        String tempWord;
        boolean needsAdd;
        CustomBTree tree = new CustomBTree(url,urlNum);
        tree.category = (String) map.getCategory();
        
        for (int i = 0; i < map.words.size(); i++) {
           // tempWord =  (String) map.words[i];
            tempWord = map.words.get(i).toString();
            
            map.get(tempWord);
            
            Double tf = tfOfWord(map,tempWord);
            
            System.out.println("\n\n\ntempWord " + tempWord + "TF = " + tf + "  @ i : " + i);
            
            Word tempWordObject = new Word(tempWord, tf);
            BEntry tempEntry = new BEntry(tempWord,tempWordObject);
            tree.insert(tempEntry);
           
        }
        return tree;
    }
    public static void makeCategory() throws IOException {          //Initialize Categorys
        
        System.out.println("Making Categories");
        
        CustomHashMap sportsMap = new CustomHashMap();
        CustomHashMap weatherMap = new CustomHashMap();
        CustomHashMap newsMap = new CustomHashMap();
        CustomHashMap bookMap = new CustomHashMap();
        CustomHashMap businessMap= new CustomHashMap();
        CustomHashMap educationMap= new CustomHashMap();
        CustomHashMap gamingMap = new CustomHashMap();
        
        sportsMap = urlToMap("https://www.espn.com/","sports.txt", "sports");
        weatherMap = urlToMap("https://www.weather.com/","weather.txt", "weather");
        newsMap = urlToMap("https://news.google.com/","news.txt","news");
        bookMap = urlToMap("http://www.barnesandnoble.com/","books.txt","books");
	businessMap = urlToMap("http://www.businessinsider.com/","business.txt","business");
	educationMap = urlToMap("http://www.oswego.edu/","education.txt","education");
        gamingMap = urlToMap("https://www.pcgamer.com/","gaming.txt","gaming");
        
        categoryList.add(sportsMap);
        categoryList.add(weatherMap);
        categoryList.add(newsMap);
        categoryList.add(bookMap);
        categoryList.add(businessMap);
        categoryList.add(educationMap);
        categoryList.add(gamingMap);
    }   
    

    
    public static void getCategory(CustomHashMap testMap) throws IOException {
        
        ArrayList<String> wordList = readWordsToList("words.txt");
        //ArrayList<String> wordList = corpus.getWords();
        
        List<Integer> similarity = new ArrayList<Integer>();
        List<Integer> top5;
        
        int sensitivity = 1;            // 1->3 3 = most sensitive
        int closest = 0;

        for(int i = 0; i < categoryList.size();i++){
            System.out.println("InCat");
            similarity.add(compareMaps(testMap,categoryList.get(i),wordList,sensitivity)); 
            if (Collections.max(similarity) > similarity.get(closest)){
                closest = i;
            }
        }
        String category = categoryList.get(closest).getCategory();
        //GUI.setText("f");
        
        testMap.category = category;
        top5 = setTop5(similarity);
        
        GUI.setCategory(category);
        for (int i=0;i < 5; i++){
            try{ 
                String tempString = categoryList.get(similarity.get(top5.get(i))).getCategory();
                GUI.setArea(tempString);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        
        //
    }
    
    public static List<Integer> setTop5(List<Integer> list){     // Do this so i keep the sort seperate
        
        Collections.sort(list);
        List<Integer> top5 = new ArrayList<Integer>(list.subList(list.size() -5, list.size()));
        for (int i=0;i < 5; i++){
            GUI.setArea(top5.get(i).toString());
        }
            return top5;
            
    }
    
    public static int compareTrees(CustomBTree tree1, CustomBTree tree2, int sensitivity){
        
        //ArrayList<String> words = corpus.getWords()
        ArrayList<String> words = smallWordList;
        int counter = 0;
        int similarity = 0;
        Double map1TF = 0.0;
        Double map2TF = 0.0;
        
        Integer wordCountMap1;
        Integer wordCountMap2;
        
        
        
        //CustomHashMap<String, Double> compareMap = new CustomHashMap();
        
        while (counter < words.size()){
            
            //wordCountMap1 = tree1.get(words.get(counter));
            Word findWord1 = tree1.getRoot().search(words.get(counter));
            if (findWord1 != null){
            map1TF = findWord1.getTf();
           // if(wordCountMap1 != null){
           // map1TF = wordCountMap1.doubleValue() / tree1.getSizeOfMap();
             //   System.out.print("\n" + words.get(counter) + " in tree1 : " + wordCountMap1.longValue());
                System.out.print("  |   TF : " + map1TF);
            }
           // }
            
          //  wordCountMap2 = map2.get(words.get(counter));
         
            Word findWord2 = tree2.getRoot().search(words.get(counter));
            if (findWord2 != null){
                map2TF = findWord2.getTf();
           // if(wordCountMap2 != null){
         //   map2TF = wordCountMap2.doubleValue() / tree2.getSizeOfMap();
            //    System.out.print("\n" + words.get(counter) + " in tree2 : " + wordCountMap2.longValue());
                System.out.print("  |   TF : " + map2TF);
           // }
              }
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
        
        smallWordList = readWordsToList("words.txt");
        
        int pageNumber = 0;
        int closest = 0;
        int secondClosest = 0;
        List<Integer> similarity = new ArrayList<Integer>();
        String inputUrl = GUI.getURL();
        String urlFile = "url.txt";
        String wordsFile = "words.txt";
        String mostSimilarLink = "IDK YET SORRY";
        
        System.out.println("try and make category");
        /**/ makeCategory();
        
        urlToFile(inputUrl,urlFile);
        
        System.out.println("Hi");
        
        int sensitivity = 1;            // 1->3 3 = most sensitive
        
        
        ArrayList<String> wordList = readWordsToList(wordsFile);
        
        CustomHashMap<String, Integer> inputMap;
        CustomHashMap<String, Integer> tempMap;
         
        inputMap = urlToMap(inputUrl,urlFile);
        
        List<String> urlList = readFile(urlFile);
        List<CustomHashMap<String,Integer>> listOfMaps = new ArrayList<>();
        
        int numberOfPages = listOfMaps.size();
        
        //System.out.println(inputUrl);
        
        CustomBTree inputTree = mapToTree(inputMap,inputUrl,0,numberOfPages);
        
        getCategory(inputMap);
        
        while (pageNumber < 5){
        //while (pageNumber < urlList.size()){
            System.out.println(urlList.get(pageNumber));
            
           
            tempMap = urlToMap(urlList.get(pageNumber),urlFile);
            
            //getCategory(tempMap);
            
            CustomBTree tempTree = mapToTree(tempMap,urlList.get(pageNumber),pageNumber+1,numberOfPages);
            
            //urlToTree(urlList.get(pageNumber),pageNumber+1,numberOfPages);
            
            similarity.add(pageNumber, compareTrees(inputTree,tempTree,sensitivity));
            //similarity.add(pageNumber, compareTrees(inputTree,tempTree,wordList,sensitivity));
            
            //similarity.add(pageNumber, compareMaps(inputMap,tempMap,wordList,sensitivity));
            System.out.println("\n\nSimilarity between inputTree & tree @ page # : " + pageNumber + 
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
        
        
        GUI.setText(urlList.get(closest));
        getCategory(inputMap);
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