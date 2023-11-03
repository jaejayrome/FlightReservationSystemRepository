/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class FlightScheduleEntitySessionBean implements FlightScheduleEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public FlightSchedule createFlightSchedule(FlightSchedule flightSchedule) {
        em.persist(flightSchedule);
        em.flush();
        
        return flightSchedule;
    }
    
    @Override
    public List<FlightSchedule> viewFlightSchedulesByFlightNumber(String flightNumber) {
        return em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.flightSchedulePlan.flight.flightNumber = :flightNumber")
                .setParameter("flightNumber", flightNumber)
                .getResultList();
    }

    

}
