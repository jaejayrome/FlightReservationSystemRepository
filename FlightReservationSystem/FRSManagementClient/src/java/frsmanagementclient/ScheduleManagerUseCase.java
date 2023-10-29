/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeUseCaseSessionBeanRemote;
import ejb.session.stateless.ScheduleManagerUseCaseSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.Fare;
import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.MultipleFlightSchedulePlan;
import entity.RecurrentFlightSchedulePlan;
import entity.RecurrentWeeklyFlightSchedulePlan;
import entity.SingleFlightSchedulePlan;
import java.math.BigDecimal;
import java.time.Duration;
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
import javax.ejb.EJB;
import util.enumerations.CabinClassType;
import util.enumerations.ScheduleType;

/**
 *
 * @author jeromegoh
 */
public class ScheduleManagerUseCase {
    
    @EJB
    private ScheduleManagerUseCaseSessionBeanRemote scheduleManagerUseCaseSessionBeanRemote;
    
    private EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote;
    
    private Scanner scanner;
    
    private HashMap<Integer, ScheduleType> hashMap;
    
    public ScheduleManagerUseCase() {
     
    }
    
    public ScheduleManagerUseCase(EmployeeUseCaseSessionBeanRemote employeeUseCaseSessionBeanRemote) {
      this.scanner = new Scanner(System.in);
      this.employeeUseCaseSessionBeanRemote = employeeUseCaseSessionBeanRemote;
      this.hashMap = new HashMap<Integer, ScheduleType>();
      this.initialiseMap();
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
        // make the flight status disbaled until a flight schedule plan is attached to it 
        
        System.out.println("Step 2: Enter a name for an aircraft configuration for this flight");
        System.out.print("> ");
        String configurationName = scanner.next();
        
        System.out.println("Step 3: Enter the Origin IATA Airport Code for the flight");
        System.out.print("> ");
        String originCity = scanner.next();
        
        System.out.println("Step 4: Enter the Destination IATA Airport Code for the flight");
        System.out.print("> ");
        String destinationCity = scanner.next();
        
        boolean haveReturnFlightRoute = scheduleManagerUseCaseSessionBeanRemote.createNewFlight(flightNumber, configurationName, originCity, destinationCity);
        if (haveReturnFlightRoute) {
            System.out.println("A Return Flight Route has been detected!");
            System.out.println("Press '1' if you would like to create this return flight route");
            System.out.print("> ");
            boolean createFlight = scanner.nextInt() == 1 ? true : false;
            if (createFlight) createFlight(configurationName, destinationCity, originCity);
        } else {
            System.out.println("Flight has been successfully created!");
        }
        
    }
    
    // used to create return flight with the same aircraft configuration
    public void createFlight(String configurationName, String originCity, String destinationCity) {
        System.out.println("Enter the Flight Number For the Return Flight");
        System.out.print("> ");
        String flightNumber = scanner.next();
   
        scheduleManagerUseCaseSessionBeanRemote.createNewFlight(flightNumber, configurationName, originCity, destinationCity);
        System.out.println("Flight has been successfully created!");
    }
    
    // used to view all flights
    // view according to ascending flight number
    // arranged if exact return flight is after it, it should be directly below it
    public void viewAllFlights() {
        List<Flight> flightList = scheduleManagerUseCaseSessionBeanRemote.viewAllFlights();
        Comparator<Flight> flightComparator = (x, y) -> {
            String routeIdX = x.getFlightRoute().getOrigin().getIataAirportCode() + "-" + x.getFlightRoute().getDestination().getIataAirportCode();
            String routeIdY = y.getFlightRoute().getOrigin().getIataAirportCode() + "-" + y.getFlightRoute().getDestination().getIataAirportCode();

            // Compare the route identifiers.
            int routeComparison = routeIdX.compareTo(routeIdY);
            
            if (routeComparison == 0) {
                // If the routes are the same, check if one is a complementary return flight.
                boolean xIsReturnFlight = x.getFlightRoute().getOrigin().getIataAirportCode().equals(y.getFlightRoute().getDestination().getIataAirportCode()) &&
                                          x.getFlightRoute().getDestination().getIataAirportCode().equals(y.getFlightRoute().getOrigin().getIataAirportCode());
                return xIsReturnFlight ? -1 : 1;
            }
            return routeComparison;
        };
                
       flightList.stream().sorted(flightComparator).forEach(x -> printSingleFlight(x));
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
        // get the id
    }
    
    // do later
    public void deleteFlight() {
        
    }
    
