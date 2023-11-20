/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.SalesManagerUseCaseSessionBeanRemote;
import entity.Employee;
import entity.FlightBooking;
import entity.FlightCabinClass;
import entity.FlightSchedule;
import entity.Seat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import util.enumerations.CabinClassType;

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
        System.out.println("Enter flight number:");
        System.out.print("> ");
        String flightNumber = scanner.next();
        List<FlightSchedule> flightScheduleList = salesManagerUseCaseSessionBeanRemote.viewFlightSchedules(flightNumber);
        if (flightScheduleList.size() > 0) {
        int counter = 1;
        for (FlightSchedule x : flightScheduleList) {
            System.out.println("");
            System.out.println("Flight Schedule Information");
            System.out.println("Departure Time: " + x.getDepartureTime());
            System.out.println("Arrival Time: " + x.getArrivalTime());
            System.out.println("Press " + counter + " to select this flight schedule");
            counter += 1;
        }
        System.out.println();
        System.out.println("Enter Your Choice Here");
        System.out.print("> ");
        int choice = scanner.nextInt();
        
        List<FlightCabinClass> fccList = salesManagerUseCaseSessionBeanRemote.viewSeatInventory(flightNumber, flightScheduleList.get(choice - 1).getDepartureTime());
        
        int totalAvail = 0;
        int totalReserved = 0;
        int totalBalanced = 0;
        
        for (FlightCabinClass x : fccList) {
           System.out.println(""); 
           System.out.println("Cabin Class Type: " + x.getCabinClass().getCabinClassName());
           System.out.println("Number of available seats " + x.getNumAvailableSeats());
           System.out.println("Number of reserved seats " + x.getNumReservedSeats());
           System.out.println("Number of balance seats " + x.getNumBalanceSeats());
           totalAvail += x.getNumAvailableSeats().intValue();
           totalReserved += x.getNumReservedSeats().intValue();
           totalBalanced += x.getNumBalanceSeats().intValue();
        }
        System.out.println();
        System.out.println("\u001B[1mFlight Cumulative Information\u001B[0m");
        System.out.println(IntStream.range(0, 20).mapToObj(i -> "-").collect(Collectors.joining()));
        System.out.println("Total Available Seats " + totalAvail);
        System.out.println("Total Reserved Seats " + totalReserved);
        System.out.println("Total Balanced Seats " + totalBalanced);
        System.out.println(IntStream.range(0, 20).mapToObj(i -> "-").collect(Collectors.joining()));
        IntStream.rangeClosed(1, 1).forEach(x -> System.out.println());
        
        } else {
            System.out.println("This flight has no active flight schedules!");
        }
    }
    
    public void viewFlightReservations() {
        System.out.println("Enter flight number: ");
        System.out.print("> ");
        String flightNumber = scanner.next();
        List<FlightSchedule> flightScheduleList = salesManagerUseCaseSessionBeanRemote.viewFlightSchedules(flightNumber);
        if (flightScheduleList.size() > 0) {
            int counter = 1;
            for (FlightSchedule x : flightScheduleList) {
                System.out.println("");
                System.out.println("Flight Schedule Information");
                System.out.println("Departure Time: " + x.getDepartureTime());
                System.out.println("Arrival Time: " + x.getArrivalTime());
                System.out.println("Press " + counter + " to select this flight schedule");
                counter += 1;
            }

            System.out.println("Enter Your Choice Here");
            System.out.print("> ");
            int choice = scanner.nextInt();
            FlightSchedule fs = flightScheduleList.get((choice - 1));
            System.out.println("Flight Reservation Information");
            
            HashMap<CabinClassType, List<Seat>> seatMap = new HashMap<>();
            for (int i = 0; i < fs.getFccList().size(); i++) {
                seatMap.put(fs.getFccList().get(i).getCabinClass().getCabinClassName(), new ArrayList<Seat>());
            }
            
            for (FlightBooking fb : fs.getFlightBookingList()) {
                // find the cabin class type of this flight booking
               CabinClassType cct = fb.getReservedSeats().get(0).getFlightCabinClass().getCabinClass().getCabinClassName();
               List<Seat> fbList = seatMap.get(cct);
               // add this to the list 
               fbList.addAll(fb.getReservedSeats());
               // replace the list with this new one
               seatMap.put(cct, fbList);
            }
            
            // once done print out in ascending order within each flight cabin class
            for (CabinClassType cct : seatMap.keySet()) {
                List<Seat> seatList = seatMap.get(cct);
                Comparator<Seat> seatComparator = Comparator.comparing(Seat::getSeatNumber);
                seatList.sort(seatComparator);
                System.out.println("Cabin Class Type: " + cct);
                if (seatList.size() == 0) System.out.println("There are currently no ongoing flight reservations for this cabin class!");
                seatList.stream().forEach(x -> {
                    System.out.println();
                    System.out.println("Seat Number: " + x.getSeatNumber());
                    System.out.println("Passenger name: " + x.getPassenger().getFirstName() + " " + x.getPassenger().getLastName());
                });
            }
            
            
        }
    }
    
}
