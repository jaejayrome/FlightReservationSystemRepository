/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

}
