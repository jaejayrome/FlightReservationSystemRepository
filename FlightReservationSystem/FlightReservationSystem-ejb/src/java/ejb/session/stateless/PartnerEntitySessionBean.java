/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class PartnerEntitySessionBean implements PartnerEntitySessionBeanLocal {
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
     
    @Override
    public long createNewPartner(String companyName, String loginUsername, String loginPassword) {
        Partner partner = new Partner(companyName, loginUsername, loginPassword);
        em.persist(partner);
        em.flush();
        return partner.getId();
    }
    
    @Override
    public Partner authenticatePartner(String username, String password) throws InvalidLoginCredentialsException {
        try {
            Partner partner = (Partner)em.createQuery("SELECT p FROM Partner p WHERE p.loginUsername = :username AND p.loginPassword = :password")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
            return partner;
        } catch (NoResultException exception) {
            throw new InvalidLoginCredentialsException("You have either entered an invalid Username or Password");
        }
    }
    
    @Override
    public void updateLogInStatus(boolean newStatus, long id) {
        Partner p = em.find(Partner.class, id);
        p.setIsLoggedIn(newStatus);
    }
    
    @Override
    public Partner findPartner(long id) {
        return em.find(Partner.class, id);
    }
}
