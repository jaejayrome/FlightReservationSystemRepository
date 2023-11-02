/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.Fare;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.MultipleFlightSchedulePlan;
import entity.RecurrentFlightSchedulePlan;
import entity.RecurrentWeeklyFlightSchedulePlan;
import entity.SingleFlightSchedulePlan;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.enumerations.CabinClassType;
import util.enumerations.FlightRouteStatus;
import util.enumerations.FlightSchedulePlanStatus;
import util.enumerations.FlightStatus;
import util.exception.InitialFlightNotInstantiatedException;
import util.exception.NoFlightRouteFoundException;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class ScheduleManagerUseCaseSessionBean implements ScheduleManagerUseCaseSessionBeanRemote, ScheduleManagerUseCaseSessionBeanLocal {

    @EJB
    private FareEntitySessionBeanLocal fareEntitySessionBean;

    @EJB
    private FlightRouteEntitySessionBeanLocal flightRouteEntitySessionBean;

    @EJB
    private AircraftConfigurationEntitySessionBeanLocal aircraftConfigurationEntitySessionBean;

    @EJB
    private FlightSchedulePlanEntitySessionBeanLocal flightSchedulePlanEntitySessionBean;

    @EJB
    private FlightScheduleEntitySessionBeanLocal flightScheduleEntitySessionBean;

    @EJB
    private FlightEntitySessionBeanLocal flightEntitySessionBean;
    
    
    
    @Override
    public boolean createNewFlight(String flightNumber, String configurationName, String originAirport, String destinationAirport) throws InitialFlightNotInstantiatedException {
        
        try {
            // cannot use city because there an be a city with 2 airports
            AircraftConfiguration aircraftConfiguration = aircraftConfigurationEntitySessionBean.getAircraftConfigurationPerConfigurationName(configurationName);
            FlightRoute flightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(originAirport, destinationAirport);
            Flight flight = new Flight(flightNumber, FlightStatus.DISABLED);

            // associate flight -> aircraft configuration
            flight.setAircraftConfiguration(aircraftConfiguration);
            // associate flight -> flightroute
            flight.setFlightRoute(flightRoute);

            // persist to database
            flightEntitySessionBean.createFlight(flight);

            // associate flightRoute -> flight
            int init = flightRoute.getFlightList().size();
            flightRoute.getFlightList().add(flight);
            flightRoute.setFlightRouteStatus(FlightRouteStatus.ACTIVE);

            // associate aircraftConfig -> flight
            int init2 = aircraftConfiguration.getFlightList().size();
            aircraftConfiguration.getFlightList().add(flight);
        } catch (NoFlightRouteFoundException e) {
            // exception is thrown if initial flight cannot be created in the first place
            throw new InitialFlightNotInstantiatedException("Invalid Flight Route Entered!");
        }
        
        // would try to create return flight but would return true or false based on this
        try {
            // if return flight route can be created
            FlightRoute returnFlightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(destinationAirport, originAirport);
            return true;
            
        } catch (NoFlightRouteFoundException e) {
            // if flight route cannot be created
            return false;
        }
        
       
    }
    
    @Override
    public List<Flight> viewAllFlights() {
        return flightEntitySessionBean.viewAllFlights();
    }
    
    @Override
    public Flight viewSpecificFlightDetails(String flightNumber) {
        long FlightID = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
        Flight flight = flightEntitySessionBean.getFlightById(FlightID);
        flight.getAircraftConfiguration().getCabinClassList().size();
        flight.getFlightSchedulePlanList().size();
        
        flight.getFlightSchedulePlanList().stream().forEach(x -> {
            x.getFlightScheduleList().size();
            x.getCabinClassList().size();
            x.getCabinClassList().forEach(y ->  y.getFareList().size());
            
        });
        
        return flight;
    }
    
    @Override
    public void updateFlightNumber(String flightNumber, String newFlightNumber) {
        long id = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
        Flight flight = flightEntitySessionBean.getFlightById(id);
        flight.setFlightNumber(newFlightNumber);
    }
    
    @Override
    public void updateFlightStatus(String flightNumber, FlightStatus newStatus) {
        long id = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
        Flight flight = flightEntitySessionBean.getFlightById(id);
        flight.setStatus(newStatus);
    } 
    
    @Override
    public boolean deleteFlight(String flightNumber) {
        // check whether is there any flight schedule plan that's associated with this flight 
        long id = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
        Flight flight = flightEntitySessionBean.getFlightById(id);
        
        // check here 
        int init = flight.getFlightSchedulePlanList().size();
        if (init > 0) {
            return flightEntitySessionBean.disableFlight(flight);
            // means that flight associated with 1 or more FSP
        } else {
            // flight is not being used can be deleted
            return flightEntitySessionBean.deleteFlight(flight);
            
        }
    }
    
 
    
    @Override
    public boolean createNewFlightSchedulePlan(String flightNumber, List<Date> departureDateList, Duration duration, Date endDate, int frequency, HashMap<CabinClassType, List<Fare>> faresForCabinClassList) {
        // need to add the fares for cabin class
        // should check for whether duration is null
        
        long flightId = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
        Flight flight = flightEntitySessionBean.getFlightById(flightId);
        
        // update the flight status is active
        flight.setStatus(FlightStatus.ACTIVE);
        
        if (departureDateList.size() == 1 && frequency == 0 ) {
            // create a single flight schedule
            
            // persist FSP
            SingleFlightSchedulePlan flightSchedulePlan = new SingleFlightSchedulePlan(FlightSchedulePlanStatus.ACTIVE, flight);
            flightSchedulePlanEntitySessionBean.createFlightSchedulePlan(flightSchedulePlan);
            
            // persist FS
            FlightSchedule flightSchedule = new FlightSchedule(departureDateList.get(0), duration, computeArrivalTime(departureDateList.get(0), duration));
            flightSchedule.setFlightSchedulePlan(flightSchedulePlan);
            flightScheduleEntitySessionBean.createFlightSchedule(flightSchedule);
            
            // associate the fsp with the fs (one to many lazy initialisation)
            int init = flightSchedulePlan.getFlightScheduleList().size();
            flightSchedulePlan.getFlightScheduleList().add(flightSchedule);
            
            // associate fs with fsp
            // flightSchedule.setFlightSchedulePlan(flightSchedulePlan);
            
            // associate fsp with flight 
            flightSchedulePlan.setFlight(flight);
          
            // associate flight with fsp
            int initFlight = flight.getFlightSchedulePlanList().size();
            flight.getFlightSchedulePlanList().add(flightSchedulePlan);
            
            
            // associate each fs to cabin class
            // associate eaach cabin class to fares
            List<FlightSchedule> flightScheduleList = new ArrayList<FlightSchedule>();
            flightScheduleList.add(flightSchedule);
            updateAndPersistFare(faresForCabinClassList, flightSchedulePlan);
            
        } else if (departureDateList.size() > 1 && frequency == 0) {
            // create a multiple flight schedule
            
            // persist FSP
            MultipleFlightSchedulePlan flightSchedulePlan = new MultipleFlightSchedulePlan( FlightSchedulePlanStatus.ACTIVE, flight);
            flightSchedulePlanEntitySessionBean.createFlightSchedulePlan(flightSchedulePlan);
            
            // persist FS
            List<FlightSchedule> flightScheduleList = new ArrayList<FlightSchedule>();
            for (int i = 0; i < departureDateList.size(); i++) {
                FlightSchedule flightschedule = new FlightSchedule(departureDateList.get(i), duration, computeArrivalTime(departureDateList.get(i), duration));
                // persist FS
                flightschedule.setFlightSchedulePlan(flightSchedulePlan);
                flightScheduleEntitySessionBean.createFlightSchedule(flightschedule);
                // associate FS to FSP
                // flightschedule.setFlightSchedulePlan(flightSchedulePlan);
                flightScheduleList.add(flightschedule);
            }
            
            // associate FSP to FS         
            int init = flightSchedulePlan.getFlightScheduleList().size();
            flightSchedulePlan.getFlightScheduleList().addAll(flightScheduleList);
            
            // associate each fs with the new Fare
            updateAndPersistFare(faresForCabinClassList, flightSchedulePlan);
            
        } else if (departureDateList.size() == 1 && endDate!=null && frequency > 0 && frequency != 7) {
            // create a recurrent flight schedule
            
            // persist FSP
            RecurrentFlightSchedulePlan flightSchedulePlan= new RecurrentFlightSchedulePlan(FlightSchedulePlanStatus.ACTIVE, endDate, new BigDecimal(frequency), flight);
            flightSchedulePlanEntitySessionBean.createFlightSchedulePlan(flightSchedulePlan);
            
            // generate the flight schedules for this flight schedule plan
            List<FlightSchedule> collatedFlightSchedules = generateFlightSchedulePlan(departureDateList.get(0), endDate, duration, frequency, flightSchedulePlan); 
            
            // associate FSP to FS
            int init = flightSchedulePlan.getFlightScheduleList().size();
            flightSchedulePlan.getFlightScheduleList().addAll(collatedFlightSchedules);
            
            // associate each FS with the new Fare through Cabin Class
            updateAndPersistFare(faresForCabinClassList, flightSchedulePlan);
            
        } else {
            // create a recurrent weekly flight schedule
            
            // persist FSP
            RecurrentWeeklyFlightSchedulePlan flightSchedulePlan= new RecurrentWeeklyFlightSchedulePlan(FlightSchedulePlanStatus.ACTIVE, endDate, flight);
            flightSchedulePlanEntitySessionBean.createFlightSchedulePlan(flightSchedulePlan);
            
            // generate the flight schedules for this flight schedule plan
            List<FlightSchedule> collatedFlightSchedules = generateFlightSchedulePlan(departureDateList.get(0), endDate, duration, 7, flightSchedulePlan); 
            
            // associate FSP to FS
            int init = flightSchedulePlan.getFlightScheduleList().size();
            flightSchedulePlan.getFlightScheduleList().addAll(collatedFlightSchedules);
            
            // associate each FS with the new Fare through Cabin Class
           updateAndPersistFare(faresForCabinClassList, flightSchedulePlan);
        }
        
        
        // to implement this logic 
        // we would have to check whether is there a return flight 
        // then if have ask the user to input whats the layover duration then 
        // create another flight schedule plan
        Flight returnFlight = flightEntitySessionBean.checkReturnFlight(flight.getFlightRoute().getOrigin().getIataAirportCode(), flight.getFlightRoute().getDestination().getIataAirportCode());
        // return true if there is a return flight 
        return returnFlight != null;
    }
    
    @Override
    public List<FlightSchedulePlan> viewAllFlightSchedulePlan() {
        List<FlightSchedulePlan> collatedList = flightSchedulePlanEntitySessionBean.viewAllFlightSchedulePlan();
        // unsure by what it means by complementary return flight schedule
        Comparator<FlightSchedulePlan> compare = (x, y) -> {
            // sort by ascending order in flight number 
            if (x.getFlight().getFlightNumber().compareTo(y.getFlight().getFlightNumber()) == 0) {
               int init1 = x.getFlightScheduleList().size();
               int init2 = y.getFlightScheduleList().size();
               
               Date xFirstDepature = x.getFlightScheduleList().get(0).getDepartureTime();
               Date yFirstDepature = y.getFlightScheduleList().get(0).getDepartureTime();
               
               // if x firstDeparture is after y, then x should come first 
               return (xFirstDepature.after(yFirstDepature)) ? -1 : 1;
            } else {
               return x.getFlight().getFlightNumber().compareTo(y.getFlight().getFlightNumber());
            }
        };
        return collatedList.stream().sorted(compare).collect(Collectors.toList());
    }
    
    @Override
    public FlightSchedulePlan updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
     // should prompt the user to enter the flight number 
     // prompt the user which flight schedule plan is to be updated
     // using the id, send it to the backend 
     // then check business rules 
     
     return null;
     
    }
    
    
    public Date computeArrivalTime(Date departureTime, Duration flightDuration) {
        Instant departureInstant = departureTime.toInstant();
        LocalDateTime departureDateTime = LocalDateTime.ofInstant(departureInstant, ZoneId.systemDefault());
        LocalDateTime arrivalDateTime = departureDateTime.plus(flightDuration);
        Instant arrivalInstant = arrivalDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date arrivalTime = Date.from(arrivalInstant);
    
        return arrivalTime;
    }
    
    public Date addDaysToDate(Date initialDate, int daysToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(initialDate);
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
        Date updatedDate = calendar.getTime();

        return updatedDate;
    }
    
    public List<FlightSchedule> generateFlightSchedulePlan(Date startDate, Date endDate, Duration duration, int frequency, FlightSchedulePlan recurrentFlightSchedulePlan) {
        List<FlightSchedule> flightSchedules = new ArrayList<FlightSchedule>();

        Date currentDate = startDate;
        while (currentDate.before(endDate)) {
            // persist FS
            FlightSchedule flightSchedule = new FlightSchedule(currentDate, duration, computeArrivalTime(currentDate, duration));
            flightSchedule.setFlightSchedulePlan(recurrentFlightSchedulePlan);
            flightScheduleEntitySessionBean.createFlightSchedule(flightSchedule);
            // associate each FS to FSP
            flightSchedules.add(flightSchedule);
            currentDate = addDaysToDate(currentDate, frequency);
        }
        return flightSchedules;
    }
    
    // for all the flight schedules i would have to update 
    //  each cabin class to the respective one
    
    // there are some issues 
    // cabin class no association to flight schedule
    // flight schedule have no association to cabin class
    // fare have no association to cabin class
    public FlightSchedulePlan updateAndPersistFare( HashMap<CabinClassType, List<Fare>> fareForEveryCabinClass, FlightSchedulePlan flightSchedulePlan) {
        
        flightSchedulePlan.getFlight().getAircraftConfiguration().getCabinClassList().size();
        List<CabinClass> cabinClassList = flightSchedulePlan.getFlight().getAircraftConfiguration().getCabinClassList();
        
        // if it's single, then only have one flight schedule so would only be run once
//        for (FlightSchedule flightSchedule : flightSchedules) {
            // second for loop running 
            for (CabinClass cabinClass : cabinClassList) {
                CabinClassType cabinClassName = cabinClass.getCabinClassName();
                List<Fare> fareListForThisCabinClass = fareForEveryCabinClass.get(cabinClassName);
                // lazy loading 
                int initFare = cabinClass.getFareList().size();
                List<Fare> cabinClassFares = cabinClass.getFareList();
                fareListForThisCabinClass.stream().forEach(x -> {
                    // persist fare
                    // association between fare -> cabinClass done 
                    fareEntitySessionBean.createFare(x);
                    // association between cabinClass -> fare
                    cabinClassFares.add(x);
                });
                cabinClass.setFlightSchedulePlan(flightSchedulePlan);
                flightSchedulePlan.getCabinClassList().add(cabinClass);
            }
            return flightSchedulePlan;
//        }
//        return flightSchedules;
    }
}
