/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsreservationclient;

import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.CustomerUseCaseSessionBeanRemote;
import entity.Customer;
import entity.Fare;
import entity.FlightBooking;
import entity.FlightCabinClass;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.Seat;
import static java.lang.System.out;
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
        System.out.println("Welcome to Merlion Airways Flight Reservation System");
        System.out.println("Press '0' to continue as guest");
        System.out.println("Press '1' to Login to an existing account");
        System.out.println("Press '2' to Register an account");
        
        int option = sc.nextInt();
        sc.nextLine();
        
        switch(option) {
            case 0:
                System.out.println("Continue as guest");
                mainMenuGuest(sc);
                break;
            case 1:
                System.out.println("Login selected");
                System.out.println("Enter Your Email: ");
                String loginEmail = sc.nextLine();
                System.out.println("Enter Your Password: ");
                String loginPassword = sc.nextLine();
                try {
                    long customerId = customerUseCaseSessionBean.customerLogin(loginEmail, loginPassword);
                    this.customerId = customerId;
                    System.out.println("Welcome, Customer Found & Authenticated");
                    customerIsLoggedIn = true;
                    mainMenuCustomer(sc);
                        
                } catch (CustomerAuthenticationFailedException e) {
                    if (e.getMessage().equals("noAccount")) {
                        System.out.println("Customer account has not been "
                                + "created yet, please try again\n");
                        userAuthPrompt(sc);
                    } else {
                        System.out.println("Invalid Password, please try again");
                        userAuthPrompt(sc);
                    }
                }
                break;
                
            case 2:
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
                System.exit(1);
                break;
            case 1: 
                searchForFlightRoutes(scanner, false);
                mainMenuGuest(scanner);
                break;
        }
    }
    
    public void logout(Customer customer) {
        System.out.println("Thank you for using Merlion Airlines");
        System.out.println("You have been logged out");
        customerIsLoggedIn = false;

    }
    
    public void mainMenuCustomer(Scanner scanner) {
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
     
    public int createNewCustomerAccount(Scanner sc) {
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
            
            
            if (customerSessionBean.createNewCustomer(firstName, lastName, email, phoneNumber, address, password) == null){
                System.out.println("Duplicate enail detected, please try again.");
                return -1;
            }
            return 1;
     }
    
    public void searchForFlightRoutes(Scanner sc, boolean isCustomer) {
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
        
        while (departureDate == null) {
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
        }
        
        
        
        if (roundTrip == 2) { //only collect return date if is return flight
            while (returnDate == null) {
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
        }
        
        System.out.println("Do you prefer a direct or connecting flight? Press 1 for direct, 2 for Connecting");
        System.out.print("> ");
        directFlight = sc.nextInt(); 
        sc.nextLine();
        
        try {
            List<List<FlightSchedule>> flightScheduleList = customerUseCaseSessionBean.searchForFlightRoutes(
            departureAirport, departureDate, destinationAirport,
            returnDate, directFlight);
            boolean needReturn = returnDate != null;
            // there is only one size nothing is being added at all 
            double directTo = callDirectTo(flightScheduleList, departureAirport, destinationAirport, numPassengers);
            if (needReturn) {
                double directReturn = callDirectBack(flightScheduleList, departureAirport, destinationAirport, numPassengers);
            }
            // for connecting airports
            if (directFlight == 2) {
                double connectingTo =  callConnectingTo(flightScheduleList, departureAirport, destinationAirport, numPassengers);
                if (needReturn) {
                    double connectingReturn = callConnectingBack(flightScheduleList, departureAirport, destinationAirport, numPassengers);
                }
            }
            
            // since flight schedules has been found 
            if(isCustomer) {
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
                            double directToOnePassengerFare = enterDetailsForDirectTo(0, 0, false, false, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
                            onePassengerFareDirect += directToOnePassengerFare;
                            ticketPricesForEachFlightSchedulePerPerson.add(directToOnePassengerFare);
                            // same procedure for return flight 
                            if (needReturn) {
                                double directReturnOnePassengerFare = enterDetailsForDirectTo(0, 1, true, false, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
                                onePassengerFareDirect += directReturnOnePassengerFare;
                                ticketPricesForEachFlightSchedulePerPerson.add(directReturnOnePassengerFare);
                            }
                        } else {
                            // means connecting flights is chosen
                            // connecting flight first leg
                             double connectingTo1OnePerson = enterDetailsForDirectTo(1, 2, false, true, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
                             onePassengerFareConnecting += connectingTo1OnePerson;
                             ticketPricesForEachFlightSchedulePerPerson.add(connectingTo1OnePerson);
                             double connectingTo2OnePerson = enterDetailsForDirectTo(2, 3, false, true, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
                             onePassengerFareConnecting += connectingTo2OnePerson;
                             ticketPricesForEachFlightSchedulePerPerson.add(connectingTo2OnePerson);
                            
                             if (needReturn) {
                                 double connectingReturn1OnePerson =  enterDetailsForDirectTo(1, 4, true, true, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
                                onePassengerFareConnecting += connectingReturn1OnePerson;
                                ticketPricesForEachFlightSchedulePerPerson.add(connectingReturn1OnePerson);
                                double connectingReturn2OnePerson = enterDetailsForDirectTo(2, 5, true, true, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
                                onePassengerFareConnecting += connectingReturn2OnePerson;
                                ticketPricesForEachFlightSchedulePerPerson.add(connectingReturn2OnePerson);
                             }
                        }
                    } else {
                        double directToOnePassengerFare = enterDetailsForDirectTo(0, 0, false, false, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
                        onePassengerFareDirect += directToOnePassengerFare;
                         ticketPricesForEachFlightSchedulePerPerson.add(directToOnePassengerFare);
                        if (needReturn) {
                             double directReturnOnePassengerFare =  enterDetailsForDirectTo(0, 1, true, false, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
                             onePassengerFareDirect += directReturnOnePassengerFare;
                             ticketPricesForEachFlightSchedulePerPerson.add(directReturnOnePassengerFare);
                        }
                    }
                    
                    // payment details 
                    double showCase = onePassengerFareConnecting == 0.0 ? onePassengerFareDirect : choice2 == 1 ? onePassengerFareDirect : onePassengerFareConnecting;
                    System.out.println("Last Step: Checkout");
                    System.out.println("Total Amount Payable: " + (showCase * numPassengers));
                    System.out.println("Enter the Credit Card Number");
                    System.out.print("> ");
                    String creditCardNumber = sc.next();
                    sc.nextLine();
                    // send to backend 
                    customerUseCaseSessionBean.makeFlightReservation(this.customerId, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails, creditCardNumber, ticketPricesForEachFlightSchedulePerPerson);
                    System.out.println("Transaction Successful!");
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
        List<FlightReservation> myFRList = customerUseCaseSessionBean.viewAllFlightReservations(this.customerId);
        if (myFRList.isEmpty()) {
            System.out.println("No current reservations to display");
            System.out.println();
            return;
        }
        
        
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
        List<FlightReservation> myFRList = customerUseCaseSessionBean.viewAllFlightReservations(customerId);
        if (myFRList.isEmpty()) {
            System.out.println("No current reservations details to display");
            System.out.println();
            return;
        }
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
        System.out.println();
        int counter = 1;
        double totalCostItenerary = 0.0;
        
        for (FlightBooking fb : fr.getFlightBookingList()) {
            System.out.println("Flight Leg #" + counter);
            System.out.println("Flight Number: "  + fb.getFlightNumber());
            System.out.println("Flight Route: " + fb.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getIataAirportCode() + " -> " + fb.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getIataAirportCode() );
            System.out.println("Flight Cost: " + fb.getFlightLegCost().doubleValue());
            System.out.println("Flight Cabin Class: " + fb.getReservedSeats().get(0).getFlightCabinClass().getCabinClass().getCabinClassName().name());
            System.out.println("Number of Passengers: " + fb.getReservedSeats().size());
            
            for (Seat s : fb.getReservedSeats()) {
                System.out.println("Passenger Name: "  + s.getPassenger().getFirstName() + " " + s.getPassenger().getLastName());
                System.out.println("Passenger Seat Number: "  + s.getSeatNumber());
            }
            totalCostItenerary +=  (fb.getReservedSeats().size() * fb.getFlightLegCost().doubleValue());
            counter += 1;
        }
        System.out.println("Total Amount Paid for Flight Reservation " + totalCostItenerary);
    }
    
    public double enterDetailsForDirectTo(int leg, int choice, boolean isReturn, boolean isConnecting, List<List<FlightSchedule>> flightScheduleList, String departureAirport, String destinationAirport, int numPassengers, Scanner sc, List<Long> finalFlightScheduleIdList, List<Long> finalFlightCabinClassIdList, List<List<String>> finalSeatsChoice, List<HashMap<Integer, String>> passengerDetails) {
        // make 
        IntStream.rangeClosed(1, 10).boxed().forEach(x -> System.out.println());
        String title = isReturn ? "RETURN" : "TO";
        int step = isReturn ? 2 : 1;
        String title2 = isConnecting ? "CONNECTING" : "DIRECT";
        System.out.println("STEP " + step + "a: SELECT " + title2 + " FLIGHT -> " + title);
        if (leg != 0) System.out.println("Connecting Flight Leg " + leg);
        switch (choice) {
            case 0: 
                callDirectTo(flightScheduleList, departureAirport, destinationAirport, numPassengers);
                break;
            case 1: 
                callDirectBack(flightScheduleList, departureAirport, destinationAirport, numPassengers);
                break;
            case 2: 
                callConnectingSingle(2, flightScheduleList, numPassengers);
                break;
            case 3:
                callConnectingSingle(3, flightScheduleList, numPassengers);
                break;
            case 4: 
                callConnectingSingle(4, flightScheduleList, numPassengers);
                break;
            case 5:
                callConnectingSingle(5, flightScheduleList, numPassengers);
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
    
    public double callDirectTo(List<List<FlightSchedule>> flightScheduleList, String departureAirport, String destinationAirport, int numPassengers) {
        List<FlightSchedule> directTo = flightScheduleList.get(0);
        return printDirectFlightsTo(directTo, departureAirport, destinationAirport, numPassengers);
         
    }
    
    public double callDirectBack(List<List<FlightSchedule>> flightScheduleList, String departureAirport, String destinationAirport, int numPassengers) {
        System.out.println();
        System.out.println("Return Flights");
        List<FlightSchedule> returnTo = flightScheduleList.get(1);
        return printDirectFlightsTo(returnTo, destinationAirport, departureAirport, numPassengers);
    }
      
    public double callConnectingTo(List<List<FlightSchedule>> flightScheduleList, String departureAirport, String destinationAirport, int numPassengers) {
        System.out.println();
        System.out.println("Connecting Flights: TO");
        List<FlightSchedule> directFirst= flightScheduleList.get(2);
        List<FlightSchedule> directSecond= flightScheduleList.get(3);
        
        return printConnectingFlights(directFirst, directSecond, departureAirport, destinationAirport, numPassengers);
    }
    
    public double callConnectingBack(List<List<FlightSchedule>> flightScheduleList, String departureAirport, String destinationAirport, int numPassengers) {
        System.out.println();
        System.out.println("Connecting Flights: RETURN");
        List<FlightSchedule> returnFirst= flightScheduleList.get(4);
        List<FlightSchedule> returnSecond= flightScheduleList.get(5);
        return printConnectingFlights(returnFirst, returnSecond, destinationAirport, departureAirport, numPassengers);
    }
    
    public void callConnectingSingle(int choice, List<List<FlightSchedule>> flightScheduleList, int numPassengers) {
        if (choice == 2 || choice == 3) {
            System.out.println("Connecting Flights: TO");
            if (choice == 2) {
                System.out.println("Leg 1 Details");
                printConnectingFlightsSingle(flightScheduleList.get(choice), numPassengers);
            } else {
                System.out.println("Leg 2 Details");
                printConnectingFlightsSingle(flightScheduleList.get(choice), numPassengers);
            }
        } else {
            System.out.println("Connecting Flights: RETURN");
            if (choice == 4) {
                System.out.println("Leg 1 Details");
                printConnectingFlightsSingle(flightScheduleList.get(choice), numPassengers);
            } else {
                System.out.println("Leg 2 Details");
                printConnectingFlightsSingle(flightScheduleList.get(choice), numPassengers);
            }
        }
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
            System.out.println("Duration " + formatDuration(fs.getFlightDuration()));
            // display cabin class availability
            System.out.println("");
            System.out.println("Cabin Class Availability");
            System.out.println("Cabin Class Options: " + fs.getFccList().size());
            System.out.println("");
            boolean isAdded = false;
            for (FlightCabinClass fcc : fs.getFccList()) {
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
            System.out.println("Duration " + formatDuration(fs.getFlightDuration()));
            // display cabin class availability
            System.out.println("");
            System.out.println("Cabin Class Availability");
            System.out.println("Cabin Class Options: " + fs.getFccList().size());
            boolean isAdded = false;
            for (FlightCabinClass fcc : fs.getFccList()) {
                double returnVal = printFareInformation(fcc, numPassengers);
                if (!isAdded) {
                    totalCostForThisSingleFlight += returnVal;
                    isAdded = true;
                }
            }
            counter += 1;
        }
        
        return totalCostForThisSingleFlight;
    }
    
     public static double printConnectingFlights(List<FlightSchedule> fs1List, List<FlightSchedule> fs2List, String start, String end, int numPassengers) {
         double totalLowestFare = 0;
         for (int i = 0; i < fs1List.size(); i++) {
             // assuming that since they are return flights they can only be of the same aircraft configuration
             // so this would mean the number of FCCs for both is the same
            Pair<FlightSchedule> connectingPair = new Pair<FlightSchedule>(fs1List.get(i), fs2List.get(i));
            System.out.println("");
            System.out.println("Option #" + (i + 1));
            System.out.println("Connecting Flight Leg 1: " + start + " -> " + connectingPair.getFirst().getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getIataAirportCode());
            System.out.println("Departure Time " + connectingPair.getFirst().getDepartureTime());
            System.out.println("Arrival Time " + connectingPair.getFirst().getArrivalTime());
            System.out.println("Duration " + formatDuration(connectingPair.getFirst().getFlightDuration()));
            System.out.println("");
            System.out.println("Connecting Flight Leg 2: " + connectingPair.getSecond().getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getIataAirportCode() + " -> " + end);
            System.out.println("Departure Time " + connectingPair.getSecond().getDepartureTime());
            System.out.println("Arrival Time " + connectingPair.getSecond().getArrivalTime());
            System.out.println("Duration " + formatDuration(connectingPair.getSecond().getFlightDuration()));
            System.out.println("");
            System.out.println("Cabin Class Information");
            // display cabin class availability
            System.out.println("Cabin Class Availability");
            System.out.println("Cabin Class Options: " + connectingPair.getSecond().getFccList().size());
            System.out.println("");
            // find the cheapest fare for a cabin class
//            for (int k = 0; k < connectingPair.getFirst().getFccList().size(); k++){
//                System.out.println("DO NOTE THAT THIS IS FOR THE SAME CABIN CLASS TYPE");
//                System.out.println("");
//                System.out.println("Cabin Class Information for 1st leg");
//                double lowestFare1 = printFlightConnectingCabinClass(connectingPair.getFirst().getFccList().get(k), true);
//                System.out.println();
//                System.out.println("Cabin Class Information for 2nd leg");
//                double lowestFare2 = printFlightConnectingCabinClass(connectingPair.getSecond().getFccList().get(k), true);
//                System.out.println();
//                System.out.println("Price per passenger: $" + (lowestFare1 + lowestFare2)) ;
//                System.out.println("Total Price for all passengers: $" + ((lowestFare1 + lowestFare2) * numPassengers));
//                totalLowestFare = ((lowestFare1 + lowestFare2) * numPassengers);
//            }
         }
        return totalLowestFare;
     }
     
     public static double printFareInformation(FlightCabinClass fcc, int numPassengers) {
         CabinClassType name = fcc.getCabinClass().getCabinClassName();
         double fccPrice = fcc.getFlightSchedule().getFlightSchedulePlan().getFares().stream().filter(x -> x.getCabinClass().getCabinClassName().equals(name)).findAny().get().getFareAmount().doubleValue();
         System.out.println(name + " " + fccPrice);
         System.out.println("Price per passenger: $" + fccPrice);
            System.out.println("Total Price for all passengers: $" + (fccPrice * numPassengers));
         return fccPrice;
     }
         
            
//    public static double printFlightCabinClass(FlightCabinClass fcc, int numPassengers, boolean isConnecting) {
//        System.out.println("");
//        System.out.println("Cabin Class Type: " + fcc.getCabinClass().getCabinClassName());
//        Comparator<Fare> fareLowest = (x, y) -> (int)(x.getFareAmount().doubleValue() - y.getFareAmount().doubleValue());
//        List<Fare> faresForThisCabinClass = fcc.getFlightSchedule().getFlightSchedulePlan().getFares().stream().filter(x -> x.getCabinClass().getCabinClassName().equals(fcc.getCabinClass().getCabinClassName())).collect(Collectors.toList());       
////        Fare lowestFare = faresForThisCabinClass.stream().min(fareLowest).get();
//        if (!isConnecting){
//            System.out.println("Price per passenger: $" + faresForThisCabinClass.get(0).getFareAmount().doubleValue());
//            System.out.println("Total Price for all passengers: $" + (faresForThisCabinClass.get(0).getFareAmount().doubleValue() * numPassengers));
//        }
//        return (faresForThisCabinClass.get(0).getFareAmount().doubleValue());
//    }
    
    
    public static double printFlightConnectingCabinClass(FlightCabinClass fcc, boolean isConnecting) {
        System.out.println("");
        System.out.println("Cabin Class Type: " + fcc.getCabinClass().getCabinClassName());
//        System.out.println("No. of Fares: "  + fcc.getCabinClass().getFareList().size());
        if (isConnecting) {
            Comparator<Fare> fareLowest = (x, y) -> (int)(x.getFareAmount().doubleValue() - y.getFareAmount().doubleValue());
            List<Fare> faresForThisCabinClass = fcc.getFlightSchedule().getFlightSchedulePlan().getFares().stream().filter(x -> x.getCabinClass().getCabinClassName().equals(fcc.getCabinClass().getCabinClassName())).collect(Collectors.toList());
            Fare lowestFare = faresForThisCabinClass.stream().min(fareLowest).get();
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
                // this woud be A, B ,C ,D 
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
                System.out.print(seatLayout[i][j] + " ");
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


}
