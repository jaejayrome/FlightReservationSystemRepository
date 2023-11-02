/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.Flight;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import util.enumerations.FlightStatus;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class FlightEntitySessionBean implements FlightEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public long createFlight(Flight flight) {
        em.persist(flight);
        em.flush();
        return flight.getId();
    }
    
    @Override
    public List<Flight> viewAllFlights() {
        return em.createQuery("SELECT flight FROM Flight flight").getResultList();
    }
    
    // remember to throw any exception if needed
    @Override
    public Flight getFlightById(long id) {
        return em.find(Flight.class, id);
    }
    
    // remember to throw any exception if needed
    @Override
    public long getIdByFlightNumber(String flightNumber) {
        Flight flight = (Flight)(em.createQuery("SELECT flight FROM Flight flight WHERE flight.flightNumber = :number").setParameter("number", flightNumber).getSingleResult());
        return flight.getId();
    }
    
    @Override
    public long updateFlightNumber(long id, String newFlightNumber) {
        Flight flight = this.getFlightById(id);
        flight.setFlightNumber(newFlightNumber);
        return flight.getId();
    }
    
    @Override
    public Flight checkReturnFlight(String originAirport, String destinationAirport) {
        try { 
        Flight flight = (Flight) em.createQuery("Select flight FROM Flight flight WHERE flight.flightRoute.origin.iataAirportCode = :destinationAirport AND flight.flightRoute.destination.iataAirportCode = :originAirport")
                             .setParameter("originAirport", originAirport)
                             .setParameter("destinationAirport", destinationAirport)
                             .getSingleResult();
        return flight;
        } catch (NoResultException exception) {
            return null;
            // throw new NoReturnFlightFoundException("No Complementary Return Flight has been found!");
        }
       
    }
    
    @Override
    public boolean deleteFlight(Flight flight) {
        if (flight == null) return false;
        // set it to an empty instance
        AircraftConfiguration aircraftConfiguration = new AircraftConfiguration();
        flight.setAircraftConfiguration(aircraftConfiguration);
        
        // set it to an empty flight route
        FlightRoute flightRoute = new FlightRoute();
        flight.setFlightRoute(flightRoute);
        
        em.remove(flight);
        return true;
        
    }
    
    @Override
    public boolean disableFlight(Flight flight) {
        if (flight == null) return false;
        flight.setStatus(FlightStatus.DISABLED);
        return true;
    }

}
