/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeUseCaseSessionBeanRemote;
import ejb.session.stateless.FleetManagerUseCaseSessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.RoutePlannerUseCaseSessionBeanRemote;

/**
 *
 * @author jeromegoh
 */
public class Main {

    @EJB
    private static FleetManagerUseCaseSessionBeanRemote fleetManagerUseCaseSessionBean;

    @EJB
    private static RoutePlannerUseCaseSessionBeanRemote routePlannerUseCaseSessionBeanRemote;
  
    @EJB
    private static EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote;
    
   
    public static void main(String[] args) {
//        System.out.println("am i null " + (fleetManagerUseCaseSessionBean == null));
////        System.out.println("fleetmanager is null" + (fleetManagerUseCaseSessionBean == null));
//        System.out.println("route planner is null" + (routePlannerUseCaseSessionBeanRemote == null));
//        System.out.println("employee is null is null" + (employeeUseCaseSessionBeanRemote == null));
        RunApp runApp = new RunApp(employeeUseCaseSessionBeanRemote, routePlannerUseCaseSessionBeanRemote, fleetManagerUseCaseSessionBean);
        runApp.showLoginScreen();
    }
    

    

    
}
