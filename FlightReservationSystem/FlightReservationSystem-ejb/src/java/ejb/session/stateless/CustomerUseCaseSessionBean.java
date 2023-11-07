/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumerations.FlightSchedulePlanStatus;

/**
 *
 * @author geraldtan
 */
@Stateless
public class CustomerUseCaseSessionBean implements CustomerUseCaseSessionBeanRemote, CustomerUseCaseSessionBeanLocal {
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    
    public void persist(Object object) {
        em.persist(object);
    }
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
    @Override
    public int customerLogin(String email, String password) {
        List <Customer> customerEmailLs = em.createQuery(
                "SELECT cust FROM Customer cust "
                    + "WHERE cust.email = :email", 
                Customer.class)
                .setParameter("email", email)
                .getResultList();
        
        if (customerEmailLs.isEmpty()) {
            System.out.println("Customer account has not been created yet");      
            return -1;
        } else {
            //customer email exists in system 
            List <Customer> customer = em.createQuery(
                "SELECT cust FROM Customer cust "
                    + "WHERE cust.password = :password "
                    + "AND cust.email = :email", 
                Customer.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getResultList();
            
            if (customer.isEmpty()) {
                System.out.println("Invalid Password");
                return 0;
            } else if (!customer.isEmpty()) {
                System.out.println("Customer Found & Authenticated");
                return 1;
            }
        }
        
            
        
        return -1;
    }
    
    
    

    @Override
    //no need add return flights
    public List<List<FlightSchedule>> searchForFlightRoutes(
        String departureAirport, Date departureDate, String destinationAirport, Date returnDate, int directFlight) {
        
        List<List<FlightSchedule>> toReturn = new ArrayList<List<FlightSchedule>>();
        //if directFlight == 1, mean prefer directlight 
        //if directFlight == 2, means no pref
        
        
        if (directFlight == 1) { //one way, direct
            
            List<FlightSchedule> flightScheduleList = em.createQuery("Select fs FROM FlightSchedule "
                + "WHERE fs.DEPARTURETIME >= DATEADD(userDepartDate, -2 @1200"
                + "AND fs.DEPARTURETIME <= DATEADD(userDepartDate, -2 @1200")
            .setParameter("userDepartDate", departureDate)
            .getResultList();
            
            
            List<FlightSchedule> tmpToReturn = new ArrayList<FlightSchedule>();
            for (int i = 0; i < flightScheduleList.size(); i ++) {
                FlightSchedulePlan fsp = flightScheduleList.get(i).getFlightSchedulePlan();
                //check if fsp is disable/active
                if (fsp.getStatus() == FlightSchedulePlanStatus.ACTIVE) {
                    Flight flight = fsp.getFlight();
                    FlightRoute fr = flight.getFlightRoute();
                    
                    if (fr.getDestination().equals(destinationAirport) && fr.getOrigin().equals(departureAirport)) {
                        tmpToReturn.add(flightScheduleList.get(i));
                    } 
                }
            }
            
            if (returnDate != null) {
                flightScheduleList = em.createQuery("Select fs FROM FlightSchedule "
                + "WHERE fs.DEPARTURETIME >= DATEADD(userDepartDate, -2 @1200"
                + "AND fs.DEPARTURETIME <= DATEADD(userDepartDate, -2 @1200")
            .setParameter("userDepartDate", returnDate)
            .getResultList();
            }
            
            List<FlightSchedule> tmpToReturn2 = new ArrayList<FlightSchedule>();
            
            for (int i = 0; i < flightScheduleList.size(); i ++) {
                FlightSchedulePlan fsp = flightScheduleList.get(i).getFlightSchedulePlan();
                //check if fsp is disable/active
                if (fsp.getStatus() == FlightSchedulePlanStatus.ACTIVE) {
                    Flight flight = fsp.getFlight();
                    FlightRoute fr = flight.getFlightRoute();
                    
                    if (fr.getDestination().equals(departureAirport) && fr.getOrigin().equals(destinationAirport)) {
                        tmpToReturn.add(flightScheduleList.get(i));
                    } 
                }
            }
            
        } else if (directFlight == 2) { //1 way, no preference
            if (returnDate == null) {
                
            } else if (returnDate != null) {
                
            }
            
        } else {
            return null;
        }
        return toReturn;
    }
    
    
    
    
    
    
}
