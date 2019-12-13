/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;


import a1.a3.Edge;
import a1.a3.Graph;
import a1.a3.Node;
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
 * A3 COMMENT CORPUS IN URLTOMAP unCOmMENT FOR A2
 * 
 * 
 * @author CJ
 */

public class A1 {
    
    
    //CustomHashMap<String, Integer> map = new CustomHashMap<>();  // Initialize with (Int) to start w/ default size
    
    public static ArrayList<String> urlList = new ArrayList<>();
            
    public static Corpus corpus = new Corpus();
    public static List<CustomHashMap<String,Integer>> categoryList = new ArrayList<>();
    public static List<CustomBTree> categoryTreeList = new ArrayList<>();
    public static ArrayList<String> smallWordList;
    
    
    public static List<CustomHashMap<String,Integer>> listOfMaps = new ArrayList<>();
    public static List<CustomBTree> listOfTrees = new ArrayList<>();
    
    
    public static List<CustomHashMap<String,Integer>> wikiMaps = new ArrayList<>();
    public static Integer[][] wikiEdges = new Integer[1000][1000];
    public static String[] edgeList = new String[1000];
    public static ArrayList<String> wikiList;
    
    public static Edge[] edges;
    public static int edgeCount = 0;
    
    static void findPath(String srcUrl, String dstUrl) throws IOException {
       // Edge[] edges = null;
        Graph graph;
        String urlFile = "wiki.txt";
        String wordsFile = "words.txt";
        
        int sensitivity = 1; // 1-3
        
        ArrayList<String> wordList = readWordsToList(wordsFile);
        wikiList = readFile(urlFile);
        
        List<Integer> similarity = new ArrayList<>();
        
        CustomHashMap<String, Integer> srcMap;                    //  SRC at [0]
        CustomHashMap<String, Integer> dstMap;                    // DEST at [1]
         
        CustomHashMap<String, Integer> tempMap;
         
        srcMap = urlToMap(srcUrl,urlFile);
        
        Node srcNode = new Node(srcUrl);
        
        for(int i=0;i<edgeList.length && edgeList[i]!= null;i++){
            Node dstNode = new Node(edgeList[i]);
            edges[edgeCount] = new Edge(srcNode,dstNode);    
        }
        
        dstMap = urlToMap(dstUrl,urlFile);
        srcNode = new Node(srcUrl);
        
        for(int i=0;i<edgeList.length && edgeList[i]!= null;i++){
            Node dstNode = new Node(edgeList[i]);
            edges[edgeCount] = new Edge(srcNode,dstNode);  
        }
       
        // IF EQUAL // CONNECT BREAK
       
        
    // HAVE TO LOOP AND CHECK PAGE VS PAGE NOT JUST SRC VS PAGE HAVE TO CHECK EVERY COMBINATION FOR WEIGHTS
        try {

            for (int pageNumber=0;pageNumber < wikiList.size(); pageNumber++){
                
                srcMap = urlToMap(wikiList.get(pageNumber),urlFile);
                srcNode = new Node(wikiList.get(pageNumber));
                wikiMaps.add(srcMap);
                
                for(int k=1;k < wikiList.size(); k++){
                    
                    dstMap = urlToMap(wikiList.get(k),urlFile);
                    wikiMaps.add(dstMap);

                    for(int i=0;i<edgeList.length && edgeList[i]!= null;i++){
                        
                        Node dstNode = new Node(edgeList[i]);
                        edges[edgeCount] = new Edge(srcNode,dstNode,compareMaps(srcMap,dstMap,wordList,sensitivity));
                    }

                    //edges[edgeCount] = new Edge(,link.text());    

                }
            }   
        } catch(Exception e){System.out.println(e);}
        
        graph = new Graph(edges);
        graph.dijkstra();
    
    }
    
    Map<String,Integer> sportList;
    Map<String,Integer> weatherList;
    Map<String,Integer> newsList;
    Map<String,Integer> bookList;
    Map<String,Integer> businessList;
    Map<String,Integer> educationList;
    Map<String,Integer> gamingList;
    
