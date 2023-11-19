/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightRoute;
import java.util.List;
import javax.ejb.Local;
import util.exception.AirportNotFoundException;
import util.exception.NoFlightRouteFoundException;
import util.util.Pair;

/**
 *
 * @author jeromegoh
 */
@Local
public interface FlightRouteEntitySessionBeanLocal {
    public FlightRoute createFlightRoute(FlightRoute flightRoute);
    public List<FlightRoute> getAllFlightRoutes();
    public FlightRoute getFlightRouteById(long id);
    // public FlightRoute deleteFlightRoute(long id);
    public FlightRoute getFlightRouteByCityName(String originAirport, String destinationAirport) throws NoFlightRouteFoundException, AirportNotFoundException;
    public boolean disableFlightRoute(String originAirport, String destinationAirport);
    public boolean deleteFlightRoute(String originAirport, String destinationAirport);
}
