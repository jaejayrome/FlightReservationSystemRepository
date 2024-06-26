/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import entity.Employee;
import ejb.session.stateless.RoutePlannerUseCaseSessionBeanRemote;
import entity.Airport;
import entity.FlightRoute;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import util.enumerations.FlightRouteStatus;
import util.exception.AirportNotFoundException;
import util.exception.DuplicateFlightRouteException;
import util.util.Pair;

/**
 *
 * @author jeromegoh
 */
public class RoutePlannerUseCase {
    
    private RoutePlannerUseCaseSessionBeanRemote routePlannerUseCaseSessionBeanRemote;
    
    private Employee employee;
    private List<Airport> airports;
    private Scanner scanner;

    public RoutePlannerUseCase(RoutePlannerUseCaseSessionBeanRemote routePlannerUseCaseSessionBeanRemote, Employee employee) {
        this.routePlannerUseCaseSessionBeanRemote = routePlannerUseCaseSessionBeanRemote;
        this.employee = employee;
        this.airports  = routePlannerUseCaseSessionBeanRemote.getAllAirport();
        this.scanner = new Scanner(System.in);
    }
    // done
    public void createFlightRoute() {
        // print out the list of airports for the user to choose m
        if (airports.size() > 0) {
//            airports.stream().forEach(airport -> printSingleAirport(airport));
            System.out.println("");
            System.out.println("Please Enter the IATA Code of the Origin Airport: ");
            System.out.print("> ");
            String origin = scanner.next();
            scanner.nextLine();
            System.out.println("");
            System.out.println("Please Enter the IATA Code of the Destination Airport"); 
            System.out.print("> ");
            String destination = scanner.next();
            scanner.nextLine();
            System.out.println();
            System.out.println("Would you like to make a complementary flight route for this?");
            System.out.println("Press 0 to skip.");
            System.out.println("Press 1 to create a return flight route.");
            System.out.print("> ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            boolean makeReturnFlightRoute = choice == 1 ? true : false;
            // intiialised as disabled until a flight has been added
            FlightRoute flightRoute = new FlightRoute(FlightRouteStatus.DISABLED);
            try {
                routePlannerUseCaseSessionBeanRemote.createNewFlightRoute(origin, destination, flightRoute, makeReturnFlightRoute);
                System.out.println("Transaction \u001B[32mSuccessful\u001B[0m!");
            } catch (DuplicateFlightRouteException e) {
                System.out.println(e.getMessage());
            } catch (AirportNotFoundException ee) {
                System.out.println(ee.getMessage());
            }
        }
    }
    
    public void viewAllFlightRoutes() {
        List<FlightRoute> flightRoutes = routePlannerUseCaseSessionBeanRemote.viewAllFlightRoute();
        if (flightRoutes.size() > 0){
            flightRoutes.stream().forEach(flightRoutePair -> {
                printSingleFlightRoute(flightRoutePair);
            });
        } else {
            System.out.println("There are currently no available flight routes in the system");
        }
        
    }
    
    
    // leave it for when all the relationships have been set up properly
    public void deleteFlighRoute() {
        System.out.println("");
        System.out.println("Enter the Flight Route Origin Airport IATA Code: ");
        System.out.print("> ");
        String originAirport = scanner.next();
        
        
        System.out.println("");
        System.out.println("Enter the Flight Route Destination Airport IATA Code: ");
        System.out.print("> ");
        String destinationAirport = scanner.next();
        
        System.out.println("Confirmation");
        System.out.println("Press 1 to confirm your deletion");
        System.out.println("Press 0 to skip back to the main menu");
        System.out.print("> ");
        int choice = scanner.nextInt();
        
        if (choice == 1) {
            boolean deletedOrDisabled = routePlannerUseCaseSessionBeanRemote.deleteFlightRoute(originAirport, destinationAirport);
            if (deletedOrDisabled) {
                System.out.println();
                System.out.println("Transaction \u001B[32mSuccessful\u001B[0m!");
                System.out.println("You have successfully disabled / deleted the flight route!");
            } else {
                System.out.println();
                System.out.println("You have entered an invalid flight route!");
            }
        } else {
            return;
        }
    }
    
    public void printSingleAirport(Airport airport) {
        System.out.println("");
        System.out.println("Airport Description");
        System.out.println("Airport Name: " + airport.getAirportName());
        System.out.println("Airport IATA Code: " + airport.getIataAirportCode());
        System.out.println("Airport City: " + airport.getCity());
        System.out.println("Airport State: " + airport.getState());
        System.out.println("Airport Country: " + airport.getCountry());
    }
    
    public void printSingleFlightRoute(FlightRoute flightRoute) {
        System.out.println("");
        System.out.println("Flight Route Information");
        System.out.println("Flight Route Origin: " + flightRoute.getOrigin().getAirportName());
        System.out.println("Flight Route Origin: " + flightRoute.getOrigin().getIataAirportCode());
        System.out.println("Flight Route Origin: " + flightRoute.getOrigin().getId());
        System.out.println("Flight Route Destination: " + flightRoute.getDestination().getAirportName());
        System.out.println("Flight Route Destination: " + flightRoute.getDestination().getId());
        System.out.println("Flight Route Destination: " + flightRoute.getDestination().getIataAirportCode());
        System.out.println("Flight Route Status: " + flightRoute.getFlightRouteStatus());
    }
    

    
    
}
