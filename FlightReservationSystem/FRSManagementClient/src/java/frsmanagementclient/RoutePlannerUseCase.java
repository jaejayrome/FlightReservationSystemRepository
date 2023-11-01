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
            airports.stream().forEach(airport -> printSingleAirport(airport));
            System.out.println("");
            System.out.print("Please Enter the IATA Code of the Origin Airport: ");
            String origin = scanner.next();
            System.out.println("");
            System.out.println("Please Enter the IATA Code of the Destination Airport"); 
            String destination = scanner.next();
            // filter out the airports of chocie
            Airport originAirport = airports.stream().filter(x -> x.getIataAirportCode().equals(origin)).findFirst().get();
            Airport destinationAirport = airports.stream().filter(x -> x.getIataAirportCode().equals(destination)).findFirst().get();
            // intiialised as disabled until a flight has been added
            FlightRoute flightRoute = new FlightRoute(FlightRouteStatus.DISABLED);
            routePlannerUseCaseSessionBeanRemote.createNewFlightRoute(originAirport, destinationAirport, flightRoute);
        }
    }
    
    public void viewAllFlightRoutes() {
        List<Pair<FlightRoute>> flightRoutes = routePlannerUseCaseSessionBeanRemote.viewAllFlightRoute();
         //Comparator<Pair<FlightRoute>> sortPairs = (x, y) -> x.getFirst().getOrigin().getAirportName().compareTo(y.getFirst().getOrigin().getAirportName());
  
        if (flightRoutes.size() > 0){
            flightRoutes.stream().forEach(flightRoutePair -> {
                
                printSingleFlightRoute(flightRoutePair.getFirst());
                printSingleFlightRoute(flightRoutePair.getSecond());
//                if (flightRoutePair.getFirst().getOrigin().getAirportName().compareTo(flightRoutePair.getSecond().getOrigin().getAirportName()) <= 0) {
//                    printSingleFlightRoute(flightRoutePair.getFirst());
//                    printSingleFlightRoute(flightRoutePair.getSecond());
//                } else {
//                    printSingleFlightRoute(flightRoutePair.getSecond());
//                    printSingleFlightRoute(flightRoutePair.getFirst());
//                }
            });
        } else {
            System.out.println("There are currently no available flight routes in the system");
        }
        
    }
    
    // leave it for when all the relationships have been set up properly
    public void deleteFlighRoute() {
        
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
    }
    

    
    
}
