/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hrspartnerclient;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static java.lang.ProcessBuilder.Redirect.to;
import static java.lang.System.out;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.util.Pair;
import ws.entity.Fare;
import ws.entity.Flight;
import ws.entity.FlightCabinClass;
import ws.entity.FlightReservationSystemWebService;
import ws.entity.FlightReservationSystemWebService_Service;
import ws.entity.FlightSchedule;


/**
 *
 * @author jeromegoh
 */
public class HRSPartnerClient {
   
    


  private static long sessionId;
    
    
    public static void showMenuOptions(Scanner sc, FlightReservationSystemWebService port) {
        System.out.println("Welcome to Merlion Airways (Partner) Holiday Reservation System");
        System.out.println("Press '0' to exit");
        System.out.println("Press '1' to Login to an existing account");
        int option = sc.nextInt();
        sc.nextLine();
        switch(option) {
            case 0:
                System.exit(1);
                break;
            case 1:
                System.out.println("Login selected");
                System.out.println("Enter Your Username: ");
                String loginEmail = sc.nextLine();
                System.out.println("Enter Your Password: ");
                String loginPassword = sc.nextLine();
                long partnerId = doLogin(loginEmail, loginPassword, port);
                sessionId = partnerId;
                if (partnerId > 0) {
                    doMainMenu(sc, port);
                }
                showMenuOptions(sc, port);
                break;
        }
    }    
    public static void doMainMenu(Scanner sc, FlightReservationSystemWebService port) {
        System.out.println("Press '0' to logout");
        System.out.println("Press '1' to Search Flights and Reserve Flights");
        System.out.println("Press '2' to View Partner Flight Reservations");
        System.out.println("Press '3' to View Partner Flight Reservation Details");
        int option2 = sc.nextInt();
        sc.nextLine();
        switch (option2) {
            case 0: 
                doLogout(port, sessionId);
                break;
            case 1: 
                searchForFlightRoutes(sessionId, sc, true, port);
                doMainMenu(sc, port);
                break;
            case 2: 
                viewFlightReservations();
                doMainMenu(sc, port);
                break;
            case 3: 
                viewFlightReservationDetails();
                doMainMenu(sc, port);
                break;
        }
    }
    
    public static void doLogout(FlightReservationSystemWebService port, long partnerId) {
        port.partnerLogout(partnerId);
        System.out.println("Partner has successfully logged out!");
    }
    
    public static long doLogin(String username, String password, FlightReservationSystemWebService port) {
        long partnerId = port.partnerLogin(username, password);
        return partnerId;
    }
    
    public static void viewFlightReservations() {
        
    }
    
    public static void viewFlightReservationDetails() {
        
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FlightReservationSystemWebService_Service service = new FlightReservationSystemWebService_Service();
        FlightReservationSystemWebService port = service.getFlightReservationSystemWebServicePort();
        showMenuOptions(sc, port);
    }
    
    
    public static void searchForFlightRoutes(long partnerid, Scanner sc, boolean isCustomer, FlightReservationSystemWebService port) {
        int roundTrip;
        String departureAirport;
        String destinationAirport;
        int directFlight;
        int numPassengers = 0;

        String startDateTimeInput = "";
        String returnDateTimeInput = "";


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
        System.out.println("Enter the Departure Time (HH:mm:ss):");
        System.out.print("> ");
        String timeInput = sc.next();
        String dateTimeInput = startDateInput + " " + timeInput;
        startDateTimeInput = dateTimeInput;
        
            try {
                // check date time input here
                // System.out.println("start is " + startDateTimeInput);
            } catch (Exception e) {                    
                System.out.println("Invalid Date format. Please try again");
            }

        if (roundTrip == 2) {
            // roundtrip
            System.out.println("Enter the Return Departure Date (yyyy-MM-dd):");
            System.out.print("> ");
            String endDateInput = sc.next();
            sc.nextLine();
            System.out.println("Enter the Return Departure Time (HH:mm:ss):");
            System.out.print("> ");
            String endTimeInput = sc.next();
            String end = endDateInput + " " + endTimeInput;
            returnDateTimeInput = end;
            try {
                // check returnDate
                // System.out.println("return is " + returnDateTimeInput);
            } catch (Exception e) {
                System.out.println("Invalid Date format. Please try again");
            }
        }

        System.out.println("Do you prefer a direct or connecting flight? Press 1 for direct, 2 for Connecting");
        System.out.print("> ");
        directFlight = sc.nextInt(); 
        sc.nextLine();
        
        
            // DIRECT: TO
            System.out.println("DIRECT: TO");
            System.out.println("");
            printFlightScheduleInformation(departureAirport, startDateTimeInput, destinationAirport, sc, port);

            // DIRECT: RETURN
            
            if (!returnDateTimeInput.isEmpty()) {
                System.out.println("DIRECT: RETURN");
                System.out.println("");
                printFlightScheduleInformation(destinationAirport, returnDateTimeInput, departureAirport, sc, port);
            }
            if (directFlight == 2) {
                // CONNECTING: TO : LEG 1
                System.out.println("CONNECTING TO: LEG 1");
                System.out.println("");
                printFlightScheduleInformationConnecting(departureAirport, startDateTimeInput, destinationAirport, sc, port, true);

                // CONNECTING TO : LEG 2
                System.out.println("CONNECTING TO: LEG 2");
                System.out.println("");
                printFlightScheduleInformationConnecting(departureAirport, startDateTimeInput, destinationAirport, sc, port, false);

                if (!returnDateTimeInput.isEmpty()) {
                    // CONNECTING BACK : LEG 1
                    System.out.println("CONNECTING RETURN: LEG 1");
                    System.out.println("");
                    printFlightScheduleInformationConnecting(destinationAirport, returnDateTimeInput, departureAirport, sc, port, true);

                    // CONNECTING BACK : LEG 2
                    System.out.println("CONNECTING RETURN: LEG 2");
                    System.out.println("");
                    printFlightScheduleInformationConnecting(destinationAirport, returnDateTimeInput, departureAirport, sc, port, false);
            }
            }
        


        
            
    }
    
