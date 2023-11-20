/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.Customer;
import entity.FlightReservation;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumerations.RoleType;

/**
 *
 * @author geraldtan
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {
    
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();
    
    @Override
    public Long createNewCustomer(String firstName, String lastName, String email, String phoneNumber, String address, String password) {
        Customer customer = new Customer(firstName, lastName, email, phoneNumber, address, password);
        Set<ConstraintViolation<Customer>> constraints = validator.validate(customer);
        if (constraints.size() == 0) {
            System.out.println("New Customer created: ");
            em.persist(customer);
            em.flush();
            return customer.getId();
        } else return -1L;
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
