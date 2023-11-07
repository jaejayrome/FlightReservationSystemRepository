/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import entity.Partner;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class PartnerUseCaseSessionBean implements PartnerUseCaseSessionBeanLocal {
    
    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBean;
    
    @Override
    public long partnerLogin(String username, String password) {
        try {
                Partner partner = partnerEntitySessionBean.authenticatePartner(username, password);
                System.out.println("Login Successful");
                long id = partner.getId();
                partnerEntitySessionBean.updateLogInStatus(true, id);
                return id;

            } catch (InvalidLoginCredentialsException exception) {
                System.out.println(exception.getMessage());
                return -1;
        }
    }
    
    @Override
    public void partnerLogout(long id) {
        partnerEntitySessionBean.updateLogInStatus(false, id);
        System.out.println("Logout Successful");
    }
    
//    @Override
//    public List<FlightSchedule> partnerSearchFlight() { 
//        
//    }
    
//    @Override
//    public FlightSchedule partnerReserveFlight() {
//        
//    }
    
   
}
