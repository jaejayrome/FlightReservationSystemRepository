/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumerations.RoleType;

/**
 *
 * @author geraldtan
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
//    @Override
//    public Long createNewCustomerRecord(Customer customer){
//        em.persist(customer);
//        em.flush();
//        
//        return customer.getId();
//    }
    
    public List<Customer> retrieveAllCustomer() {
        List<Customer> allCustomerList = new ArrayList<Customer>();
        Query query = em.createQuery("SELECT customer FROM Customer c");
        allCustomerList.addAll(query.getResultList());
        
        return allCustomerList;
    }

    @Override
    public Long createNewCustomerRecord(String firstName, String lastName, 
                String email, String phoneNumber, String address, String password, RoleType roletype) {
        
        Customer customer = new Customer (firstName, lastName, email, phoneNumber, address, password, roletype);
        em.persist(customer);
        em.flush();
        return customer.getId();
    }
}
