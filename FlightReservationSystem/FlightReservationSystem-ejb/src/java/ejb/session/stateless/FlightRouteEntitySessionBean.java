/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightRoute;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class FlightRouteEntitySessionBean implements FlightRouteEntitySessionBeanLocal {

    public FlightRouteEntitySessionBean() {
    }

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public long createFlightRoute(FlightRoute flightRoute) {
        em.persist(flightRoute);
        em.flush();
        // missing associatation with flight Route and flight
        return flightRoute.getId();
    }
    
    // never check for exceptions yet
    @Override
    public FlightRoute getFlightRouteById(long id) {
        return em.find(FlightRoute.class, id);
    }
    
    @Override
    public List<FlightRoute> getAllFlightRoutes() {
        return em.createQuery("SELECT flightroute FROM FlightRoute flightroute").getResultList();
    }
    
    public FlightRoute deleteFlightRoute(long id) {
        FlightRoute flightRoute = this.getFlightRouteById(id);
        // delete need to be careful about the associations
        return null;
    }
}
