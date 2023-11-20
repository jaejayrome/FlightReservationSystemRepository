/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hrspartnerclient;

import hrspartnerclient.exception.NoFlightScheduleResultException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.util.Pair;
import ws.entity.CabinClass;
import ws.entity.CabinClassType;
import ws.entity.Fare;
import ws.entity.Flight;
import ws.entity.FlightBooking;
import ws.entity.FlightCabinClass;
import ws.entity.FlightReservation;
import ws.entity.FlightReservationSystemWebService;
import ws.entity.FlightReservationSystemWebService_Service;
import ws.entity.FlightRoute;
import ws.entity.FlightSchedule;
import ws.entity.Passenger;
import ws.entity.Seat;
import ws.entity.SeatStatus;


/**
 *
 * @author jeromegoh
 */
public class HRSPartnerClient {
   
    

  private static HashMap<Integer, CabinClassType> hashMap;
  private static long sessionId;
  
  
  public HRSPartnerClient() {
      
  }
    
    
    public static void showMenuOptions(Scanner sc, FlightReservationSystemWebService port) {
        System.out.println("                                 .''.");
        System.out.println("       .''.             *''*    :_\\/_:     . ");
        System.out.println("      :_\\/_:   .    .:.*_\\/_*   : /\\ :  .'.:.'.");
        System.out.println("  .''.: /\\ : _\\(/_  ':'* /\\ *  : '..'.  -=:o:=-");
        System.out.println(" :_\\/_:'.:::. /)\\*''*  .|.* '.\\'/.'_\\(/_'.':'.");
        System.out.println(" : /\\ : :::::  '*_\\/_* | |  -= o =- /)\\    '  *");
        System.out.println("  '..'  ':::'   * /\\ * |'|  .'/.'\\'.  '._____.");
        System.out.println("      *        __*..* |  |     :      |.   |' .---\"|");
        System.out.println("       _*   .-'   '-. |  |     .--'|  ||   | _|    |");
        System.out.println("    .-'|  _.|  |    ||   '-__  |   |  |    ||      |");
        System.out.println("    |' | |.    |    ||       | |   |  |    ||      |");
        System.out.println(" ___|  '-'     '    \"\"       '-'   '-.'    '`      |____");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
        System.out.println("                      _ _                     _      _ _                                    ");
        System.out.println("  _ __ ___   ___ _ __| (_) ___  _ __     __ _(_)_ __| (_)_ __   ___  ___                    ");
        System.out.println(" | '_ ` _ \\ / _ \\ '__| | |/ _ \\| '_ \\   / _` | | '__| | | '_ \\ / _ \\/ __|                   ");
        System.out.println(" | | | | | |  __/ |  | | | (_) | | | | | (_| | | |  | | | | | |  __/\\__ \\                   ");
        System.out.println(" |_| |_| |_|\\___|_|  |_|_|\\___/|_| |_|  \\__,_|_|_|  |_|_|_| |_|\\___||___/_   _              ");
        System.out.println(" | |__   ___ | (_) __| | __ _ _   _   _ __ ___  ___  ___ _ ____   ____ _| |_(_) ___  _ __   ");
        System.out.println(" | '_ \\ / _ \\| | |/ _` |/ _` | | | | | '__/ _ \\/ __|/ _ \\ '__\\ \\ / / _` | __| |/ _ \\| '_ \\  ");
        System.out.println(" | | | | (_) | | | (_| | (_| | |_| | | | |  __/\\__ \\  __/ |   \\ V / (_| | |_| | (_) | | | | ");
        System.out.println(" |_| |_|\\___/|_|_|\\__,_|\\__, | |_|  |_|_|  \\___||___/\\___|_|    \\_/ \\__,_|\\__|_|\\___/|_| |_| ");
        System.out.println("  ___ _   _ ___| |_ ___ _ __ _|___/                                                        ");
        System.out.println(" / __| | | / __| __/ _ \\ '_ ` _ \\                                                          ");
        System.out.println(" \\__ \\ |_| \\__ \\ ||  __/ | | | | |                                                         ");
        System.out.println(" |___/\\__, |___/\\__\\___|_| |_| |_|                                                         ");
        System.out.println("      |___/                                                                                ");
        
        IntStream.rangeClosed(1, 5).forEach(x -> System.out.println());
        System.out.println("Welcome to Merlion Airways (Partner) Holiday Reservation System");
        System.out.println("Press '0' to exit");
        System.out.println("Press '1' to Login to an existing account");
        System.out.print("> ");
        int option = sc.nextInt();
        sc.nextLine();
        switch(option) {
            case 0:
                System.exit(1);
                break;
            case 1:
                System.out.println();
                System.out.println("Enter Your Username");
                System.out.print("> ");
                String loginEmail = sc.nextLine();
                System.out.println("Enter Your Password");
                System.out.print("> ");
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
        IntStream.rangeClosed(1, 1).forEach(x -> System.out.println());
        System.out.println(IntStream.range(0, 20).mapToObj(i -> "-").collect(Collectors.joining()));
        System.out.println("Press '0' to logout");
        System.out.println("Press '1' to Search Flights and Reserve Flights");
        System.out.println("Press '2' to View Partner Flight Reservations");
        System.out.println("Press '3' to View Partner Flight Reservation Details");
        System.out.println(IntStream.range(0, 20).mapToObj(i -> "-").collect(Collectors.joining()));
        System.out.print("> ");
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
                viewFlightReservations(port);
                doMainMenu(sc, port);
                break;
            case 3: 
                viewFlightReservationDetails(port, sc);
                doMainMenu(sc, port);
                break;
        }
    }
    
