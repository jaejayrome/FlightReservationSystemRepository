package frsmanagementclient;

import ejb.session.stateless.EmployeeUseCaseSessionBeanRemote;
import ejb.session.stateless.ScheduleManagerUseCaseSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.Fare;
import entity.Flight;
import entity.FlightBooking;
import entity.FlightCabinClass;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.MultipleFlightSchedulePlan;
import entity.RecurrentFlightSchedulePlan;
import entity.Seat;
import entity.WeeklyFlightSchedulePlan;
import entity.SingleFlightSchedulePlan;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import util.enumerations.CabinClassType;
import util.enumerations.FlightSchedulePlanStatus;
import util.enumerations.FlightStatus;
import util.enumerations.ScheduleType;
import util.exception.DuplicateFlightMadeException;
import util.exception.InitialFlightNotInstantiatedException;
import util.exception.UpdateFlightSchedulePlanException;
import util.util.Pair;
import util.util.TwoPair;

/**
 *
 * @author jeromegoh
 */
public class ScheduleManagerUseCase {
    
    
    private EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote;
    
    private Scanner scanner;
    
    private HashMap<Integer, ScheduleType> hashMap;
   
    private ScheduleManagerUseCaseSessionBeanRemote scheduleManagerUseCaseSessionBeanRemote;
    
    private HashMap<String, DayOfWeek> dayOfWeekMap; 
    
    private HashMap<Integer, CabinClassType> ccTypeMAP;
    
    public ScheduleManagerUseCase() {
    }
    
    public ScheduleManagerUseCase(EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote, ScheduleManagerUseCaseSessionBeanRemote scheduleManagerUseCaseSessionBeanRemote) {
      this.scanner = new Scanner(System.in);
      this.employeeUseCaseSessionBeanRemote = employeeUseCaseSessionBeanRemote;
      this.hashMap = new HashMap<Integer, ScheduleType>();
      this.scheduleManagerUseCaseSessionBeanRemote = scheduleManagerUseCaseSessionBeanRemote;
      this.dayOfWeekMap = new HashMap<String, DayOfWeek>();
      this.ccTypeMAP = new HashMap<Integer, CabinClassType>();
      this.initialiseMap();
      this.initialiseWeekMap();
      this.initialiseCCMap();
    }
    
    /*
    we should create flight first before create flight schedule plan 
    For Flight, we would have to receive user 
    we would need to ask for the user input
    then after that check whether is the thing one way 
    then after that we would have to associate it with the flight route
    */
    public void createFlight() {
        System.out.println("Step 1: Enter the Flight Number");
        System.out.print("> ");
        String flightNumber = scanner.next();
        scanner.nextLine();
        // make the flight status disbaled until a flight schedule plan is attached to it 
        
        System.out.println("Step 2: Enter a name for an aircraft configuration for this flight");
        System.out.print("> ");
        String configurationName = scanner.nextLine();
        
        
        System.out.println("Step 3: Enter the Origin IATA Airport Code for the flight");
        System.out.print("> ");
        String originCity = scanner.nextLine();
        
        System.out.println("Step 4: Enter the Destination IATA Airport Code for the flight");
        System.out.print("> ");
        String destinationCity = scanner.nextLine();
        
        // catch exception if there's for initial flight 
        try {
            long haveReturnFlight = scheduleManagerUseCaseSessionBeanRemote.createNewFlight(flightNumber, configurationName, originCity, destinationCity, false, -1);
            System.out.println("Flight has been successfully created!");
            
            if (haveReturnFlight != -1) {
                System.out.println("A Return Flight Route has been detected!");
                System.out.println("Press '1' if you would like to create this return flight");
                System.out.println("Press '0' if you do not wish to do so");
                System.out.print("> ");
                boolean createFlight = scanner.nextInt() == 1 ? true : false;
                // checks whether user would want to create this
                if (createFlight) createFlight(configurationName, destinationCity, originCity, haveReturnFlight);
                if (!createFlight) return; 
            }
                
        } catch (InitialFlightNotInstantiatedException e) {
            System.out.println(e.getMessage());
        } catch (DuplicateFlightMadeException exception) {
            System.out.println(exception.getMessage());
        }
    }
    
    // used to create return flight with the same aircraft configuration
    public void createFlight(String configurationName, String originCity, String destinationCity, long id) {
        System.out.println("Enter the Flight Number For the Return Flight");
        System.out.print("> ");
        String flightNumber = scanner.next();
        
        try {
            scheduleManagerUseCaseSessionBeanRemote.createNewFlight(flightNumber, configurationName, originCity, destinationCity, true, id);
            System.out.println("Transaction \u001B[32mSuccessful\u001B[0m!");
        } catch (InitialFlightNotInstantiatedException e) {
            System.out.println(e.getMessage());
        } catch (DuplicateFlightMadeException exception) {
            System.out.println(exception.getMessage());
        }
    }
    