    public static void printFlightScheduleInformationConnecting(String departureAirport, String startDateTimeInput, String destinationAirport, Scanner sc, FlightReservationSystemWebService port, boolean isFirst) {
        List<FlightSchedule> flightScheduleList = port.partnerSearchConnectingFlightLeg1(departureAirport, startDateTimeInput, destinationAirport, isFirst);
        // print flight cabin class fares
            HashMap<String, Fare> fareListForEachCabinClass = new HashMap<>();
            if (!flightScheduleList.isEmpty()) {
                // get the first flight schedule plan and cabin class configuration 
                long fspId = flightScheduleList.get(0).getId();
                // get all flight cabin calsses
                fareListForEachCabinClass = getFaresFromBackend(port, fspId);
                
                // allow user to customise their flight cabin class preferences
                if (fareListForEachCabinClass.isEmpty()) {
                    System.out.println("Nothing inside");
                }
                
                // allow user to customise their flight cabin class preferences
                System.out.println("Do you have any cabin class preferences?");
                System.out.println("Press 0 if there isn't");
                System.out.println("Press 1 if there is");
                System.out.print("> ");
                int choice = sc.nextInt();
                
                if (choice == 1) {
                    Pair<String, Fare> chosenFareList = chooseFCC(fareListForEachCabinClass, sc);
                    // print flight information
                    Flight flight = port.retrieveFlights(fspId);
                    System.out.println(flight.getFlightNumber());
                    // print flight route information
                    List<String> flightRouteAirports = port.retrieveFlightRoute(fspId);
                    System.out.println(flightRouteAirports.get(0) + " -> " + flightRouteAirports.get(1));
                    // print single cabin class preference
                    printSingleCabinClassFares(chosenFareList);
                    // print all flight schedules
                    printFlightSchedulesForPreferredCabinClass(flightScheduleList, chosenFareList);
                    
                } else if (choice == 0) {
                    // print all cabin class preferences
                    printNoPreferenceCabinClassFares(fareListForEachCabinClass);
                    // print all flight schedules 
                    printFlightSchedulesForNoPreference(flightScheduleList, fareListForEachCabinClass);
                } else {
                    System.out.println("You have entered an invalid option");
                    System.exit(1);
                }                 
            }
    }
    
    
    
