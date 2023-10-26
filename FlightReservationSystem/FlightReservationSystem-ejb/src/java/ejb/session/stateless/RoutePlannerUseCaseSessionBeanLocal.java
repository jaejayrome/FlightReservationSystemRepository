/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.Flight;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jeromegoh
 */
@Local
public interface RoutePlannerUseCaseSessionBeanLocal {
    public long createNewFlightRoute(FlightRoute flightRoute);
    public List<FlightRoute> viewAllFlightRoute();
    public List<Airport> getAllAirport();
}
