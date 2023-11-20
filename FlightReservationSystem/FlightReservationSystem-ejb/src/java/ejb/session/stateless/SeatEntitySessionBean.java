/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Seat;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class SeatEntitySessionBean implements SeatEntitySessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

    
   @Override
   public long createSeat(Seat seat) {
        em.persist(seat);
        em.flush();
        return seat.getId();
   }
   
   @Override
   public Seat findSeat(long seatId) {
       return em.find(Seat.class, seatId);
   }


}
