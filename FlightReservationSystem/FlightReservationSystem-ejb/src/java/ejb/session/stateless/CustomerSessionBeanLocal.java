/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightReservation;
import java.util.List;
import javax.ejb.Local;
import util.enumerations.RoleType;


/**
 *
 * @author geraldtan
 */
@Local
public interface CustomerSessionBeanLocal {
//    Long createNewCustomer(String firstName, String lastName, String email, 
//            String phoneNumber, String address, String password, RoleType roleType);
//    
    Long createNewCustomer(String firstName, String lastName, String email, 
            String phoneNumber, String address, String password);

}
