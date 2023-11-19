/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftConfigurationEntitySessionBeanLocal;
import ejb.session.stateless.AircraftTypeEntitySessionBeanLocal;
import ejb.session.stateless.AirportEntitySessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.FleetManagerUseCaseSessionBeanLocal;
import ejb.session.stateless.FlightRouteEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.RoutePlannerUseCaseSessionBeanLocal;
import ejb.session.stateless.ScheduleManagerUseCaseSessionBeanLocal;
import entity.AircraftConfiguration;
import entity.Fare;
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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.NoResultException;
import util.enumerations.AircraftTypeName;
import util.enumerations.CabinClassType;
import util.enumerations.EmploymentType;
import util.enumerations.FlightRouteStatus;
import util.enumerations.GenderType;
import util.enumerations.JobTitle;
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
public class ActualDataInitSessionBean {

    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBean;
    
    @EJB
    private FleetManagerUseCaseSessionBeanLocal fleetManagerUseCaseSessionBean;

    @EJB
    private ScheduleManagerUseCaseSessionBeanLocal scheduleManagerUseCaseSessionBean;
    
    @EJB
    private CustomerSessionBeanLocal customerSessionBean;
    
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
    
    

    @PostConstruct
    public void seedDatabase() {
        try {
            employeeEntitySessionBean.checkActualDataInitialised("fleetmanager");
        } catch (InitialDatabaseException e) {
            initialiseActualTestData();
        }
    }
    
