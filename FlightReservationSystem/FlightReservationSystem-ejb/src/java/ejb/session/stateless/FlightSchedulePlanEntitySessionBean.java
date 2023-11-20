/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightCabinClass;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Seat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumerations.FlightSchedulePlanStatus;

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
    public FlightSchedulePlan createFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        em.persist(flightSchedulePlan);
        em.flush();
        return flightSchedulePlan;
    }
    
    @Override
    public List<FlightSchedulePlan> viewAllFlightSchedulePlan() {
        // sort the list in accoradnce 
        List<FlightSchedulePlan> fspList = em.createQuery("SELECT fsp FROM FlightSchedulePlan fsp").getResultList();
        Comparator<FlightSchedulePlan> comparator = (x, y) -> {
            if (x.getFlightSchedulePlanGroup() - y.getFlightSchedulePlanGroup() != 0) {
                return x.getFlightSchedulePlanGroup() - y.getFlightSchedulePlanGroup() < 0 ? -1 : 1;
            } else {
                // if are not complementary of each other
                if (x.getFlight().getFlightNumber().compareTo(y.getFlight().getFlightNumber()) != 0) {
                    // compare by flight number
                    return x.getFlight().getFlightNumber().compareTo(y.getFlight().getFlightNumber());
                } else {
                    // if they are the same flight schedule plan group and flight number then we would sort according to their first departure time
                    return x.getFlightScheduleList().get(0).getDepartureTime().compareTo(y.getFlightScheduleList().get(0).getDepartureTime());
                }
            }
        };
        
        // sort the list 
        fspList.sort(comparator);
        // returns the sorted list
        return fspList;
    }
    
    @Override
    public FlightSchedulePlan getFSPfromID(long id) {
        return em.find(FlightSchedulePlan.class, id);
    }
    
    @Override
    public void disableFlightSchedulePlan(FlightSchedulePlan fsp) {
        fsp.setStatus(FlightSchedulePlanStatus.DISABLED);
    }
    
    @Override
    public void deleteFlightSchedulePlan(FlightSchedulePlan fsp) {
        // need check whether are there any flight bookings for any fs within the fsp
         fsp.getFlightScheduleList().size();
        List<FlightSchedule> fsList = fsp.getFlightScheduleList();
        
        // delete all flight schedules, fcc, seats and fares
       
        for (FlightSchedule fs : fsList) {
            fs.getFccList().size();
            List<FlightCabinClass> fccList = fs.getFccList();
            for (FlightCabinClass fcc: fccList) {
                fcc.getSeatList().size();
                List<Seat> seatList = fcc.getSeatList();
                for (Seat s : seatList) {
                    em.remove(s);
                }
                fcc.setSeatList(null);
                em.remove(fcc);
            }
            fs.setFccList(null);
            em.remove(fs);
        }
        
        fsp.setFlightScheduleList(null);
        em.remove(fsp);
    }
    
    
}
