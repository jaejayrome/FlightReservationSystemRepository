/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import javax.persistence.EntityManager;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class AirportEntitySessionBean implements AirportEntitySessionBeanLocal {
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public long createNewAirport(String airportName, String iataAirportCode, String city, String state, String country) {
        Airport airport = new Airport (airportName, iataAirportCode, city, state, country);
        em.persist(airport);
        return airport.getId();
    }


    
    
    
}