    public static void doLogout(FlightReservationSystemWebService port, long partnerId) {
        port.partnerLogout(partnerId);
        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
        System.out.println("Partner has successfully logged out!");
    }
    
    public static long doLogin(String username, String password, FlightReservationSystemWebService port) {
        long partnerId = port.partnerLogin(username, password);
        return partnerId;
    }
    
    public static void viewFlightReservations(FlightReservationSystemWebService port) {
        List<FlightReservation> frlist = port.viewFlightResevations(sessionId);
        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
        if (frlist.isEmpty()) {
            System.out.println("There are currently no transactions ongoing!");
        }
        
        for (FlightReservation fr : frlist) {
            System.out.println("Credit Card Number Paid " + fr.getCreditCardNumber());
            System.out.println("Date of Transaction " + fr.getDate());
        }
    }
    
    public static void viewFlightReservationDetails(FlightReservationSystemWebService port, Scanner scanner) {
        List<FlightReservation> frlist = port.viewFlightResevations(sessionId);
        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
        if (frlist.isEmpty()) {
            System.out.println("No flight reservations found.");
            return;
        }

        // Display available flight reservations
        System.out.println("Available Flight Reservations:");
        for (int i = 0; i < frlist.size(); i++) {
            System.out.println((i + 1) + ". " + frlist.get(i).getDate());
        }

        // Prompt the user to choose a flight reservation
        System.out.print("Enter the number corresponding to the desired flight reservation: ");
        int selectedReservationIndex = scanner.nextInt();

        // Ensure the selected index is valid
        if (selectedReservationIndex < 1 || selectedReservationIndex > frlist.size()) {
            System.out.println("Invalid selection. Exiting...");
            return;
        }

        long selectedReservationId = frlist.get(selectedReservationIndex - 1).getId();
        
        
        
        List<FlightBooking> flightBookings = port.getFlightBookingsForReservation(selectedReservationId);
        
        System.out.println("Details for Flight Reservation " + selectedReservationId + ":");
        System.out.println("Associated Flight Bookings:");
        System.out.println("Number of Flight Legs: " + flightBookings.size());
        
        double totalCostPerPassenger = 0;
        int numPassengers = 0;
        int counter = 1;
        for (FlightBooking flightBooking : flightBookings) {
            List<Seat> passengersInvolved = port.getFlightBooking(flightBooking.getId());
            // sort according to the ascending order
            passengersInvolved.sort((x, y) -> x.getSeatNumber().compareTo(y.getSeatNumber()));
            FlightCabinClass fcc = port.getFlightCabinClass(passengersInvolved.get(0).getId());
            numPassengers = passengersInvolved.size(); 
            System.out.println("Flight Itenerary Number #" + counter);
            System.out.println("Flight Booking ID: " + flightBooking.getId());
            System.out.println("Flight Number: " + flightBooking.getFlightNumber());
            System.out.println("Flight Leg Cost: " + flightBooking.getFlightLegCost().doubleValue());
            
            for (Seat s : passengersInvolved) {
                IntStream.rangeClosed(1, 1).forEach(x -> System.out.println());
                System.out.println(IntStream.range(0, 20).mapToObj(i -> "-").collect(Collectors.joining()));
                System.out.println("Seat Number: " + s.getSeatNumber());
                Passenger passengerThisSeat = port.getSeatForBooking(s.getId());
                System.out.println("Passenger First Name: " + passengerThisSeat.getFirstName());
                System.out.println("Passenger Last Name: " + passengerThisSeat.getLastName());
                System.out.println("Passenger Passport Number: " + passengerThisSeat.getPassportNumber());
                System.out.println(IntStream.range(0, 20).mapToObj(i -> "-").collect(Collectors.joining()));
                IntStream.rangeClosed(1, 1).forEach(x -> System.out.println());
            }
            totalCostPerPassenger += flightBooking.getFlightLegCost().doubleValue();
            counter += 1;
        }
        
        System.out.println("Total Flight Fare Information");
        System.out.println("Total Amount Paid: $" + (totalCostPerPassenger * numPassengers));
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
        boolean needReturn = false;

        String startDateTimeInput = "";
        String returnDateTimeInput = "";

        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
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
            } catch (Exception e) {                    
                System.out.println("Invalid Date format. Please try again");
            }

