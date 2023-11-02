/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.Flight;
import entity.FlightSchedulePlan;
import java.util.Date;
import java.util.List;
import java.time.Duration;
import java.util.HashMap;
import javax.ejb.Local;
import util.enumerations.CabinClassType;
import util.enumerations.FlightStatus;
import util.exception.InitialFlightNotInstantiatedException;

/**
 *
 * @author jeromegoh
 */
@Local
public interface ScheduleManagerUseCaseSessionBeanLocal {
     public boolean createNewFlight(String flightNumber, String configurationName, String originCity, String destinationCity) throws InitialFlightNotInstantiatedException;
     public List<Flight> viewAllFlights();
     public Flight viewSpecificFlightDetails(String flightNumber);
     public boolean createNewFlightSchedulePlan(String flightNumber, List<Date> departureDateList, Duration duration, Date endDate, int frequency, HashMap<CabinClassType, List<Fare>> faresForCabinClassList);
     public List<FlightSchedulePlan> viewAllFlightSchedulePlan();
     public void updateFlightNumber(String flightNumber, String newFlightNumber);
     public void updateFlightStatus(String flightNumber, FlightStatus newStatus);
     public boolean deleteFlight(String flightNumber);
     public FlightSchedulePlan updateFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan);
}
