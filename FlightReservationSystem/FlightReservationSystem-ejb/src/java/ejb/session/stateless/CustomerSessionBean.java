/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.FlightReservation;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import util.exception.CustomerAuthenticationFailedException;

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
        Customer customer = new Customer(firstName, lastName, email, phoneNumber, address, password);
        System.out.println("New Customer created: ");
         List<Customer> c = em.createQuery(
                "SELECT cust FROM Customer cust "
                    + "WHERE cust.email = :email", 
                Customer.class)
                .setParameter("email", email)
                .getResultList();
        if (c.isEmpty()) {
            // this means the customer doesn't exist at all
            em.persist(customer);
            em.flush();
            return customer.getId();        
        } else {
            return null;
        }
    }

    @Override
    public List<FlightReservation> addFlightReservation() {
        return null;
    }
    
    @Override
    public Customer getCustomerById(long customerId) {
        return em.find(Customer.class, customerId);
    }
    
    @Override
    public List<FlightReservation> returnAllFlightReservations(long customerId) {
        Customer customer = this.getCustomerById(customerId);
        customer.getFlightReservationList().size();
        return customer.getFlightReservationList();
    } 
}
