/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightBooking;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class FlightBookingEntitySessionBean implements FlightBookingEntitySessionBeanLocal {
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public FlightBooking makeBooking(FlightBooking flightBooking) {
        em.persist(flightBooking);
        em.flush();
        return flightBooking;
    }

    @Override
    public FlightBooking findBooking(long id) {
        return em.find(FlightBooking.class, id);
    }
   
    
}
