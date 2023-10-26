/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class RoutePlannerUseCaseSessionBean implements RoutePlannerUseCaseSessionBeanRemote, RoutePlannerUseCaseSessionBeanLocal {

    @EJB
    private AirportEntitySessionBeanLocal airportEntitySessionBean;

    @EJB
    private FlightRouteEntitySessionBeanLocal flightRouteEntitySessionBean;
    
    
    public RoutePlannerUseCaseSessionBean() {
    }
    
    @Override
    public long createNewFlightRoute(FlightRoute flightRoute) {
        return flightRouteEntitySessionBean.createFlightRoute(flightRoute);
    }
    
    @Override
    public List<FlightRoute> viewAllFlightRoute() {
        return flightRouteEntitySessionBean.getAllFlightRoutes();
    }
    
    @Override
    public List<Airport> getAllAirport() {
        return airportEntitySessionBean.getAllAirports();
    }
    
//    @Override
//    public 
//    
    
}
