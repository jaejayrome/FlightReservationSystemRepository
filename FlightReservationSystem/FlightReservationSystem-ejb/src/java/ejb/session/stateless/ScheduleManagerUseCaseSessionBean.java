/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.Fare;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.MultipleFlightSchedulePlan;
import entity.SingleFlightSchedulePlan;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.enumerations.FlightSchedulePlanStatus;
import util.enumerations.FlightStatus;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class ScheduleManagerUseCaseSessionBean implements ScheduleManagerUseCaseSessionBeanRemote, ScheduleManagerUseCaseSessionBeanLocal {

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
    public boolean createNewFlight(String flightNumber, String configurationName, String originCity, String destinationCity) {
        AircraftConfiguration aircraftConfiguration = aircraftConfigurationEntitySessionBean.getAircraftConfigurationPerConfigurationName(configurationName);
        FlightRoute flightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(originCity, destinationCity);
        Flight flight = new Flight(flightNumber, FlightStatus.DISABLED, aircraftConfiguration, flightRoute);
        flightEntitySessionBean.createFlight(flight);
        
        FlightRoute returnFlightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(destinationCity, originCity);
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
    
    /*  public FlightSchedule(Date departureTime, Duration flightDuration, Date arrivalTime, FlightSchedulePlan flightSchedulePlan) {
        this.departureTime = departureTime;
        this.flightDuration = flightDuration;
        this.arrivalTime = arrivalTime;
        this.flightSchedulePlan = flightSchedulePlan;
        this.cabinClassList = new ArrayList<CabinClass>();
    }
    */
    @Override
    public boolean createNewFlightSchedulePlan(String flightNumber, List<Date> departureDateList, Duration duration, Date endDate, int frequency, List<List<Fare>> faresForCabinClassList) {
        // should check for whether duration is null
        long flightId = flightEntitySessionBean.getIdByFlightNumber(flightNumber);
        Flight flight = flightEntitySessionBean.getFlightById(flightId);
        
        if (departureDateList.size() == 1 && frequency == 0 ) {
            // create a single flight schedule
            
            // need a flight
            SingleFlightSchedulePlan flightSchedulePlan = new SingleFlightSchedulePlan(flight, FlightSchedulePlanStatus.ACTIVE);
            FlightSchedule flightSchedule = new FlightSchedule(departureDateList.get(0), duration, computeArrivalTime(departureDateList.get(0), duration), flightSchedulePlan);
            // associate the flightschedule with the fsp (one to many lazy initialisation)
            int init = flightSchedulePlan.getFlightScheduleList().size();
            flightSchedulePlan.getFlightScheduleList().add(flightSchedule);
        } else if (departureDateList.size() > 1 && frequency == 0) {
            // create a multiple flight schedule
            
            MultipleFlightSchedulePlan flightSchedulePlan = new MultipleFlightSchedulePlan(flight, FlightSchedulePlanStatus.ACTIVE);
            List<FlightSchedule> flightScheduleList = new ArrayList<FlightSchedule>();
            
            for (int i = 0; i < departureDateList.size(); i++) {
                FlightSchedule flightschedule = new FlightSchedule(departureDateList.get(i), duration, computeArrivalTime(departureDateList.get(i), duration), flightSchedulePlan);
                flightScheduleList.add(flightschedule);
            }
            
            
            int init = flightSchedulePlan.getFlightScheduleList().size();
            flightSchedulePlan.getFlightScheduleList().addAll(flightScheduleList);
            
        } else if (departureDateList.size() == 1 && endDate!=null && frequency > 0 && frequency != 7) {
            // create a recurrent flight schedule
            
            
            // the schema has changed forgot to add in endTime and frequency
        } else {
            // the schema has changed for got o add in
        }
    }
    

    public Date computeArrivalTime(Date departureTime, Duration flightDuration) {
        // Convert the Date (departureTime) to LocalDateTime
        LocalDateTime departureDateTime = LocalDateTime.ofInstant(departureTime.toInstant(), java.time.ZoneId.systemDefault());

        // Calculate the arrival time by adding the flight duration to the departure time
        LocalDateTime arrivalDateTime = departureDateTime.plus(flightDuration);

        // Convert the LocalDateTime back to Date
        Date arrivalTime = Date.from(arrivalDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());

        return arrivalTime;
    }
    
    public boolean createSingleFlightSchedulePlan() {
        
    }
    
}