    public static void printFlightScheduleInformation(String departureAirport, String startDateTimeInput, String destinationAirport, Scanner sc, FlightReservationSystemWebService port) {
        List<FlightSchedule> flightScheduleList = port.partnerSearchFlight(departureAirport, startDateTimeInput, destinationAirport);
       
        // print flight cabin class fares
            HashMap<String, Fare> fareListForEachCabinClass = new HashMap<>();
            if (!flightScheduleList.isEmpty()) {
                // get the first flight schedule plan and cabin class configuration 
                long fspId = flightScheduleList.get(0).getId();
                // get all flight cabin calsses
                fareListForEachCabinClass = getFaresFromBackend(port, fspId);
                
                System.out.println("Do you have any cabin class preferences?");
                System.out.println("Press 0 if there isn't");
                System.out.println("Press 1 if there is");
                System.out.print("> ");
                int choice = sc.nextInt();
                
                if (choice == 1) {
                    Pair<String, Fare> chosenFareList = chooseFCC(fareListForEachCabinClass, sc);
                    // print single cabin class preference
                    printSingleCabinClassFares(chosenFareList);
                    // print all flight schedules
                    printFlightSchedulesForPreferredCabinClass(flightScheduleList, chosenFareList);
                    
                } else if (choice == 0) {
                    // print all cabin class preferences
                    printNoPreferenceCabinClassFares(fareListForEachCabinClass);
                    // print all flight schedules 
                    printFlightSchedulesForNoPreference(flightScheduleList, fareListForEachCabinClass);
                } else {
                    System.out.println("You have entered an invalid option");
                    System.exit(1);
                }                 
            }
    }
    
    public static void printFlightSchedulesForPreferredCabinClass(List<FlightSchedule> flightScheduleList, Pair<String, Fare> highestFare) {
         flightScheduleList.stream().forEach(x -> {
               System.out.println("Departure Time: " + x.getDepartureTime());
               System.out.println("Arrival Time: " + x.getArrivalTime());
               System.out.println("Flight Duration: " + x.getFlightDuration().toString());
               System.out.println("Cabin Class Name: " + highestFare.getKey());
               System.out.println("Price Per Passenger: " + highestFare.getValue().getFareAmount().doubleValue());
               System.out.println();
            });
    }
    
    public static void printFlightSchedulesForNoPreference(List<FlightSchedule> flightScheduleList, HashMap<String, Fare> fareListForEachCabinClass) {
         flightScheduleList.stream().forEach(x -> {
               System.out.println("Departure Time: " + x.getDepartureTime());
               System.out.println("Arrival Time: " + x.getArrivalTime());
               System.out.println("Flight Duration: " + x.getFlightDuration().toString());
               for (String key : fareListForEachCabinClass.keySet()) {
                   System.out.println("Cabin Class Name: " + key);
                   System.out.println("Price Per Passenger: " + fareListForEachCabinClass.get(key).getFareAmount().doubleValue());
               }
               System.out.println();
            });
    }
    
    
    public static HashMap<String, Fare> getFaresFromBackend(FlightReservationSystemWebService port, long fspId) {
        HashMap<String, Fare> fareListForEachCabinClass = new HashMap<>();
        String [] arr = {"F", "Y", "J", "W"};
        
        for (int i = 0; i < arr.length; i++) {
            Fare highestFare  = port.retrieveFaresForFlightSchedule(fspId, arr[i]);
            if (highestFare != null) {
                fareListForEachCabinClass.put(arr[i], highestFare);
            }
        }
        return fareListForEachCabinClass;
    }
    
    public static void printSingleCabinClassFares(Pair<String, Fare> cabinClassDetails) {
        System.out.println("Fare Information");
        System.out.println();
        System.out.println(cabinClassDetails.getKey() + " : " + cabinClassDetails.getValue().getFareAmount().doubleValue());
    }
    
