/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.Flight;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.enumerations.FlightRouteStatus;
import util.exception.NoFlightRouteFoundException;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class RoutePlannerUseCaseSessionBean implements RoutePlannerUseCaseSessionBeanRemote, RoutePlannerUseCaseSessionBeanLocal {

    @EJB
    private FlightEntitySessionBeanLocal flightEntitySessionBean;

    @EJB
    private AirportEntitySessionBeanLocal airportEntitySessionBean;

    @EJB
    private FlightRouteEntitySessionBeanLocal flightRouteEntitySessionBean;
    
    
    
    
    public RoutePlannerUseCaseSessionBean() {
    }
    
    @Override
    public long createNewFlightRoute(Airport originAirport, Airport destinationAirport, FlightRoute flightRoute, boolean makeReturnFlightRoute) {
        
        FlightRoute persistedFlightRoute = flightRouteEntitySessionBean.createFlightRoute(flightRoute);
        flightRoute.setOrigin(originAirport);
        flightRoute.setDestination(destinationAirport);
        flightRoute.setFlightGroup(flightRoute.getId());
        
        if (makeReturnFlightRoute) {
            FlightRoute returnFlightRoute = new FlightRoute(FlightRouteStatus.DISABLED);
            FlightRoute returnPersistedFlightRoute = flightRouteEntitySessionBean.createFlightRoute(returnFlightRoute);
            returnPersistedFlightRoute.setOrigin(destinationAirport);
            returnPersistedFlightRoute.setDestination(originAirport);
            returnPersistedFlightRoute.setFlightGroup(flightRoute.getId());
        }
        return persistedFlightRoute.getId();
    }
    
    @Override
    public long createNewFlightRouteDataInit(String originIATA, String destinationIATA, FlightRoute flightRoute, boolean makeReturnFlightRoute) {
        // check for duplicates
        FlightRoute persistedFlightRoute = flightRouteEntitySessionBean.createFlightRoute(flightRoute);
        
        Airport originAirport = airportEntitySessionBean.findAirport(originIATA);
        Airport destinationAirport = airportEntitySessionBean.findAirport(destinationIATA);
        persistedFlightRoute.setOrigin(originAirport);
        persistedFlightRoute.setDestination(destinationAirport);
        persistedFlightRoute.setFlightGroup(persistedFlightRoute.getId());
        
        if (makeReturnFlightRoute) {
            FlightRoute returnFlightRoute = new FlightRoute(FlightRouteStatus.DISABLED);
            FlightRoute returnPersistedFlightRoute = flightRouteEntitySessionBean.createFlightRoute(returnFlightRoute);
            returnPersistedFlightRoute.setOrigin(destinationAirport);
            returnPersistedFlightRoute.setDestination(originAirport);
            returnPersistedFlightRoute.setFlightGroup(persistedFlightRoute.getId());
        } else {
            // check whether is there any flight route for it  
            // airportEntitySessionBean
            try {
                FlightRoute complementaryFlightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(destinationIATA, originIATA);
                // if there is flight route that is being found 
                long flightRouteGroup = complementaryFlightRoute.getId();
                persistedFlightRoute.setFlightGroup(flightRouteGroup);
            } catch (NoFlightRouteFoundException e) {
                System.out.println(e.toString());
            }
        }
        
        return persistedFlightRoute.getId();
    }
    
    @Override
    public List<FlightRoute> viewAllFlightRoute() {
        return flightRouteEntitySessionBean.getAllFlightRoutes();
    }
    
    @Override
    public List<Airport> getAllAirport() {
        return airportEntitySessionBean.getAllAirports();
    }
    
    @Override
    public boolean deleteFlightRoute(String originAirport, String destinationAirport) {
        // checks whether is this flight route in use 
        List<Flight> flightsFound = flightEntitySessionBean.checkFlightRouteUsed(originAirport, destinationAirport);
        // if in use ,disable it 
        if (!flightsFound.isEmpty()) {
            return flightRouteEntitySessionBean.deleteFlightRoute(originAirport, destinationAirport);
        } else {
            return flightRouteEntitySessionBean.disableFlightRoute(originAirport, destinationAirport);
        }
        
    }
    
}
