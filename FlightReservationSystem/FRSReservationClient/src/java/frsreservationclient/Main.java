/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.Customer;
import java.util.List;
import java.util.Scanner;
import javax.ejb.EJB;

/**
 *
 * @author jeromegoh
 */
public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); 
        
    }
    
    public static void homepage(Scanner sc) {
        RunApp runApp = new RunApp();
        System.out.println("This exists");
    }
    
    
    
    
}
