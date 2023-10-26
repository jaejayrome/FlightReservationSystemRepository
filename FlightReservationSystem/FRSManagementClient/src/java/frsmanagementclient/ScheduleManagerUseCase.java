/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.EmployeeUseCaseSessionBeanRemote;
import ejb.session.stateless.ScheduleManagerUseCaseSessionBeanRemote;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
        System.out.println("");
    }
    
    public void viewAllFlights() {
        
    }
    
    public void viewSpecificFlightDetails() {
        
    }
    
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
        // Step 3: Check the number of flight schedules that employee wants to make
        if (chosen == ScheduleType.MULTIPLE) {
            System.out.println("Enter the number of flight schedules to be associated with: ");
            System.out.print("> ");
            number = scanner.nextInt();
        }
        
        // assuming that for different flight schedules of teh same type, the duration is the same 
        // for SINGLE and MULTIPLE flight schedules
        List<Date> departureDateList = new ArrayList<Date>();
        Duration duration = null;
        
        // for RECURRENT and RECURRENT_WEEK flight schedules
        Date endDate = null;
        int frequency = 0;
        
        // loop would run more than once only for MULTIPLE
        for (int i = 1; i <= number; i++){
            boolean isRecurrent = chosen == ScheduleType.RECURRENT || chosen == ScheduleType.RECURRENT_WEEK;
            boolean isRecurrentWeek = chosen == ScheduleType.RECURRENT_WEEK;
            // ask for duration (if needed) 
            if (duration == null) duration = checkDuration();
            
            System.out.println("Flight Schedule #" + i + " for " + flightNumber);        
            // ask for departure date
            Date date = dateInitialisation();
            if (date != null) departureDateList.add(date);
            
            // only for recurrent schedules, loop stops here
            // ask for frequency (if needed) only ask once
            if (isRecurrent && frequency == 0) frequency = checkFrequency(isRecurrentWeek);
            // ask for end date(if needed)
            if (isRecurrent) endDate = checkEndDate();
        }
        
        // last one would be the fare for every cabin class
        // but the number of cabin classes would depend on the flight that would depend on the aircraft configuration
        // missing fare
        
    }
    
    public void viewAllFlightSchedulePlan() {
        
    }
    
    public void viewFlightSchedulePlanDetails() {
        
    }
    // this can be made nicer
    public void printAllScheduleTypes() {
        System.out.println("Press 1 for Single Schedule Type");
        System.out.println("Press 2 for Multiple Schedule Type");
        System.out.println("Press 3 for Recurrent Schedule Type");
        System.out.println("Press 4 for Recurrent (Week) Schedule Type");
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
        long seconds = scanner.nextInt() * 3600;
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
