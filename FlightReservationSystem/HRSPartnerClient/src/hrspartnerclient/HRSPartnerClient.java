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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.util.Pair;
import ws.entity.Fare;
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
            Date departureDate = null;
            String destinationAirport;
            Date returnDate = null;
            int directFlight;
            int numPassengers = 0;
            
            String startDateTimeInput;
            String endDateTimeInput = "";
            

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
                } catch (Exception e) {
                    System.out.println("Departure Date: " + returnDate);

                    System.out.println("Invalid Date format. Please try again");
                }

            if (roundTrip == 2) { // only collect return date if is return flight
                // roundtrip
                System.out.println("Enter the Return Departure Date (yyyy-MM-dd):");
                System.out.print("> ");
                String endDateInput = sc.next();
                sc.nextLine();
                System.out.println("Enter the Return Departure Time (HH:mm:ss):");
                System.out.print("> ");
                String endTimeInput = sc.next();
                String end = endDateInput + " " + endTimeInput;
                endDateTimeInput = end;
                try {
                    // check returnDate
//                    returnDate = formatDate(endDateTimeInput);
                    System.out.println("Return Date: " + returnDate.toString());
                } catch (Exception e) {
                    System.out.println("Invalid Date format. Please try again");
                }
            }

            System.out.println("Do you prefer a direct or connecting flight? Press 1 for direct, 2 for Connecting");
            System.out.print("> ");
            directFlight = sc.nextInt(); 
            sc.nextLine();
            
            List<FlightSchedule> flightScheduleList = port.partnerSearchFlight(departureAirport, startDateTimeInput, destinationAirport, endDateTimeInput, directFlight);
            System.out.println(flightScheduleList.size());
            flightScheduleList.stream().forEach(x -> {
                System.out.println(x.getArrivalTime());
                System.out.println(x.getDepartureTime());
                System.out.println(port.retrieveFaresForFlightSchedule(x.getId(), "F").size());
                
                port.retrieveFaresForFlightSchedule(x.getId(), "F").stream().forEach(y -> System.out.println(y.getFareAmount().intValue()));
            });
            
            boolean needReturn = returnDate != null;
             // there is only one size nothing is being added at all 
            //double directTo = callDirectTo(flightScheduleList, departureAirport, destinationAirport, numPassengers);
            if (needReturn) {
//                double directReturn = callDirectBack(flightScheduleList, departureAirport, destinationAirport, numPassengers);
            }
             //for connecting airports
//            if (directFlight == 2) {
//                double connectingTo =  callConnectingTo(flightScheduleList, departureAirport, destinationAirport, numPassengers);
//                if (needReturn) {
//                    double connectingReturn = callConnectingBack(flightScheduleList, departureAirport, destinationAirport, numPassengers);
//                }
//            }
           


