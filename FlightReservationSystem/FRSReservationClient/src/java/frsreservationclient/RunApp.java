/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.CustomerUseCaseSessionBeanRemote;
import entity.Fare;
import entity.FlightCabinClass;
import entity.FlightSchedule;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.NoFlightFoundException;
import util.util.Pair;

/**
 *
 * @author geraldtan
 */
public class RunApp {
    
    private CustomerSessionBeanRemote customerSessionBean;
    private CustomerUseCaseSessionBeanRemote customerUseCaseSessionBean;
    private boolean customerIsLoggedIn;

    public RunApp() {}
    
    public RunApp(CustomerSessionBeanRemote customerSessionBean, CustomerUseCaseSessionBeanRemote customerUseCaseSessionBean) {
        //expose runApp to remote SessionBean methods
        this.customerSessionBean = customerSessionBean; 
        this.customerUseCaseSessionBean = customerUseCaseSessionBean;
    }
    
    
    
    public void showVisitorHomeScreen() {
        Scanner sc = new Scanner(System.in);
        
        userAuthPrompt(sc);
        
        sc.close();
    }
    
  
    public void userAuthPrompt(Scanner sc) {
        System.out.println("Press '0' to continue as guest");
        System.out.println("Press '1' to Login to an existing account");
        System.out.println("Press '2' to Register an account");
        
        int option = Integer.valueOf(sc.nextLine());
        
        switch(option) {
            case 0:
                System.out.println("Continue as guest");
                userAuthPrompt(sc);
                break;
            case 1:
                System.out.println("Login selected");
                System.out.println("Enter Your Email: ");
                String loginEmail = sc.nextLine();
                System.out.println("Enter Your Password: ");
                String loginPassword = sc.nextLine();
                int customerLogin = customerUseCaseSessionBean.customerLogin(loginEmail, loginPassword);
                switch (customerLogin) {
                    case -1:
                        System.out.println("Customer account has not been "
                                + "created yet, would you like to create a new "
                                + "account?\n");
                        System.out.println("Enter Y to register new account");
                        System.out.println("Enter N try logging in again");
                        String tmpOption = sc.nextLine();
                        if (tmpOption.equals("Y")) {
                            createNewCustomerAccount(sc);
                            this.customerIsLoggedIn = true;
                            System.out.println("You account has been created and you are logged in");
                        } else {
                            userAuthPrompt(sc);
                        }
                        break;
                    case 0:
                        System.out.println("Invalid Password, please try again");
                        userAuthPrompt(sc);

                    case 1:
                        System.out.println("Customer Found & Authenticated");
                        System.out.println("Press 1 to search for flights");
                        System.out.print("> ");
                        int choice = sc.nextInt();
                        if (choice == 1) searchForFlightRoutes(sc);
                        customerIsLoggedIn = true;
                        userAuthPrompt(sc);
                        break;
                    default: 
                        invalidOption();
                        break;
                }
                //case 1 break
                break;
                
            case 2:
            System.out.println("Registering your account");
                createNewCustomerAccount(sc);
                
                userAuthPrompt(sc);
                break;
            default: 
                invalidOption();
        }
    }
    
    public void invalidOption() {
        System.out.println("You have selected an invalid option!");
     }
     
    public void createNewCustomerAccount(Scanner sc) {
         System.out.println("Input First Name: ");
            String firstName = sc.nextLine();
            System.out.println("Input Last Name: ");
            String lastName = sc.nextLine();
            System.out.println("Input Email: ");
            String email = sc.nextLine();
            System.out.println("Input Phone Number: ");
            String phoneNumber = sc.nextLine();
            System.out.println("Input your Address: ");
            String address = sc.nextLine();
            System.out.println("Input your Password: ");
            String password = sc.nextLine();

            customerSessionBean.createNewCustomer(firstName, lastName, 
                    email, phoneNumber, address, password);
     }
    
