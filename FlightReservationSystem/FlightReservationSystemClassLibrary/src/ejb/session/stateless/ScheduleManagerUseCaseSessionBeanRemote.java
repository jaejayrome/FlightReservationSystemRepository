/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.Flight;
import entity.FlightSchedulePlan;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;
import util.enumerations.CabinClassType;
import util.enumerations.FlightStatus;
import util.exception.InitialFlightNotInstantiatedException;
import util.exception.UpdateFlightSchedulePlanException;

/**
 *
 * @author jeromegoh
 */
@Remote
public interface ScheduleManagerUseCaseSessionBeanRemote {
    public long createNewFlight(String flightNumber, String configurationName, String originAirport, String destinationAirport, boolean createReturn, long initialId) throws InitialFlightNotInstantiatedException;
    public List<Flight> viewAllFlights();
    public Flight viewSpecificFlightDetails(String flightNumber);
    public long createNewFlightSchedulePlan(String flightNumber, List<Date> departureDateList, Duration duration, Date endDate, int frequency, HashMap<CabinClassType, List<Fare>> faresForCabinClassList, boolean makeReturn, long id, Duration layover);
    public List<FlightSchedulePlan> viewAllFlightSchedulePlan();
    public void updateFlightNumber(String flightNumber, String newFlightNumber);
    public void updateFlightStatus(String flightNumber, FlightStatus newStatus);
    public boolean deleteFlight(String flightNumber);
    public void updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) throws UpdateFlightSchedulePlanException;
    public void deleteFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan);
    public long createNewFlightForDataInit(String flightNumber, String configurationName, String originAirport, String destinationAirport, boolean createReturn, long initialId);
}
