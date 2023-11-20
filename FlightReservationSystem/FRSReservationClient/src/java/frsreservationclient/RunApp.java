/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.CustomerUseCaseSessionBeanRemote;
import entity.Fare;
import entity.FlightBooking;
import entity.FlightCabinClass;
import entity.FlightReservation;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.Seat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import util.enumerations.CabinClassType;
import util.enumerations.SeatStatus;
import util.exception.CustomerAuthenticationFailedException;
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
    private long customerId;
    private HashMap<Integer, CabinClassType> hashMap;

    public RunApp() {}
    
    public RunApp(CustomerSessionBeanRemote customerSessionBean, CustomerUseCaseSessionBeanRemote customerUseCaseSessionBean) {
        //expose runApp to remote SessionBean methods
        this.customerSessionBean = customerSessionBean; 
        this.customerUseCaseSessionBean = customerUseCaseSessionBean;
        this.hashMap = new HashMap<Integer, CabinClassType>();
        this.initialiseMap();
    }
    
    
    
    public void showVisitorHomeScreen() {
        Scanner sc = new Scanner(System.in);
        
        userAuthPrompt(sc);
        
        sc.close();
    }
    
    public void showLogo() {
        System.out.println("                      ___");
        System.out.println("                      \\\\ \\");
        System.out.println("                       \\\\ `\\");
        System.out.println("    ___                 \\\\  \\");
        System.out.println("   |    \\                \\\\  `\\");
        System.out.println("   |_____\\                \\    \\");
        System.out.println("   |______\\                \\    `\\");
        System.out.println("   |       \\                \\     \\");
        System.out.println("   |      __\\__---------------------------------._.");
        System.out.println(" __|---~~~__o_o_o_o_o_o_o_o_o_o_o_o_o_o_o_o_o_o_[][__\\");
        System.out.println("|___                         /~      )                \\__");
        System.out.println("    ~~~---..._______________/      ,/_________________/");
        System.out.println("                           /      /");
        System.out.println("                          /     ,/");
        System.out.println("                         /     /");
        System.out.println("                        /    ,/");
        System.out.println("                       /    /");
        System.out.println("                      //  ,/");
        System.out.println("                     //  /");
        System.out.println("                    // ,/");
        System.out.println("                   //_/");
        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
        System.out.println("                      _ _                               ");
        System.out.println("  _ __ ___   ___ _ __| (_) ___  _ __                    ");
        System.out.println(" | '_ ` _ \\ / _ \\ '__| | |/ _ \\| '_ \\                   ");
        System.out.println(" | | | | | |  __/ |  | | | (_) | | | |                  ");
        System.out.println(" |_| |_|_|_|\\___|_|  |_|_|\\___/|_| |_|                  ");
        System.out.println("   __ _(_)_ __| (_)_ __   ___  ___                      ");
        System.out.println("  / _` | | '__| | | '_ \\ / _ \\/ __|                     ");
        System.out.println(" | (_| | | |  | | | | | |  __/\\__ \\                     ");
        System.out.println("  \\__,_|_|_|  |_|_|_| |_|\\___||___/                     ");
        System.out.println("  / _| (_) __ _| |__ | |_                               ");
        System.out.println(" | |_| | |/ _` | '_ \\| __|                              ");
        System.out.println(" |  _| | | (_| | | | | |_                               ");
        System.out.println(" |_| |_|_|\\__, |_| |_|\\__|           _   _              ");
        System.out.println("  _ __ ___|___/  ___ _ ____   ____ _| |_(_) ___  _ __   ");
        System.out.println(" | '__/ _ \\/ __|/ _ \\ '__\\ \\ / / _` | __| |/ _ \\| '_ \\  ");
        System.out.println(" | | |  __/\\__ \\  __/ |   \\ V / (_| | |_| | (_) | | | | ");
        System.out.println(" |_|  \\___||___/\\___|_|    \\_/ \\__,_|\\__|_|\\___/|_| |_| ");
        System.out.println("  ___ _   _ ___| |_ ___ _ __ ___                        ");
        System.out.println(" / __| | | / __| __/ _ \\ '_ ` _ \\                       ");
        System.out.println(" \\__ \\ |_| \\__ \\ ||  __/ | | | | |                      ");
        System.out.println(" |___/\\__, |___/\\__\\___|_| |_| |_|                      ");
        System.out.println("      |___/                                             ");
        
        
    }
    
    
  
    public void userAuthPrompt(Scanner sc) {
        showLogo();
        IntStream.rangeClosed(1, 5).forEach(x -> System.out.println());
        System.out.println("Welcome to Merlion Airlines Flight Reservation System");
        System.out.println("Press '0' to continue as guest");
        System.out.println("Press '1' to Login to an existing account");
        System.out.println("Press '2' to Register an account");
        System.out.print("> ");
        int option = sc.nextInt();
        sc.nextLine();
        
        switch(option) {
            case 0:
                System.out.println("Continue as guest");
                mainMenuGuest(sc);
                break;
            case 1:
                System.out.println("Enter Your Email: ");
                System.out.print("> ");
                String loginEmail = sc.nextLine();
                System.out.println("Enter Your Password: ");
                System.out.print("> ");
                String loginPassword = sc.nextLine();
                try {
                    long customerId = customerUseCaseSessionBean.customerLogin(loginEmail, loginPassword);
                    this.customerId = customerId;
                    System.out.println();
                    System.out.println("Welcome! Customer Found & Authenticated");
                    customerIsLoggedIn = true;
                    mainMenuCustomer(sc);
                        
                } catch (CustomerAuthenticationFailedException e) {
                    if (e.getMessage().equals("noAccount")) {
                        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
                        System.out.println("Customer account has not been "
                                + "created yet, would you like to create a new "
                                + "account?\n");
                        System.out.println("Enter Y to register new account");
                        System.out.println("Enter N try logging in again");
                        System.out.println("> ");
                        String tmpOption = sc.nextLine();
                        if (tmpOption.equals("Y")) {
                            createNewCustomerAccount(sc);
                            this.customerIsLoggedIn = true;
                            System.out.println("You account has been created! \n Please Login to your acccount!");
                            IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
                        } else {
                            userAuthPrompt(sc);
                        }
                    } else {
                        System.out.println("Invalid Password, please try again");
                        userAuthPrompt(sc);
                    }
                }
                break;
                
            case 2:
                IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
                System.out.println("Registering your account");
                createNewCustomerAccount(sc);
                userAuthPrompt(sc);
                break;
            default: 
                invalidOption();
                break;
        }
    }
    
    public void mainMenuGuest(Scanner scanner) {
        System.out.println("Press 0 to exit");
        System.out.println("Pres 1 to search flights");
        System.out.print("> ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 0: 
                userAuthPrompt(scanner);
                break;
            case 1: 
                searchForFlightRoutes(scanner, false);
                mainMenuGuest(scanner);
                break;
            default:
                System.exit(1);
        }
    }
    
    public void mainMenuCustomer(Scanner scanner) {
         IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
        System.out.println("Press 0 to logout");
        System.out.println("Press 1 to search flights and make reservations");
        System.out.println("Press 2 to view my flight reservations");
        System.out.println("Press 3 to view my flight reservation details");
        System.out.print("> ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 0: 
                mainMenuGuest(scanner);
                break;
            case 1: 
                searchForFlightRoutes(scanner, true);
                mainMenuCustomer(scanner);
                break;
            case 2: 
                viewAllFlightReservations();
                mainMenuCustomer(scanner);
                break;
            case 3: 
                viewFlightReservationDetails(scanner);
                mainMenuCustomer(scanner);
                break;
        }
         
    }
    
    public void invalidOption() {
        System.out.println("You have selected an invalid option!");
     }
     
    public void createNewCustomerAccount(Scanner sc) {
         System.out.println("Enter Your First Name");
            System.out.print("> ");
            String firstName = sc.nextLine();
            System.out.println("Enter Your Last Name");
            System.out.print("> ");
            String lastName = sc.nextLine();
            System.out.println("Enter Your Email Address: ");
            System.out.print("> ");
            String email = sc.nextLine();
            System.out.println("Enter Your Phone Number: ");
            System.out.print("> ");
            String phoneNumber = sc.nextLine();
            System.out.println("Enter your Address: ");
            System.out.print("> ");
            String address = sc.nextLine();
            System.out.println("Enter your Password: ");
            System.out.print("> ");
            String password = sc.nextLine();
            
            // need check whether a customer has been created already or not
            customerSessionBean.createNewCustomer(firstName, lastName, email, phoneNumber, address, password);
     }
    
    public void searchForFlightRoutes(Scanner sc, boolean isCustomer) {
        int roundTrip;
        String departureAirport;
        Date departureDate = null;
        String destinationAirport;
        Date returnDate = null;
        int directFlight;
        int numPassengers = 0;
        
        IntStream.rangeClosed(1, 3).forEach(x -> System.out.println());        
        System.out.println("SYSTEM USE CASE: SEARCH FLIGHT");
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
            System.out.println("Enter the Return Departure Time (HH:mm:ss):");
            System.out.print("> ");
            String endTimeInput = sc.next();
            String endDateTimeInput = endDateInput + " " + endTimeInput;
            
            try {
                returnDate = formatDate(endDateTimeInput);

            } catch (Exception e) {
                System.out.println("Invalid Date format. Please try again");
            }
        }
        
        // flight type  prefeence
        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
        System.out.println("\u001B[1mFLIGHT TYPE PREFERENCE\u001B[0m\nPress 1 for \u001B[1mDirect Flights Only\u001B[0m\nPress 2 For \u001B[1mNo Preference\u001B[0m");
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
            chosenType = this.hashMap.get(choice);
        } 
        
        try {
            List<List<FlightSchedule>> flightScheduleList = customerUseCaseSessionBean.searchForFlightRoutes(
            departureAirport, departureDate, destinationAirport,
            returnDate, directFlight);
            boolean needReturn = returnDate != null;
            
            printFlightInformation(true, flightScheduleList.get(0), departureAirport, destinationAirport, numPassengers, chosenType);
            if (needReturn) {
                printFlightInformation(true, flightScheduleList.get(1), destinationAirport, departureAirport, numPassengers, chosenType);
            }
            // for connecting airports
            if (directFlight == 2) {
                printFlightInformation(false, flightScheduleList.get(2), departureAirport, destinationAirport, numPassengers, chosenType);
                printFlightInformation(false, flightScheduleList.get(3), departureAirport, destinationAirport, numPassengers, chosenType);
                if (needReturn) {
                    printFlightInformation(false, flightScheduleList.get(4), destinationAirport, departureAirport, numPassengers, chosenType);
                    printFlightInformation(false, flightScheduleList.get(5), destinationAirport, departureAirport, numPassengers, chosenType);
                }
            }
            
            // since flight schedules has been found 
            if(isCustomer) {
                IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
                System.out.println("Would you to proceed to make a flight reservation?");
                System.out.println("Press 0 to proceed back to the main menu");
                System.out.println("Press 1 to proceed with flight reservation");
                System.out.print("> ");
                int choiceOne = sc.nextInt();
                if (choiceOne == 1) {
                    // return these 4
                    List<Long> finalFlightScheduleIdList = new ArrayList<Long>();
                    List<Long> finalFlightCabinClassIdList = new ArrayList<Long>();
                    List<List<String>> finalSeatsChoice = new ArrayList<List<String>>();
                    List<Double> ticketPricesForEachFlightSchedulePerPerson = new ArrayList<Double>();
                    // register the passengers
                    System.out.println("Step 0: Input Passenger Details");
                    System.out.println("");
                    // return this too
                    List<HashMap<Integer, String>> allPDetails = enterPassengerDetails(numPassengers, sc);
                    
                    boolean connectingFound = flightScheduleList.size() > 2;
                    double onePassengerFareDirect = 0.0;
                    double onePassengerFareConnecting = 0.0;
                    int choice2 = -1;
                    if (connectingFound) {
                        System.out.println("There is a choice between connecting and direct flights");
                        System.out.println("Press 0 to choose connecting flights");
                        System.out.println("Press 1 to choose direct flights");
                        System.out.print("> ");
                        
                        
                        choice2 = sc.nextInt();
                        if (choice2 == 1) {
                             // means direct flight is chosen
                            double directToOnePassengerFare = enterDetailsForDirectTo(0, 0, false, false, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails, chosenType);
                            onePassengerFareDirect += directToOnePassengerFare;
                            ticketPricesForEachFlightSchedulePerPerson.add(directToOnePassengerFare);
                            // same procedure for return flight 
                            if (needReturn) {
                                double directReturnOnePassengerFare = enterDetailsForDirectTo(0, 1, true, false, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails, chosenType);
                                onePassengerFareDirect += directReturnOnePassengerFare;
                                ticketPricesForEachFlightSchedulePerPerson.add(directReturnOnePassengerFare);
                            }
                        } else {
                            // means connecting flights is chosen
                            // connecting flight first leg
                             double connectingTo1OnePerson = enterDetailsForDirectTo(1, 2, false, true, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails, chosenType);
                             onePassengerFareConnecting += connectingTo1OnePerson;
                             ticketPricesForEachFlightSchedulePerPerson.add(connectingTo1OnePerson);
                             double connectingTo2OnePerson = enterDetailsForDirectTo(2, 3, false, true, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails, chosenType);
                             onePassengerFareConnecting += connectingTo2OnePerson;
                             ticketPricesForEachFlightSchedulePerPerson.add(connectingTo2OnePerson);
                            
                             if (needReturn) {
                                 double connectingReturn1OnePerson =  enterDetailsForDirectTo(1, 4, true, true, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails, chosenType);
                                onePassengerFareConnecting += connectingReturn1OnePerson;
                                ticketPricesForEachFlightSchedulePerPerson.add(connectingReturn1OnePerson);
                                double connectingReturn2OnePerson = enterDetailsForDirectTo(2, 5, true, true, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails, chosenType);
                                onePassengerFareConnecting += connectingReturn2OnePerson;
                                ticketPricesForEachFlightSchedulePerPerson.add(connectingReturn2OnePerson);
                             }
                        }
                    } else {
                        double directToOnePassengerFare = enterDetailsForDirectTo(0, 0, false, false, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails, chosenType);
                        onePassengerFareDirect += directToOnePassengerFare;
                         ticketPricesForEachFlightSchedulePerPerson.add(directToOnePassengerFare);
                        if (needReturn) {
                             double directReturnOnePassengerFare =  enterDetailsForDirectTo(0, 1, true, false, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails, chosenType);
                             onePassengerFareDirect += directReturnOnePassengerFare;
                             ticketPricesForEachFlightSchedulePerPerson.add(directReturnOnePassengerFare);
                        }
                    }
                    
                    // payment details 
                    System.out.println("Last Step: Checkout");
                    double totalCostPerPerson = ticketPricesForEachFlightSchedulePerPerson.stream().reduce(0.0, (x, y) -> x + y);
                    System.out.println("Total Cost for Your Trip would be: $" + (totalCostPerPerson * numPassengers));
                    System.out.println("Enter the Credit Card Number");
                    System.out.print("> ");
                    String creditCardNumber = sc.next();
                    sc.nextLine();
                    // send to backend 
                    customerUseCaseSessionBean.makeFlightReservation(this.customerId, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails, creditCardNumber, ticketPricesForEachFlightSchedulePerPerson);
                    System.out.println("Transaction \u001B[32mSuccessful\u001B[0m!");
                    return;
                } else {
                    // customer do not wish to make a flight reservation
                    return;
                }
            }
            
        } catch (NoFlightFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Please try again!");
        }
    }
    
    public void viewAllFlightReservations() {
        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
        List<FlightReservation> myFRList = customerUseCaseSessionBean.viewAllFlightReservations(this.customerId);
        
        int counter = 1; 
        for (FlightReservation fr : myFRList) {
            System.out.println();
            System.out.println("Flight Reservation #" + counter);
            System.out.println("Credit Card Number: " + fr.getCreditCardNumber());
            System.out.println("Transaction Date: " + fr.getDate());
            counter += 1;
        }
    }
    
    public void viewFlightReservationDetails(Scanner scanner) {
        IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
        List<FlightReservation> myFRList = customerUseCaseSessionBean.viewAllFlightReservations(customerId);
        FlightReservation chosenFR = chooseFlightReservation(myFRList, scanner);
        printSpecificFlightReservation(chosenFR);
    }
    
    public FlightReservation chooseFlightReservation(List<FlightReservation> myFRList, Scanner sc) {
        int counter = 1;
        for (FlightReservation fr : myFRList) {
            System.out.println();
            System.out.println("Flight Reservation #" + counter);
            System.out.println("Credit Card Number: " + fr.getCreditCardNumber());
            System.out.println("Transaction Date: " + fr.getDate());
            System.out.println("Press " + counter + " to select this flight reservation");
            counter += 1;
        }
        System.out.println();
        System.out.println("Enter Choice");
        System.out.print("> ");
        return myFRList.get(sc.nextInt() - 1);
    }
    
    public void printSpecificFlightReservation(FlightReservation fr) {
        System.out.println("Number of Flight Legs in this Itenerary: " + fr.getFlightBookingList().size());
        int counter = 1;
        double totalCostItenerary = 0.0;
        
        for (FlightBooking fb : fr.getFlightBookingList()) {
            IntStream.rangeClosed(1, 2).forEach(x -> System.out.println());
            System.out.println("Flight Leg #" + counter);
            System.out.println("Flight Number: "  + fb.getFlightNumber());
            System.out.println("Flight Route: " + fb.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getIataAirportCode() + " -> " + fb.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getIataAirportCode() );
            System.out.println("Flight Cost: $" + fb.getFlightLegCost().doubleValue());
            System.out.println("Flight Cabin Class: " + fb.getReservedSeats().get(0).getFlightCabinClass().getCabinClass().getCabinClassName().name());
            System.out.println("Number of Passengers: " + fb.getReservedSeats().size());
            
            // sort according to ascending seat number ordering 
            Comparator<Seat> ascendingOrder = (x, y) -> x.getSeatNumber().compareTo(y.getSeatNumber());
            fb.getReservedSeats().sort(ascendingOrder);
            
            for (Seat s : fb.getReservedSeats()) {
                IntStream.rangeClosed(1, 1).forEach(x -> System.out.println());
                System.out.println(IntStream.range(0, 20).mapToObj(i -> "-").collect(Collectors.joining()));
                System.out.println("Passenger Name: "  + s.getPassenger().getFirstName() + " " + s.getPassenger().getLastName());
                System.out.println("Passenger Seat Number: "  + s.getSeatNumber());
                System.out.println(IntStream.range(0, 20).mapToObj(i -> "-").collect(Collectors.joining()));
                IntStream.rangeClosed(1, 1).forEach(x -> System.out.println());
            }
            totalCostItenerary +=  (fb.getReservedSeats().size() * fb.getFlightLegCost().doubleValue());
            counter += 1;
        }
        System.out.println("Total Amount Paid for Flight Reservation: $" + totalCostItenerary);
    }
    
    public double enterDetailsForDirectTo(int leg, int choice, boolean isReturn, boolean isConnecting, List<List<FlightSchedule>> flightScheduleList, String departureAirport, String destinationAirport, int numPassengers, Scanner sc, 
            List<Long> finalFlightScheduleIdList, List<Long> finalFlightCabinClassIdList, List<List<String>> finalSeatsChoice, List<HashMap<Integer, String>> passengerDetails, CabinClassType cabinClassPreference) {
        // make 
        IntStream.rangeClosed(1, 5).boxed().forEach(x -> System.out.println());
        String title = isReturn ? "RETURN" : "TO";
        int step = isReturn ? 2 : 1;
        String title2 = isConnecting ? "CONNECTING" : "DIRECT";
        System.out.println("STEP " + step + "a: SELECT " + title2 + " FLIGHT -> " + title);
        if (leg != 0) System.out.println("Connecting Flight Leg " + leg);
        switch (choice) {
            case 0: 
                // direct flight TO
                printFlightInformation(true, flightScheduleList.get(0), departureAirport, destinationAirport, numPassengers, cabinClassPreference);
                break;
            case 1: 
                // direct flight BACK
                printFlightInformation(true, flightScheduleList.get(1), destinationAirport, departureAirport, numPassengers, cabinClassPreference);
                break;
            case 2: 
                // connecting TO : leg 1
                printFlightInformation(false, flightScheduleList.get(2), departureAirport, destinationAirport, numPassengers, cabinClassPreference);
                break;
            case 3:
                // connecting TO : leg 2
                printFlightInformation(false, flightScheduleList.get(3), departureAirport, destinationAirport, numPassengers, cabinClassPreference);
                break;
            case 4: 
                // connecting RETURN : leg 1
                printFlightInformation(false, flightScheduleList.get(4), destinationAirport, departureAirport, numPassengers, cabinClassPreference);
                break;
            case 5:
                // connecting RETURN : leg 2
                 printFlightInformation(false, flightScheduleList.get(5), destinationAirport, departureAirport, numPassengers, cabinClassPreference);
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
        IntStream.rangeClosed(1, 5).boxed().forEach(x -> System.out.println());
        System.out.println("STEP 1b: SELECT CABIN CLASS");
        System.out.println("");
        FlightCabinClass chosenFlightCabinClass = chooseFCC(flightSchedule, sc);
        long chosenFCCId = chosenFlightCabinClass.getId();
        finalFlightCabinClassIdList.add(chosenFCCId);
        Comparator<Fare> lowestFareComparator = Comparator.comparingDouble(fare -> fare.getFareAmount().doubleValue());
        // choses the lowest fare for this flight cbin class
        List<Fare> faresForThisCabinClass = chosenFlightCabinClass.getFlightSchedule().getFlightSchedulePlan().getFares().stream().filter(x -> x.getCabinClass().getCabinClassName().equals(chosenFlightCabinClass.getCabinClass().getCabinClassName())).collect(Collectors.toList());
        Fare fare = faresForThisCabinClass.stream().min(lowestFareComparator).get();

        // this would be the total amount of money for one leg of the flight itnerary
        double amountForOnePassenger = fare.getFareAmount().doubleValue();
        
        IntStream.rangeClosed(1, 5).boxed().forEach(x -> System.out.println());
        System.out.println("STEP 1c: SELECT SEAT FOR PASSENGERS");
        System.out.println();
        // add the list of seats chosen for one flight schedule
        List<String> seatsChosen = printSeatLayout(chosenFlightCabinClass, numPassengers, sc, passengerDetails);
        finalSeatsChoice.add(seatsChosen);
        return amountForOnePassenger;
    }
    
    public FlightCabinClass chooseFCC(FlightSchedule fs, Scanner scanner) {
        int counter = 1;
        for (FlightCabinClass fcc : fs.getFccList()) {
            System.out.println("Cabin Class Type: " + fcc.getCabinClass().getCabinClassName());
            System.out.println("Press " + counter + " to choose this cabin class");
            System.out.println("");
            counter += 1;
        }
        
        System.out.println("Enter Your Option:");
        System.out.print("> ");
        FlightCabinClass chosenFlightCabinclass = fs.getFccList().get(scanner.nextInt() - 1);
        return chosenFlightCabinclass;
    }
    
    
    public List<HashMap<Integer, String>> enterPassengerDetails(int numP, Scanner scanner) {
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
    
    public static double printFlightInformation(boolean isDirect, List<FlightSchedule> fsList, String start, String end, int numPassengers, CabinClassType preference) {
        System.out.println();
        String header = isDirect ? "DIRECT" : "CONNECTING";
        System.out.println(header);
        double totalCostForThisSingleFlight = 0.0;
        int counter = 1;
        
        if (fsList.isEmpty()) {
            System.out.println();
            System.out.println("NO FLIGHT SCHEDULES FOUND");
            return 0.0;
        }
        for (FlightSchedule fs : fsList) {
            System.out.println("");
            System.out.println("Option #" + counter);
            FlightRoute fr = fs.getFlightSchedulePlan().getFlight().getFlightRoute();
            System.out.println(fr.getOrigin().getIataAirportCode() + " -> " + fr.getDestination().getIataAirportCode());
            System.out.println("Departure Time " + fs.getDepartureTime());
            System.out.println("Arrival Time " + fs.getArrivalTime());
            System.out.println("Duration " + formatDuration(fs.getFlightDuration()));
            // display cabin class availability
            System.out.println("");
            System.out.println("Cabin Class Availability");
            System.out.println("");
            boolean isAdded = false;
            List<FlightCabinClass> curatedList = fs.getFccList();
            if (preference != null) {
                curatedList = curatedList.stream().filter(x -> x.getCabinClass().getCabinClassName().equals(preference)).collect(Collectors.toList());
            }
            if (curatedList.isEmpty()) {
                System.out.println("Oops, there are currently no cabin class that befits your requirements");
            }
            for (FlightCabinClass fcc : curatedList) {
                double pricePerPassengerPerCCPerFS = printFareInformation(fcc, numPassengers);
                if (!isAdded) {
                    totalCostForThisSingleFlight += pricePerPassengerPerCCPerFS;
                    isAdded = true;
                }
            }
            counter += 1;
        }
        return totalCostForThisSingleFlight;
    }
    

     public static double printFareInformation(FlightCabinClass fcc, int numPassengers) {
         CabinClassType name = fcc.getCabinClass().getCabinClassName();
         double fccPrice = fcc.getFlightSchedule().getFlightSchedulePlan().getFares().stream().filter(x -> x.getCabinClass().getCabinClassName().equals(name)).findAny().get().getFareAmount().doubleValue();
         System.out.println(name + " " + fccPrice);
         System.out.println("Price per passenger: $" + fccPrice);
            System.out.println("Total Price for all passengers: $" + (fccPrice * numPassengers));
         return fccPrice;
     }
    
   
    public Date formatDate(String dateTimeInput) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeInput, formatter);
            ZoneId zoneId = ZoneId.of("Asia/Singapore");
            Date date = Date.from(dateTime.atZone(zoneId).toInstant());
        return date;
    }
    
    public List<String> printSeatLayout(FlightCabinClass chosenFlightCabinClass, int numPassengers, Scanner sc, List<HashMap<Integer, String>> passengerDetails) {
        int numRows = chosenFlightCabinClass.getCabinClass().getNumRows().intValue();
        int numColumns = chosenFlightCabinClass.getCabinClass().getNumAisles().intValue() + chosenFlightCabinClass.getCabinClass().getNumSeatsAbreast().intValue();
        HashSet<String> reservedSeatsSet = new HashSet<String>();
        // need to initalise this
        List<String> seatChosen = new ArrayList<String>();
        List<Seat> seatList = chosenFlightCabinClass.getSeatList();
        
        // make a seat array
        String[][] seatLayout = new String[numRows][numColumns];
        String convertedConfiguration = convertSeatingConfiguration(chosenFlightCabinClass.getCabinClass().getSeatingConfiguration());
        int[] breakpoints = parseSeatingConfiguration(convertedConfiguration);
        HashSet<Integer> breakpointSet = convertToHashSet(breakpoints);
        
        // sort the seatlist based on seat number
         Comparator<Seat> customComparator = Comparator
        .<Seat, Integer>comparing(seat -> Integer.parseInt(seat.getSeatNumber().substring(0, seat.getSeatNumber().length() - 1)))
        .thenComparing(Comparator.comparing(Seat::getSeatNumber));

        // Sort the list using the custom comparator
        Collections.sort(seatList, customComparator);
        int counter = 0;
        // initalise all the aisles
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                if (breakpointSet.contains(j)) {
                    seatLayout[i][j] = "<=>";
                } else {
                    if (counter < seatList.size()) {
                        if (seatList.get(counter).getSeatStatus() == SeatStatus.RESERVED) {
                            String xxx = seatList.get(counter).getSeatNumber().length() == 3 ? "XXX" : "XX";
                            seatLayout[i][j] = xxx;
                        } else {
                            seatLayout[i][j] = seatList.get(counter).getSeatNumber();
                        }
                        counter += 1;
                    }
                }
            }
        }
        
        // print array
        for (int i = 0; i < seatLayout.length; i++) {
            for (int j = 0; j < seatLayout[i].length; j++) {
                String seat = seatLayout[i][j];
                if (seat.equals("<=>")) {
                    System.out.print("\u001B[33m" + seat + "\u001B[0m "); 
                } else if (seat.equals("XXX") || seat.equals("XX")) {
                    System.out.print("\u001B[31m" + seat + "\u001B[0m "); 
                } else {
                    System.out.print("\u001B[32m" + seat + "\u001B[0m "); 
                }
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
    
    public static String formatDuration(Duration duration) {
        long totalMinutes = duration.toMinutes();
        long seconds = duration.minusMinutes(totalMinutes).getSeconds();

        String formattedDuration = String.format("%d:%02d", totalMinutes, seconds);
        
        return formattedDuration;
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
    
    public void printAllCabinClassTypes() {
        System.out.println("Press 1 for First (F) Class");
        System.out.println("Press 2 for Business (J) Class");
        System.out.println("Press 3 for Premium Economy (W) Class");
        System.out.println("Press 4 for Economy (Y) Class");
    }
    
    public void initialiseMap() {
        hashMap.put(1, CabinClassType.F);
        hashMap.put(2, CabinClassType.J);
        hashMap.put(3, CabinClassType.W);
        hashMap.put(4, CabinClassType.Y);
    }
    


}
