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

/**
 *
 * @author jeromegoh
 */
public class RoutePlannerUseCase {
    
    private RoutePlannerUseCaseSessionBeanRemote routePlannerUseCaseSessionBeanRemote;
    
    private Employee employee;
    private List<Airport> airports;
    private Scanner scanner;
    
    public RoutePlannerUseCase() {
        this.airports = routePlannerUseCaseSessionBeanRemote.getAllAirport();
    }

    public RoutePlannerUseCase(RoutePlannerUseCaseSessionBeanRemote routePlannerUseCaseSessionBeanRemote, Employee employee) {
        this.routePlannerUseCaseSessionBeanRemote = routePlannerUseCaseSessionBeanRemote;
        this.employee = employee;
        this.scanner = new Scanner(System.in);
    }
        
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
            FlightRoute flightRoute = new FlightRoute(originAirport, destinationAirport, FlightRouteStatus.ACTIVE);
            routePlannerUseCaseSessionBeanRemote.createNewFlightRoute(flightRoute);
        }
    }
    
    public void viewAllFlightRoutes() {
        List<FlightRoute> flightRoutes = routePlannerUseCaseSessionBeanRemote.viewAllFlightRoute();
        if (flightRoutes.size() > 0){
            flightRoutes.stream().forEach(flightRoute -> printSingleFlightRoute(flightRoute));
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
        System.out.println("Flight Route Origin: " + flightRoute.getOrigin());
        System.out.println("Flight Route Destination: " + flightRoute.getDestination());
    }
    

    
    
}
