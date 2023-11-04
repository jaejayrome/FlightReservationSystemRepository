/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author jeromegoh
 */
public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBean;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        RunApp runApp = new RunApp(customerSessionBean);
        
        
        runApp.showVisitorHomeScreen();
    }
    
}