    public Date formatDate(String dateTimeInput) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeInput, formatter);
        ZoneId zoneId = ZoneId.of("Asia/Singapore");
        Date date = Date.from(dateTime.atZone(zoneId).toInstant());
        return date;
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
        fleetManagerUseCaseSessionBean.createAircraftConfigurationForFleetManagerDatabaseInit(JobTitle.FLEET_MANAGER, AircraftTypeName.BOEING_737, "Boeing 737 All Economy", firstCabinClassType, firstNumAisles, firstNumRows, firstSeatAbreast, firstSeatingConfiguration);

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
        fleetManagerUseCaseSessionBean.createAircraftConfigurationForFleetManagerDatabaseInit(JobTitle.FLEET_MANAGER, AircraftTypeName.BOEING_737, "Boeing 737 Three Classes", secondCabinClassType, secondNumAisles, secondNumRows, secondSeatAbreast, secondSeatingConfiguration);

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
        fleetManagerUseCaseSessionBean.createAircraftConfigurationForFleetManagerDatabaseInit(JobTitle.FLEET_MANAGER, AircraftTypeName.BOEING_737, "Boeing 747 All Economy", thirdCabinClassType, thirdNumAisles, thirdNumRows, thirdSeatAbreast, thirdSeatingConfiguration);

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
        fleetManagerUseCaseSessionBean.createAircraftConfigurationForFleetManagerDatabaseInit(JobTitle.FLEET_MANAGER, AircraftTypeName.BOEING_737, "Boeing 747 Three Classes", fourthCabinClassType, fourthNumAisles, fourthNumRows, fourthSeatAbreast, fourthSeatingConfiguration);



        // flight route
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("SIN", "HKG", new FlightRoute(FlightRouteStatus.DISABLED), true);
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("SIN", "TPE", new FlightRoute(FlightRouteStatus.DISABLED), true);
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("SIN", "NRT", new FlightRoute(FlightRouteStatus.DISABLED), true);
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("HKG", "NRT", new FlightRoute(FlightRouteStatus.DISABLED), true);
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("TPE", "NRT", new FlightRoute(FlightRouteStatus.DISABLED), true);
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("SIN", "SYD", new FlightRoute(FlightRouteStatus.DISABLED), true);
        routePlannerUseCaseSessionBean.createNewFlightRouteDataInit("SYD", "NRT", new FlightRoute(FlightRouteStatus.DISABLED), true);


        long firstPairId = scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML111", "Boeing 737 Three Classes", "SIN", "HKG", false, -1);
        scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML112", "Boeing 737 Three Classes", "HKG", "SIN", true, firstPairId);

        long secondpairId = scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML211", "Boeing 737 Three Classes", "SIN", "TPE", false, -1);
        scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML212", "Boeing 737 Three Classes", "TPE", "SIN", true, secondpairId);

        long thirdPairId = scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML311", "Boeing 747 Three Classes", "SIN", "NRT", false, -1);
        scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML312", "Boeing 747 Three Classes", "NRT", "SIN", true, thirdPairId);

        long fourthPairId = scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML411", "Boeing 737 Three Classes", "HKG", "NRT", false, -1);
        scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML412", "Boeing 737 Three Classes", "NRT", "HKG", true, fourthPairId);

        long fifthPairId = scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML511", "Boeing 737 Three Classes", "TPE", "NRT", false, -1);
        scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML512", "Boeing 737 Three Classes", "NRT", "TPE", true, fifthPairId);

        long sixthPairId = scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML611", "Boeing 737 Three Classes", "SIN", "SYD", false, -1);
        scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML612", "Boeing 737 Three Classes", "SYD", "SIN", true, sixthPairId);

        long seventhPair = scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML621", "Boeing 737 All Economy", "SIN", "SYD", false, -1);
        scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML622", "Boeing 737 All Economy", "SYD", "SIN", true, seventhPair);

        long eightPair = scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML711", "Boeing 747 Three Classes", "SYD", "NRT", false, -1);
        scheduleManagerUseCaseSessionBean.createNewFlightForDataInit("ML712", "Boeing 747 Three Classes", "NRT", "SYD", true, eightPair);

        // flight schedule plan (recurrent need to change logic)
        
        // recurrent weekly flight schedule plan
        Date firstRWDate = formatDate("2023-12-04 09:00:00");
        List<Date> firstDepartDateList = new ArrayList<Date>();
        firstDepartDateList.add(firstRWDate);
        HashMap<CabinClassType, List<Fare>> firstRWfaresForCabinClassList = new HashMap<CabinClassType, List<Fare>>();
        AircraftConfiguration a1 = aircraftConfigurationEntitySessionBean.getAircraftConfigurationPerConfigurationName("Boeing 747 Three Classes");
        a1.getCabinClassList().size();
        
        Fare f1 = new Fare("ABD1234", new BigDecimal(6000), a1.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.F).findFirst().get());
        List<Fare> fareList10 = new ArrayList<Fare>();
        fareList10.add(f1);
        
        Fare j1 = new Fare("ABE1234", new BigDecimal(3000), a1.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.J).findFirst().get());
        List<Fare> fareList11 = new ArrayList<Fare>();
        fareList11.add(j1);
        
        Fare y1 = new Fare("ABF1234", new BigDecimal(1000), a1.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.Y).findFirst().get());
        List<Fare> fareList12 = new ArrayList<Fare>();
        fareList12.add(y1);
        
        firstRWfaresForCabinClassList.put(CabinClassType.F, fareList10);
        firstRWfaresForCabinClassList.put(CabinClassType.J, fareList11);
        firstRWfaresForCabinClassList.put(CabinClassType.Y, fareList12);
        
        long idFirstt = scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML711", firstDepartDateList, Duration.ofSeconds(14 * 60 * 60), formatDate("2023-12-31 09:00:00"), 7, firstRWfaresForCabinClassList, false, -1, null);
        scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML711", firstDepartDateList, Duration.ofSeconds(14 * 60 * 60), formatDate("2023-12-31 09:00:00"), 7, firstRWfaresForCabinClassList, true, idFirstt, Duration.ofSeconds(2 * 60 * 60));
        
        // recurrent weekly flight schedule plan 2
        Date secondRWDate = formatDate("2023-12-03 12:00:00");
        List<Date> secondDepartDateList = new ArrayList<Date>();
        secondDepartDateList.add(secondRWDate);
        HashMap<CabinClassType, List<Fare>> secondRWfaresForCabinClassList = new HashMap<CabinClassType, List<Fare>>();
        AircraftConfiguration a2 = aircraftConfigurationEntitySessionBean.getAircraftConfigurationPerConfigurationName("Boeing 737 Three Classes");
        a2.getCabinClassList().size();
        
        Fare f2 = new Fare("ABW1234", new BigDecimal(3000), a2.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.F).findFirst().get());
        List<Fare> fareList13 = new ArrayList<Fare>();
        fareList13.add(f2);
        
        Fare j2 = new Fare("ABX1234", new BigDecimal(1500), a2.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.J).findFirst().get());
        List<Fare> fareList14 = new ArrayList<Fare>();
        fareList14.add(j2);
        
        Fare y2 = new Fare("ABY1234", new BigDecimal(500), a2.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.Y).findFirst().get());
        List<Fare> fareList15 = new ArrayList<Fare>();
        fareList15.add(y2);
        
        secondRWfaresForCabinClassList.put(CabinClassType.F, fareList13);
        secondRWfaresForCabinClassList.put(CabinClassType.J, fareList14);
        secondRWfaresForCabinClassList.put(CabinClassType.Y, fareList15);
        long idsecondt = scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML611", secondDepartDateList, Duration.ofSeconds(8 * 60 * 60), formatDate("2023-12-31 12:00:00"), 7, secondRWfaresForCabinClassList, false, -1, null);
        scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML611", secondDepartDateList, Duration.ofSeconds(8 * 60 * 60), formatDate("2023-12-31 12:00:00"), 7, secondRWfaresForCabinClassList, true, idsecondt, Duration.ofSeconds(2 * 60 * 60));
        
        // refcurrent weekly flight schedule plan 3
        Date thirdRWDate = formatDate("2023-12-05 10:00:00");
        List<Date> thirdDepartDateList = new ArrayList<Date>();
        thirdDepartDateList.add(thirdRWDate);
        HashMap<CabinClassType, List<Fare>> thirdRWfaresForCabinClassList = new HashMap<CabinClassType, List<Fare>>();
        AircraftConfiguration a3 = aircraftConfigurationEntitySessionBean.getAircraftConfigurationPerConfigurationName("Boeing 737 All Economy");
        a3.getCabinClassList().size();
        
        Fare y3 = new Fare("ABF1234", new BigDecimal(700), a3.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.Y).findFirst().get());
        List<Fare> fareList16 = new ArrayList<Fare>();
        fareList16.add(y3);

        thirdRWfaresForCabinClassList.put(CabinClassType.Y, fareList16);
        long idthirdt = scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML621", thirdDepartDateList, Duration.ofSeconds(8 * 60 * 60), formatDate("2023-12-31 10:00:00"), 7, thirdRWfaresForCabinClassList, false, -1, null);
        scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML621", thirdDepartDateList, Duration.ofSeconds(8 * 60 * 60), formatDate("2023-12-31 10:00:00"), 7, thirdRWfaresForCabinClassList, true, idthirdt, Duration.ofSeconds(2 * 60 * 60));
        
        // recurrent weekly flight schedule plan 4
        Date fourthRWDate = formatDate("2023-12-04 10:00:00");
        List<Date> fourthDepartDateList = new ArrayList<Date>();
        fourthDepartDateList.add(fourthRWDate);
        HashMap<CabinClassType, List<Fare>> fourthRWfaresForCabinClassList = new HashMap<CabinClassType, List<Fare>>();
        AircraftConfiguration a4 = aircraftConfigurationEntitySessionBean.getAircraftConfigurationPerConfigurationName("Boeing 747 Three Classes");
        a4.getCabinClassList().size();

        Fare f3 = new Fare("BBF1234", new BigDecimal(3100), a4.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.F).findFirst().get());
        List<Fare> fareList17 = new ArrayList<Fare>();
        fareList17.add(f3);

        Fare j3 = new Fare("CBF1234", new BigDecimal(1600), a4.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.J).findFirst().get());
        List<Fare> fareList18 = new ArrayList<Fare>();
        fareList18.add(j3);

        Fare y4 = new Fare("DBF1234", new BigDecimal(600), a4.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.Y).findFirst().get());
        List<Fare> fareList19 = new ArrayList<Fare>();
        fareList19.add(y4);

        fourthRWfaresForCabinClassList.put(CabinClassType.F, fareList17);
        fourthRWfaresForCabinClassList.put(CabinClassType.J, fareList18);
        fourthRWfaresForCabinClassList.put(CabinClassType.Y, fareList19);
        long idfourtht = scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML311", fourthDepartDateList, Duration.ofSeconds(23400), formatDate("2023-12-31 10:00:00"), 7, fourthRWfaresForCabinClassList, false, -1, null);
        scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML311", fourthDepartDateList, Duration.ofSeconds(23400), formatDate("2023-12-31 10:00:00"), 7, fourthRWfaresForCabinClassList, true, idfourtht, Duration.ofSeconds(3 * 60 * 60));
        
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
        fareList4.add(toAdd4);
        
        Fare toAdd5 = new Fare("BCD2345", new BigDecimal(1400), b.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.J).findFirst().get());
        List<Fare> fareList5 = new ArrayList<Fare>();
        fareList5.add(toAdd5);
        
        Fare toAdd6 = new Fare("CDE3456", new BigDecimal(400), b.getCabinClassList().stream().filter(x -> x.getCabinClassName() == CabinClassType.Y).findFirst().get());
        List<Fare> fareList6 = new ArrayList<Fare>();
        fareList6.add(toAdd6);
        
        secondfaresForCabinClassList.put(CabinClassType.F, fareList4);
        secondfaresForCabinClassList.put(CabinClassType.J, fareList5);
        secondfaresForCabinClassList.put(CabinClassType.Y, fareList6);
        long idsecond = scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML511", secondDepartureDateList, Duration.ofSeconds(3 * 60 * 60), null, 0, secondfaresForCabinClassList, false, -1, null);
        scheduleManagerUseCaseSessionBean.createNewFlightSchedulePlan("ML511", secondDepartureDateList, Duration.ofSeconds(3 * 60 * 60), null, 0, secondfaresForCabinClassList, true, idsecond, Duration.ofSeconds(2 * 60 * 60));
        
    }
}
