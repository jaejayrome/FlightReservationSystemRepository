/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightReservation;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class FlightScheduleEntitySessionBean implements FlightScheduleEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();
    
    @Override
    public FlightSchedule createFlightSchedule(FlightSchedule flightSchedule) {
        Set<ConstraintViolation<FlightSchedule>> constraints = validator.validate(flightSchedule);
        if (constraints.size() == 0) {
            em.persist(flightSchedule);
            em.flush();
            return flightSchedule;
        } return null;
        
    }
    
    @Override
    public List<FlightSchedule> viewFlightSchedulesByFlightNumber(String flightNumber) {
        return em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.flightSchedulePlan.flight.flightNumber = :flightNumber")
                .setParameter("flightNumber", flightNumber)
                .getResultList();
    }
    
    @Override
    public List<FlightSchedule> getFlightSchedulesByAirportOrigin(String iataCode) {
        return em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.flightSchedulePlan.flight.flightRoute.origin.iataAirportCode = :originAirport")
                .setParameter("originAirport", iataCode)
                .getResultList();
    }
    
    @Override
    public List<FlightSchedule> getFlightSchedulesByAirportDestination(String iataCode) {
        return em.createQuery("SELECT fs FROM FlightSchedule fs WHERE fs.flightSchedulePlan.flight.flightRoute.destination.iataAirportCode = :destinationAirport")
                .setParameter("destinationAirport", iataCode)
                .getResultList();
    }
    

    @Override
    public FlightSchedule getFlightScheduleById(long id) {
        return em.find(FlightSchedule.class, id);
    }
}
