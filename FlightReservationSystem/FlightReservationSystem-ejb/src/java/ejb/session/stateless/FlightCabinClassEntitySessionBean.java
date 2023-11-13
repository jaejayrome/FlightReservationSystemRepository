/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightCabinClass;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class FlightCabinClassEntitySessionBean implements FlightCabinClassEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    
    @Override
    public FlightCabinClass createFlightCabinClass(FlightCabinClass fcc) {
        em.persist(fcc);
        em.flush();
        return fcc;
    }
    
    @Override
    public List<FlightCabinClass> findFccForParticularFS(String flightNumber, Date uniqueDate) {
        return em.createQuery("SELECT fcc FROM FlightCabinClass fcc WHERE fcc.flightSchedule.departureTime = :date AND fcc.flightSchedule.flightSchedulePlan.flight.flightNumber = :flightNumber")
                .setParameter("date", uniqueDate)
                .setParameter("flightNumber", flightNumber)
                .getResultList();
    }
    
    @Override
    public FlightCabinClass getFCCByID(long id) {
        return em.find(FlightCabinClass.class, id);
    }

 
}