//            try {
//                List<FlightSchedule> flightScheduleList = customerUseCaseSessionBean.searchForFlightRoutes(
//                departureAirport, departureDate, destinationAirport,
//                returnDate, directFlight);
//                boolean needReturn = returnDate != null;
//                 //there is only one size nothing is being added at all 
//                double directTo = callDirectTo(flightScheduleList, departureAirport, destinationAirport, numPassengers);
//                if (needReturn) {
//                    double directReturn = callDirectBack(flightScheduleList, departureAirport, destinationAirport, numPassengers);
//                }
//                // for connecting airports
//                if (directFlight == 2) {
//                    double connectingTo =  callConnectingTo(flightScheduleList, departureAirport, destinationAirport, numPassengers);
//                    if (needReturn) {
//                        double connectingReturn = callConnectingBack(flightScheduleList, departureAirport, destinationAirport, numPassengers);
//                    }
//                }
//
//                 // since flight schedules has been found 
//                if(isCustomer) {
//                    System.out.println("Would you to proceed to make a flight reservation?");
//                    System.out.println("Press 0 to proceed back to the main menu");
//                    System.out.println("Press 1 to proceed with flight reservation");
//                    System.out.print("> ");
//                    int choiceOne = sc.nextInt();
//                    if (choiceOne == 1) {
//                         // return these 4
//                        List<Long> finalFlightScheduleIdList = new ArrayList<Long>();
//                        List<Long> finalFlightCabinClassIdList = new ArrayList<Long>();
//                        List<List<String>> finalSeatsChoice = new ArrayList<List<String>>();
//                        List<Double> ticketPricesForEachFlightSchedulePerPerson = new ArrayList<Double>();
//                         // register the passengers
//                        System.out.println("Step 0: Input Passenger Details");
//                        System.out.println("");
//                        //  return this too
//                        List<HashMap<Integer, String>> allPDetails = enterPassengerDetails(numPassengers, sc);
//
//                        boolean connectingFound = flightScheduleList.size() > 2;
//                        double onePassengerFareDirect = 0.0;
//                        double onePassengerFareConnecting = 0.0;
//                        int choice2 = -1;
//                        if (connectingFound) {
//                            System.out.println("There is a choice between connecting and direct flights");
//                            System.out.println("Press 0 to choose connecting flights");
//                            System.out.println("Press 1 to choose direct flights");
//                            System.out.print("> ");
//
//
//                            choice2 = sc.nextInt();
//                            if (choice2 == 1) {
//                                //  means direct flight is chosen
//                                double directToOnePassengerFare = enterDetailsForDirectTo(0, 0, false, false, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
//                                onePassengerFareDirect += directToOnePassengerFare;
//                                ticketPricesForEachFlightSchedulePerPerson.add(directToOnePassengerFare);
//                                // same procedure for return flight 
//                                if (needReturn) {
//                                    double directReturnOnePassengerFare = enterDetailsForDirectTo(0, 1, true, false, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
//                                    onePassengerFareDirect += directReturnOnePassengerFare;
//                                    ticketPricesForEachFlightSchedulePerPerson.add(directReturnOnePassengerFare);
//                                }
//                            } else {
//                                 //means connecting flights is chosen
//                                 // connecting flight first leg
//                                 double connectingTo1OnePerson = enterDetailsForDirectTo(1, 2, false, true, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
//                                 onePassengerFareConnecting += connectingTo1OnePerson;
//                                 ticketPricesForEachFlightSchedulePerPerson.add(connectingTo1OnePerson);
//                                 double connectingTo2OnePerson = enterDetailsForDirectTo(2, 3, false, true, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
//                                 onePassengerFareConnecting += connectingTo2OnePerson;
//                                 ticketPricesForEachFlightSchedulePerPerson.add(connectingTo2OnePerson);
//
//                                 if (needReturn) {
//                                     double connectingReturn1OnePerson =  enterDetailsForDirectTo(1, 4, true, true, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
//                                    onePassengerFareConnecting += connectingReturn1OnePerson;
//                                    ticketPricesForEachFlightSchedulePerPerson.add(connectingReturn1OnePerson);
//                                    double connectingReturn2OnePerson = enterDetailsForDirectTo(2, 5, true, true, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
//                                    onePassengerFareConnecting += connectingReturn2OnePerson;
//                                    ticketPricesForEachFlightSchedulePerPerson.add(connectingReturn2OnePerson);
//                                 }
//                            }
//                        } else {
//                            double directToOnePassengerFare = enterDetailsForDirectTo(0, 0, false, false, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
//                            onePassengerFareDirect += directToOnePassengerFare;
//                             ticketPricesForEachFlightSchedulePerPerson.add(directToOnePassengerFare);
//                            if (needReturn) {
//                                 double directReturnOnePassengerFare =  enterDetailsForDirectTo(0, 1, true, false, flightScheduleList, departureAirport, destinationAirport, numPassengers, sc, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails);
//                                 onePassengerFareDirect += directReturnOnePassengerFare;
//                                 ticketPricesForEachFlightSchedulePerPerson.add(directReturnOnePassengerFare);
//                            }
//                        }
//
//                        //payment details 
//                        double showCase = onePassengerFareConnecting == 0.0 ? onePassengerFareDirect : choice2 == 1 ? onePassengerFareDirect : onePassengerFareConnecting;
//                        System.out.println("Last Step: Checkout");
//                        System.out.println("Total Amount Payable: " + (showCase * numPassengers));
//                        System.out.println("Enter the Credit Card Number");
//                        System.out.print("> ");
//                        String creditCardNumber = sc.next();
//                        sc.nextLine();
//                        //send to backend 
//                        customerUseCaseSessionBean.makeFlightReservation(sessionId, finalFlightScheduleIdList, finalFlightCabinClassIdList, finalSeatsChoice, allPDetails, creditCardNumber, ticketPricesForEachFlightSchedulePerPerson);
//                        System.out.println("Transaction Successful!");
//                        return;
//                    } else {
//                        // customer do not wish to make a flight reservation
//                        return;
//                    }
//                }
//
//            } catch (NoFlightFoundException e) {
//                System.out.println(e.getMessage());
//                System.out.println("Please try again!");
//            }
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
    
    public static double enterDetailsForDirectTo(int leg, int choice, boolean isReturn, boolean isConnecting, List<FlightSchedule> flightScheduleList, String departureAirport, String destinationAirport, int numPassengers, Scanner sc, List<Long> finalFlightScheduleIdList, List<Long> finalFlightCabinClassIdList, List<List<String>> finalSeatsChoice, List<HashMap<Integer, String>> passengerDetails) {
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
//            case 2: 
//                callConnectingSingle(2, flightScheduleList, numPassengers);
//                break;
//            case 3:
//                callConnectingSingle(3, flightScheduleList, numPassengers);
//                break;
//            case 4: 
//                callConnectingSingle(4, flightScheduleList, numPassengers);
//                break;
//            case 5:
//                callConnectingSingle(5, flightScheduleList, numPassengers);
//                break;
        }
        
        System.out.println();
        System.out.println("Enter the option number to indicate the flight schedule that you intend to select");
        System.out.print("> ");
        FlightSchedule flightSchedule = flightScheduleList.get(sc.nextInt() -1);
        long chosenFlightScheduleId = flightSchedule.getId();
        sc.nextLine();
