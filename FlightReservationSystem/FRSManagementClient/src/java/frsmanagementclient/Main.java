/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeUseCaseSessionBeanRemote;
import ejb.session.stateless.FleetManagerUseCaseSessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.RoutePlannerUseCaseSessionBeanRemote;
import ejb.session.stateless.SalesManagerUseCaseSessionBeanRemote;
import ejb.session.stateless.ScheduleManagerUseCaseSessionBeanRemote;

/**
 *
 * @author jeromegoh
 */
public class Main {

    @EJB
    private static SalesManagerUseCaseSessionBeanRemote salesManagerUseCaseSessionBean;

    @EJB
    private static ScheduleManagerUseCaseSessionBeanRemote scheduleManagerUseCaseSessionBean;

    @EJB
    private static FleetManagerUseCaseSessionBeanRemote fleetManagerUseCaseSessionBean;

    @EJB
    private static RoutePlannerUseCaseSessionBeanRemote routePlannerUseCaseSessionBeanRemote;
  
    @EJB
    private static EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote;
    
    
    
    
    
    
    public static void main(String[] args) {
        RunApp runApp = new RunApp(employeeUseCaseSessionBeanRemote, routePlannerUseCaseSessionBeanRemote, fleetManagerUseCaseSessionBean, scheduleManagerUseCaseSessionBean, salesManagerUseCaseSessionBean);
        runApp.showLoginScreen();
    }
    

    

    
}