    // 
    public void createFlightSchedulePlan() {
        // can create one flight schedule plan it must be associated with a flight 
        // a flight might not have a schedule plan but a flight schedule plan
        System.out.println("Step 1: Choose Your Schedule Type");
        // enforce that there can be only one schedule type for all schedules within a FSP
        printAllScheduleTypes();
        System.out.print("> ");
        int choice = scanner.nextInt();
        ScheduleType chosen = this.hashMap.get(choice);
        
        System.out.println("Step 2: Enter Flight Number of Associated Flight");
        System.out.print("> ");
        String flightNumber = scanner.next();
 
        int number = 1; 
        Duration duration = null;
        List<Date> departureDateList = new ArrayList<Date>();
        Date endDate = null;
        int frequency = 0;
        
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
                frequency = checkFrequency(true);
                break;
        }
        System.out.println("Step 4: Fare Input for All Available Cabin Classes");
        // prompted to enter at least one fare for each 
        HashMap<CabinClassType, List<Fare>> faresForCabinClassList = new HashMap<CabinClassType, List<Fare>>();
        AircraftConfiguration airCraftConfiguration = scheduleManagerUseCaseSessionBeanRemote.viewSpecificFlightDetails(flightNumber).getAircraftConfiguration();
        // circumvent lazy fetching
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
        