    public static void printNoPreferenceCabinClassFares(HashMap<String, Fare> cabinClassDetails) {
        System.out.println("Fare Information");
        
        for (String key : cabinClassDetails.keySet()) {
            System.out.println("");
            Fare highestFare = cabinClassDetails.get(key);
            System.out.println(key + " : " + highestFare.getFareAmount().doubleValue());
        }
    }
    
    
    public static Pair<String, Fare> chooseFCC(HashMap<String, Fare> fareForEachCabinClass, Scanner scanner) {
        int counter = 1;
        Set<String> cabinClassNames = fareForEachCabinClass.keySet();
        
        HashMap<Integer, String> mapToChoose = new HashMap<>();
        
        for (String cabinClassName : cabinClassNames) {
            Fare fareForCabinClass = fareForEachCabinClass.get(cabinClassName);
            if (fareForCabinClass != null) {
                // cabin class exist
                System.out.println("Cabin Class Type: " + cabinClassName);
                System.out.println("Press " + counter + " to choose this cabin class");
                System.out.println("");
                mapToChoose.put(counter, cabinClassName);
                counter += 1;
            }
        }
        System.out.println("Enter Your Option:");
        System.out.print("> ");
        String chosenCabinClassName = mapToChoose.get(scanner.nextInt());
        System.out.println(fareForEachCabinClass.get(chosenCabinClassName));
        return new Pair<String, Fare>(chosenCabinClassName, fareForEachCabinClass.get(chosenCabinClassName));
    }
    
    
    public static List<HashMap<Integer, String>> enterPassengerDetails(int numP, Scanner scanner) {
        List<HashMap<Integer, String>> allDetails = new ArrayList<HashMap<Integer, String>>();
        for (int i = 0; i < numP; i++) {
            HashMap<Integer, String> details = new HashMap<Integer, String>();
            System.out.println("Passenger Details #" + (i + 1));
            System.out.println("Enter Passenger First Name");
            System.out.print("> ");
            String firstName = scanner.next();
            details.put(1, firstName);
            scanner.nextLine();
            
            System.out.println("Enter Passenger Last Name");
            System.out.print("> ");
            String lastName = scanner.next();
            details.put(2, lastName);
            scanner.nextLine();
            
            System.out.println("Enter Passenger Passport Number");
            System.out.print("> ");
            String passportNumber = scanner.next();
            details.put(3, passportNumber);
            scanner.nextLine();
            
            allDetails.add(details);
        }
        return allDetails;
    }
    
    
    public static double printDirectFlightsTo(List<FlightSchedule> fsList, String start, String end, int numPassengers) {
        System.out.println();
        System.out.println("Direct Flights: " + start + " -> " + end);
        System.out.println();
        double totalCostForThisSingleFlight = 0.0;
        int counter = 1;
        for (FlightSchedule fs : fsList) {
            System.out.println("");
            System.out.println("Option #" + counter);
            System.out.println("Departure Time " + fs.getDepartureTime());
            System.out.println("Arrival Time " + fs.getArrivalTime());
//            System.out.println("Duration " + formatDuration(fs.getFlightDuration()));
            // display cabin class availability
            System.out.println("");
            System.out.println("Cabin Class Availability");
            System.out.println("Cabin Class Options: " + fs.getFccList().size());
            System.out.println("");
            boolean isAdded = false;
            for (FlightCabinClass fcc : fs.getFccList()) {
//                double pricePerPassengerPerCCPerFS = printFlightCabinClass(fcc, numPassengers, false);
                if (!isAdded) {
                    // totalCostForThisSingleFlight += pricePerPassengerPerCCPerFS;
                    isAdded = true;
                }
            }
            counter += 1;
        }
        return totalCostForThisSingleFlight;
    }
    
        public static double printConnectingFlightsSingle(List<FlightSchedule> fsList, int numPassengers) {
        System.out.println();
        int counter = 1;
        double totalCostForThisSingleFlight = 0.0;
        for (FlightSchedule fs : fsList) {
            System.out.println("");
            System.out.println("Connecting Flight Information: " + fs.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getIataAirportCode() + " -> " + fs.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getIataAirportCode());
            System.out.println("Option #" + counter);
            System.out.println("Departure Time " + fs.getDepartureTime());
            System.out.println("Arrival Time " + fs.getArrivalTime());
//            System.out.println("Duration " + formatDuration(fs.getFlightDuration()));
            // display cabin class availability
            System.out.println("");
            System.out.println("Cabin Class Availability");
            System.out.println("Cabin Class Options: " + fs.getFccList().size());
//            System.out.println("");
            boolean isAdded = false;
            for (FlightCabinClass fcc : fs.getFccList()) {
                // double returnVal = printFlightCabinClass(fcc, numPassengers, true);
                if (!isAdded) {
                  //  totalCostForThisSingleFlight += returnVal;
                    isAdded = true;
                }
            }
            counter += 1;
        }
        
        return totalCostForThisSingleFlight;
    }
        
    
    public static String formatDuration(Duration duration) {
        long totalMinutes = duration.toMinutes();
        long seconds = duration.minusMinutes(totalMinutes).getSeconds();

        String formattedDuration = String.format("%d:%02d", totalMinutes, seconds);
        
        return formattedDuration;
    }
    
    

    
}
