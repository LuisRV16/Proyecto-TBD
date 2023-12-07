/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pruebacorreo;

/**
 *
 * @author Manuel
 */
public class PruebaCorreo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String str0 = "a";
        String str1 = "@";
        String str2 = ".";
        String str3 = "a@";
        String str4 = "a@.";
        String str5 = "a@a.";
        String str6 = "a@a";
        String str7 = "@a.a";
        String str8 = "@a.";
        String str9 = "@aa";
        String str10 = "a@a.a";
        
        System.out.println(correo(str0));
        System.out.println(correo(str1));    
        System.out.println(correo(str2));    
        System.out.println(correo(str3));    
        System.out.println(correo(str4));    
        System.out.println(correo(str5));    
        System.out.println(correo(str6));    
        System.out.println(correo(str7)); 
        System.out.println(correo(str8)); 
        System.out.println(correo(str9)); 
        System.out.println(correo(str10)); 
        
    }
    
    public static boolean correo(String str){
             return str.matches("^[a-zA-Z0-9_!#$%&'*+/=?{|}^.-]+@[a-zA-Z0-9]+.[a-z]+$");
        }
    
}
