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
import util.enumerations.FlightSchedulePlanStatus;
import util.enumerations.FlightStatus;

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
    public boolean createNewFlight(String flightNumber, String configurationName, String originAirport, String destinationAirport) {
        // cannot use city because there an be a city with 2 airports
        AircraftConfiguration aircraftConfiguration = aircraftConfigurationEntitySessionBean.getAircraftConfigurationPerConfigurationName(configurationName);
        FlightRoute flightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(originAirport, destinationAirport);
        Flight flight = new Flight(flightNumber, FlightStatus.DISABLED);
        // persist to database
        flightEntitySessionBean.createFlight(flight);
        // associate flight -> aircraft configuration
        flight.setAircraftConfiguration(aircraftConfiguration);
        // associate flight -> flightroute
        flight.setFlightRoute(flightRoute);
        
        // associate flightRoute -> flight
        int init = flightRoute.getFlightList().size();
        flightRoute.getFlightList().add(flight);
        
        // associate aircraftConfig -> flight
        int init2 = aircraftConfiguration.getFlightList().size();
        aircraftConfiguration.getFlightList().add(flight);
        
        FlightRoute returnFlightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(destinationAirport, originAirport);
        return returnFlightRoute != null ? true : false;
    }
    
    @Override
    public List<Flight> viewAllFlights() {
        return flightEntitySessionBean.viewAllFlights();
    }
    
    @Override
    public Flight viewSpecificFlightDetails(String flightNumber) {
        long FlightID = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
        return flightEntitySessionBean.getFlightById(FlightID);
    }
    
    @Override
    public boolean createNewFlightSchedulePlan(String flightNumber, List<Date> departureDateList, Duration duration, Date endDate, int frequency, HashMap<CabinClassType, List<Fare>> faresForCabinClassList) {
        // need to add the fares for cabin class
        // should check for whether duration is null
        
        long flightId = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
        Flight flight = flightEntitySessionBean.getFlightById(flightId);
        
        if (departureDateList.size() == 1 && frequency == 0 ) {
            // create a single flight schedule
            
            // persist FSP
            SingleFlightSchedulePlan flightSchedulePlan = new SingleFlightSchedulePlan(FlightSchedulePlanStatus.ACTIVE);
            flightSchedulePlanEntitySessionBean.createFlightSchedulePlan(flightSchedulePlan);
            
            // persist FS
            FlightSchedule flightSchedule = new FlightSchedule(departureDateList.get(0), duration, computeArrivalTime(departureDateList.get(0), duration));
            flightScheduleEntitySessionBean.createFlightSchedule(flightSchedule);
            
            // associate the fsp with the fs (one to many lazy initialisation)
            int init = flightSchedulePlan.getFlightScheduleList().size();
            flightSchedulePlan.getFlightScheduleList().add(flightSchedule);
            
            // associate fs with fsp
            flightSchedule.setFlightSchedulePlan(flightSchedulePlan);
            
            // associate fsp with flight 
            flightSchedulePlan.setFlight(flight);
          
            // associate flight with fsp
            int initFlight = flight.getFlightSchedulePlanList().size();
            flight.getFlightSchedulePlanList().add(flightSchedulePlan);
            
            // associate each fs with the new fare
            List<FlightSchedule> flightScheduleList = new ArrayList<FlightSchedule>();
            flightScheduleList.add(flightSchedule);
            updateAndPersistFare(flightScheduleList, faresForCabinClassList);
            
        } else if (departureDateList.size() > 1 && frequency == 0) {
            // create a multiple flight schedule
            
            // persist FSP
            MultipleFlightSchedulePlan flightSchedulePlan = new MultipleFlightSchedulePlan( FlightSchedulePlanStatus.ACTIVE);
            flightSchedulePlanEntitySessionBean.createFlightSchedulePlan(flightSchedulePlan);
            
            // persist FS
            List<FlightSchedule> flightScheduleList = new ArrayList<FlightSchedule>();
            for (int i = 0; i < departureDateList.size(); i++) {
                FlightSchedule flightschedule = new FlightSchedule(departureDateList.get(i), duration, computeArrivalTime(departureDateList.get(i), duration));
                // persist FS
                flightScheduleEntitySessionBean.createFlightSchedule(flightschedule);
                // associate FS to FSP
                flightschedule.setFlightSchedulePlan(flightSchedulePlan);
                flightScheduleList.add(flightschedule);
            }
            
            // associate FSP to FS         
            int init = flightSchedulePlan.getFlightScheduleList().size();
            flightSchedulePlan.getFlightScheduleList().addAll(flightScheduleList);
            
            // associate each fs with the new Fare
            updateAndPersistFare(flightScheduleList, faresForCabinClassList);
            
        } else if (departureDateList.size() == 1 && endDate!=null && frequency > 0 && frequency != 7) {
            // create a recurrent flight schedule
            
            // persist FSP
            RecurrentFlightSchedulePlan flightSchedulePlan= new RecurrentFlightSchedulePlan(FlightSchedulePlanStatus.ACTIVE, endDate, new BigDecimal(frequency));
            flightSchedulePlanEntitySessionBean.createFlightSchedulePlan(flightSchedulePlan);
            
            // generate the flight schedules for this flight schedule plan
            List<FlightSchedule> collatedFlightSchedules = generateFlightSchedulePlan(departureDateList.get(0), endDate, duration, frequency, flightSchedulePlan); 
            
            // associate FSP to FS
            int init = flightSchedulePlan.getFlightScheduleList().size();
            flightSchedulePlan.getFlightScheduleList().addAll(collatedFlightSchedules);
            
            // associate each FS with the new Fare through Cabin Class
            updateAndPersistFare(collatedFlightSchedules, faresForCabinClassList);
            
        } else {
            // create a recurrent weekly flight schedule
            
            // persist FSP
            RecurrentWeeklyFlightSchedulePlan flightSchedulePlan= new RecurrentWeeklyFlightSchedulePlan(FlightSchedulePlanStatus.ACTIVE, endDate);
            flightSchedulePlanEntitySessionBean.createFlightSchedulePlan(flightSchedulePlan);
            
            // generate the flight schedules for this flight schedule plan
            List<FlightSchedule> collatedFlightSchedules = generateFlightSchedulePlan(departureDateList.get(0), endDate, duration, 7, flightSchedulePlan); 
            
            // associate FSP to FS
            int init = flightSchedulePlan.getFlightScheduleList().size();
            flightSchedulePlan.getFlightScheduleList().addAll(collatedFlightSchedules);
            
            // associate each FS with the new Fare through Cabin Class
           updateAndPersistFare(collatedFlightSchedules, faresForCabinClassList);
        }
        
        // so if there is return flight and the time between the arrival of the outbound flight and the departure of the inbound flight is less than a day?
        // now need to check whether have return flight schedule plan 
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
            flightScheduleEntitySessionBean.createFlightSchedule(flightSchedule);
            // associate each FS to FSP
            flightSchedule.setFlightSchedulePlan(recurrentFlightSchedulePlan);
            flightSchedules.add(flightSchedule);
            currentDate = addDaysToDate(currentDate, frequency);
        }
        return flightSchedules;
    }
    
    // for all the flight schedules i would have to update 
    //  each cabin class to the respective one
    public List<FlightSchedule> updateAndPersistFare(List<FlightSchedule> flightSchedules, HashMap<CabinClassType, List<Fare>> fareForEveryCabinClass) {
        
        // if it's single, then only have one flight schedule so would only be run once
        for (FlightSchedule flightSchedule : flightSchedules) {
            int init = flightSchedule.getCabinClassList().size();
            List<CabinClass> cabinClassList = new ArrayList<CabinClass>();
            for (CabinClass cabinClass : flightSchedule.getCabinClassList()) {
                CabinClassType cabinClassName = cabinClass.getCabinClassName();
                
                List<Fare> fareListForThisCabinClass = fareForEveryCabinClass.get(cabinClassName);
                fareListForThisCabinClass.stream().forEach(x -> {
                    // persist fare
                    fareEntitySessionBean.createFare(x);
                    // associate each fare to the cabin class
                    x.setCabinClass(cabinClass);
                });
                
                int initFare = cabinClass.getFareList().size();
                // update the cabinClass for thiese fares
                cabinClass.getFareList().addAll(fareListForThisCabinClass);
                // update the new collection
                cabinClassList.add(cabinClass);
            }
            // update the list of cabin class for each flight schedule
            flightSchedule.setCabinClassList(cabinClassList);
        }
        
        return flightSchedules;
    }
}
