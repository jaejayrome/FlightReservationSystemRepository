/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.SalesManagerUseCaseSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;

/**
 *
 * @author jeromegoh
 */
public class SalesManagerUseCase {
    
    private SalesManagerUseCaseSessionBeanRemote salesManagerUseCaseSessionBeanRemote;
    private Employee employee;
    private Scanner scanner;

    public SalesManagerUseCase() {
    }
    
    public SalesManagerUseCase(Employee employee, SalesManagerUseCaseSessionBeanRemote salesManagerUseCaseSessionBeanRemote) {
        this.employee = employee;
        this.salesManagerUseCaseSessionBeanRemote = salesManagerUseCaseSessionBeanRemote;
        this.scanner = new Scanner(System.in);
    }
    
    
    public void viewSeatsinventory() {
        System.out.println("Enter flight number: ");
        String flightNumber = scanner.next();
        // gives me a lot of flight schedule
    }
    
    public void viewFlightReservations() {
        
    }
    
}