//        System.out.println("flight schedule is " + flightSchedule.getId());
        finalFlightScheduleIdList.add(chosenFlightScheduleId);

        // fare should be computed here 
        System.out.println("STEP 1b: SELECT CABIN CLASS");
        System.out.println("");
        FlightCabinClass chosenFlightCabinClass = chooseFCC(flightSchedule, sc);
        long chosenFCCId = chosenFlightCabinClass.getId();
//        System.out.println("cabin class id is " + chosenFCCId);
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
        // List<String> seatsChosen = printSeatLayout(chosenFlightCabinClass, numPassengers, sc, passengerDetails);
//        finalSeatsChoice.add(seatsChosen);
        return amountForOnePassenger;
    }
    
    public static FlightCabinClass chooseFCC(FlightSchedule fs, Scanner scanner) {
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
    
    

    
    public static double callDirectTo(List<FlightSchedule> flightScheduleList, String departureAirport, String destinationAirport, int numPassengers) {
//        List<FlightSchedule> directTo = flightScheduleList.get(0);
        return printDirectFlightsTo(flightScheduleList, departureAirport, destinationAirport, numPassengers);
         
    }
    
    public static double callDirectBack(List<FlightSchedule> flightScheduleList, String departureAirport, String destinationAirport, int numPassengers) {
        System.out.println();
        System.out.println("Return Flights");
//        List<FlightSchedule> returnTo = flightScheduleList.get(1);
        return printDirectFlightsTo(flightScheduleList, destinationAirport, departureAirport, numPassengers);
    }
      
//    public double callConnectingTo(List<FlightSchedule> flightScheduleList, String departureAirport, String destinationAirport, int numPassengers) {
//        System.out.println();
//        System.out.println("Connecting Flights: TO");
//        List<FlightSchedule> directFirst= flightScheduleList.get(2);
//        List<FlightSchedule> directSecond= flightScheduleList.get(3);
//        
//        return printConnectingFlights(directFirst, directSecond, departureAirport, destinationAirport, numPassengers);
//    }
//    
//    public double callConnectingBack(List<List<FlightSchedule>> flightScheduleList, String departureAirport, String destinationAirport, int numPassengers) {
//        System.out.println();
//        System.out.println("Connecting Flights: RETURN");
//        List<FlightSchedule> returnFirst= flightScheduleList.get(4);
//        List<FlightSchedule> returnSecond= flightScheduleList.get(5);
//        return printConnectingFlights(returnFirst, returnSecond, destinationAirport, departureAirport, numPassengers);
//    }
//    
//    public void callConnectingSingle(int choice, List<List<FlightSchedule>> flightScheduleList, int numPassengers) {
//        if (choice == 2 || choice == 3) {
//            System.out.println("Connecting Flights: TO");
//            if (choice == 2) {
//                System.out.println("Leg 1 Details");
//                printConnectingFlightsSingle(flightScheduleList.get(choice), numPassengers);
//            } else {
//                System.out.println("Leg 2 Details");
//                printConnectingFlightsSingle(flightScheduleList.get(choice), numPassengers);
//            }
//        } else {
//            System.out.println("Connecting Flights: RETURN");
//            if (choice == 4) {
//                System.out.println("Leg 1 Details");
//                printConnectingFlightsSingle(flightScheduleList.get(choice), numPassengers);
//            } else {
//                System.out.println("Leg 2 Details");
//                printConnectingFlightsSingle(flightScheduleList.get(choice), numPassengers);
//            }
//        }
//    }
    
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
                double pricePerPassengerPerCCPerFS = printFlightCabinClass(fcc, numPassengers, false);
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
//            System.out.println("Duration " + formatDuration(fs.getFlightDuration()));
            // display cabin class availability
            System.out.println("");
            System.out.println("Cabin Class Availability");
            System.out.println("Cabin Class Options: " + fs.getFccList().size());
//            System.out.println("");
            boolean isAdded = false;
            for (FlightCabinClass fcc : fs.getFccList()) {
                double returnVal = printFlightCabinClass(fcc, numPassengers, true);
                if (!isAdded) {
                    totalCostForThisSingleFlight += returnVal;
                    isAdded = true;
                }
            }
            counter += 1;
        }
        
        return totalCostForThisSingleFlight;
    }
    
