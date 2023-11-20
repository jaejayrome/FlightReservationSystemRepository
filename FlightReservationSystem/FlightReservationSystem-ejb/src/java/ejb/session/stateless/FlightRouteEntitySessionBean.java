/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import util.util.Pair;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumerations.FlightRouteStatus;
import util.exception.AirportNotFoundException;
import util.exception.NoFlightRouteFoundException;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class FlightRouteEntitySessionBean implements FlightRouteEntitySessionBeanLocal {

    @EJB
    private AirportEntitySessionBeanLocal airportEntitySessionBean;

    public FlightRouteEntitySessionBean() {
    }

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();
    

    @Override
    public FlightRoute createFlightRoute(FlightRoute flightRoute) {
         Set<ConstraintViolation<FlightRoute>> errors = validator.validate(flightRoute);
        if (errors.size() == 0) {
            em.persist(flightRoute);
            em.flush();
            return flightRoute;
        } else return null;
    }
    
    // never check for exceptions yet
    @Override
    public FlightRoute getFlightRouteById(long id) {
        return em.find(FlightRoute.class, id);
    }
    
    @Override
    public List<FlightRoute> getAllFlightRoutes() {
        return em.createQuery("SELECT fr " +
               "FROM FlightRoute fr " +
               "ORDER BY fr.flightGroup, fr.origin.iataAirportCode").getResultList();
    }
    
    
    @Override
    public FlightRoute getFlightRouteByCityName(String originAirport, String destinationAirport) throws NoFlightRouteFoundException, AirportNotFoundException {
        try {
        long originId = airportEntitySessionBean.findAirport(originAirport).getId();
        long destinationId = airportEntitySessionBean.findAirport(destinationAirport).getId();
        
        return (FlightRoute)em.createQuery("SELECT flightRoute FROM FlightRoute flightRoute WHERE flightRoute.origin.id = :originId AND flightRoute.destination.id = :destinationId")
                             .setParameter("originId", originId)
                             .setParameter("destinationId", destinationId)
                             .getSingleResult();
        } catch (AirportNotFoundException exception) {
            throw new AirportNotFoundException("TRANSACTION ABORTED: AIRPORT NOT FOUND IN DATABASE");
        } catch (NoResultException exception) {
            throw new NoFlightRouteFoundException("No Flight Route has been detected");
        }
    }
    
    
    
    @Override
    public boolean disableFlightRoute(String originAirport, String destinationAirport) {
        // just label as disabled 
        try {
            FlightRoute flightRoute = this.getFlightRouteByCityName(originAirport, destinationAirport);
            // update the flightRoute
            flightRoute.setFlightRouteStatus(FlightRouteStatus.DISABLED);
        } catch (AirportNotFoundException exception) {
            
        } catch (NoFlightRouteFoundException exception) {
            // no flight has been found at all
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean deleteFlightRoute(String originAirport, String destinationAirport) {
        try {
            FlightRoute flightRoute = this.getFlightRouteByCityName(originAirport, destinationAirport);
            // dissociate the flightRoute with the airport
            flightRoute.setOrigin(null);
            flightRoute.setDestination(null);
            // delete the flightRoute from the database
            em.remove(flightRoute);
        } catch (AirportNotFoundException exception) {
            
        } catch (NoFlightRouteFoundException exception) {
            // no flight has been found at all
            return false;
        }
        
        return true;
    }
}
