/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author geraldtan
 */
@Remote
public interface CustomerSessionBeanRemote {
        public Long createNewCustomerRecord(Customer customer);

    public List<Customer> retrieveAllCustomer();
}
