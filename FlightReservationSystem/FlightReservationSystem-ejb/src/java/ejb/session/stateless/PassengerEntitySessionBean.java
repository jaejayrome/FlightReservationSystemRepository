/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightReservation;
import entity.Passenger;
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
public class PassengerEntitySessionBean implements PassengerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();
    
    @Override
    public Passenger persistPassenger(Passenger passenger) {
        Set<ConstraintViolation<Passenger>> constraints = validator.validate(passenger);
        if (constraints.size() == 0) {
            em.persist(passenger);
            em.flush();
            return passenger;
        } else return null;
    }
    
    @Override
    public Passenger findPassenger(long id) {
        return em.find(Passenger.class, id);
    }
    
}