    public void searchForFlightRoutes(Scanner sc) {
        int roundTrip;
        String departureAirport;
        Date departureDate = null;
        String destinationAirport;
        Date returnDate = null;
        int directFlight;
        int numPassengers = 0;
        
        System.out.println("Please enter the number of passengers");
        System.out.print("> ");
        numPassengers = sc.nextInt();
        sc.nextLine();
        
        System.out.println("Please enter your Flight details to check for available flights");
        System.out.println("Trip Type: Press 1 for One-Way, 2 for Return");
        System.out.print("> ");
        roundTrip = sc.nextInt();
        sc.nextLine();
        
        System.out.println("Enter Trip Departure Airport: ");
        System.out.print("> ");
        departureAirport = sc.next();
        sc.nextLine();
        
        System.out.println("Enter Destination Airport: ");
        System.out.print("> ");
        destinationAirport = sc.next();
        sc.nextLine();
        
        System.out.println("Enter the Departure Date (yyyy-MM-dd):");
        System.out.print("> ");
        String startDateInput = sc.next();
        sc.nextLine();
        System.out.println("");
        System.out.println("Enter the Departure Time (HH:mm:ss):");
        System.out.print("> ");
        String timeInput = sc.next();
        String dateTimeInput = startDateInput + " " + timeInput;
            try {
                departureDate = formatDate(dateTimeInput);
            } catch (Exception e) {
                System.out.println("Departure Date: " + returnDate);

                System.out.println("Invalid Date format. Please try again");
            }
        
        if (roundTrip == 2) { //only collect return date if is return flight
            //roundtrip
            System.out.println("Enter the Return Departure Date (yyyy-MM-dd):");
            System.out.print("> ");
            String endDateInput = sc.next();
            sc.nextLine();
            System.out.println("");
            System.out.println("Enter the Return Departure Time (HH:mm:ss):");
            System.out.print("> ");
            String endTimeInput = sc.next();
            String endDateTimeInput = endDateInput + " " + endTimeInput;
            
            try {
                returnDate = formatDate(endDateTimeInput);
                System.out.println("Return Date: " + returnDate.toString());
            } catch (Exception e) {
                System.out.println("Invalid Date format. Please try again");
            }
        }
        
        System.out.println("Do you prefer a direct or connecting flight? Press 1 for direct, 2 for Connecting");
        System.out.print("> ");
        directFlight = sc.nextInt(); 
        sc.nextLine();
        
        try {
            List<List<FlightSchedule>> flightScheduleList = customerUseCaseSessionBean.searchForFlightRoutes(
            departureAirport, departureDate, destinationAirport,
            returnDate, directFlight);
            // there is only one size nothing is being added at all 
            List<FlightSchedule> directTo = flightScheduleList.get(0);
            printDirectFlightsTo(directTo, departureAirport, destinationAirport, numPassengers);
            if (returnDate != null) {
                System.out.println("Return Flights");
                List<FlightSchedule> returnTo = flightScheduleList.get(1);
                printDirectFlightsTo(returnTo, destinationAirport, departureAirport, numPassengers);
            }
            // for connecting airports
            if (directFlight == 2) {
                // System.out.println(flightScheduleList.size());
                System.out.println();
                System.out.println();
                System.out.println("Connecting Flights: TO");
                List<FlightSchedule> directFirst= flightScheduleList.get(2);
                List<FlightSchedule> directSecond= flightScheduleList.get(3);
                printConnectingFlights(directFirst, directSecond, departureAirport, destinationAirport, numPassengers);
                
                if (returnDate != null) {
                    System.out.println("");
                    System.out.println("Connecting Flights: RETURN");
                    List<FlightSchedule> returnFirst= flightScheduleList.get(4);
                    List<FlightSchedule> returnSecond= flightScheduleList.get(5);
                    printConnectingFlights(returnFirst, returnSecond, destinationAirport, departureAirport, numPassengers);
                }
            }
        } catch (NoFlightFoundException e) {
            System.out.println(e.getMessage());
        }
        
       
    }
    
    public static void printDirectFlightsTo(List<FlightSchedule> fsList, String start, String end, int numPassengers) {
        System.out.println();
        System.out.println("Direct Flights: " + start + " -> " + end);
        System.out.println();
        int counter = 1;
        for (FlightSchedule fs : fsList) {
            System.out.println("");
            System.out.println("Option #" + counter);
            System.out.println("Departure Time " + fs.getDepartureTime());
            System.out.println("Arrival Time " + fs.getArrivalTime());
            System.out.println("Duration " + fs.getFlightDuration());
            // display cabin class availability
            System.out.println("");
            System.out.println("Cabin Class Availability");
            System.out.println("Cabin Class Options: " + fs.getFccList().size());
            for (FlightCabinClass fcc : fs.getFccList()) {
                printFlightCabinClass(fcc, numPassengers);
            }
            counter += 1;
        }
    }
    
