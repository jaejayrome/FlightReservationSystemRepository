/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.FlightReservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumerations.RoleType;

/**
 *
 * @author geraldtan
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    
    @Override
    public Long createNewCustomer(String firstName, String lastName, String email, String phoneNumber, String address, String password) {
        Customer customer = new Customer(firstName, lastName, email, phoneNumber, address, password, RoleType.CUSTOMER);
        
        System.out.println("New Customer created: ");
        em.persist(customer);
        em.flush();
        return customer.getId();
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createNewCustomer(String firstName, String lastName, String email, 
            String phoneNumber, String address, String password, RoleType rollType) {
        Customer customer = new Customer(firstName, lastName, email, phoneNumber, 
                address, password, RoleType.CUSTOMER);
        
        System.out.println("New Customer created: ");
        em.persist(customer);
        em.flush();
        return customer.getId();
    }

    @Override
    public List<FlightReservation> addFlightReservation() {
        return null;
    }
}