    public void viewAllFlights() {
        // sorting has already been done by pairs then by flight number in jpql
        List<Flight> flightList = scheduleManagerUseCaseSessionBeanRemote.viewAllFlights();
        flightList.stream().forEach(x -> printSingleFlight(x));
    }
    
    // everything down to the cabin class have been printed out
    public void viewSpecificFlightDetails() {
        System.out.println("Enter the flight number");
        System.out.print("> ");
        String flightNumber = scanner.next();
        Flight flight = scheduleManagerUseCaseSessionBeanRemote.viewSpecificFlightDetails(flightNumber);
        printSpecificSingleFlight(flight);
        
    }
    
    // give them a few metrics to choose from 
    public void updateFlight() {
        System.out.println("");
        System.out.println("Enter the Flight Number of the Flight that you want to update");
        System.out.print("> ");
        String flightNumber = scanner.next();
        System.out.println("What would you like to update?");
        System.out.println("Press 1 to update Flight Number");
        System.out.println("Press 2 to update Flight Status");
        System.out.print("> ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1: 
                System.out.println("Enter the new flight number: ");
                System.out.print("> ");
                String newFlightNumber = scanner.next();
                scheduleManagerUseCaseSessionBeanRemote.updateFlightNumber(flightNumber, newFlightNumber);
                break;
            case 2: 
                System.out.println("Choose Your New Flight Status");
                System.out.println("Press 1 for active flight status");
                System.out.println("Press 2 for disabled flight Status");
                System.out.print("> ");
                int choose = scanner.nextInt();
                FlightStatus flightStatus = FlightStatus.ACTIVE;
                if (choose == 2) {
                    flightStatus = FlightStatus.DISABLED;
                }
                scheduleManagerUseCaseSessionBeanRemote.updateFlightStatus(flightNumber, flightStatus);
                break;
        }
    }
    
    // do later
    public void deleteFlight() {
        System.out.println("");
        System.out.println("DO NOTE: Flight would only be deleted if it doesn't have a flight schedule plan");
        System.out.println("Else, it would be marked as DISABLED instead");
        System.out.println("Enter the Flight Number of the Flight that you want to delete/disable");
        System.out.print("> ");
        String flightNumber = scanner.next();
        boolean disabledOrDelete = scheduleManagerUseCaseSessionBeanRemote.deleteFlight(flightNumber);
        if (disabledOrDelete) {
            System.out.println("You have successfully disabled/delete the flight");
        } else {
            System.out.println("Processing failed as you have entered an invalid flight number");
        }
    }
    
