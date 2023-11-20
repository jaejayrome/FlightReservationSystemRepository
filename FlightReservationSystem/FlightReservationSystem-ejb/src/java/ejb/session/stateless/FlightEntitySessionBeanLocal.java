/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightNotFoundException;

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
    public Flight checkReturnFlight(String originAirport, String destinationAirport, long flightGroup);
    public boolean deleteFlight(Flight flight);
    public boolean disableFlight(Flight flight);
    public List<Flight> checkFlightRouteUsed(String originAirport, String destinationAirport);
    public Flight getFlightByFlightNumber(String flightNumber) throws FlightNotFoundException;
}
