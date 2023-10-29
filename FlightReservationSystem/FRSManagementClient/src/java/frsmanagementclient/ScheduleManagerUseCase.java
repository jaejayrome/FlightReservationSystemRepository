/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeUseCaseSessionBeanRemote;
import ejb.session.stateless.ScheduleManagerUseCaseSessionBeanRemote;
import entity.CabinClass;
import entity.Fare;
import entity.Flight;
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
        
        System.out.println("Step 3: Enter the origin city of the flight");
        System.out.print("> ");
        String originCity = scanner.next();
        
        System.out.println("Step 4: Enter the destination city of the flight");
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
        List<List<Fare>> faresForCabinClassList = new ArrayList<List<Fare>>();
        List<CabinClass> cabinClassList = scheduleManagerUseCaseSessionBeanRemote.viewSpecificFlightDetails(flightNumber).getAircraftConfiguration().getCabinClassList();
        int cabinClassNumber = cabinClassList.size();
        for (int i = 0 ; i < cabinClassNumber; i++) {
            CabinClass cabinClass = cabinClassList.get(i);
            System.out.println("Enter the number of fares that you want to enter for " + cabinClass.getCabinClassName().name() + " class on " + flightNumber);
            System.out.print("> ");
            int nFares = scanner.nextInt();
            List<Fare> fareList = makeFares(nFares, cabinClass, flightNumber);
            faresForCabinClassList.add(fareList);
        }
        
        
       boolean promptReturnFlightSchedule = scheduleManagerUseCaseSessionBeanRemote.createNewFlightSchedulePlan(departureDateList, duration, endDate, frequency, faresForCabinClassList);

        
    }
    
    public void viewAllFlightSchedulePlan() {
        
    }
    
    public void viewFlightSchedulePlanDetails() {
        
    }
    
    
    // non-recurrent schedules only need date and duration
    public void inputForScheduleType(int n, String flightNumber, List<Date> departureDateList) {
      for (int i = 1; i <= n; i++) {   
          System.out.println("Flight Schedule #" + i + " for " + flightNumber); 
          Date date = dateInitialisation();
         if (date != null) departureDateList.add(date);
      }
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
        System.out.println("Enter the departure time (HH:mm:ss):");
        System.out.print("> ");
        String timeInput = scanner.nextLine();
        String dateTimeInput = startDateInput + " " + timeInput;
        Date date = formatDate(dateTimeInput);
        return date;        
    }
    
    public Duration checkDuration() {
        System.out.println("Duration is the time taken to reach the destination from the origin");
        System.out.println("");
        System.out.println("Enter the duration (in hours)");
        System.out.println("e.g '3.5' is equivalent to 3.5 hours or 3 hour 30 mins");
        System.out.print("> "); 
        long seconds = (long)scanner.nextDouble() * 3600;
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
        System.out.println("End Date is when this recurrent schedule would come to an end");
        System.out.println("");
        System.out.println("Enter the end date (yyyy-MM-dd):");
        System.out.print("> ");
        String endDate = scanner.nextLine();
        Date date = formatDate(endDate);
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
