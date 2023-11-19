/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Local;
import util.exception.AirportNotFoundException;
import util.exception.DuplicateFlightRouteException;
import util.exception.NoExistingAirportException;
import util.exception.NoFlightRouteFoundException;

/**
 *
 * @author jeromegoh
 */
@Local
public interface RoutePlannerUseCaseSessionBeanLocal {
    public long createNewFlightRoute(String originAirport, String destinationAirport, FlightRoute flightRoute, boolean makeReturnFlightRoute) throws DuplicateFlightRouteException, AirportNotFoundException;
    public List<FlightRoute> viewAllFlightRoute();
    public List<Airport> getAllAirport();
    public boolean deleteFlightRoute(String originAirport, String destinationAirport) throws NoExistingAirportException, NoFlightRouteFoundException, AirportNotFoundException;
    public long createNewFlightRouteDataInit(String originIATA, String destinationIATA, FlightRoute flightRoute, boolean makeReturnFlightRoute);
}