    // 
    public void createFlightSchedulePlan() {
        // can create one flight schedule plan it must be associated with a flight 
        // a flight might not have a schedule plan but a flight schedule plan
        System.out.println("Step 1: Choose Your Schedule Type");
        System.out.println("");
        // enforce that there can be only one schedule type for all schedules within a FSP
        printAllScheduleTypes();
        System.out.print("> ");
        int choice = scanner.nextInt();
        ScheduleType chosen = this.hashMap.get(choice);
        
        System.out.println("");
        System.out.println("Step 2: Enter Flight Number of Associated Flight");
        System.out.print("> ");
        String flightNumber = scanner.next();
 
        int number = 1; 
        Duration duration = null;
        List<Date> departureDateList = new ArrayList<Date>();
        Date endDate = null;
        int frequency = 0;
        
        System.out.println("");
        System.out.println("Step 3: Date Specification For Chosen Flight Schedule Type");
        
        switch (chosen) {
            case SINGLE:
                inputForScheduleType(number, flightNumber, departureDateList);
                duration = checkDuration();
                break;
            case MULTIPLE:
                System.out.println("Enter the number of flight schedules to be associated with: ");
                System.out.print("> ");
                number = scanner.nextInt();
                inputForScheduleType(number, flightNumber, departureDateList);
                duration = checkDuration();
                break;
            case RECURRENT:
                inputForScheduleType(number, flightNumber, departureDateList);
                duration = checkDuration();
                endDate = checkEndDate();
                frequency = checkFrequency(false);
                break;
            case RECURRENT_WEEK:
                inputForScheduleType(number, flightNumber, departureDateList);
                duration = checkDuration();
                endDate = checkEndDate();
                System.out.println("Enter the day of the week that you would like this schedule to be based on (e.g: MON)");
                System.out.print("> ");
                String abbreviation = scanner.next();
                scanner.nextLine();
                Date updatedDate = findNearestDayOfWeek(departureDateList, endDate, abbreviation); 
                departureDateList.set(0, updatedDate);
                frequency = checkFrequency(true);
                break;
        }
        
        System.out.println("");
        System.out.println("Step 4: Fare Input for All Available Cabin Classes");
        // prompted to enter at least one fare for each 
        HashMap<CabinClassType, List<Fare>> faresForCabinClassList = new HashMap<CabinClassType, List<Fare>>();
        AircraftConfiguration airCraftConfiguration = scheduleManagerUseCaseSessionBeanRemote.viewSpecificFlightDetails(flightNumber).getAircraftConfiguration();
        // circumvent lazy fetching (lazy fetching would still fail outside of the persistence context) (done)
        int init = airCraftConfiguration.getCabinClassList().size();
        List<CabinClass> cabinClassList = airCraftConfiguration.getCabinClassList();
        int cabinClassNumber = cabinClassList.size();
        for (int i = 0 ; i < cabinClassNumber; i++) {
            // doesn't matter because they would be prompted to key in at least one 
            CabinClass cabinClass = cabinClassList.get(i);
            System.out.println("Enter the number of fares that you want to enter for " + cabinClass.getCabinClassName().name() + " class on " + flightNumber);
            System.out.print("> ");
            int nFares = scanner.nextInt();
            List<Fare> fareList = makeFares(nFares, cabinClass, flightNumber);
            faresForCabinClassList.put(cabinClass.getCabinClassName(), fareList);
        }
        
       
       long promptReturnFlightSchedule = scheduleManagerUseCaseSessionBeanRemote.createNewFlightSchedulePlan(flightNumber, departureDateList, duration, endDate, frequency, faresForCabinClassList, false, -1, null);
       if (promptReturnFlightSchedule != -1) {
            System.out.println("An existing complementary flight schedule plan has been detected!");
            System.out.println("Press '1' if you would like to create this return flight schedule plan");
            System.out.print("> ");
            boolean createFlight = scanner.nextInt() == 1 ? true : false;
            // checks whether user would want to create this
            if (createFlight) {
                System.out.println("To proceed, please enter your layover duration");
                Duration layover = checkDuration();
                scheduleManagerUseCaseSessionBeanRemote.createNewFlightSchedulePlan(flightNumber, departureDateList, duration, endDate, frequency, faresForCabinClassList, true, promptReturnFlightSchedule, layover);
            }
            System.out.println("Transaction \u001B[32mSuccessful\u001B[0m!");
       }
       
    }
    
    public void viewAllFlightSchedulePlan() {
        List<FlightSchedulePlan> flightSchedulePlanList = scheduleManagerUseCaseSessionBeanRemote.viewAllFlightSchedulePlan();
        flightSchedulePlanList.forEach(x -> {
            System.out.println("");
            printSpecificFlightSchedulePlan(x, false);
        });
    }
    
    public void viewFlightSchedulePlanDetails() {
        System.out.println("Enter the flight number");
        System.out.print("> ");
        String flightNumber = scanner.next();
        Flight flight = scheduleManagerUseCaseSessionBeanRemote.viewSpecificFlightDetails(flightNumber);
        
        // print the flight details first 
        printSpecificSingleFlight(flight);
        
        // ask for the flight schedule that user wants (issue might happen) heret
        FlightSchedulePlan flightSchedulePlan = askForWhichFlightSchedule(flight);
        
        // print specific flight schedule plan
        printSpecificFlightSchedulePlan(flightSchedulePlan, true);
    }
    
    
    
