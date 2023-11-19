/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author jeromegoh
 */
@Local
public interface PartnerEntitySessionBeanLocal {
    public long createNewPartner(String companyName, String loginUsername, String loginPassword);
    public Partner authenticatePartner(String username, String password) throws InvalidLoginCredentialsException;
    public void updateLogInStatus(boolean newStatus, long id);
}
