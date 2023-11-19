/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import entity.Employee;
import ejb.session.stateless.RoutePlannerUseCaseSessionBeanRemote;
import entity.Airport;
import entity.FlightRoute;
import java.util.List;
import java.util.Scanner;
import util.enumerations.FlightRouteStatus;
import util.exception.AirportNotFoundException;
import util.exception.DuplicateFlightRouteException;
import util.exception.NoExistingAirportException;
import util.exception.NoFlightRouteFoundException;

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
            } catch (DuplicateFlightRouteException e) {
                System.out.println(e.getMessage());
            } catch (AirportNotFoundException ee) {
                System.out.println(ee.getMessage());
            }
        }
    }
    
    public void viewAllFlightRoutes() {
        List<FlightRoute> flightRoutes = routePlannerUseCaseSessionBeanRemote.viewAllFlightRoute();
         //Comparator<Pair<FlightRoute>> sortPairs = (x, y) -> x.getFirst().getOrigin().getAirportName().compareTo(y.getFirst().getOrigin().getAirportName());
  
        if (flightRoutes.size() > 0) {
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
            try {
                try {
                    boolean deletedOrDisabled = routePlannerUseCaseSessionBeanRemote.deleteFlightRoute(originAirport, destinationAirport);
                    if (deletedOrDisabled) {
                        System.out.println("You have successfully disabled / deleted the flight route!");
                    } else {
                        System.out.println("You have entered an invalid flight route!");
                    }
                } catch (AirportNotFoundException e){
                    throw new NoExistingAirportException();
                }
                
            } catch (NoExistingAirportException e) {
                System.out.println("Entered Airports is invalid, unable to find and delete flight route");
            } catch (NoFlightRouteFoundException e) {
                System.out.println("Entered Flight Route is inavalid, unable to delete flight route");

            }  
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
        if (flightRoute.getFlightRouteStatus() == FlightRouteStatus.ACTIVE) {
            System.out.println("Flight Route Status: ACTIVE");
        } else {
            System.out.println("Flight Route Status: DISABLED");
        }        System.out.println("Flight Route Origin: " + flightRoute.getOrigin().getAirportName());
        System.out.println("Flight Route Origin: " + flightRoute.getOrigin().getIataAirportCode());
        System.out.println("Flight Route Origin: " + flightRoute.getOrigin().getId());
        System.out.println("Flight Route Destination: " + flightRoute.getDestination().getAirportName());
        System.out.println("Flight Route Destination: " + flightRoute.getDestination().getId());
        System.out.println("Flight Route Destination: " + flightRoute.getDestination().getIataAirportCode());
    }
    

    
    
}
