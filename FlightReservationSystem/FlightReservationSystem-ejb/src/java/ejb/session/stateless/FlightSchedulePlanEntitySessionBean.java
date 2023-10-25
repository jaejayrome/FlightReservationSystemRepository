/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightSchedulePlan;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class FlightSchedulePlanEntitySessionBean implements FlightSchedulePlanEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    public FlightSchedulePlanEntitySessionBean() {
    }
    
    // need to throw excpetiosn to catch the base case 
    // should also do the association before peristing into the database 
    @Override
    public long createFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        em.persist(flightSchedulePlan);
        em.flush();
        return flightSchedulePlan.getId();
    }
    
//    @Override
//    public FlightSchedulePlan viewFlightSchedulePlanDetails() {
//        
//    }
}