     public static void printConnectingFlights(List<FlightSchedule> fs1List, List<FlightSchedule> fs2List, String start, String end, int numPassengers) {
         for (int i = 0; i < fs1List.size(); i++) {
             // assuming that since they are return flights they can only be of the same aircraft configuration
             // so this would mean the number of FCCs for both is the same
            Pair<FlightSchedule> connectingPair = new Pair<FlightSchedule>(fs1List.get(i), fs2List.get(i));
            System.out.println("");
            System.out.println("Option #" + (i + 1));
            System.out.println("Connecting Flight Leg 1: " + start + " -> " + connectingPair.getFirst().getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getIataAirportCode());
            System.out.println("Departure Time " + connectingPair.getFirst().getDepartureTime());
            System.out.println("Arrival Time " + connectingPair.getFirst().getArrivalTime());
            System.out.println("Duration " + connectingPair.getFirst().getFlightDuration());
            System.out.println("");
            System.out.println("Connecting Flight Leg 2: " + connectingPair.getSecond().getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getIataAirportCode() + " -> " + end);
            System.out.println("Departure Time " + connectingPair.getSecond().getDepartureTime());
            System.out.println("Arrival Time " + connectingPair.getSecond().getArrivalTime());
            System.out.println("Duration " + connectingPair.getSecond().getFlightDuration());
            System.out.println("");
            System.out.println("Cabin Class Information");
            // display cabin class availability
            System.out.println("Cabin Class Availability");
            System.out.println("Cabin Class Options: " + connectingPair.getSecond().getFccList().size());
            // find the cheapest fare for a cabin class
            for (int k = 0; k < connectingPair.getFirst().getFccList().size(); k++){
                double lowestFare1 = printFlightConnectingCabinClass(connectingPair.getFirst().getFccList().get(k), true);
                double lowestFare2 = printFlightConnectingCabinClass(connectingPair.getSecond().getFccList().get(k), true);
                System.out.println("Lowest Fare Offered!");
                System.out.println("Price per passenger: $" + (lowestFare1 + lowestFare2)) ;
                System.out.println("Total Price for all passengers: $" + ((lowestFare1 + lowestFare2) * numPassengers));
            }
         }
         
     }
         
            
    public static void printFlightCabinClass(FlightCabinClass fcc, int numPassengers) {
        System.out.println("Cabin Class Type: " + fcc.getCabinClass().getCabinClassName());
        System.out.println("No. of Fares for this Particular Flight Schedule & Cabin Class " + fcc.getCabinClass().getFareList().size());
        Comparator<Fare> fareLowest = (x, y) -> (int)(x.getFareAmount().doubleValue() - y.getFareAmount().doubleValue());
        Fare lowestFare = fcc.getCabinClass().getFareList().stream().sorted(fareLowest).findFirst().get();
        System.out.println("Lowest Fare Offered!");
        System.out.println("Price per passenger: $" + lowestFare.getFareAmount().doubleValue());
        System.out.println("Total Price for all passengers: $" + (lowestFare.getFareAmount().doubleValue() * numPassengers));
    }
    
    
    public static double printFlightConnectingCabinClass(FlightCabinClass fcc, boolean isConnecting) {
        System.out.println("Cabin Class Type: " + fcc.getCabinClass().getCabinClassName());
        System.out.println("No. of Fares for this Particular Flight Schedule & Cabin Class "  + fcc.getCabinClass().getFareList().size());
        if (isConnecting) {
            Comparator<Fare> fareLowest = (x, y) -> (int)(x.getFareAmount().doubleValue() - y.getFareAmount().doubleValue());
            Fare lowestFare = fcc.getCabinClass().getFareList().stream().sorted(fareLowest).findFirst().get();
            return  (lowestFare.getFareAmount().doubleValue());
        } else {
            return 0.0;
        }
    }
   
    public Date formatDate(String dateTimeInput) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeInput, formatter);
            ZoneId zoneId = ZoneId.of("Asia/Singapore");
            Date date = Date.from(dateTime.atZone(zoneId).toInstant());
        return date;
    }
}