    // need customer side to be done before able to complete
    // need to check whether something has been reserved
    public void updateFlightSchedulePlan() {
        System.out.println("Enter the flight number");
        System.out.print("> ");
        String flightNumber = scanner.next();
        
        // retreieve all flight schedule plan for this flight and ask which one wanna eddit
        Flight flight = scheduleManagerUseCaseSessionBeanRemote.viewSpecificFlightDetails(flightNumber);
        FlightSchedulePlan flightSchedulePlan = askForWhichFlightSchedule(flight);
        System.out.println("yoyo " + flightSchedulePlan);

        if (flightSchedulePlan != null) {
            

            boolean changesFinalized = false;
            while (!changesFinalized) {
                
                System.out.println("Choose the following options:");
                System.out.println("1 to Add Flight Schedule");
                System.out.println("2 to Delete Flight Schedule");
                System.out.println("3 to Update Fares of the Cabin Classes");
                System.out.println("0 to Save Changes");
                System.out.print("> ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        Date newStartDate = dateInitializationForFlightSchedule();
                        Duration newDuration = checkDuration();
                        FlightSchedule newFS = new FlightSchedule(newStartDate, newDuration, computeArrivalTime(newStartDate, newDuration));
                        // add a new flight schedule inside
                        flightSchedulePlan.getFlightScheduleList().add(newFS);
                        newFS.setFlightSchedulePlan(flightSchedulePlan);
                        // havent generate FCC yet
                        break;
                    case 2:
                        // Delete Flight Schedule logic
                        FlightSchedule chosenFS = chooseFlightScheduleFromFlightSchedulePlanToDelete(flightSchedulePlan);
                        flightSchedulePlan.getFlightScheduleList().remove(chosenFS);
                        // disassociate it not here but within the peristence context
                        break;
                    case 3:
                        // Update Fares logic
                        System.out.println("STEP 1: Choose Cabin Class");
                        CabinClassType chosenCC = chooseCabinClassToUpdateFare();
                        
                        System.out.println("Enter the number of fares for the selected class:");
                        int numFares = scanner.nextInt();
                        scanner.nextLine();

                        List<TwoPair<String, BigDecimal>> updatedFares = new ArrayList<TwoPair<String, BigDecimal>>();
                        for (int i = 0; i < numFares; i++) {
                            Fare fare = new Fare();

                            System.out.println("Enter the fare basis code:");
                            System.out.print("> ");
                            String fbc = scanner.nextLine().trim();
                            

                            System.out.println("Enter the updated fare amount:");
                            System.out.print("> ");
                            double amt = scanner.nextDouble();
                            
                            updatedFares.add(new TwoPair<String, BigDecimal>(fbc, new BigDecimal(amt)));
                        }
                        for (TwoPair<String, BigDecimal> newInfo : updatedFares) {
                            for (Fare f : flightSchedulePlan.getFares()) {
                                if (f.getCabinClass().getCabinClassName() == chosenCC && f.getFareBasicCode().equals(newInfo.getFirst())) {
                                    f.setFareAmount(newInfo.getSecond());
                                }
                            }
                        }
                        break;
                    case 0:
                        changesFinalized = true;
                        try {
                            // make call to backend 
                            scheduleManagerUseCaseSessionBeanRemote.updateFlightSchedulePlan(flightSchedulePlan);
                        } catch (UpdateFlightSchedulePlanException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose again.");
                        break;
                }
            }
        } else {
            System.out.println("Flight schedule plan not found.");
        }
        
    }
    
    
    public void deleteFlightSchedulePlan() {
        // give the flight scheudle plan id 
        System.out.println("Enter the flight number");
        System.out.print("> ");
        String flightNumber = scanner.next();
        Flight flight = scheduleManagerUseCaseSessionBeanRemote.viewSpecificFlightDetails(flightNumber);
        FlightSchedulePlan flightSchedulePlan = askForWhichFlightSchedule(flight);
         scheduleManagerUseCaseSessionBeanRemote.deleteFlightSchedulePlan(flightSchedulePlan);   
    }
    
    public void viewSeatsInventory() {
        System.out.println("Enter the flight number");
        System.out.print("> ");
        String flightNumber = scanner.next();
        Flight flight = scheduleManagerUseCaseSessionBeanRemote.viewSpecificFlightDetails(flightNumber);
        
        // print the flight details first 
        printSpecificSingleFlight(flight);
        
        // ask for the flight schedule that user wants (issue might happen) heret
        FlightSchedulePlan flightSchedulePlan = askForWhichFlightSchedule(flight);
        
        // chosen FS
        FlightSchedule chosenFS = chooseFlightScheduleFromFlightSchedulePlan(flightSchedulePlan);
        
        // print the cabin class information for the flight schedule
        printSpecificCabinClassOfFlightSchedule(chosenFS);
    }
    
    public void viewFlightReservations() {
        System.out.println("Enter the flight number");
        System.out.print("> ");
        String flightNumber = scanner.next();
        Flight flight = scheduleManagerUseCaseSessionBeanRemote.viewSpecificFlightDetails(flightNumber);
        
        // print the flight details first 
        printSpecificSingleFlight(flight);
        
        // ask for the flight schedule that user wants (issue might happen) heret
        FlightSchedulePlan flightSchedulePlan = askForWhichFlightSchedule(flight);
        
        // chosen FS
        FlightSchedule chosenFS = chooseFlightScheduleFromFlightSchedulePlan(flightSchedulePlan);
        
        // print out all flight reservations
        printFlightReservationsOfFlightSchedule(chosenFS);
    }
    
    public CabinClassType chooseCabinClassToUpdateFare() {
        printAllCabinClassTypes();
        System.out.println("Enter choice");
        System.out.print("> ");
        int choice = scanner.nextInt();
        
        return this.ccTypeMAP.get(choice);
    }
    
    
    
    // non-schedules only need date and duration
    public void inputForScheduleType(int n, String flightNumber, List<Date> departureDateList) {
      for (int i = 1; i <= n; i++) {   
          System.out.println("");
          System.out.println("Flight Schedule #" + i + " for " + flightNumber); 
          Date date = dateInitialisation();
         if (date != null) {
             departureDateList.add(date);
             System.out.println(date.toString());
         }
      }
    }
    
    public FlightSchedulePlan askForWhichFlightSchedule(Flight flight) {
        System.out.println("Flight Schedule Plans for Flight " + flight.getFlightNumber());
        int init = flight.getFlightSchedulePlanList().size();
        int counter = 1;
        System.out.println("");
        for (FlightSchedulePlan fp : flight.getFlightSchedulePlanList()) {
            System.out.println("");
            System.out.println();
            printSpecificFlightSchedulePlan(fp, false);
            System.out.println("Press " + counter + " for to view this flight schedule plan in detail");
            counter += 1;
        }
        
        System.out.println("");
        System.out.print("> ");
        int choice = scanner.nextInt();
        return flight.getFlightSchedulePlanList().get(choice - 1);
    }
    
    // this can be made nicer
    public void printAllScheduleTypes() {
        System.out.println("Press 1 for Single Schedule Type");
        System.out.println("Press 2 for Multiple Schedule Type");
        System.out.println("Press 3 for Recurrent Schedule Type");
        System.out.println("Press 4 for Recurrent (Week) Schedule Type");
    }
    
    public void printFlightReservationsOfFlightSchedule(FlightSchedule flightSchedule) {
        int counter = 1; 
        List<TwoPair<Double, Seat>> flightBookingInformation = new ArrayList<TwoPair<Double, Seat>>();
        flightSchedule.getFlightBookingList().stream().forEach(x -> {
           double costForThisFlightBooking = x.getFlightLegCost().doubleValue();
           x.getReservedSeats().stream().forEach(y -> {
               flightBookingInformation.add(new TwoPair<Double, Seat>(costForThisFlightBooking, y));
           });
       });
       
       // sort the list in terms of ascending order
       Comparator<TwoPair<Double, Seat>> sortSeatNumber = (x, y) -> x.getSecond().getSeatNumber().compareTo(y.getSecond().getSeatNumber());
       flightBookingInformation.sort(sortSeatNumber);
       
        for (TwoPair<Double, Seat> flightR : flightBookingInformation) {
            System.out.println("Flight Reservation #" + counter);
            System.out.println("Seat Number: " + flightR.getSecond().getSeatNumber());
            System.out.println("Passenger First Name: " + flightR.getSecond().getPassenger().getFirstName());
            System.out.println("Passenger Last Name: " + flightR.getSecond().getPassenger().getFirstName());
            System.out.println("Passenger Passport Number: " + flightR.getSecond().getPassenger().getPassportNumber());
            System.out.println("Fare Amount Paid By Passenger: " + flightR.getFirst());
            counter += 1;
        }
    }
    
    public void printSingleFlight(Flight flight) {
        System.out.println("");
        System.out.println("Flight Number: " + flight.getFlightNumber());
        System.out.println("Flight Aircraft Configuration Name: " + flight.getAircraftConfiguration().getConfigurationName());
        System.out.println("Flight Origin City: " + flight.getFlightRoute().getOrigin().getCity());
        System.out.println("Flight Destination City: " + flight.getFlightRoute().getDestination().getCity());
        System.out.println("Flight Status: " + flight.getStatus().name());
        System.out.println("");
    }
    
    public void printSpecificSingleFlight(Flight flight) {
        printSingleFlight(flight);
        int init = flight.getAircraftConfiguration().getCabinClassList().size();
        System.out.println("Number of Cabin Classes Available " + init);
        flight.getAircraftConfiguration().getCabinClassList().forEach(x -> printSingleCabinClass(x));
    }
    
    public void printMoreSpecific(FlightSchedulePlan flightSchedulePlan) {
        System.out.println("Origin Airport: " + flightSchedulePlan.getFlight().getFlightRoute().getOrigin().getAirportName());
        System.out.println("Origin City: " + flightSchedulePlan.getFlight().getFlightRoute().getOrigin().getCity());
        System.out.println("Origin Country: " + flightSchedulePlan.getFlight().getFlightRoute().getOrigin().getCountry());
        
        System.out.println("");
        System.out.println("Destination Airport: " + flightSchedulePlan.getFlight().getFlightRoute().getDestination().getAirportName());
        System.out.println("Destination City: " + flightSchedulePlan.getFlight().getFlightRoute().getDestination().getCity());
        System.out.println("Destination Country: " + flightSchedulePlan.getFlight().getFlightRoute().getDestination().getCountry());
        
        int init = flightSchedulePlan.getFlightScheduleList().size();
        System.out.println("");
        System.out.println("Fare Information");
        flightSchedulePlan.getFares().forEach(y -> {
            System.out.println("Fare Basis Code: " + y.getFareBasicCode());
            System.out.println("Fare Amount: " + y.getFareAmount());
            System.out.println("");
        });

        flightSchedulePlan.getFlightScheduleList().forEach(x -> printSpecificFlightSchedule(x));
    }
    
    public void printSpecificFlightSchedule(FlightSchedule flightSchedule) {
        System.out.println("");
        System.out.println("Flight Schedule Departure Time: " + flightSchedule.getDepartureTime().toString());
        System.out.println("Flight Schedule Arrival Time: " + flightSchedule.getArrivalTime().toString());
    }
    
    public void printSpecificCabinClassOfFlightSchedule(FlightSchedule flightSchedule) {
        System.out.println("");
        
        int totalAvailableSeats = 0;
        int totalReservedSeats = 0;
        int totalBalanceSeats = 0;
        
        for (FlightCabinClass fcc : flightSchedule.getFccList()) {
            System.out.println("Number of Available Seats " + fcc.getNumAvailableSeats().intValue());
            System.out.println("Number of Reserved Seats " + fcc.getNumReservedSeats().intValue());
            System.out.println("Number of Balance Seats " + fcc.getNumBalanceSeats().intValue());
            
            totalAvailableSeats += fcc.getNumAvailableSeats().intValue();
            totalReservedSeats += fcc.getNumReservedSeats().intValue();
            totalBalanceSeats += fcc.getNumBalanceSeats().intValue();
        }
        
        System.out.println("Statistics Of All Cabin Classes for Flight Schedule");
        System.out.println("Number of Available Seats " + totalAvailableSeats);
        System.out.println("Number of Reserved Seats " + totalReservedSeats);
        System.out.println("Number of Balance Seats " + totalBalanceSeats);
    }
    
    public FlightSchedule chooseFlightScheduleFromFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        System.out.println("Choose a Flight Schedule Plan");
        int counter = 1;
        for (FlightSchedule fs : flightSchedulePlan.getFlightScheduleList()) {
            printSpecificFlightSchedule(fs);
            System.out.println("Press " + counter + " to view this flight schedule in detail");
            counter += 1;
        }
        
        System.out.println("Enter Choice");
        System.out.print("> ");
        int choice = this.scanner.nextInt();
        return flightSchedulePlan.getFlightScheduleList().get(choice - 1);
    } 
    
