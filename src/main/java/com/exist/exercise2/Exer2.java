package com.exist.exercise2;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Exer2 {
        
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        String userFilePath = null;
        int matNum, row, col;
        int choice = 0;
        boolean loop = true;
        List<Integer> colList = new ArrayList<>();
        
        System.out.println("How many matrices would you like to generate: ");
        matNum = input.nextInt();
        
        SortedMap<Integer , TreeMap<Integer, String>> mainMap = new TreeMap<>();
        // need to update file
        for(int ctr = 0; ctr < matNum; ctr++){
            System.out.println("Enter table row: ");	
            row = input.nextInt();
            System.out.println("Enter table col: ");	
            col = input.nextInt();
            colList.add(col);
            
            // calls initRandMatrix, which returns a TreeMap<Integer, String>
            mainMap.put(ctr, initRandMatrix(row, col));       
        }
        
        // calls print matrix
        printMat(mainMap, colList);
        
        // asks for filename and calls write to file
        System.out.println("Enter filename to save data: ");
        userFilePath = input.next();
        writeToFile(mainMap, userFilePath, colList);

        // Main Menu
        while(loop){
            try{
                System.out.println("\nMain Menu: ");
                System.out.println("[1] Add Row");
                System.out.println("[2] Search ");
                System.out.println("[3] Edit ");
                System.out.println("[4] Print ");
                System.out.println("[5] Reset ");
                System.out.println("[6] Import ");
                System.out.println("[7] Exit ");
                System.out.println("Enter input: ");
                choice = input.nextInt();
                
                switch(choice){
                    case 1:
                        System.out.println(" -- Add Row -- ");
                        int matIndex;
                        String addFilePath = null;
                        System.out.println("Enter matrix index no: ");
                        matIndex = input.nextInt();
                        if(mainMap.get(matIndex) != null){                            // check if selected matrix exists
                            // replace mainMap innermap with innermap generated from the method
                            mainMap.put(matIndex, addRow(mainMap, matIndex, colList));
                            // updates the file
                            if(oldPath() == true){
                                writeToFile(mainMap, userFilePath, colList);
                            }
                            else{
                                System.out.println("Enter filename to save data: ");
                                addFilePath = input.next();
                                writeToFile(mainMap, addFilePath, colList);
                            }
                        }
                        else{
                            System.out.println("Matrix does not exist!");
                        }
                        break;
                    case 2: 
                        System.out.println(" --Search-- ");
                        String term;
                        System.out.println("Enter search term: ");
                        term = input.next();
                        // calls search from mat method
                        searchFromMat(mainMap, term);
                        break;
                    case 3:
                        System.out.println(" --Edit-- ");
                        int matIndex2;
                        String editFilePath = null;
                        printMat(mainMap, colList);                                     // calls print matrix so user can see
                        System.out.println("Enter matrix index no: ");
                        matIndex2 = input.nextInt();
                        if(mainMap.get(matIndex2) != null){                             // check if selected matrix exists
                            mainMap.put(matIndex2, replaceMatElem(mainMap, matIndex2)); // calls replace method and updates main map
                            // updates the file
                            if(oldPath() == true){
                                writeToFile(mainMap, userFilePath, colList);
                            }
                            else{
                                System.out.println("Enter filename to save data: ");
                                editFilePath = input.next();
                                writeToFile(mainMap, editFilePath, colList);
                            }
                        }
                        else{
                            System.out.println("Matrix does not exist!");
                        }
                        break;
                    case 4:
                        System.out.println(" --Print-- ");
                        // calls print array
                        printMat(mainMap, colList);
                        break;
                    case 5: 
                        System.out.println(" --Reset-- ");
                        int row2, col2, matNum2;
                        String resFilePath = null;
                        System.out.println("How many matrices would you like to generate: ");
                        matNum2 = input.nextInt();
                        colList.clear();
                        for(int ctr = 0; ctr < matNum2; ctr++){
                            System.out.println("Enter table row: ");	
                            row2 = input.nextInt();
                            System.out.println("Enter table col: ");	
                            col2 = input.nextInt();
                            colList.add(col2);
                            
                            mainMap.put(ctr, initRandMatrix(row2, col2)); // overwrite mainArr by calling initRandMatrix
                        }
                        // updates the file
                        if(oldPath() == true){
                            writeToFile(mainMap, userFilePath, colList);
                        }
                        else{
                            System.out.println("Enter filename to save data: ");
                            resFilePath = input.next();
                            writeToFile(mainMap, resFilePath, colList);
                        }
                        // calls print matrix
                        printMat(mainMap, colList);
                        break;
                    case 6:
                        System.out.println(" --Import Matrix-- ");
                        ArrayList<Object> obj = new ArrayList<>();
                        obj = readFromFile();
                        mainMap = (SortedMap<Integer, TreeMap<Integer, String>>) obj.get(0);
                        colList = (List<Integer>) obj.get(1);
                        break;
                    case 7: 
                        System.out.println("Thank you for using the program!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid input.Please enter a numeric value.");
                }
            } catch (NullPointerException npe){
                System.out.println("Object does not exist. Please enter numeric value to continue."); // error handling to catch nullpointerexceptions
                npe.printStackTrace();
                input.next(); 
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a numeric value.");  // error handling to catch non-numeric input
                e.printStackTrace();
                input.next();  // asks the user to enter choice agian to avoid infinite loop
            }
        }
    }
    
    public static void printMat(SortedMap<Integer, TreeMap<Integer, String>> outerMap, List<Integer> cols){
        outerMap.values().removeIf(Objects::isNull);                                         
        for(Map.Entry<Integer, TreeMap<Integer, String>> outerEntry : outerMap.entrySet()){                  
            System.out.println("Matrix " + outerEntry.getKey());
            int ctr = 0;                                                                    
            for(Map.Entry<Integer, String> innerEntry : outerMap.get(outerEntry.getKey()).entrySet()){ 
                ctr++;                                                                       
                System.out.print(innerEntry.getKey() + ":" + innerEntry.getValue() + " ");      
                if(ctr == cols.get(outerEntry.getKey())){                                    
                    System.out.println("");                                                  
                    ctr = 0;                                                           
                }
            }
            System.out.println("");
        }  
    }

    public static TreeMap<Integer, String> initRandMatrix(int row, int col){
        Random rand = new Random();

        // main inner map
        SortedMap<Integer, String> map = new TreeMap<>();

        // array data
        int totalCells = row * col;                   
        for (int i = 0; i < totalCells; i++) {        
            String arrStr = "";                       
            for (int j = 0; j < 3; j++) {             // loop to enable the program to generate 3-digits of elements individually
                arrStr += Character.toString((char) (rand.nextInt(122-65) + 65)); // compiles each digit into sets of 3 and store into string variable
            }
            map.put(i, arrStr);  // stores the generated string variable into the inner map and assigns a key to it 
        }
        return (TreeMap<Integer, String>) map;            // cast sortedMap into treeMap then return
    }
    
    public static TreeMap addRow(SortedMap<Integer, TreeMap<Integer, String>> outerMap, int index, List<Integer> cols){
        Scanner input = new Scanner(System.in);
        String elem = null;                               // string variable to store user input
        TreeMap innerMap = outerMap.get(index);           // variable to store the inner Map based on index 
        
        for(Map.Entry<Integer, TreeMap<Integer, String>> outerEntry : outerMap.entrySet()){   // loop through outer map
            if(outerEntry.getKey() == index){  
                for(int i = 0; i < cols.get(index); i++){                          
                    System.out.println("Enter element to add to row: ");         
                    elem = input.next();                                         
                    innerMap.put(Integer.parseInt(innerMap.lastKey().toString()) + 1, elem);  // finds the last key in inner matrix and adds a new val                                                                                              // ue to the next key created
                }
            }
        }
        
        return innerMap; // returns the modified innerMap 
    }

    public static void searchFromMat(SortedMap<Integer, TreeMap<Integer, String>> outerMap, String sTerm){
        int countOccurrence = 0;
        
        for(Map.Entry<Integer, TreeMap<Integer, String>> outerEntry : outerMap.entrySet()){ // loops through outer map
            System.out.println("\nSearching matrix " + outerEntry.getKey());                
            
            for(Map.Entry<Integer, String> innerEntry : outerMap.get(outerEntry.getKey()).entrySet()){ // loops through inner map
                String temp = "";                                                         
                if(innerEntry.getValue().contains(sTerm)){                                
                    System.out.println("String index by key: " + innerEntry.getKey());    
                    temp = innerEntry.getValue();                                         
                    countOccurrence += (temp.split(sTerm, -1).length - 1);                
                    System.out.println("Occurrence for element in given key index: " + (temp.split(sTerm, -1).length - 1));
                }   // prints the occurrence of the term in each matrix
            }
        }
        System.out.println("Total Occurrences: " + countOccurrence); // prints the total number of occurrences of the search term across all matrices
    }

    public static TreeMap<Integer, String> replaceMatElem(SortedMap<Integer, TreeMap<Integer, String>> outerMap, int outerMapIndex){
        Scanner input = new Scanner(System.in);
        int innerIndex = 0;                                          //
        String strReplace = null;                                    
        TreeMap innerMap = outerMap.get(outerMapIndex);
        
        for(Map.Entry<Integer, TreeMap<Integer, String>> outerEntry : outerMap.entrySet()){  
            if(outerEntry.getKey() == outerMapIndex){                                        
                System.out.println("Editing Matrix " + outerEntry.getKey());

                System.out.println("Enter the index key of the value you want to edit: ");   
                innerIndex = input.nextInt();
                System.out.println("Enter the new value: ");
                strReplace = input.next();
                innerMap.put(innerIndex, strReplace);            
            }
        }
        return innerMap;    // returns the modified inner matrix
    }
    
    public static void writeToFile(SortedMap<Integer, TreeMap<Integer, String>> outerMap, String filePath, List<Integer> cols) throws IOException{
        File defFile = new File("default.txt");                   
        File defColFile = new File("defaultc.txt");
        File userFile = new File(filePath + ".txt");              
        File userColFile = new File(filePath + "c.txt");          
        String choice = null;
        Scanner input = new Scanner(System.in);                      
        FileOutputStream fos, fos2;
        ObjectOutputStream oos, oos2;
        
        if(!userFile.isFile()){                                   
            while(true){                                          
                System.out.println("File does not exist! Would you like to create a new file with that name? [Y/N]");
                System.out.println("[Y] Writes to file with the name entered earlier. [N] Writes to default file.");
                choice = input.next();
                if(choice.equals("Y") || choice.equals("y")){       
                    System.out.println("User file " + userFile + " created.");
                    
                    fos = new FileOutputStream(userFile);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(outerMap);
                    
                    fos2 = new FileOutputStream(userColFile);
                    oos2 = new ObjectOutputStream(fos2);
                    oos2.writeObject(cols);
                    break;
                }
                else if(choice.equals("N") || choice.equals("n")){
                    System.out.println("Writing to default file.");
                    
                    fos = new FileOutputStream(defFile);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(outerMap);
                    
                    fos2 = new FileOutputStream(defColFile);
                    oos2 = new ObjectOutputStream(fos2);
                    oos2.writeObject(cols);
                    break;
                }
                else{
                    System.out.println("Invalid Input!");
                }
            }
        }
        else{                                                     // user file exists
            System.out.println("File exists! Writing to user file.");
            
            fos = new FileOutputStream(userFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(outerMap);
            
            fos2 = new FileOutputStream(userColFile);
            oos2 = new ObjectOutputStream(fos2);
            oos2.writeObject(cols);
        }
        fos.close();
        oos.close();
        fos2.close();
        oos2.close();
    }
    
    // need fixing
    public static ArrayList<Object> readFromFile() throws IOException{
        Scanner input = new Scanner(System.in);
        System.out.println("Enter filename of file to import data from: ");
        String userPath = input.next();
        
        File userFile = new File(userPath + ".txt");
        FileInputStream fis = null, fis2 = null;
        ObjectInputStream ois = null, ois2 = null;
        
        ArrayList<Object> res = new ArrayList<>();
        SortedMap<Integer, TreeMap<Integer, String>> outerMap = new TreeMap<>();
        List<Integer> cols = new ArrayList<>();
        
        if(!userFile.isFile()){
            System.out.println("File does not exist! Reading from default file.");
            try {
                fis = new FileInputStream("default.txt");
                ois = new ObjectInputStream(fis);
                outerMap = (SortedMap<Integer, TreeMap<Integer, String>>)ois.readObject();
                             
                fis2 = new FileInputStream("defaultc.txt");
                ois2 = new ObjectInputStream(fis2);
                cols = (List<Integer>)ois2.readObject();
                
                res.add(outerMap);
                res.add(cols);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Exer2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }    
        else{                                                     // user file exists
            System.out.println("File exists! Reading from user file.");
            try {
                fis = new FileInputStream(userFile);
                ois = new ObjectInputStream(fis);
                outerMap = (SortedMap<Integer, TreeMap<Integer, String>>)ois.readObject();
                             
                fis2 = new FileInputStream(userPath + "c.txt");
                ois2 = new ObjectInputStream(fis2);
                cols = (List<Integer>)ois2.readObject();
                
                res.add(outerMap);
                res.add(cols);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Exer2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
        return res;
    }
    
    public static boolean oldPath(){
        Scanner input = new Scanner(System.in);
        int choice = 0;
        boolean oldPath = false;
        System.out.println("Would you like to write to previous file or write to a new file? ");
        System.out.println("[1] Write to previous file. [2] Write to a new file.");
        choice = input.nextInt();
        if(choice == 1){
            oldPath = true;
        }
        else if(choice == 2){
            oldPath = false;
        }
        else{
            System.out.println("Invalid Input!");
        }
        return oldPath;
    }
    
}

