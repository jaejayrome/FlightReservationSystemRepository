/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeUseCaseSessionBeanRemote;
import entity.Employee;
import util.enumerations.JobTitle;

/**
 *
 * @author jeromegoh
 */
public class FleetManagerUseCase {
    private EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote;
    private Employee currentEmployee;
    
    public FleetManagerUseCase() {
    }
    
    public FleetManagerUseCase(EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote, Employee currentEmployee) {
        this.employeeUseCaseSessionBeanRemote = employeeUseCaseSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void creatAircraftConfiguration() {
        // use an employee use case session bean that would be the best 
//        employeeUseCaseSessionBeanRemote.command1(JobTitle.FLEET_MANAGER, currentEmployee.getId());
    }
    
    public void viewAllAircraftConfiguration() {
        
    }
    
    public void viewAircraftConfigurationDetails() {
        
    }

   
    
}
