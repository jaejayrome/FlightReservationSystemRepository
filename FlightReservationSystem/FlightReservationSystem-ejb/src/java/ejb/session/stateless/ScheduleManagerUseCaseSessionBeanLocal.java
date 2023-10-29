/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.Flight;
import java.util.Date;
import java.util.List;
import java.time.Duration;
import javax.ejb.Local;

/**
 *
 * @author jeromegoh
 */
@Local
public interface ScheduleManagerUseCaseSessionBeanLocal {
     public boolean createNewFlight(String flightNumber, String configurationName, String originCity, String destinationCity);
     public List<Flight> viewAllFlights();
     public Flight viewSpecificFlightDetails(String flightNumber);
     public boolean createNewFlightSchedulePlan(List<Date> departureDateList, Duration duration, Date endDate, int frequency, List<List<Fare>> faresForCabinClassList);
}
