/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Vienji
 */
public class StringSample {
    public static void main(String... arguments){
        String path = "C:\\Users\\userpc\\Desktop\\Cabinet\\Music\\New songs 4";
        
        System.out.println(filenameSplitter(path));  
    }
    
    private static String filenameSplitter(String path){
        String filename = "";
        String temp = "";
        int size = path.length();
        
        out:
        for(int i = size - 1; i >= 0; i--){
            if(path.charAt(i) != '\\'){
                temp += path.charAt(i);
            } else {
                break out;
            }
        }
        
        for(int i = temp.length() - 1; i >= 0; i--){
            filename += temp.charAt(i);
        }
        
        return filename;            
    }
}
