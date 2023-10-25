/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeUseCaseSessionBeanRemote;
import entity.FlightRoute;
import javax.ejb.EJB;
import util.enumerations.JobTitle;

/**
 *
 * @author jeromegoh
 */
public class RoutePlannerUseCase {
    
    @EJB
    private EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote;

    public RoutePlannerUseCase() {
    }
    
    
    public void createFlightRoute(String origin, String destination) {
//        
//        FlightRoute flightRoute = new FlightRoute(origin, destination);
//        employeeUseCaseSessionBeanRemote.employeeCommandOne(JobTitle.ROUTE_PLANNER, flightRoute);
    }
    
    public void viewAllFlightRoutes() {
        
    }
    
    public void deleteFlighRoute() {
        
    }

    
    
}