        if (roundTrip == 2) {
            // roundtrip
            needReturn = true;
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
            } catch (Exception e) {
                System.out.println("Invalid Date format. Please try again");
            }
        }
        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
        System.out.println("FLIGHT TYPE PREFERENCE \nPress 1 for \u001B[1mDirect Flights Only\u001B[0m\nPress 2 For \u001B[1mNo Preference\u001B[0m");
        System.out.print("> ");
        directFlight = sc.nextInt(); 
        sc.nextLine();
        
        // ask for cabin class preference
        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
        System.out.println("\u001B[1mCABIN CLASS PREFERENCE\u001B[0m");
        System.out.println("Press 0 if you have \u001B[1mno preference\u001B[0m");
        System.out.println("Press 1 if you want to \u001B[1mmake a preference\u001B[0m");
        System.out.print("> ");
        int ccPreference = sc.nextInt();
        sc.nextLine();
        CabinClassType chosenType = null;
        boolean haveCCPreference = ccPreference == 1;
        
        if (haveCCPreference) {
            printAllCabinClassTypes();
            System.out.print("> ");
            int choice = sc.nextInt();
            sc.nextLine();
           
            switch (choice) {
                case 1: 
                    chosenType = CabinClassType.F;
                    break;
                case 2:
                    chosenType = CabinClassType.J;
                    break;
                case 3:
                    chosenType = CabinClassType.W;
                    break;
                case 4: 
                    chosenType = CabinClassType.Y;
                    break;
            }
        } 
        
            List<List<FlightSchedule>> masterList = new ArrayList<>();
            // DIRECT: TO
            System.out.println("\u001B[1mDIRECT: TO\u001B[0m");
            System.out.println("");
            List<FlightSchedule> list1  =  printFlightScheduleInformation(departureAirport, startDateTimeInput, destinationAirport, sc, port, chosenType);
            if (!list1.isEmpty()) masterList.add(list1);
            if (list1.isEmpty()) System.out.println("sorry, no flight schedules have been found");
            
            // DIRECT: RETURN
            if (!returnDateTimeInput.isEmpty()) {
                System.out.println("\u001B[1mDIRECT: TO\u001B[0m");
                System.out.println("");
                List<FlightSchedule> list2  = printFlightScheduleInformation(destinationAirport, returnDateTimeInput, departureAirport, sc, port, chosenType);
                if (!list2.isEmpty()) masterList.add(list2);
                if (list2.isEmpty()) System.out.println("sorry, no flight schedules have been found");
            }
            if (directFlight == 2) {
                // CONNECTING: TO : LEG 1
                System.out.println("\u001B[1mCONNECTING TO: LEG 1\u001B[0m");
                System.out.println("");
                List<FlightSchedule> list3  = printFlightScheduleInformationConnecting(departureAirport, startDateTimeInput, destinationAirport, sc, port, true, chosenType);
                if (!list3.isEmpty()) masterList.add(list3);
                if (list3.isEmpty()) System.out.println("sorry, no flight schedules have been found");

                // CONNECTING TO : LEG 2
                System.out.println("\u001B[1mCONNECTING TO: LEG 2\u001B[0m");
                System.out.println("");
                List<FlightSchedule> list4  = printFlightScheduleInformationConnecting(departureAirport, startDateTimeInput, destinationAirport, sc, port, false, chosenType);
                if (!list4.isEmpty()) masterList.add(list4);
                if (list3.isEmpty()) System.out.println("sorry, no flight schedules have been found");

                if (!returnDateTimeInput.isEmpty()) {
                    // CONNECTING BACK : LEG 1
                    System.out.println("\u001B[1mCONNECTING RETURN: LEG 1\u001B[0m");
                    System.out.println("");
                    List<FlightSchedule> list5 = printFlightScheduleInformationConnecting(destinationAirport, returnDateTimeInput, departureAirport, sc, port, true, chosenType);
                    if (!list5.isEmpty()) masterList.add(list5);
                    if (list5.isEmpty()) System.out.println("sorry, no flight schedules have been found");

                    // CONNECTING BACK : LEG 2
                    System.out.println("\u001B[1mCONNECTING RETURN: LEG 2\u001B[0m");
                    System.out.println("");
                    List<FlightSchedule> list6 = printFlightScheduleInformationConnecting(destinationAirport, returnDateTimeInput, departureAirport, sc, port, false, chosenType);
                    if (!list6.isEmpty()) masterList.add(list6);
                    if (list6.isEmpty()) System.out.println("sorry, no flight schedules have been found");
                }
            }
            
            // at least one outcome would have 1 flight
            if (masterList.size() >= 1) {
                IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
                System.out.println("Would you like to proceed to make reservations?");
                System.out.println("Press 0 to head back to the main menu");
                System.out.println("Press 1 to proceed");
                System.out.print("> ");
                int choiceOne = sc.nextInt();
                if (choiceOne == 1) {
                
                List<Long> finalFlightScheduleIdList = new ArrayList<Long>();
                    List<String> finalFlightCabinClassTypeList = new ArrayList<String>();
                    List<List<String>> finalSeatsChoice = new ArrayList<List<String>>();
                    List<Double> ticketPricesForEachFlightSchedulePerPerson = new ArrayList<Double>();
                    // register the passengers
                    System.out.println("Step 0: Input Passenger Details");
                    System.out.println("");
                    // return this too
                    List<HashMap<Integer, String>> allPDetails = enterPassengerDetails(numPassengers, sc);
                    List<List<String>> allPDetailsList = new ArrayList<>();

                    // Convert List<HashMap<Integer, String>> to List<List<String>>
                    for (HashMap<Integer, String> map : allPDetails) {
                        List<String> detailsList = new ArrayList<>();
                        detailsList.add(map.get(0));
                        detailsList.add(map.get(1));
                        detailsList.add(map.get(2));
                        allPDetailsList.add(detailsList);
                    }

                    
                    boolean connectingFound = (!needReturn) ? (masterList.size() == 4) :  (masterList.size() == 6);
                    double onePassengerFareDirect = 0.0;
                    double onePassengerFareConnecting = 0.0;
                    int choice2 = -1;
                    if (connectingFound) {
                        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
                        System.out.println("\u001B[1mFLIGHT TYPE PREFERENCE\u001B[0m");
                        System.out.println("Press 0 to choose \u001B[1mConnecting Flights\u001B[0m");
                        System.out.println("Press 1 to choose \u001B[1mDirect Flights\u001B[0m");
                        System.out.print("> ");
                        choice2 = sc.nextInt();
                        if (choice2 == 1) {
                             // means direct flight is chosen
                            double directToOnePassengerFare = enterDetailsForDirectTo(0, 0, false, false, masterList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassTypeList, finalSeatsChoice, allPDetails, chosenType, port);
                            onePassengerFareDirect += directToOnePassengerFare;
                            ticketPricesForEachFlightSchedulePerPerson.add(directToOnePassengerFare);
                            // same procedure for return flight 
                            if (needReturn) {
                                double directReturnOnePassengerFare = enterDetailsForDirectTo(0, 1, true, false, masterList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassTypeList, finalSeatsChoice, allPDetails, chosenType, port);
                                onePassengerFareDirect += directReturnOnePassengerFare;
                                ticketPricesForEachFlightSchedulePerPerson.add(directReturnOnePassengerFare);
                            }
                            
                        } else {
                            // means connecting flights is chosen
                            // connecting flight first leg
                             double connectingTo1OnePerson = enterDetailsForDirectTo(1, 2, false, true, masterList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassTypeList, finalSeatsChoice, allPDetails, chosenType, port);
                             onePassengerFareConnecting += connectingTo1OnePerson;
                             ticketPricesForEachFlightSchedulePerPerson.add(connectingTo1OnePerson);
                             double connectingTo2OnePerson = enterDetailsForDirectTo(2, 3, false, true, masterList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassTypeList, finalSeatsChoice, allPDetails, chosenType, port);
                             onePassengerFareConnecting += connectingTo2OnePerson;
                             ticketPricesForEachFlightSchedulePerPerson.add(connectingTo2OnePerson);
                            
                             if (needReturn) {
                                 double connectingReturn1OnePerson =  enterDetailsForDirectTo(1, 4, true, true, masterList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassTypeList, finalSeatsChoice, allPDetails, chosenType, port);
                                onePassengerFareConnecting += connectingReturn1OnePerson;
                                ticketPricesForEachFlightSchedulePerPerson.add(connectingReturn1OnePerson);
                                double connectingReturn2OnePerson = enterDetailsForDirectTo(2, 5, true, true, masterList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassTypeList, finalSeatsChoice, allPDetails, chosenType, port);
                                onePassengerFareConnecting += connectingReturn2OnePerson;
                                ticketPricesForEachFlightSchedulePerPerson.add(connectingReturn2OnePerson);
                             }
                        }
                    } else {
                        double directToOnePassengerFare = enterDetailsForDirectTo(0, 0, false, false, masterList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassTypeList, finalSeatsChoice, allPDetails, chosenType, port);
                        onePassengerFareDirect += directToOnePassengerFare;
                        ticketPricesForEachFlightSchedulePerPerson.add(directToOnePassengerFare);
                        if (needReturn) {
                             double directReturnOnePassengerFare =  enterDetailsForDirectTo(0, 1, true, false, masterList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassTypeList, finalSeatsChoice, allPDetails, chosenType, port);
                             onePassengerFareDirect += directReturnOnePassengerFare;
                             ticketPricesForEachFlightSchedulePerPerson.add(directReturnOnePassengerFare);
                        }
                    }
                        
                        double totalCostPerPerson = ticketPricesForEachFlightSchedulePerPerson.stream().reduce(0.0, (x, y) -> x + y);
                        System.out.println();
                        System.out.println("\u001B[1mCHECKOUT PROCEDURE\u001B[0m");

                        System.out.println("Total Cost for Your Trip would be: $\u001B[1m" + (totalCostPerPerson * numPassengers) + "\u001B[0m");
                        
                        System.out.println("Please Enter Your Credit Card Number");
                        System.out.print("> ");
                        String creditCard = sc.next();
                        sc.nextLine();
                        
                        List<Passenger> passengerList = new ArrayList<Passenger>();
                        // persist passenger
                        for (List<String> map : allPDetailsList) {
                            Passenger p = port.persistPassengers(map);
                            passengerList.add(p);
                        }
                        
                        // make flight bookings 
                        List<FlightBooking> flightBookingList = new ArrayList<FlightBooking>();
                        for (int i = 0; i < finalFlightScheduleIdList.size(); i++) {
                            // do the booking here
                            long fsID = finalFlightScheduleIdList.get(i);
                            String ccName = finalFlightCabinClassTypeList.get(i);
                            List<String> reservedSeats = finalSeatsChoice.get(i);
                            double cost = ticketPricesForEachFlightSchedulePerPerson.get(i);
                            // need the cost of this
                            flightBookingList.add(port.makeFlightBooking(fsID, ccName, reservedSeats, cost, passengerList));
                        }
                        
                        // make flight reservation
                        port.makeFlightReservation(partnerid, finalFlightScheduleIdList.get(0), passengerList, creditCard, flightBookingList);
                        
                    
                    
                } 
            }
                    
    }
    
    public static double enterDetailsForDirectTo(int leg, int choice, boolean isReturn, boolean isConnecting, List<List<FlightSchedule>> flightScheduleList, String departureAirport, String destinationAirport, int numPassengers, Scanner sc, 
        List<Long> finalFlightScheduleIdList, List<String> finalFlightCabinClassTypeList, List<List<String>> finalSeatsChoice, 
        List<HashMap<Integer, String>> passengerDetails, CabinClassType cabinClassPreference,
        FlightReservationSystemWebService port) {
        // make 
        IntStream.rangeClosed(1, 10).boxed().forEach(x -> System.out.println());
        String title = isReturn ? "RETURN" : "TO";
        int step = isReturn ? 2 : 1;
        String title2 = isConnecting ? "CONNECTING" : "DIRECT";
        System.out.println("STEP " + step + "a: SELECT " + title2 + " FLIGHT -> " + title);
        if (leg != 0) System.out.println("Connecting Flight Leg " + leg);
        
        HashMap<String, Fare> fareListForEachCabinClass = new HashMap<>();
        switch (choice) {
            case 0: 
                // direct flight TO
                fareListForEachCabinClass = printFlightInformation(true, flightScheduleList.get(0), departureAirport, destinationAirport, numPassengers, cabinClassPreference, port);
                break;
            case 1: 
                // direct flight BACK
                fareListForEachCabinClass = printFlightInformation(true, flightScheduleList.get(1), destinationAirport, departureAirport, numPassengers, cabinClassPreference, port);
                break;
            case 2: 
                // connecting TO : leg 1
                fareListForEachCabinClass = printFlightInformation(false, flightScheduleList.get(2), departureAirport, destinationAirport, numPassengers, cabinClassPreference, port);
                break;
            case 3:
                // connecting TO : leg 2
                fareListForEachCabinClass = printFlightInformation(false, flightScheduleList.get(3), departureAirport, destinationAirport, numPassengers, cabinClassPreference, port);
                break;
            case 4: 
                // connecting RETURN : leg 1
                fareListForEachCabinClass = printFlightInformation(false, flightScheduleList.get(4), destinationAirport, departureAirport, numPassengers, cabinClassPreference, port);
                break;
            case 5:
                // connecting RETURN : leg 2
                fareListForEachCabinClass = printFlightInformation(false, flightScheduleList.get(5), destinationAirport, departureAirport, numPassengers, cabinClassPreference, port);
                break;
        }
        
        System.out.println();
        System.out.println("Enter the option number to indicate the flight schedule that you intend to select");
        System.out.print("> ");
        FlightSchedule flightSchedule = flightScheduleList.get(choice).get(sc.nextInt() -1);
        long chosenFlightScheduleId = flightSchedule.getId();
        sc.nextLine();
        finalFlightScheduleIdList.add(chosenFlightScheduleId);
        
        // fare should be computed here 
        System.out.println();
        System.out.println("STEP 1b: SELECT CABIN CLASS");
        System.out.println("");
        Pair<String, Fare> chosenFCC = chooseFCC(fareListForEachCabinClass, sc);
        finalFlightCabinClassTypeList.add(chosenFCC.getKey());

        // this would be the total amount of money for one leg of the flight itnerary
        double amountForOnePassenger = chosenFCC.getValue().getFareAmount().doubleValue();
        System.out.println(chosenFCC.getKey());
        System.out.println();
        System.out.println("STEP 1c: SELECT SEAT FOR PASSENGERS");
        List<String> reservedSeats = printSeatLayout(chosenFlightScheduleId, chosenFCC.getKey(), numPassengers, sc, passengerDetails, port);
        finalSeatsChoice.add(reservedSeats);

        return amountForOnePassenger;
    }
    
    public static HashMap<String, Fare> printFlightInformation(boolean isDirect, List<FlightSchedule> fsList, String start, String end, int numPassengers, CabinClassType preference,
            FlightReservationSystemWebService port) {
        System.out.println();
        String header = isDirect ? "DIRECT" : "CONNECTING";
        System.out.println(header);
        double totalCostForThisSingleFlight = 0.0;
        int counter = 1;
        
        if (fsList.isEmpty()) {
            System.out.println();
            System.out.println("NO FLIGHT SCHEDULES FOUND");

        }
        
        // print all fare information
        HashMap<String, Fare> fareListForEachCabinClass = new HashMap<>();
        
        for (FlightSchedule fs : fsList) {
            System.out.println("");
            System.out.println("Option #" + counter);
            
            // print flight information
            Flight flight = port.retrieveFlights(fs.getId());
            System.out.println("Flight Number: " + flight.getFlightNumber());
            
            
            long fspId = fs.getId();
            fareListForEachCabinClass = getFaresFromBackend(port, fspId);
            
            if (preference != null) {
                HashMap<String, Fare> newFares = new HashMap<>();
                newFares.put(preference.name(), fareListForEachCabinClass.get(preference.name()));
                fareListForEachCabinClass = newFares;
            }
            
            // print all cabin class preferences
            printNoPreferenceCabinClassFares(fareListForEachCabinClass);

            // print all flight schedules 
            printFlightSchedulesForNoPreference(fsList, fareListForEachCabinClass);
            counter += 1;
        }
        
        return fareListForEachCabinClass;
        
    }
    
    
    
    public static List<FlightSchedule> printFlightScheduleInformationConnecting(String departureAirport, String startDateTimeInput, String destinationAirport, Scanner sc, FlightReservationSystemWebService port, boolean isFirst, CabinClassType chosenPreference) 
    {
        List<FlightSchedule> flightScheduleList = port.partnerSearchConnectingFlightLeg1(departureAirport, startDateTimeInput, destinationAirport, isFirst);
        // print flight cabin class fares
        HashMap<String, Fare> fareListForEachCabinClass = new HashMap<>();
        if (!flightScheduleList.isEmpty()) {
            // get the first flight schedule plan and cabin class configuration 
            long fspId = flightScheduleList.get(0).getId();
            // get all flight cabin classes
            fareListForEachCabinClass = getFaresFromBackend(port, fspId);


            if (chosenPreference != null) {
                HashMap<String, Fare> newFares = new HashMap<>();
                newFares.put(chosenPreference.name(), fareListForEachCabinClass.get(chosenPreference.name()));
                fareListForEachCabinClass = newFares;
            }

            // print flight information
            Flight flight = port.retrieveFlights(fspId);
            System.out.println(flight.getFlightNumber());

            // print all cabin class preferences
            printNoPreferenceCabinClassFares(fareListForEachCabinClass);

            // print all flight schedules 
            printFlightSchedulesForNoPreference(flightScheduleList, fareListForEachCabinClass);
        }
        return flightScheduleList;
    }
    
    
    
    public static List<FlightSchedule> printFlightScheduleInformation(String departureAirport, String startDateTimeInput, String destinationAirport, Scanner sc, FlightReservationSystemWebService port, CabinClassType chosenPreference) {
        List<FlightSchedule> flightScheduleList = port.partnerSearchFlight(departureAirport, startDateTimeInput, destinationAirport);

        if (!flightScheduleList.isEmpty()) {
            // get the first flight schedule plan and cabin class configuration 
            long fspId = flightScheduleList.get(0).getId();

            // get all flight cabin classes
            HashMap<String, Fare> fareListForEachCabinClass = getFaresFromBackend(port, fspId);

            if (chosenPreference != null && fareListForEachCabinClass.containsKey(chosenPreference.name())) {
                // If the chosen preference is valid, keep only that cabin class in the map
                fareListForEachCabinClass = new HashMap<>();
                fareListForEachCabinClass.put(chosenPreference.name(), getFaresFromBackend(port, fspId).get(chosenPreference.name()));
            }

            // print flight information
            Flight flight = port.retrieveFlights(fspId);
            System.out.println(flight.getFlightNumber());

            // print all cabin class preferences
            printNoPreferenceCabinClassFares(fareListForEachCabinClass);

            // print all flight schedules 
            printFlightSchedulesForNoPreference(flightScheduleList, fareListForEachCabinClass);
        }

        return flightScheduleList;
    }

    
   public static void printFlightSchedulesForNoPreference(List<FlightSchedule> flightScheduleList, HashMap<String, Fare> fareListForEachCabinClass) {
        flightScheduleList.forEach(schedule -> {
            ZonedDateTime departureTime = schedule.getDepartureTime().toGregorianCalendar().toZonedDateTime();
            ZonedDateTime arrivalTime = schedule.getArrivalTime().toGregorianCalendar().toZonedDateTime();

            // Format departure and arrival times
            String formattedDepartureTime = departureTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String formattedArrivalTime = arrivalTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Calculate duration
            Duration duration = Duration.between(departureTime, arrivalTime);

            System.out.println("Departure Time: " + formattedDepartureTime);
            System.out.println("Arrival Time: " + formattedArrivalTime);
            System.out.println("Flight Duration: " + formatDuration(duration));

            // Print fare information for each cabin class
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
    
    public static void printNoPreferenceCabinClassFares(HashMap<String, Fare> cabinClassDetails) {
        System.out.println("Fare Information");
        
        for (String key : cabinClassDetails.keySet()) {
            System.out.println("");
            Fare highestFare = cabinClassDetails.get(key);
            System.out.println(key + " : \u001B[1m" + highestFare.getFareAmount().doubleValue() + "\u001B[0m");
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
        return new Pair<String, Fare>(chosenCabinClassName, fareForEachCabinClass.get(chosenCabinClassName));
    }
    
    
    public static List<HashMap<Integer, String>> enterPassengerDetails(int numP, Scanner scanner) {
        List<HashMap<Integer, String>> allDetails = new ArrayList<HashMap<Integer, String>>();
        for (int i = 0; i < numP; i++) {
            HashMap<Integer, String> details = new HashMap<Integer, String>();
            System.out.println();
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
        System.out.println("Direct Flights: \u001B[1m" + start + "\u001B[0m -> \u001B[1m" + end + "\u001B[0m");
        System.out.println();
        double totalCostForThisSingleFlight = 0.0;
        int counter = 1;
        for (FlightSchedule fs : fsList) {
            System.out.println("");
            System.out.println("\u001B[1mOption #" + counter + "\u001B[0m");
            System.out.println("Departure Time " + fs.getDepartureTime());
            System.out.println("Arrival Time " + fs.getArrivalTime());
            System.out.println("");
            System.out.println("Cabin Class Availability");
            System.out.println("Cabin Class Options: " + fs.getFccList().size());
            System.out.println("");
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
            System.out.println("Connecting Flight Information: " + 
            "\u001B[1m" + fs.getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getIataAirportCode() + "\u001B[0m" +
            " -> " +
            "\u001B[1m" + fs.getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getIataAirportCode() + "\u001B[0m");

            System.out.println("\u001B[1mOption #" + counter + "\u001B[0m");
            System.out.println("Departure Time " + fs.getDepartureTime());
            System.out.println("Arrival Time " + fs.getArrivalTime());
            System.out.println("");
            System.out.println("Cabin Class Availability");
            System.out.println("Cabin Class Options: " + fs.getFccList().size());
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
    
    
    public static void printAllCabinClassTypes() {
        System.out.println("Press 1 for First (F) Class");
        System.out.println("Press 2 for Business (J) Class");
        System.out.println("Press 3 for Premium Economy (W) Class");
        System.out.println("Press 4 for Economy (Y) Class");
    }
    
    public static void initialiseMap() {
        hashMap.put(1, CabinClassType.F);
        hashMap.put(2, CabinClassType.J);
        hashMap.put(3, CabinClassType.W);
        hashMap.put(4, CabinClassType.Y);
    }
    
    
    
    public static List<String> printSeatLayout(long fspID, String chosen, int numPassengers, Scanner sc, 
        List<HashMap<Integer, String>> passengerDetails, FlightReservationSystemWebService port) {
        
        // must get a list of seats
        CabinClass chosenCabinClass = port.retrieveCabinClass(fspID, chosen);
       
        int numRows = chosenCabinClass.getNumRows().intValue();
        int numColumns = chosenCabinClass.getNumSeatsAbreast().intValue();
        HashSet<String> reservedSeatsSet = new HashSet<String>();
        // need to initalise this
        List<String> seatChosen = new ArrayList<String>();
        List<Seat> seatList = port.retrieveSeats(fspID, chosen);
        
        // make a seat array
        String[][] seatLayout = new String[numRows][numColumns];
        String convertedConfiguration = convertSeatingConfiguration(chosenCabinClass.getSeatingConfiguration());
        int[] breakpoints = parseSeatingConfiguration(convertedConfiguration);
        HashSet<Integer> breakpointSet = convertToHashSet(breakpoints);
    
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                char rowChar = (char) ('A' + j % (chosenCabinClass.getNumSeatsAbreast().intValue()));
                seatLayout[i][j] = (i + 1) + "" + rowChar;
            }
        }
        
        for (int i = 0; i < seatLayout.length; i++) {
            for (int j = 0; j < seatLayout[i].length; j++) {
                System.out.print("\u001B[32m" + seatLayout[i][j] + "\u001B[0m" + " ");
            }
            System.out.println(); 
        }
        

       // choosing of seats for all passengers
       HashSet<String> chosenSeats = new HashSet<String>();
        for (int i = 0; i < numPassengers; i++) {
            boolean validSeatChosen = false;
            while (!validSeatChosen) {
                String passengerName = passengerDetails.get(i).get(1) + " " + passengerDetails.get(i).get(2);
                System.out.println("Choose Seat for Passenger #" + (i+1) + " : " + passengerName);
                System.out.println("Enter Seat Number");
                System.out.print("> ");
                String seatNum = sc.next();
                sc.nextLine();

                // Check if the seat is not already chosen or reserved
                if (!chosenSeats.contains(seatNum) && !reservedSeatsSet.contains(seatNum)) {
                    chosenSeats.add(seatNum);
                    seatChosen.add(seatNum);
                    validSeatChosen = true;
                } else {
                    System.out.println("Seat " + seatNum + " is already chosen or reserved. Please choose another seat.");
                }
            }
        }
        return seatChosen;
    }
    
    public static String convertSeatingConfiguration(String seatingConfiguration) {
        StringBuilder convertedConfiguration = new StringBuilder();

        String[] groups = seatingConfiguration.split("-");
        for (String group : groups) {
            int length = Integer.parseInt(group);
            for (int i = 0; i < length; i++) {
                convertedConfiguration.append("1");
            }
            convertedConfiguration.append('-');
        }

        convertedConfiguration.deleteCharAt(convertedConfiguration.length() - 1);

        return convertedConfiguration.toString();
    }
    public static int[] parseSeatingConfiguration(String seatingConfiguration) {
        List<Integer> xIndices = new ArrayList<>();

        for (int i = 0; i < seatingConfiguration.length(); i++) {
            if (seatingConfiguration.charAt(i) == '-') {
                xIndices.add(i);
            }
        }

        return xIndices.stream().mapToInt(Integer::intValue).toArray();
    }
    
    public static HashSet<Integer> convertToHashSet(int[] array) {
        HashSet<Integer> hashSet = new HashSet<>();

        for (int element : array) {
            hashSet.add(element);
        }

        return hashSet;
    }
    
    

    
}
