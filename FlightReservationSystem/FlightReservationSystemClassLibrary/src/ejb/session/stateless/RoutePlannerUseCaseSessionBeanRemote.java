/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author jeromegoh
 */
@Remote
public interface RoutePlannerUseCaseSessionBeanRemote {
    public long createNewFlightRoute(Airport originAirport, Airport destinationAirport, FlightRoute flightRoute, boolean makeReturnFlightRoute);
    public List<FlightRoute> viewAllFlightRoute();
    public List<Airport> getAllAirport();
    public boolean deleteFlightRoute(String originAirport, String destinationAirport);
    public long createNewFlightRouteDataInit(String originIATA, String destinationIATA, FlightRoute flightRoute, boolean makeReturnFlightRoute);
}
