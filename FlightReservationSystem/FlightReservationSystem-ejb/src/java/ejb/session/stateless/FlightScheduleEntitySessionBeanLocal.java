/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jeromegoh
 */
@Local
public interface FlightScheduleEntitySessionBeanLocal {
    public FlightSchedule createFlightSchedule(FlightSchedule flightSchedule);
    public List<FlightSchedule> viewFlightSchedulesByFlightNumber(String flightNumber);
    public List<FlightSchedule> getFlightSchedulesByAirportDestination(String iataCode);
    public List<FlightSchedule> getFlightSchedulesByAirportOrigin(String iataCode);
    public FlightSchedule getFlightScheduleById(long id);
}
