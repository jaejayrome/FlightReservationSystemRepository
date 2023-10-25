/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeUseCaseSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author jeromegoh
 */
public class Main {
    
  
    @EJB
    private static EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote;
    
    public static void main(String[] args) {
        
        RunApp runApp = new RunApp(employeeUseCaseSessionBeanRemote);
        runApp.showLoginScreen();
    }
    

    

    
}
