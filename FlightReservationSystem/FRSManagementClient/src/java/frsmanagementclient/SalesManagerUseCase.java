/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.SalesManagerUseCaseSessionBeanRemote;
import entity.Employee;
import entity.FlightCabinClass;
import entity.FlightSchedule;
import java.util.List;
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
        List<FlightSchedule> flightScheduleList = salesManagerUseCaseSessionBeanRemote.viewFlightSchedules(flightNumber);
        int counter = 1;
        for (FlightSchedule x : flightScheduleList) {
            System.out.println("");
            System.out.println("Flight Schedule Information");
            System.out.println("Departure Time: " + x.getDepartureTime());
            System.out.println("Arrival Time: " + x.getArrivalTime());
            System.out.println("Press " + counter + " to select this flight schedule");
        }
        
        System.out.println("Enter Your Choice Here");
        System.out.print("> ");
        int choice = scanner.nextInt();
        
        List<FlightCabinClass> fccList = salesManagerUseCaseSessionBeanRemote.viewSeatInventory(flightNumber, flightScheduleList.get(choice - 1).getDepartureTime());
        
        int totalAvail = 0;
        int totalReserved = 0;
        int totalBalanced = 0;
        
        for (FlightCabinClass x : fccList) {
           System.out.println(""); 
           System.out.println("Number of available seats " + x.getNumAvailableSeats());
           System.out.println("Number of reserved seats " + x.getNumReservedSeats());
           System.out.println("Number of balance seats " + x.getNumBalanceSeats());
           totalAvail += x.getNumAvailableSeats().intValue();
           totalReserved += x.getNumReservedSeats().intValue();
           totalBalanced += x.getNumBalanceSeats().intValue();
        }
        
        System.out.println("Total Available Seats " + totalAvail);
        System.out.println("Total Reserved Seats " + totalReserved);
        System.out.println("Total Balanced Seats " + totalBalanced);
    }
    
    public void viewFlightReservations() {
        
    }
    
}