//     public static double printConnectingFlights(List<FlightSchedule> fs1List, List<FlightSchedule> fs2List, String start, String end, int numPassengers) {
//         double totalLowestFare = 0;
//         for (int i = 0; i < fs1List.size(); i++) {
//             // assuming that since they are return flights they can only be of the same aircraft configuration
//             // so this would mean the number of FCCs for both is the same
//            Pair<FlightSchedule, FlightSchedule> connectingPair = new Pair<FlightSchedule, FlightSchedule>(fs1List.get(i), fs2List.get(i));
//            
//            System.out.println("");
//            System.out.println("Option #" + (i + 1));
//            System.out.println("Connecting Flight Leg 1: " + start + " -> " + connectingPair.getKey().getFlightSchedulePlan().getFlight().getFlightRoute().getDestination().getIataAirportCode());
//            System.out.println("Departure Time " + connectingPair.getFirst().getDepartureTime());
//            System.out.println("Arrival Time " + connectingPair.getFirst().getArrivalTime());
//            System.out.println("Duration " + formatDuration(connectingPair.getFirst().getFlightDuration()));
//            System.out.println("");
//            System.out.println("Connecting Flight Leg 2: " + connectingPair.getSecond().getFlightSchedulePlan().getFlight().getFlightRoute().getOrigin().getIataAirportCode() + " -> " + end);
//            System.out.println("Departure Time " + connectingPair.getSecond().getDepartureTime());
//            System.out.println("Arrival Time " + connectingPair.getSecond().getArrivalTime());
//            System.out.println("Duration " + formatDuration(connectingPair.getSecond().getFlightDuration()));
//            System.out.println("");
//            System.out.println("Cabin Class Information");
//            // display cabin class availability
//            System.out.println("Cabin Class Availability");
//            System.out.println("Cabin Class Options: " + connectingPair.getSecond().getFccList().size());
//            System.out.println("");
//            // find the cheapest fare for a cabin class
//            for (int k = 0; k < connectingPair.getFirst().getFccList().size(); k++){
//                System.out.println("DO NOTE THAT THIS IS FOR THE SAME CABIN CLASS TYPE");
//                System.out.println("");
//                System.out.println("Cabin Class Information for 1st leg");
//                double lowestFare1 = printFlightConnectingCabinClass(connectingPair.getFirst().getFccList().get(k), true);
//                System.out.println();
//                System.out.println("Cabin Class Information for 2nd leg");
//                double lowestFare2 = printFlightConnectingCabinClass(connectingPair.getSecond().getFccList().get(k), true);
//                System.out.println();
//                // System.out.println("Lowest Fare Offered!");
//                System.out.println("Price per passenger: $" + (lowestFare1 + lowestFare2)) ;
//                System.out.println("Total Price for all passengers: $" + ((lowestFare1 + lowestFare2) * numPassengers));
//                totalLowestFare = ((lowestFare1 + lowestFare2) * numPassengers);
//            }
//         }
//        return totalLowestFare;
//     }
//         
            
    public static double printFlightCabinClass(FlightCabinClass fcc, int numPassengers, boolean isConnecting) {
        System.out.println("");
        System.out.println("Cabin Class Type: " + fcc.getCabinClass().getCabinClassName());
        Comparator<Fare> fareLowest = (x, y) -> (int)(x.getFareAmount().doubleValue() - y.getFareAmount().doubleValue());
        List<Fare> faresForThisCabinClass = fcc.getFlightSchedule().getFlightSchedulePlan().getFares().stream().filter(x -> x.getCabinClass().getCabinClassName().equals(fcc.getCabinClass().getCabinClassName())).collect(Collectors.toList());
        Fare lowestFare = faresForThisCabinClass.stream().min(fareLowest).get();
        if (!isConnecting){
            System.out.println("Fare Basis Code is " + lowestFare.getFareBasicCode());
            System.out.println("Price per passenger: $" + lowestFare.getFareAmount().doubleValue());
            System.out.println("Total Price for all passengers: $" + (lowestFare.getFareAmount().doubleValue() * numPassengers));
        }
        return (lowestFare.getFareAmount().doubleValue());
    }
    
    
//    public static double printFlightConnectingCabinClass(FlightCabinClass fcc, boolean isConnecting) {
//        System.out.println("");
//        System.out.println("Cabin Class Type: " + fcc.getCabinClass().getCabinClassName());
//        if (isConnecting) {
//            Comparator<Fare> fareLowest = (x, y) -> (int)(x.getFareAmount().doubleValue() - y.getFareAmount().doubleValue());
//            List<Fare> faresForThisCabinClass = fcc.getFlightSchedule().getFlightSchedulePlan().getFares().stream().filter(x -> x.getCabinClass().getCabinClassName().equals(fcc.getCabinClass().getCabinClassName())).collect(Collectors.toList());
//            Fare lowestFare = faresForThisCabinClass.stream().min(fareLowest).get();
//            return  (lowestFare.getFareAmount().doubleValue());
//        } else {
//            return 0.0;
//        }
//    }
    
    public static String formatDuration(Duration duration) {
        long totalMinutes = duration.toMinutes();
        long seconds = duration.minusMinutes(totalMinutes).getSeconds();

        String formattedDuration = String.format("%d:%02d", totalMinutes, seconds);
        
        return formattedDuration;
    }
    
    

    
}
