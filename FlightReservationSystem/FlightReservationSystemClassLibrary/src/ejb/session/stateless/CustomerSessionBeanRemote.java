/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.FlightReservation;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CustomerAuthenticationFailedException;

/**
 *
 * @author geraldtan
 */
@Remote
public interface CustomerSessionBeanRemote {

    Long createNewCustomer(String firstName, String lastName, String email, 
            String phoneNumber, String address, String password);

    List<FlightReservation> addFlightReservation();
    
    Customer getCustomerById(long customerId);

}
