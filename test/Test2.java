
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Vienji
 */
public class Test2 {
    public static void main(String[] args){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        
        System.out.println("Date: " + date);
    }
}
