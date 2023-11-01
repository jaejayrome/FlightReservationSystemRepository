/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightRoute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import util.util.Pair;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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
    
    

    @Override
    public FlightRoute createFlightRoute(FlightRoute flightRoute) {
        em.persist(flightRoute);
        em.flush();
        // missing associatation with flight Route and flight
        return flightRoute;
    }
    
    // never check for exceptions yet
    @Override
    public FlightRoute getFlightRouteById(long id) {
        return em.find(FlightRoute.class, id);
    }
    
    @Override
    public List<Pair<FlightRoute>> getAllFlightRoutes() {
        
        List<FlightRoute> allFlightRoutes = new ArrayList<FlightRoute>();
        String sortedQuery = "LEFT JOIN FlightRoute fr2 " +
                        "ON fr1.destination = fr2.origin AND fr2.destination = fr1.origin " +
                        "ORDER BY CASE WHEN fr1.name < fr2.name THEN fr1.name ELSE fr2.name END ASC, " +
                        "         CASE WHEN fr1.name < fr2.name THEN fr2.name ELSE fr1.name END ASC";

        List<Object[]> results = em.createQuery(sortedQuery).getResultList();
    
        List<Pair<FlightRoute>> flightRoutePairs = new ArrayList<>();

        for (Object[] result : results) {
            FlightRoute fr1 = (FlightRoute) result[0];
            FlightRoute fr2 = (FlightRoute) result[1];

            // Create a Pair<FlightRoute> and add it to the list
            Pair<FlightRoute> flightRoutePair = new Pair<>(fr1, fr2);
            flightRoutePairs.add(flightRoutePair);
        }
        
        return flightRoutePairs;
    }
    
//   @Override
//public List<Pair<FlightRoute>> getAllFlightRoutes() {
//        String query = "SELECT fr1, fr2 FROM FlightRoute fr1 LEFT JOIN FlightRoute fr2 " +
//                   "ON (fr1.destination = fr2.origin AND fr2.destination = fr1.origin) " +
//                   "LEFT JOIN Airport a1 ON fr1.origin = a1.id " +
//                   "LEFT JOIN Airport a2 ON fr1.destination = a2.id " +
//                   "LEFT JOIN Airport a3 ON fr2.origin = a3.id " +
//                   "LEFT JOIN Airport a4 ON fr2.destination = a4.id";
//    List<Object[]> results = em.createQuery(query).getResultList();
//
//    List<Pair<FlightRoute>> flightRoutePairs = new ArrayList<>();
//
//    for (Object[] result : results) {
//        FlightRoute fr1 = (FlightRoute) result[0];
//        FlightRoute fr2 = (FlightRoute) result[1];
//
//        // Create a Pair<FlightRoute> and add it to the list
//        Pair<FlightRoute> flightRoutePair = new Pair<>(fr1, fr2);
//        flightRoutePairs.add(flightRoutePair);
//    }
//
//    // Sort the flightRoutePairs list based on your criteria
//    Collections.sort(flightRoutePairs, (pair1, pair2) -> {
//        String name1 = pair1.getFirst().getOrigin().getAirportName();
//        String name2 = pair2.getFirst().getOrigin().getAirportName();
//        return name1.compareTo(name2);
//    });
//
//    return flightRoutePairs;
// }

    
    @Override
    public FlightRoute getFlightRouteByCityName(String originAirport, String destinationAirport) throws NoFlightRouteFoundException{
        
        // functional programming way (fails)
        long originId = airportEntitySessionBean.findAirport(originAirport);
        long destinationId = airportEntitySessionBean.findAirport(destinationAirport);
//        Predicate<FlightRoute> pred = x -> x.getDestination().getId() == destinationId && x.getOrigin().getId() == originId;
//        return this.getAllFlightRoutes().stream().filter(pred).findFirst().get();
        
        // this way of normal jpql doesn't work also 
        try {
        return (FlightRoute)em.createQuery("SELECT flightRoute FROM FlightRoute flightRoute WHERE flightRoute.origin.id = :originId AND flightRoute.destination.id = :destinationId")
                             .setParameter("originId", originId)
                             .setParameter("destinationId", destinationId)
                             .getSingleResult();
        } catch (NoResultException exception) {
            throw new NoFlightRouteFoundException("No Flight Route has been detected");
        }
    }
    
    public FlightRoute deleteFlightRoute(long id) {
        FlightRoute flightRoute = this.getFlightRouteById(id);
        // delete need to be careful about the associations
        return null;
    }
}
