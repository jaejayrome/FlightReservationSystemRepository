/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jeromegoh
 */
@Local
public interface FlightEntitySessionBeanLocal {
    public long createFlight(Flight flight);
    public List<Flight> viewAllFlights();
    public Flight getFlightById(long id);
    public long getIdByFlightNumber(String flightNumber);
    public long updateFlightNumber(long id, String newFlightNumber);
}
