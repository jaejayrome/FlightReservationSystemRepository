/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import entity.Seat;
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
public class SeatEntitySessionBean implements SeatEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();
    
    
   @Override
   public long createSeat(Seat seat) {
        Set<ConstraintViolation<Seat>> errors = validator.validate(seat);
        if (errors.size() == 0) {
            em.persist(seat);
            em.flush();
            return seat.getId();
        } else return -1L;
   }
   
   @Override
   public Seat findSeat(long seatId) {
       return em.find(Seat.class, seatId);
   }


}
