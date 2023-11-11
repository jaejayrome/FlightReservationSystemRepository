/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftConfigurationEntitySessionBeanLocal;
import ejb.session.stateless.AircraftTypeEntitySessionBeanLocal;
import javax.annotation.PostConstruct;
import ejb.session.stateless.AirportEntitySessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.FleetManagerUseCaseSessionBeanLocal;
import ejb.session.stateless.FlightRouteEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.RoutePlannerUseCaseSessionBeanLocal;
import ejb.session.stateless.ScheduleManagerUseCaseSessionBeanLocal;
import entity.AircraftConfiguration;
import entity.Employee;
import entity.Fare;
import entity.Flight;
import entity.FlightRoute;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import util.enumerations.AircraftTypeName;
import util.enumerations.CabinClassType;
import util.enumerations.EmploymentType;
import util.enumerations.FlightRouteStatus;
import util.enumerations.GenderType;
import util.enumerations.JobTitle;
import util.enumerations.RoleType;
import util.exception.EmployeeNotFoundException;
import util.exception.InitialDatabaseException;
import util.exception.InitialFlightNotInstantiatedException;
import util.exception.InvalidStringLengthException;

/**
 *
 * @author jeromegoh
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean  {

    @EJB
    private FleetManagerUseCaseSessionBeanLocal fleetManagerUseCaseSessionBean;

    @EJB
    private ScheduleManagerUseCaseSessionBeanLocal scheduleManagerUseCaseSessionBean;

    
    

   

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    @EJB
    private AirportEntitySessionBeanLocal airportEntitySessionBeanLocal;
    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;
    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;
    @EJB
    private AircraftTypeEntitySessionBeanLocal aircraftTypeEntitySessionBeanLocal;
    @EJB
    private AircraftConfigurationEntitySessionBeanLocal aircraftConfigurationEntitySessionBean;
    @EJB
    private FlightRouteEntitySessionBeanLocal flightRouteEntitySessionBean;
    
    @EJB
    private RoutePlannerUseCaseSessionBeanLocal routePlannerUseCaseSessionBean;

    
    
    
    public DataInitSessionBean() {
    }

    // initialised all the data in the post construct 
    @PostConstruct
    public void seedDatabase() {
        try {
            checkDatabaseInitialised();
        } catch (InitialDatabaseException exception) {
            initialiseData();
            System.out.println(exception.getMessage());
        }
    }
    // 
    public void checkDatabaseInitialised() throws InitialDatabaseException{
        try {
            Employee employee = employeeEntitySessionBeanLocal.retrieveEmployeeById(1l);
        } catch (EmployeeNotFoundException exception) {
            throw new InitialDatabaseException("Database has not been seeded yet!");
        }
    }
    
    public void initialiseActualTestData() {
        // employees
        employeeEntitySessionBeanLocal.createNewEmployee("Fleet Manager", "", GenderType.MALE, "fleetmanager@mlair.com.sg", "12345678", JobTitle.FLEET_MANAGER, EmploymentType.FULL_TIME, "fleetmanager", "password");
        employeeEntitySessionBeanLocal.createNewEmployee("Route Planner", "", GenderType.MALE, "routeplanner@mlair.com.sg", "12345678", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "routeplanner", "password");
        employeeEntitySessionBeanLocal.createNewEmployee("Schedule Manager", "", GenderType.MALE, "schedulemanager@mlair.com.sg", "12345678", JobTitle.SCHEDULE_MANAGER, EmploymentType.FULL_TIME, "schedulemanager", "password");
        employeeEntitySessionBeanLocal.createNewEmployee("Sales Manager", "", GenderType.MALE, "salesmanager@mlair.com.sg", "12345678", JobTitle.SALES_MANAGER, EmploymentType.FULL_TIME, "salesmanager", "password");
        
        // partner
        partnerEntitySessionBeanLocal.createNewPartner("Holiday.com", "holidaydotcom", "password");
        
        //airport
        airportEntitySessionBeanLocal.createNewAirport("Changi", "SIN", "Singapore", "Singapore", "Singapore");
        airportEntitySessionBeanLocal.createNewAirport("Hong Kong", "HKG", "Chek Lap Kok", "Hong Kong", "China");
        airportEntitySessionBeanLocal.createNewAirport("Taoyuan", "TPE", "Taoyuan", "Taipei", "Taiwan R.O.C.");
        airportEntitySessionBeanLocal.createNewAirport("Narita", "NRT", "Narita", "Chiba", "Japan");
        airportEntitySessionBeanLocal.createNewAirport("Sydney", "SYD", "Sydney", "New South Wales", "Australia");
        
        // aircraft type
        aircraftTypeEntitySessionBeanLocal.createNewAircraftType(AircraftTypeName.BOEING_737, new BigDecimal(200));
        aircraftTypeEntitySessionBeanLocal.createNewAircraftType(AircraftTypeName.BOEING_747, new BigDecimal(400));
        
        // aircraft configuration (need to change)
        try {
            List<CabinClassType> firstCabinClassType = new ArrayList<CabinClassType>();
            firstCabinClassType.add(CabinClassType.Y);
            List<Integer> firstNumAisles = new ArrayList<Integer>();
            firstNumAisles.add(1);
            List<Integer> firstNumRows = new ArrayList<Integer>();
            firstNumRows.add(30);
            List<Integer> firstSeatAbreast = new ArrayList<Integer>();
            firstSeatAbreast.add(6);
            List<String> firstSeatingConfiguration = new ArrayList<String>();
            firstSeatingConfiguration.add("3-3");
            fleetManagerUseCaseSessionBean.createAircraftConfigurationForFleetManager(JobTitle.FLEET_MANAGER, AircraftTypeName.BOEING_737, "Boeing 737 All Economy", firstCabinClassType, firstNumAisles, firstNumRows, firstSeatAbreast, firstSeatingConfiguration);
            
            List<CabinClassType> secondCabinClassType = new ArrayList<CabinClassType>();
            secondCabinClassType.add(CabinClassType.F);
            secondCabinClassType.add(CabinClassType.J);
            secondCabinClassType.add(CabinClassType.Y);
            List<Integer> secondNumAisles = new ArrayList<Integer>();
            secondNumAisles.add(1);
            secondNumAisles.add(1);
            secondNumAisles.add(1);
            List<Integer> secondNumRows = new ArrayList<Integer>();
            secondNumRows.add(5);
            secondNumRows.add(5);
            secondNumRows.add(25);
            List<Integer> secondSeatAbreast = new ArrayList<Integer>();
            secondSeatAbreast.add(2);
            secondSeatAbreast.add(4);
            secondSeatAbreast.add(6);
            List<String> secondSeatingConfiguration = new ArrayList<String>();
            secondSeatingConfiguration.add("1-1");
            secondSeatingConfiguration.add("2-2");
            secondSeatingConfiguration.add("3-3");
            fleetManagerUseCaseSessionBean.createAircraftConfigurationForFleetManager(JobTitle.FLEET_MANAGER, AircraftTypeName.BOEING_737, "Boeing 737 Three Classes", secondCabinClassType, secondNumAisles, secondNumRows, secondSeatAbreast, secondSeatingConfiguration);
            
            List<CabinClassType> thirdCabinClassType = new ArrayList<CabinClassType>();
            thirdCabinClassType.add(CabinClassType.Y);
            List<Integer> thirdNumAisles = new ArrayList<Integer>();
            thirdNumAisles.add(2);
            List<Integer> thirdNumRows = new ArrayList<Integer>();
            thirdNumRows.add(38);
            List<Integer> thirdSeatAbreast = new ArrayList<Integer>();
            thirdSeatAbreast.add(10);
            List<String> thirdSeatingConfiguration = new ArrayList<String>();
            thirdSeatingConfiguration.add("3-4-3");
            fleetManagerUseCaseSessionBean.createAircraftConfigurationForFleetManager(JobTitle.FLEET_MANAGER, AircraftTypeName.BOEING_737, "Boeing 747 All Economy", thirdCabinClassType, thirdNumAisles, thirdNumRows, thirdSeatAbreast, thirdSeatingConfiguration);
            
            List<CabinClassType> fourthCabinClassType = new ArrayList<CabinClassType>();
            fourthCabinClassType.add(CabinClassType.F);
            fourthCabinClassType.add(CabinClassType.J);
            fourthCabinClassType.add(CabinClassType.Y);
            List<Integer> fourthNumAisles = new ArrayList<Integer>();
            fourthNumAisles.add(1);
            fourthNumAisles.add(2);
            fourthNumAisles.add(2);
            List<Integer> fourthNumRows = new ArrayList<Integer>();
            fourthNumRows.add(5);
            fourthNumRows.add(5);
            fourthNumRows.add(32);
            List<Integer> fourthSeatAbreast = new ArrayList<Integer>();
            fourthSeatAbreast.add(2);
            fourthSeatAbreast.add(6);
            fourthSeatAbreast.add(10);
            List<String> fourthSeatingConfiguration = new ArrayList<String>();
            fourthSeatingConfiguration.add("1-1");
            fourthSeatingConfiguration.add("2-2-2");
            fourthSeatingConfiguration.add("3-4-3");
            fleetManagerUseCaseSessionBean.createAircraftConfigurationForFleetManager(JobTitle.FLEET_MANAGER, AircraftTypeName.BOEING_737, "Boeing 747 Three Classes", fourthCabinClassType, fourthNumAisles, fourthNumRows, fourthSeatAbreast, fourthSeatingConfiguration);
            
        } catch (InvalidStringLengthException e) {
            System.out.println(e.getMessage());
        }
        
        // flight route
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("SIN", "HKG", new FlightRoute(FlightRouteStatus.DISABLED), true);
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("SIN", "TPE", new FlightRoute(FlightRouteStatus.DISABLED), true);
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("SIN", "NRT", new FlightRoute(FlightRouteStatus.DISABLED), true);
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("HKG", "NRT", new FlightRoute(FlightRouteStatus.DISABLED), true);
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("TPE", "NRT", new FlightRoute(FlightRouteStatus.DISABLED), true);
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("SIN", "SYD", new FlightRoute(FlightRouteStatus.DISABLED), true);
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("SYD", "NRT", new FlightRoute(FlightRouteStatus.DISABLED), true);
        
        
        // flights
        try {
            long firstPairId = scheduleManagerUseCaseSessionBean.createNewFlight("ML111", "Boeing 737 Three Classes", "SIN", "HKG", false, -1);
            scheduleManagerUseCaseSessionBean.createNewFlight("ML112", "Boeing 737 Three Classes", "HKG", "SIN", true, firstPairId);

            long secondpairId = scheduleManagerUseCaseSessionBean.createNewFlight("ML211", "Boeing 737 Three Classes", "SIN", "TPE", false, -1);
            scheduleManagerUseCaseSessionBean.createNewFlight("ML212", "Boeing 737 Three Classes", "TPE", "SIN", true, secondpairId);

            long thirdPairId = scheduleManagerUseCaseSessionBean.createNewFlight("ML311", "Boeing 747 Three Classes", "SIN", "NRT", false, -1);
            scheduleManagerUseCaseSessionBean.createNewFlight("ML312", "Boeing 747 Three Classes", "NRT", "SIN", true, thirdPairId);

            long fourthPairId = scheduleManagerUseCaseSessionBean.createNewFlight("ML411", "Boeing 737 Three Classes", "HKG", "NRT", false, -1);
            scheduleManagerUseCaseSessionBean.createNewFlight("ML412", "Boeing 737 Three Classes", "NRT", "HKG", true, fourthPairId);

            long fifthPairId = scheduleManagerUseCaseSessionBean.createNewFlight("ML511", "Boeing 737 Three Classes", "TPE", "NRT", false, -1);
            scheduleManagerUseCaseSessionBean.createNewFlight("ML512", "Boeing 737 Three Classes", "NRT", "TPE", true, fifthPairId);

            long sixthPairId = scheduleManagerUseCaseSessionBean.createNewFlight("ML611", "Boeing 737 Three Classes", "SIN", "SYD", false, -1);
            scheduleManagerUseCaseSessionBean.createNewFlight("ML612", "Boeing 737 Three Classes", "SYD", "SIN", true, sixthPairId);

            long seventhPair = scheduleManagerUseCaseSessionBean.createNewFlight("ML621", "Boeing 737 All Economy", "SIN", "SYD", false, -1);
            scheduleManagerUseCaseSessionBean.createNewFlight("ML622", "Boeing 737 All Economy", "SYD", "SIN", true, seventhPair);

            long eightPair = scheduleManagerUseCaseSessionBean.createNewFlight("ML711", "Boeing 747 Three Classes", "SYD", "NRT", false, -1);
            scheduleManagerUseCaseSessionBean.createNewFlight("ML712", "Boeing 747 Three Classes", "NRT", "SYD", true, seventhPair);
        } catch (InitialFlightNotInstantiatedException e) {
            System.out.println(e.getMessage());
        }
        
       
        // flight schedule plan (recurrent need to change logic)
        
        // recurrent n day flight schedule plan
        Date firstFormatDate = formatDate("2023-12-01 13:00:00");
        List<Date> firstDepartureDateList = new ArrayList<Date>();
        firstDepartureDateList.add(firstFormatDate);
        HashMap<CabinClassType, List<Fare>> firstfaresForCabinClassList = new HashMap<CabinClassType, List<Fare>>();
        AircraftConfiguration a = aircraftConfigurationEntitySessionBean.getAircraftConfigurationPerConfigurationName("Boeing 737 Three Classes");
        a.getCabinClassList().size();
        
        Fare toAdd = new Fare("ABC1234", new BigDecimal(2900), a.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.F).findFirst().get());
        List<Fare> fareList1 = new ArrayList<Fare>();
        fareList1.add(toAdd);
        
        Fare toAdd2 = new Fare("BCD2345", new BigDecimal(1400), a.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.J).findFirst().get());
        List<Fare> fareList2 = new ArrayList<Fare>();
        fareList2.add(toAdd2);
        
        Fare toAdd3 = new Fare("CDE3456", new BigDecimal(400), a.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.Y).findFirst().get());
        List<Fare> fareList3 = new ArrayList<Fare>();
        fareList3.add(toAdd3);
        
         firstfaresForCabinClassList.put(CabinClassType.F, fareList1);
         firstfaresForCabinClassList.put(CabinClassType.J, fareList2);
         firstfaresForCabinClassList.put(CabinClassType.Y, fareList3);
        long idFirst = scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML411", firstDepartureDateList, Duration.ofSeconds(4 * 60 * 60), formatDate("2023-12-31 13:00:00"), 2, firstfaresForCabinClassList, false, -1, null);
        scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML411", firstDepartureDateList, Duration.ofSeconds(4 * 60 * 60), formatDate("2023-12-31 13:00:00"), 2, firstfaresForCabinClassList, true, idFirst, Duration.ofSeconds(4 * 60 * 60));
        
        // manual multiple flight schedule plan
        Date secondFormatDate = formatDate("2023-12-07 17:00:00");
        Date thirdFormatDate = formatDate("2023-12-08 17:00:00");
        Date fourthFormatDate = formatDate("2023-12-09 17:00:00");
        List<Date> secondDepartureDateList = new ArrayList<Date>();
        secondDepartureDateList.add(secondFormatDate);
        secondDepartureDateList.add(thirdFormatDate);
        secondDepartureDateList.add(fourthFormatDate);
        HashMap<CabinClassType, List<Fare>> secondfaresForCabinClassList = new HashMap<CabinClassType, List<Fare>>();
        AircraftConfiguration b = aircraftConfigurationEntitySessionBean.getAircraftConfigurationPerConfigurationName("Boeing 737 Three Classes");
        b.getCabinClassList().size();
        
        Fare toAdd4 = new Fare("ABC1234", new BigDecimal(2900), b.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.F).findFirst().get());
        List<Fare> fareList4 = new ArrayList<Fare>();
        fareList1.add(toAdd4);
        
        Fare toAdd5 = new Fare("BCD2345", new BigDecimal(1400), b.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.J).findFirst().get());
        List<Fare> fareList5 = new ArrayList<Fare>();
        fareList5.add(toAdd5);
        
        Fare toAdd6 = new Fare("CDE3456", new BigDecimal(400), b.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.Y).findFirst().get());
        List<Fare> fareList6 = new ArrayList<Fare>();
        fareList6.add(toAdd6);
        
        secondfaresForCabinClassList.put(CabinClassType.F, fareList1);
        secondfaresForCabinClassList.put(CabinClassType.J, fareList2);
        secondfaresForCabinClassList.put(CabinClassType.Y, fareList3);
        long idsecond = scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML511", secondDepartureDateList, Duration.ofSeconds(3 * 60 * 60), null, 0, secondfaresForCabinClassList, false, -1, null);
        scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML512", secondDepartureDateList, Duration.ofSeconds(3 * 60 * 60), null, 0, secondfaresForCabinClassList, true, idsecond, Duration.ofSeconds(2 * 60 * 60));
        
    }
    
    public void initialiseData() {
        System.out.println("Seeding the database with initial data");
//        // create a new airport
        airportEntitySessionBeanLocal.createNewAirport("Singapore Changi Airport", "SIN", "Singapore", "Singapore", "Singapore");
        airportEntitySessionBeanLocal.createNewAirport("Kuala Lumpur International Airport", "KUL", "Kuala Lumpur", "Kuala Lumpur", "Malaysia");
        airportEntitySessionBeanLocal.createNewAirport("Ngurah Rai International Airport", "DPS", "Depansar", "Depansar", "Indonesia");
        airportEntitySessionBeanLocal.createNewAirport("Halim Perdanakusuma International Airport", "HLP", "Jakarta", "Jakarta", "Indonesia");
        airportEntitySessionBeanLocal.createNewAirport("Husein Sastranegara International Airport", "BDO", "Bandung", "Bandung", "Indonesia");
        airportEntitySessionBeanLocal.createNewAirport("Suvarnabhumi Airport", "BKK", "Bangkok", "Bangkok", "Thailand");
        airportEntitySessionBeanLocal.createNewAirport("Chiang Mai International Airport", "CNX", "Chiang Mai", "Chiang Mai", "Thailand");
        airportEntitySessionBeanLocal.createNewAirport("Krabi Airport", "KBV", "Krabi", "Krabi", "Thailand");
        airportEntitySessionBeanLocal.createNewAirport("Kansai International Airport", "KIX", "Osaka", "Osaka", "Japan");
        airportEntitySessionBeanLocal.createNewAirport("Narita International Airport", "NRT", "Tokyo", "Tokyo", "Japan");
        airportEntitySessionBeanLocal.createNewAirport("Chubu Centrair International Airport", "NGO", "Nagoya", "Nagoya", "Japan");
        airportEntitySessionBeanLocal.createNewAirport("Taiwan Taoyuan International Airport", "TPE", "Taipei", "Taipei", "Taiwan");
        airportEntitySessionBeanLocal.createNewAirport("Kaohsiung International Airport", "KHH", "Kaohsiung", "Kaohsiung", "Taiwan");
        airportEntitySessionBeanLocal.createNewAirport("Beijing Daxing International Airport", "PKX", "Beijing", "Beijing", "China");
        airportEntitySessionBeanLocal.createNewAirport("Shanghai Pudong International Airport", "PVG", "Shanghai", "Shanghai", "China");
        airportEntitySessionBeanLocal.createNewAirport("Guangzhou Baiyun International Airport", "CAN", "Guangzhou", "Guangzhou", "China");
        airportEntitySessionBeanLocal.createNewAirport("Shenzhen Bao'an International Airport", "SGX", "Shenzhen", "Shenzhen", "China");
        airportEntitySessionBeanLocal.createNewAirport("Perth International Airport", "PER", "Perth", "Perth", "Australia");
        airportEntitySessionBeanLocal.createNewAirport("Sydney Kingsford Smith International Airport", "SYD", "Sydney", "Sydney", "Australia");
        airportEntitySessionBeanLocal.createNewAirport("Adelaide International Airport", "ADL", "Adelaide", "Adelaide", "Australia");
        airportEntitySessionBeanLocal.createNewAirport("Melbourne International Airport", "MEL", "Melbourne", "Melbourne", "Australia");
        airportEntitySessionBeanLocal.createNewAirport("Noi Bai International Airport", "HAN", "Hanoi", "Hanoi", "Vietnam");
        airportEntitySessionBeanLocal.createNewAirport("Da Nang International Airport", "DAD", "Danang", "Danang", "Vietnam");
        airportEntitySessionBeanLocal.createNewAirport("Tan Son Nhat International Airport", "SGN", "Ho Chi Minh City", "Ho Chi Minh City", "Vietnam");
        airportEntitySessionBeanLocal.createNewAirport("Gimhae International Airport", "PUS", "Busan", "Busan", "South Korea");
        airportEntitySessionBeanLocal.createNewAirport("Incheon International Airport", "ICN", "Seoul", "Seoul", "South Korea");
        airportEntitySessionBeanLocal.createNewAirport("Jeju International Airport", "CJU", "Jeju", "Jeju", "South Korea");
        
        // there would be total 16 employees initialised
        // String firstName, String lastName, GenderType gender, String email, String phoneNumber, JobTitle jobTitle, EmploymentType employmentType, String loginUsername, String loginPassword
        // create 2 sales manager
        employeeEntitySessionBeanLocal.createNewEmployee("John", "Doe", GenderType.MALE, "johndoe@email.com", "91234567", JobTitle.SALES_MANAGER, EmploymentType.FULL_TIME, "johndoe", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Isabel", "Tan", GenderType.FEMALE, "isabeltan@email.com", "92324546", JobTitle.SALES_MANAGER, EmploymentType.FULL_TIME, "isabeltan", "12345678");
        
        // create 2 fleet managers
        employeeEntitySessionBeanLocal.createNewEmployee("Chris", "Chia", GenderType.MALE, "chrischia@email.com", "93568623", JobTitle.FLEET_MANAGER, EmploymentType.FULL_TIME, "chrischia", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Samantha", "Lee", GenderType.FEMALE, "samanthalee@email.com", "97753324", JobTitle.FLEET_MANAGER, EmploymentType.FULL_TIME, "samanthalee", "12345678");
        
        // create 2 schedule managers
        employeeEntitySessionBeanLocal.createNewEmployee("Lucas", "Seah", GenderType.MALE, "lucasseah@email.com", "95334522", JobTitle.SCHEDULE_MANAGER, EmploymentType.FULL_TIME, "lucasseah", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Amanda", "Koh", GenderType.FEMALE, "amandakoh@email.com", "99232937", JobTitle.SCHEDULE_MANAGER, EmploymentType.FULL_TIME, "amandakoh", "12345678");
        
        // create 10 route planners
        employeeEntitySessionBeanLocal.createNewEmployee("Jason", "Tan", GenderType.MALE, "jasontan@email.com", "91224232", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "jasontan", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Cheryl", "Chia", GenderType.FEMALE, "cherylchia@email.com", "92324133", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "cherylchia", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Ben", "Tan", GenderType.MALE, "bentan@email.com", "92324242", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "bentan", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Jasmine", "Lim", GenderType.FEMALE, "jasminelim@email.com", "92424267", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "jasminelim", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Jayden", "Lee", GenderType.MALE, "jaydenlee@email.com", "92457234", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "jaydenlee", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Cynthia", "Chong", GenderType.FEMALE, "cynthiachong@email.com", "96383484", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "cynthiachong", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Elliot", "Teo", GenderType.MALE, "elliotteo@email.com", "92432424", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "elliottoh", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Tania", "Wong", GenderType.FEMALE, "taniawong@email.com", "98234733", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "taniawong", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Jordan", "Soh", GenderType.MALE, "jordansoh@email.com", "92242374", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "jordansoh", "12345678");
        employeeEntitySessionBeanLocal.createNewEmployee("Nicole", "Goh", GenderType.FEMALE, "nicolegoh@email.com", "93547345", JobTitle.ROUTE_PLANNER, EmploymentType.FULL_TIME, "nicolegoh", "12345678");
        
        
        // create 3 partners
        partnerEntitySessionBeanLocal.createNewPartner("Booking.com", "booking.com", "password");
        partnerEntitySessionBeanLocal.createNewPartner("Trivago", "trivago", "password");
        partnerEntitySessionBeanLocal.createNewPartner("Skyscanner", "skyscanner", "password");
        
        // initialise 2 aircraft type // BOEIGN 747 and BOEING 737
        aircraftTypeEntitySessionBeanLocal.createNewAircraftType(AircraftTypeName.BOEING_737, new BigDecimal(215));
        aircraftTypeEntitySessionBeanLocal.createNewAircraftType(AircraftTypeName.BOEING_747, new BigDecimal(416));
    

        //default customer creation is already init as roleType.customer
        customerSessionBeanLocal.createNewCustomer("FirstName", "LastName", "email@test", "phoneNumber", "Address", "Password");

    }
    
    
    public Date formatDate(String dateTimeInput) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeInput, formatter);
        ZoneId zoneId = ZoneId.of("Asia/Singapore");
        Date date = Date.from(dateTime.atZone(zoneId).toInstant());
        return date;
    }
}