        // still haven't implement the use of checking whether should we create another flight schedule plan
       boolean promptReturnFlightSchedule = scheduleManagerUseCaseSessionBeanRemote.createNewFlightSchedulePlan(flightNumber, departureDateList, duration, endDate, frequency, faresForCabinClassList);
       System.out.println("FlightSchedulePlan has been successfully created!");
    }
    
    public void viewAllFlightSchedulePlan() {
        List<FlightSchedulePlan> flightSchedulePlanList = scheduleManagerUseCaseSessionBeanRemote.viewAllFlightSchedulePlan();
        flightSchedulePlanList.forEach(x -> printSpecificFlightSchedulePlan(x, false));
    }
    
    public void viewFlightSchedulePlanDetails() {
        System.out.println("Enter the flight number");
        System.out.print("> ");
        String flightNumber = scanner.next();
        Flight flight = scheduleManagerUseCaseSessionBeanRemote.viewSpecificFlightDetails(flightNumber);
        
        // print the flight details first 
        printSpecificSingleFlight(flight);
        
        // ask for the flight schedule that user wants
        FlightSchedulePlan flightSchedulePlan = askForWhichFlightSchedule(flight);
        
        // print specific flight schedule plan
        printSpecificFlightSchedulePlan(flightSchedulePlan, true);
    }
    
    
    // non-recurrent schedules only need date and duration
    public void inputForScheduleType(int n, String flightNumber, List<Date> departureDateList) {
      for (int i = 1; i <= n; i++) {   
          System.out.println("Flight Schedule #" + i + " for " + flightNumber); 
          Date date = dateInitialisation();
         if (date != null) departureDateList.add(date);
      }
    }
    
    public FlightSchedulePlan askForWhichFlightSchedule(Flight flight) {
        System.out.println("Flight Schedule List for Flight " + flight.getFlightNumber());
        int init = flight.getFlightSchedulePlanList().size();
        int counter = 1;
        System.out.println("");
        for (FlightSchedulePlan fp : flight.getFlightSchedulePlanList()) {
            System.out.println("Press " + counter + " for to view this flight schedule plan in detail");
            counter += 1;
        }
        
        System.out.println("");
        System.out.print("> ");
        int choice = scanner.nextInt();
        return flight.getFlightSchedulePlanList().get(counter - 1);
    }
    
    // this can be made nicer
    public void printAllScheduleTypes() {
        System.out.println("Press 1 for Single Schedule Type");
        System.out.println("Press 2 for Multiple Schedule Type");
        System.out.println("Press 3 for Recurrent Schedule Type");
        System.out.println("Press 4 for Recurrent (Week) Schedule Type");
    }
    
    public void printSingleFlight(Flight flight) {
        System.out.println("Flight Number " + flight.getFlightNumber());
        System.out.println("Flight Aircraft Configuration Name" + flight.getAircraftConfiguration().getConfigurationName());
        System.out.println("Flight Origin City" + flight.getFlightRoute().getOrigin().getCity());
        System.out.println("Flight Destination City" + flight.getFlightRoute().getDestination().getCity());
        System.out.println("Flight Status " + flight.getStatus().name());
        System.out.println("--------------------------------------------");
    }
    
    public void printSpecificSingleFlight(Flight flight) {
        printSingleFlight(flight);
        System.out.println("Number of Cabin Classes Available " + flight.getAircraftConfiguration().getCabinClassList().size());
        flight.getAircraftConfiguration().getCabinClassList().forEach(x -> printSingleCabinClass(x));
    }
    
    public void printMoreSpecific(FlightSchedulePlan flightSchedulePlan) {
        System.out.println("Origin Airport " + flightSchedulePlan.getFlight().getFlightRoute().getOrigin().getAirportName());
        System.out.println("Origin City " + flightSchedulePlan.getFlight().getFlightRoute().getOrigin().getCity());
        System.out.println("Origin Country " + flightSchedulePlan.getFlight().getFlightRoute().getOrigin().getCountry());
        int init = flightSchedulePlan.getFlightScheduleList().size();
        flightSchedulePlan.getFlightScheduleList().forEach(x -> printSpecificFlightSchedule(x));
    }
    
    public void printSpecificFlightSchedule(FlightSchedule flightSchedule) {
        System.out.println("Flight Schedule Departure Time: " + flightSchedule.getDepartureTime().toString());
        System.out.println("Flight Schedule Arrival Time: " + flightSchedule.getArrivalTime().toString());
        System.out.println("Number of Cabin Class: " + flightSchedule.getCabinClassList().size());
        System.out.println("Cabin Class Fares");
        flightSchedule.getCabinClassList().forEach(x -> {
            System.out.println("");
            System.out.println("Cabin Class Type: " + x.getCabinClassName().name());
            System.out.println("");
            x.getFareList().forEach(y -> {
                System.out.println("Fare Basis Code: " + y.getFareBasicCode());
                System.out.println("Fare Amount: " + y.getFareAmount());
                System.out.println("");
            });
        });
    }
    
    public void printSpecificFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan, boolean printMoreSpecific) {
        System.out.println("Flight Number " + flightSchedulePlan.getFlight().getFlightNumber());
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
            RecurrentWeeklyFlightSchedulePlan recurrentWeeklyPlan = (RecurrentWeeklyFlightSchedulePlan) flightSchedulePlan;
            System.out.println("Schedule Plan Type: " + "Recurrent Weekly");
            System.out.println("Schedule Plan Status: " + recurrentWeeklyPlan.getStatus());
            System.out.println("Number of Schedules Under this Plan: " + recurrentWeeklyPlan.getFlightScheduleList().size());
            System.out.println("Recurrent End Date & Time: " + recurrentWeeklyPlan.getEndDate().toString());
            System.out.println("Recurrent Frequency (in days): " + recurrentWeeklyPlan.getFrequency().intValue()); 
        }
        
        if (printMoreSpecific) {
                printMoreSpecific(flightSchedulePlan);
        }
    }
    
    
    
    public List<Fare> makeFares(int nFares, CabinClass cabinClass, String flightNumber) {
        List<Fare> fareList = new ArrayList<Fare>();
        for (int i = 0; i < nFares; i++) {
           System.out.println("Enter the Fare Amount for Fare #" + (i+1) + " for " + cabinClass.getCabinClassName().name() + " class " + flightNumber); 
           System.out.print("> ");
           int fareAmt = scanner.nextInt();
           Fare fare = new Fare(generateRandomFareBasisCode(), new BigDecimal(fareAmt));
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
        System.out.println("Cabin Class Name " + cabinClass.getCabinClassName().name());
        System.out.println("Number of Aisles: " + cabinClass.getNumAisles());
        System.out.println("Number of Rows: " + cabinClass.getNumRows());
        System.out.println("Number of Seats Abreast " + cabinClass.getNumSeatsAbreast());
        System.out.println("Seating Configuration " + cabinClass.getSeatingConfiguration());
        System.out.println("Number of Available Seats " + cabinClass.getNumAvailableSeats());
        System.out.println("Number of Reserved Seats " + cabinClass.getNumReservedSeats());
        System.out.println("Number of Balanced Seats " + cabinClass.getNumBalanceSeats());
        System.out.println("");
    }
    
    public Date dateInitialisation() {
        System.out.println("Commencement Date is the date at which the first flight would commence");
        System.out.println("Departure Time is the time that the flight is scheduled to leave");
        System.out.println("Flight Duration is the time taken to reach the destination from the origin");
        System.out.println("");
        System.out.println("Enter the commencment date (yyyy-MM-dd):");
        System.out.print("> ");
        String startDateInput = scanner.nextLine();
        System.out.println("Enter the commencement time (HH:mm:ss):");
        System.out.print("> ");
        String timeInput = scanner.nextLine();
        String dateTimeInput = startDateInput + " " + timeInput;
        Date date = formatDate(dateTimeInput);
        return date;        
    }
    
    public Duration checkDuration() {
        System.out.println("Duration is the time taken to reach the destination from the origin");
        System.out.println("");
        System.out.println("Enter the hours"); 
        System.out.print("> ");
        int hours = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter the minutes");
        System.out.print("> ");
        int minutes = Integer.parseInt(scanner.nextLine());
        
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
        String startDateInput = scanner.nextLine();
        System.out.println("Enter the end time (HH:mm:ss):");
        System.out.print("> ");
        String timeInput = scanner.nextLine();
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

   
}
