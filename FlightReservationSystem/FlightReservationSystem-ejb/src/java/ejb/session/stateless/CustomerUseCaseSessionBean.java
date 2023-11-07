/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.FlightRoute;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author geraldtan
 */
@Stateless
public class CustomerUseCaseSessionBean implements CustomerUseCaseSessionBeanRemote, CustomerUseCaseSessionBeanLocal {
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;

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
    
    
    

    public void persist(Object object) {
        em.persist(object);
    }

    
    @Override
    public List<FlightRoute> searchForFlightRoutes(
        String departureAirport, 
        Date departureDate, 
        String arrivalAirport,
        Date returnDate, 
        boolean dfbl) {
        
        
        return null;
    }
    
    @Override
    //no need add return flights
    public List<FlightRoute> searchForFlightRoutes(
        String departureAirport, 
        Date departureDate, 
        String arrivalAirport,
        boolean dfbl) {
        
        String jpql = "SELECT fs, fsp FROM FlightSchedule fs JOIN fs.flightscheduleplan_id fsp";

        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);

        
        
        return em.createQuery("SELECT fs FROM FlightSchedule "
                + "WHERE fs.DEPARTURETIME.date := departureDate")
                .setParameter("deparetureDate", departureDate)
                .getResultList();
    }
}