    public FlightSchedule chooseFlightScheduleFromFlightSchedulePlanToDelete(FlightSchedulePlan flightSchedulePlan) {
        System.out.println("Choose a Flight Schedule Plan");
        int counter = 1;
        for (FlightSchedule fs : flightSchedulePlan.getFlightScheduleList()) {
            printSpecificFlightSchedule(fs);
            System.out.println("Press " + counter + " to delete this flight schedule");
            counter += 1;
        }
        
        System.out.println("Enter Choice");
        System.out.print("> ");
        int choice = this.scanner.nextInt();
        return flightSchedulePlan.getFlightScheduleList().get(choice - 1);
    } 
    
    
    public void printSpecificFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan, boolean printMoreSpecific) {
        System.out.println("Flight Schedule Plan Information");
        System.out.println("");
        System.out.println("Flight Number: " + flightSchedulePlan.getFlight().getFlightNumber());
        if (flightSchedulePlan instanceof SingleFlightSchedulePlan) {
            SingleFlightSchedulePlan singlePlan = (SingleFlightSchedulePlan) flightSchedulePlan;
            System.out.println("Schedule Plan Type: " + "Single");
            System.out.println("Schedule Plan Status: " + singlePlan.getStatus());
            System.out.println("Number of Schedules Under this Plan: " + singlePlan.getFlightScheduleList().size());
        } else if (flightSchedulePlan instanceof MultipleFlightSchedulePlan) {
            MultipleFlightSchedulePlan multiPlan = (MultipleFlightSchedulePlan) flightSchedulePlan;
            System.out.println("Schedule Plan Type: " + "Multiple");
            System.out.println("Schedule Plan Status: " + multiPlan.getStatus());
            System.out.println("Number of Schedules Under this Plan: " + multiPlan.getFlightScheduleList().size());
        } else if (flightSchedulePlan instanceof RecurrentFlightSchedulePlan) {
            RecurrentFlightSchedulePlan recurrentPlan = (RecurrentFlightSchedulePlan) flightSchedulePlan;
            System.out.println("Schedule Plan Type: " + "Recurrent");
            System.out.println("Schedule Plan Status: " + recurrentPlan.getStatus());
            System.out.println("Number of Schedules Under this Plan: " + recurrentPlan.getFlightScheduleList().size());
            System.out.println("Recurrent End Date & Time: " + recurrentPlan.getEndDate().toString());
            System.out.println("Recurrent Frequency (in days): " + recurrentPlan.getFrequency().intValue());  
        } else {
            WeeklyFlightSchedulePlan recurrentWeeklyPlan = (WeeklyFlightSchedulePlan) flightSchedulePlan;
            System.out.println("Schedule Plan Type: " + "Recurrent Weekly");
            System.out.println("Schedule Plan Status: " + recurrentWeeklyPlan.getStatus());
            System.out.println("Number of Schedules Under this Plan: " + recurrentWeeklyPlan.getFlightScheduleList().size());
            System.out.println("Recurrent End Date & Time: " + recurrentWeeklyPlan.getEndDate().toString());
            System.out.println("Recurrent Frequency (in days): " + recurrentWeeklyPlan.getFrequency().intValue()); 
        }
        
        if (printMoreSpecific) {
                System.out.println("Flight Schedules Information");
                System.out.println("");
                printMoreSpecific(flightSchedulePlan);
        }
    }
    
    
    
    public List<Fare> makeFares(int nFares, CabinClass cabinClass, String flightNumber) {
        List<Fare> fareList = new ArrayList<Fare>();
        for (int i = 0; i < nFares; i++) {
           System.out.println("Enter the Fare Amount for Fare #" + (i+1) + " for " + cabinClass.getCabinClassName().name() + " class " + flightNumber); 
           System.out.print("> ");
           int fareAmt = scanner.nextInt();
           Fare fare = new Fare(generateRandomFareBasisCode(), new BigDecimal(fareAmt), cabinClass);
           fareList.add(fare);
        }
        return fareList;
    }
    
    public String generateRandomFareBasisCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        
        // Generate the first 3 characters (alphabets)
        for (int i = 0; i < 3; i++) {
            char randomChar = (char) ('A' + random.nextInt(26)); // A-Z
            sb.append(randomChar);
        }
        
        // Generate the last 4 characters (digits)
        for (int i = 0; i < 4; i++) {
            int randomDigit = random.nextInt(10); // 0-9
            sb.append(randomDigit);
        }

        return sb.toString();
    }
    
    public void printSingleCabinClass(CabinClass cabinClass) {
        System.out.println("Cabin Class Type: " + cabinClass.getCabinClassName().name());
        System.out.println("Number of Aisles: " + cabinClass.getNumAisles());
        System.out.println("Number of Rows: " + cabinClass.getNumRows());
        System.out.println("Number of Seats Abreast: " + cabinClass.getNumSeatsAbreast());
        System.out.println("Seating Configuration: " + cabinClass.getSeatingConfiguration());
        System.out.println("");
    }
    
    public Date dateInitialisation() {
        System.out.println("Commencement Date is the date at which the first flight would commence");
        System.out.println("Departure Time is the time that the flight is scheduled to leave");
        System.out.println("Flight Duration is the time taken to reach the destination from the origin");
        System.out.println("");
        System.out.println("Enter the commencement date (yyyy-MM-dd):");
        System.out.print("> ");
        String startDateInput = scanner.next();
        scanner.nextLine();
        System.out.println("");
        System.out.println("Enter the commencement time (HH:mm:ss):");
        System.out.print("> ");
        String timeInput = scanner.next();
        String dateTimeInput = startDateInput + " " + timeInput;
        Date date = formatDate(dateTimeInput);
        return date;
    }
    
    public Date dateInitializationForFlightSchedule() {
        System.out.println("Enter the new departure date and time for the flight schedule");
        System.out.println("Commencement Date is the date at which the flight would commence");
        System.out.println("Departure Time is the time that the flight is scheduled to leave");
        System.out.println("");

        System.out.println("Enter the new departure date (yyyy-MM-dd):");
        System.out.print("> ");
        String startDateInput = scanner.next();
        scanner.nextLine();

        System.out.println("");
        System.out.println("Enter the new departure time (HH:mm:ss):");
        System.out.print("> ");
        String timeInput = scanner.next();

        String dateTimeInput = startDateInput + " " + timeInput;
        Date date = formatDate(dateTimeInput);
        return date;
}
    
    public Duration checkDuration() {
        System.out.println("");
        System.out.println("Duration is the time taken to reach the destination from the origin");
        System.out.println("");
        System.out.println("Enter the hours"); 
        System.out.print("> ");
        int hours = Integer.parseInt(scanner.next());
        scanner.nextLine();
        System.out.println("Enter the minutes");
        System.out.print("> ");
        int minutes = Integer.parseInt(scanner.next());
        
        int totalMinutes = (hours * 60) + minutes;
        long seconds = totalMinutes * 60;
        Duration duration = Duration.ofSeconds(seconds);
        
        return duration;
    }
    
    public int checkFrequency(boolean isRecurrentWeek) {
        System.out.println("Frequency is the how often that the same flight would follow this schedule");
        System.out.println("");
        int daysOfFrequency = 0;
        if (!isRecurrentWeek) {
            System.out.println("Enter the frequency of flights (in Days)");
            System.out.print("> ");
            daysOfFrequency = scanner.nextInt();

        } else { 
            daysOfFrequency = 7;
        }
        return daysOfFrequency;
    }
    
    public Date checkEndDate() {
        System.out.println("End Date is the date and time at which the recurrent schedule would come to an end");
        System.out.println("");
        System.out.println("Enter the end date (yyyy-MM-dd):");
        System.out.print("> ");
        String startDateInput = scanner.next();
        scanner.nextLine();
        System.out.println("");
        System.out.println("Enter the end time (HH:mm:ss):");
        System.out.print("> ");
        String timeInput = scanner.next();
        String dateTimeInput = startDateInput + " " + timeInput;
        Date date = formatDate(dateTimeInput);
        return date;
    }
    
    public Date formatDate(String dateTimeInput) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeInput, formatter);
        ZoneId zoneId = ZoneId.of("Asia/Singapore");
        Date date = Date.from(dateTime.atZone(zoneId).toInstant());
        return date;
    }

    public void initialiseMap() {
        hashMap.put(1, ScheduleType.SINGLE);
        hashMap.put(2, ScheduleType.MULTIPLE);
        hashMap.put(3, ScheduleType.RECURRENT);
        hashMap.put(4, ScheduleType.RECURRENT_WEEK);
    }
    
    public void initialiseWeekMap() {
        dayOfWeekMap.put("MON", DayOfWeek.MONDAY);
        dayOfWeekMap.put("TUE", DayOfWeek.TUESDAY);
        dayOfWeekMap.put("WED", DayOfWeek.WEDNESDAY);
        dayOfWeekMap.put("THU", DayOfWeek.THURSDAY);
        dayOfWeekMap.put("FRI", DayOfWeek.FRIDAY);
        dayOfWeekMap.put("SAT", DayOfWeek.SATURDAY);
        dayOfWeekMap.put("SUN", DayOfWeek.SUNDAY);
    }
    
    
    public Date findNearestDayOfWeek(List<Date> departureDateList, Date endDate, String abbreviation) {
        
        DayOfWeek targetDayOfWeek = dayOfWeekMap.get(abbreviation);
        Date startDate = departureDateList.get(0);
        Instant startInstant = startDate.toInstant();
        Instant endInstant = endDate.toInstant();

        LocalDateTime startLocalDateTime = startInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endLocalDateTime = endInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (startLocalDateTime.isAfter(endLocalDateTime)) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        LocalDateTime currentLocalDateTime = startLocalDateTime;

        while (currentLocalDateTime.isBefore(endLocalDateTime)) {
            if (currentLocalDateTime.getDayOfWeek() == targetDayOfWeek) {
                Instant resultInstant = currentLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
                return Date.from(resultInstant);
            }
            currentLocalDateTime = currentLocalDateTime.plusDays(1);
        }

        currentLocalDateTime = startLocalDateTime.plusDays(7 - startLocalDateTime.getDayOfWeek().getValue() + targetDayOfWeek.getValue());

        Instant resultInstant = currentLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
        
        // update the departureDateList to that time
        return (resultInstant.isBefore(endInstant) || resultInstant.equals(endInstant)) ? Date.from(resultInstant) : null;
    }
    
    public Date computeArrivalTime(Date departureTime, Duration flightDuration) {
        Instant departureInstant = departureTime.toInstant();
        LocalDateTime departureDateTime = LocalDateTime.ofInstant(departureInstant, ZoneId.systemDefault());
        LocalDateTime arrivalDateTime = departureDateTime.plus(flightDuration);
        Instant arrivalInstant = arrivalDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date arrivalTime = Date.from(arrivalInstant);
    
        return arrivalTime;
    }
    
    public void printAllCabinClassTypes() {
        System.out.println("Press 1 for First (F) Class");
        System.out.println("Press 2 for Business (J) Class");
        System.out.println("Press 3 for Premium Economy (W) Class");
        System.out.println("Press 4 for Economy (Y) Class");
    }
    
    public void initialiseCCMap() {
        ccTypeMAP.put(1, CabinClassType.F);
        ccTypeMAP.put(2, CabinClassType.J);
        ccTypeMAP.put(3, CabinClassType.W);
        ccTypeMAP.put(4, CabinClassType.Y);
    }
         

   
}