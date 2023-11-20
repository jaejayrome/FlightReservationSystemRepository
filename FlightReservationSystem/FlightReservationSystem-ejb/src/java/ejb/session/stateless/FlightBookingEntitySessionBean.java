/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.FlightBooking;
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
public class FlightBookingEntitySessionBean implements FlightBookingEntitySessionBeanLocal {
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();
    
    
    @Override
    public FlightBooking makeBooking(FlightBooking flightBooking) {
        Set<ConstraintViolation<FlightBooking>> constraints = validator.validate(flightBooking);
        if (constraints.size() == 0) {
            em.persist(flightBooking);
            em.flush();
            return flightBooking;
        } else return null;
    }

    @Override
    public FlightBooking findBooking(long id) {
        return em.find(FlightBooking.class, id);
    }
   
    
}