    public static String[] urlFiles = new String[10];
    
    public static HashMap<String,Double> mostSimilarKey = new HashMap<>();
    public static String simKey;
    
    
    
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    
   
    
    
    
    
    
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
        
                                
            while (counter < urlList.size() && counter < 500){
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
               if (link.toString().contains("www.wikipedia.com")){
               linksList.add(link.attr("abs:href"));
                }
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
        String alreadyAdded[] = new String[5000];
        int addedCounter = 0;
        int b=0;
        boolean needsAdd;
        
        String tempWord;
        
        try{
            GUI.setOutput("Map from" + url);
            
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
                if (wikiList.contains(link)){
                edgeList[b] = link.text();
                }
               //System.out.println("link.attr(\"abs:href\") : " + link.attr("abs:href"));
                if (link.text().contains("www.wikipedia.com")){
                   linksList.add(link.attr("abs:href"));
                }
                b++;
            }
            for (int x=links.size();x < edgeList.length;x++){
                edgeList[x] = null;                             // empty the rest of array
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
        int b=0;
        boolean needsAdd;
        
        
        String tempWord;
        
        try{
            GUI.setOutput("Map from" + url);
            File input = new File(url);

           
            Document doc = Jsoup.connect(url).get();
            String title = doc.title();
            String body = doc.body().text();
            String[] splitInput = body.split("\\s+");

            Elements links = doc.select("a[href]");
            List linksList = new ArrayList();

            System.out.println(links.size());
            for (Element link : links) {
                if (wikiList.contains(link)){
                edgeList[b] = link.text();
                b++;
                }
               //System.out.println("link.attr(\"abs:href\") : " + link.attr("abs:href"));
                if (link.text().contains("www.wikipedia.com")){
                   linksList.add(link.attr("abs:href"));
                }
                
            }
            for (int x=links.size();x < edgeList.length;x++){
                edgeList[x] = null;                             // empty the rest of array
            }

            addListToFile(urlFile, linksList);

            /*
              File file = new File(System.getProperty("user.dir") + "//pagefiles//" + title + ".txt");
              FileOutputStream out = new FileOutputStream(
                    System.getProperty("user.dir") + "//pagefiles//" + title + ".txt");
            out.write(title.getBytes());
            out.close();
            FileWriter fr = new FileWriter(file);
            BufferedWriter  br = new BufferedWriter(fr);
            // **/
            
            System.out.println("oldurlToMap Word Loop");
            while(splitCounter < splitInput.length){
                
                tempWord = splitInput[splitCounter];
                tempWord = tempWord.replaceAll("\\p{Punct}",""); 
                int l = map.get(tempWord);
                needsAdd = true;
                
                /*
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
                    }*/
                //}
        
                
                /*
                if (checkForURL(tempWord.toLowerCase(),"words.txt") == false) {
                    addToFile("words.txt", tempWord.toLowerCase());
                }
                */
                // ***************
               // br.write(tempWord);
              //  br.newLine();
                // ******************
            
                
                if (l == 0){
                    map.put(tempWord.toLowerCase(), l);
                } else {
                    map.increment(tempWord);
                }
              
                //System.out.println(tempWord + " : " + l);
                
                splitCounter++;
            }
            
          //  br.close();  // **
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
        //tree.category = (String) map.getCategory();
        
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
        
        CustomBTree sportsTree;
        CustomBTree weatherTree;
        CustomBTree newsTree;
        CustomBTree bookTree;
        CustomBTree businessTree;
        CustomBTree educationTree;
        CustomBTree gamingTree;
        
        sportsMap = urlToMap("https://www.espn.com/","sports.txt", "sports");
        weatherMap = urlToMap("https://www.weather.com/","weather.txt", "weather");
        newsMap = urlToMap("https://news.google.com/","news.txt","news");
        bookMap = urlToMap("http://www.barnesandnoble.com/","books.txt","books");
	businessMap = urlToMap("http://www.businessinsider.com/","business.txt","business");
	educationMap = urlToMap("http://www.oswego.edu/","education.txt","education");
        gamingMap = urlToMap("https://www.pcgamer.com/","gaming.txt","gaming");
        
        sportsTree = mapToTree(sportsMap,sportsMap.getUrl().toString(),1000,1000);
        weatherTree = mapToTree(weatherMap,weatherMap.getUrl().toString(),1001,1001);
        newsTree = mapToTree(newsMap,newsMap.getUrl().toString(),1002,1000);
        bookTree = mapToTree(bookMap,bookMap.getUrl().toString(),1003,1000);
        businessTree = mapToTree(businessMap,businessMap.getUrl().toString(),1004,1000);
        educationTree = mapToTree(educationMap,educationMap.getUrl().toString(),1005,1000);
        gamingTree = mapToTree(gamingMap,gamingMap.getUrl().toString(),1006,1000);
        
        categoryList.add(sportsMap);
        categoryList.add(weatherMap);
        categoryList.add(newsMap);
        categoryList.add(bookMap);
        categoryList.add(businessMap);
        categoryList.add(educationMap);
        categoryList.add(gamingMap);
        
        categoryTreeList.add(sportsTree);
        categoryTreeList.add(weatherTree);
        categoryTreeList.add(newsTree);
        categoryTreeList.add(bookTree);
        categoryTreeList.add(businessTree);
        categoryTreeList.add(educationTree);
        categoryTreeList.add(gamingTree);
        
    }   
    

    
    public static void getCategory(CustomBTree testTree) throws IOException {
        
        ArrayList<String> wordList = readWordsToList("words.txt");
        //ArrayList<String> wordList = corpus.getWords();
        
        List<Integer> similarity = new ArrayList<Integer>();
        List<String> top5;
        
        int sensitivity = 1;            // 1->3 3 = most sensitive
        int closest = 0;

        for(int i = 0; i < categoryList.size();i++){
            System.out.println("InCat");
            similarity.add(compareTrees(testTree,categoryTreeList.get(i),sensitivity)); 
            //similarity.add(compareMaps(testMap,categoryList.get(i),wordList,sensitivity)); 
            if (Collections.max(similarity) > similarity.get(closest)){
                closest = i;
            }
        }
        String category = categoryList.get(closest).getCategory();
        //GUI.setText("f");
        
        testTree.category = category;
        top5 = setTop5(category);
        
        GUI.setCategory(category);
      /*  for (int i=0;i < 5; i++){
            try{ 
                //String tempString = categoryList.get(similarity.get(top5.get(i))).getCategory();
                GUI.setArea(top5.get(i));
            } catch(Exception e){
                e.printStackTrace();
            }
        }*/
        
        //
    }
    
    public static List<String> setTop5(String category) throws IOException{     // Do this so i keep the sort seperate
        String fileName = category + ".txt";
        ArrayList<String> list = readWordsToList(fileName);
        Collections.sort(list);
        List<String> top5 = new ArrayList<>(list.subList(list.size() -5, list.size()));
        
        for (int i=0;i < 5; i++){
            GUI.setArea(top5.get(i));
        }
            return top5;
            
    }
    
    public static int compareTrees(CustomBTree tree1, CustomBTree tree2, int sensitivity) throws IOException{
        
        //ArrayList<String> words = corpus.getWords()
        ArrayList<String> words = smallWordList;
        int counter = 0;
        int similarity = 0;
        Random random = new Random();
        int ra = random.nextInt();
        Double map1TF = 0.0;
        Double map2TF = 0.0;
        
        Integer wordCountMap1;
        Integer wordCountMap2;
        
        
        
        //CustomHashMap<String, Double> compareMap = new CustomHashMap();
        
        while (counter < words.size()){
            
            //wordCountMap1 = tree1.get(words.get(counter));
            Word findWord1 = tree1.getRoot().search(words.get(counter));
         //   System.out.println(findWord1.getWord());
            if (findWord1 != null){
                map1TF = findWord1.getTf();
           // if(wordCountMap1 != null){
           // map1TF = wordCountMap1.doubleValue() / tree1.getSizeOfMap();
             //   System.out.print("\n" + words.get(counter) + " in tree1 : " + wordCountMap1.longValue());
                System.out.print(findWord1.getWord() + "  |   TF : " + map1TF);
            }
           // }
            
          //  wordCountMap2 = map2.get(words.get(counter));
         
            Word findWord2 = tree2.getRoot().search(words.get(counter));
          //  System.out.println(findWord2.getWord());
            if (findWord2 != null){
                map2TF = findWord2.getTf();
           // if(wordCountMap2 != null){
         //   map2TF = wordCountMap2.doubleValue() / tree2.getSizeOfMap();
            //    System.out.print("\n" + words.get(counter) + " in tree2 : " + wordCountMap2.longValue());
                System.out.print("  |   TF : " + map2TF);
           // }
              }
            //if (map1TF != 0 && map2TF != 0){
           
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
           //}
           if (findWord2 != null && findWord1 != null){
            if (map1TF > map2TF){
                 if (mostSimilarKey.isEmpty()){
                mostSimilarKey.put(findWord1.getWord(),map1TF-map2TF);
                simKey = findWord1.getWord();
            } else
                if ((map1TF - map2TF) < mostSimilarKey.get(simKey)){
                   // mostSimilarKey = new HashMap<>();
                    mostSimilarKey.keySet().clear();
                    mostSimilarKey.put(findWord1.getWord(),map1TF-map2TF);
                    simKey = findWord1.getWord();
                }
            } else if (map2TF > map1TF){
                 if (mostSimilarKey.isEmpty()){
                mostSimilarKey.put(findWord2.getWord(),map2TF-map1TF);
                simKey = findWord2.getWord();
            } else
                if ((map2TF - map1TF) < mostSimilarKey.get(simKey)){
                   // mostSimilarKey = new HashMap<>();
                    mostSimilarKey.keySet().clear();
                    mostSimilarKey.put(findWord1.getWord(),map2TF-map1TF);
                    simKey = findWord1.getWord();
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
        
        return similarity / ra ;
    }
   
        
    public static void dijkstra(){
        
            int[] preD = new int[5];
            int min = 999, nextNode = 0; // min holds the minimum value, nextNode holds the value for the next node.
          //  scan = new Scanner(System.in); 
            int[] distance = new int[5]; // the distance matrix
            int[][] matrix = new int[5][5]; // the actual matrix
            int[] visited = new int[5]; // the visited array

            System.out.println("Enter the cost matrix"); 

            for(int i = 0; i < distance.length; i++){

                    visited[i] = 0; //initialize visited array to zeros

                    preD[i] = 0;

                    for(int j = 0; j < distance.length; j++){

                          //  matrix[i][j] = scan.nextInt(); //fill the matrix

                            if(matrix[i][j]==0){

                                    matrix[i][j] = 999; // make the zeros as 999

                            }

                    }

            }

            distance = matrix[0]; //initialize the distance array
            visited[0] = 1; //set the source node as visited
            distance[0] = 0; //set the distance from source to source to zero which is the starting point

            for(int counter = 0; counter < 5; counter++){

                    min = 999;

                    for(int i = 0; i < 5; i++){

                            if(min > distance[i] && visited[i]!=1){

                                    min = distance[i];
                                    nextNode = i;

                            }

                    }

                    visited[nextNode] = 1;

                    for(int i = 0; i < 5; i++){

                            if(visited[i]!=1){

                                    if(min+matrix[nextNode][i] < distance[i]){

                                            distance[i] = min+matrix[nextNode][i];
                                            preD[i] = nextNode;

                                    }

                            }

                    }

            }

            for(int i = 0; i < 5; i++){

                    System.out.print("|" + distance[i]);

            }
            System.out.println("|");

            int j;
            for(int i = 0; i < 5; i++){

                    if(i!=0){

                            System.out.print("Path = " + i);
                            j = i;
                            do{

                                    j=preD[j];
                                    System.out.print(" <- " + j);

                            }while(j!=0);

                    }

                    System.out.println();

            }

    }	
	

    
    
    
        public static void main(String input) throws Exception {
        
        smallWordList = readWordsToList("words.txt");
        
        urlFiles[0] = "url.txt";
        urlFiles[1] = "books.txt";
        urlFiles[2] = "business.txt";
        urlFiles[3] = "education.txt";
        urlFiles[4] = "gaming.txt";
        urlFiles[5] = "news.txt";
        urlFiles[6] = "weather.txt";
        
        int pageNumber = 0;
        int closest = 0;
        int secondClosest = 0;
        List<Integer> similarity = new ArrayList<Integer>();
        String inputUrl = GUI.getURL();
        String urlFile = "url.txt";
        String wordsFile = "words.txt";
        String mostSimilarLink = "IDK YET SORRY";
        
        System.out.println("try and make category");
       // /**/ makeCategory();
        
        urlToFile(inputUrl,urlFile);
        
        System.out.println("Hi");
        
        int sensitivity = 1;            // 1->3 3 = most sensitive
        
        
        ArrayList<String> wordList = readWordsToList(wordsFile);
        
        CustomHashMap<String, Integer> inputMap;
        CustomHashMap<String, Integer> tempMap;
         
        inputMap = urlToMap(inputUrl,urlFile);
        
        urlList = readFile(urlFile);
        
        for (int i = 0; i < urlFiles.length - 1 ;i++){                              // Add different category url pages
            if (urlFiles[i] != null) {
                System.out.println("HERE : urlFiles[i] : " + urlFiles[i]);
                urlList.addAll(readFile(urlFiles[i]));
            }
            
        }
        for (int i = 0; i < urlList.size() - 1;i++){
            System.out.println("url : " + urlList.get(i));
        }
        
        int numberOfPages = listOfMaps.size();
        
        //System.out.println(inputUrl);
        
        CustomBTree inputTree = mapToTree(inputMap,inputUrl,0,numberOfPages);
        
        //getCategory(inputTree);
        
        
        try {
            while (pageNumber < 10){
        //while (pageNumber < urlList.size()){
        
            System.out.println("***************");
            System.out.println(PURPLE+"***************");
            System.out.println(RED + "Page Number: " + pageNumber);
            System.out.println(RED + "Page Number: " + pageNumber);
            System.out.println(PURPLE+"***************");       
           /* for (int i=0; i<1000; i++){ // JUST SO I CAN SEE 
                System.out.println("Size= " + urlList.size()); 
                System.out.println(urlList.get(pageNumber));
            }*/
           
            tempMap = urlToMap(urlList.get(pageNumber),urlFile);
            
            //getCategory(tempMap);
            GUI.setOutput("Create Tree #" + pageNumber);
            CustomBTree tempTree = mapToTree(tempMap,urlList.get(pageNumber),pageNumber+1,numberOfPages);
            
            
          /*  similarity.add(pageNumber, compareTrees(inputTree,tempTree,sensitivity));
            System.out.println("\n\nSimilarity between inputTree & tree @ page # : " + pageNumber + 
                    " = " + similarity.get(pageNumber));
           
            
            if (Collections.max(similarity) > similarity.get(closest)){
                closest = pageNumber;
            }
       */
            listOfTrees.add(tempTree);
            listOfMaps.add(tempMap);
            
        
            pageNumber++;
        }
        } catch(Exception e){System.out.println(e);}
        
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
       
     //   System.out.println("\n\n---------------------------------");
     //   System.out.println("|     1st Page # :" + closest
     //           + " = " + Collections.max(similarity) + "     |");
       // System.out.println("|     2nd Page # : " + secondClosest
       //         + " = " + similarity.get(secondClosest)+ "     |");
        

        
        
       // tfidfOfWord(listOfMaps,"so".toLowerCase());       
        
        
        
        
        //similar / total number of words  * tf5
        
        
  //      GUI.setText(urlList.get(closest));
        
    //    GUI.setSimilarKey(simKey);
    //    getCategory(inputTree);
        // addToFile(urlFile, "test");
        //checkSym(listOfMaps);  // Improved with tfidfOfWord
        //CustomHashMap<String, Integer> map = urlToMap("https://en.wikipedia.org/wiki/Java_ConcurrentMap");
        //CustomHashMap<String, Integer> map2 = urlToMap("https://en.wikipedia.org/wiki/Doug_Lea");
      
    }
    
    
    public static void run(String input) throws Exception {
        
        smallWordList = readWordsToList("words.txt");
        
        urlFiles[0] = "url.txt";
        urlFiles[1] = "books.txt";
        urlFiles[2] = "business.txt";
        urlFiles[3] = "education.txt";
        urlFiles[4] = "gaming.txt";
        urlFiles[5] = "news.txt";
        urlFiles[6] = "weather.txt";
        
        int pageNumber = 0;
        int closest = 0;
        List<Integer> similarity = new ArrayList<>();
        String inputUrl = GUI.getURL();
        String urlFile = "url.txt";
         
        urlList = readFile(urlFile);
        
        for (int i = 0; i < urlFiles.length - 1 ;i++){                              // Add different category url pages
            if (urlFiles[i] != null) {
                System.out.println("HERE : urlFiles[i] : " + urlFiles[i]);
                urlList.addAll(readFile(urlFiles[i]));
            }
            
        }
        for (int i = 0; i < urlList.size() - 1;i++){
            System.out.println("url : " + urlList.get(i));
        }
        
        
       // System.out.println("try and make category");
      //  /**/ makeCategory();
        
        //urlToFile(inputUrl,urlFile);
        
        System.out.println("Hi");
        
        int sensitivity = 1;            // 1->3 3 = most sensitive
        
        
        CustomHashMap<String, Integer> inputMap;
        CustomHashMap<String, Integer> tempMap;
         
        inputMap = urlToMap(inputUrl,urlFile);
        
        int numberOfPages = listOfMaps.size();
        
        //System.out.println(inputUrl);
        
        CustomBTree inputTree = mapToTree(inputMap,inputUrl,0,numberOfPages);
        
        GUI.setOutput("Getting Category of Input");
        //makeCategory();
        //getCategory(inputTree);
        
        
        //repopulate trees
        
        
        
        while (pageNumber < 10){
       // while (pageNumber < urlList.size()){
       CustomBTree tempTree;
            if (listOfTrees.size() > pageNumber){
                tempTree = listOfTrees.get(pageNumber);
            } else {
                tempTree = new CustomBTree(pageNumber+1);
            }
            
           
            listOfTrees.add(tempTree);
            
            
            
            similarity.add(pageNumber, compareTrees(inputTree,tempTree,sensitivity));
            
            GUI.setOutput("Compare w/ Tree #" + pageNumber + " = " + similarity.get(pageNumber));
            
            System.out.println("\n\nSimilarity between inputTree & tree @ page # : " + pageNumber + 
                    " = " + similarity.get(pageNumber));
           
            if (Collections.max(similarity) > similarity.get(closest)){
                closest = pageNumber;
            }
            pageNumber++;
        }
        
        
   
         
        System.out.println("\n\n---------------------------------");
        System.out.println("|     1st Page # :" + closest
                + " = " + Collections.max(similarity) + "     |");
        
        makeCategory();
        getCategory(inputTree);
        
        GUI.setText(urlList.get(closest));
        GUI.setSimilarKey(simKey);
        
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