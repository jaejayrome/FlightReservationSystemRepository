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
import util.exception.DuplicateFlightRouteException;
import util.exception.NoExistingAirportException;
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
    public long createNewFlightRoute(Airport originAirport, Airport destinationAirport, FlightRoute flightRoute, boolean makeReturnFlightRoute) throws DuplicateFlightRouteException {
        // check for duplicates
        try {
            FlightRoute complementaryFlightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(originAirport.getIataAirportCode(), destinationAirport.getIataAirportCode());
            throw new DuplicateFlightRouteException("Duplicated Flight Route, did not create a new route");
        } catch (NoFlightRouteFoundException e) {
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
            } else {
                // check whether is there any flight route for it  
                // airportEntitySessionBean
                try {
                    FlightRoute complementaryFlightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(destinationAirport.getIataAirportCode(), originAirport.getIataAirportCode());
                // if there is flight route that is being found 
                    long flightRouteGroup = complementaryFlightRoute.getId();
                    persistedFlightRoute.setFlightGroup(flightRouteGroup);
                } catch (NoFlightRouteFoundException ee) {
                    System.out.println(ee.toString());
                } catch (NoExistingAirportException exception) {
                    System.out.println("Airport does not exits");
                }
            }
            return persistedFlightRoute.getId();
        } catch (NoExistingAirportException exception) {
                System.out.println("Airport does not exits");
                //dummy return method to get compiler to stop yelling @ me
                long tmp = 1;
                return tmp;
        }
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
            } catch (NoExistingAirportException exception) {
                System.out.println("Airport does not exits");

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
    public boolean deleteFlightRoute(String originAirport, String destinationAirport) throws NoExistingAirportException {
        // checks whether is this flight route in use 
        List<Flight> flightsFound = flightEntitySessionBean.checkFlightRouteUsed(originAirport, destinationAirport);
        // if in use ,disable it 
        
        try {
            if (!flightsFound.isEmpty()) {
                return flightRouteEntitySessionBean.disableFlightRoute(originAirport, destinationAirport);
            } else {
                return flightRouteEntitySessionBean.deleteFlightRoute(originAirport, destinationAirport);
            }    
        } catch (NoExistingAirportException exception) {
            return false;
        }      
    }
}
