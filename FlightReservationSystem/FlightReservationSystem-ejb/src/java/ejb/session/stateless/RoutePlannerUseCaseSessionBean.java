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
import javax.persistence.NoResultException;
import util.enumerations.FlightRouteStatus;
import util.exception.AirportNotFoundException;
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
    public long createNewFlightRoute(String originAirport, String destinationAirport, FlightRoute flightRoute, boolean makeReturnFlightRoute) throws DuplicateFlightRouteException, AirportNotFoundException {
        // check for duplicates
        try {
            Airport originAirportInstance = airportEntitySessionBean.findAirport(originAirport);
            Airport destinationAirportInstance = airportEntitySessionBean.findAirport(destinationAirport);
                
            FlightRoute complementaryFlightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(originAirport, destinationAirport);
            if (complementaryFlightRoute != null) throw new DuplicateFlightRouteException("TRANSACTION ABORTED: DUPLICATE FLIGHT ROUTE FOUND");
        } catch (NoResultException e) {
            throw new AirportNotFoundException("TRANSACTION ABORTED: AIRPORT NOT FOUND IN DATABASE");
            
//            FlightRoute complementaryFlightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(originAirport.getIataAirportCode(), destinationAirport.getIataAirportCode());
//            throw new DuplicateFlightRouteException("Duplicated Flight Route, did not create a new route");
        } catch (NoFlightRouteFoundException e) {
            try {
                Airport originAirportInstance = airportEntitySessionBean.findAirport(originAirport);
                Airport destinationAirportInstance = airportEntitySessionBean.findAirport(destinationAirport);
                FlightRoute persistedFlightRoute = flightRouteEntitySessionBean.createFlightRoute(flightRoute);
            
            
                flightRoute.setOrigin(originAirportInstance);
                flightRoute.setDestination(destinationAirportInstance);
                flightRoute.setFlightGroup(flightRoute.getId());

                if (makeReturnFlightRoute) {
                    FlightRoute returnFlightRoute = new FlightRoute(FlightRouteStatus.DISABLED);
                    FlightRoute returnPersistedFlightRoute = flightRouteEntitySessionBean.createFlightRoute(returnFlightRoute);
                    returnPersistedFlightRoute.setOrigin(destinationAirportInstance);
                    returnPersistedFlightRoute.setDestination(originAirportInstance);
                    returnPersistedFlightRoute.setFlightGroup(flightRoute.getId());
                } else {
                    // check whether is there any flight route for it  
                    // airportEntitySessionBean
                    try {
                    FlightRoute complementaryFlightRoute = flightRouteEntitySessionBean.getFlightRouteByCityName(destinationAirport, originAirport);
                    // if there is flight route that is being found 
                    long flightRouteGroup = complementaryFlightRoute.getId();
                    persistedFlightRoute.setFlightGroup(flightRouteGroup);
                    } catch (NoFlightRouteFoundException ee) {
                        System.out.println(ee.toString());
                    }

                }
            return persistedFlightRoute.getId();
            } catch (NoResultException eee) {
                // if airport isnt found in database
                throw new AirportNotFoundException("TRANSACTION ABORTED: AIRPORT NOT FOUND IN DATABASE");
            }
        }
        return -1;

    }
    
    @Override
    public long createNewFlightRouteDataInit(String originIATA, String destinationIATA, FlightRoute flightRoute, boolean makeReturnFlightRoute) {
        // check for duplicates
        FlightRoute persistedFlightRoute = flightRouteEntitySessionBean.createFlightRoute(flightRoute);
        
        Airport originAirport = airportEntitySessionBean.findAirportForDataInit(originIATA);
        Airport destinationAirport = airportEntitySessionBean.findAirportForDataInit(destinationIATA);
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
    public boolean deleteFlightRoute(String originAirport, String destinationAirport) throws NoExistingAirportException, NoFlightRouteFoundException, AirportNotFoundException{
        // check whether is there a flight route in the first palce
        
        try {
            // checks whether is this flight route in use 
            List<Flight> flightsFound = flightEntitySessionBean.checkFlightRouteUsed(originAirport, destinationAirport);
            // if in use ,disable it 
            if (!flightsFound.isEmpty()) {
                try {
                    return flightRouteEntitySessionBean.disableFlightRoute(originAirport, destinationAirport);
                } catch (AirportNotFoundException e) {
                    throw new NoExistingAirportException();
                }
                
               
            } else {
                try {
                    return flightRouteEntitySessionBean.deleteFlightRoute(originAirport, destinationAirport);
                } catch (AirportNotFoundException e) {
                    throw new NoExistingAirportException();
                }
            }    
        } catch (NoExistingAirportException exception) {
            System.out.println("No airport found, route deletion is invalid");
            return false;
        } catch (NoFlightRouteFoundException e){
            System.out.println("Flight route not found, unable to delete flight route");
        }

//        } catch (AirportNotFound e) {
//            System.out.println("Airport not found, unable to delete flight route");
//
//        }
        

//        
//        try {
//            if (!flightsFound.isEmpty()) {
//                return flightRouteEntitySessionBean.disableFlightRoute(originAirport, destinationAirport);
//            } else {
//                return flightRouteEntitySessionBean.deleteFlightRoute(originAirport, destinationAirport);
//            }    
//        } catch (NoExistingAirportException exception) {
//            return false;
//        }      
        return false;
    }
}
