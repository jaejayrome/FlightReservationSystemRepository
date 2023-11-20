/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import entity.FlightReservation;
import entity.Partner;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author jeromegoh
 */
@Stateless
public class PartnerEntitySessionBean implements PartnerEntitySessionBeanLocal {
    @PersistenceContext(unitName = "FlightReservationSystem-ejbPU")
    private EntityManager em;
    
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();
    
     
    @Override
    public long createNewPartner(String companyName, String loginUsername, String loginPassword) {
        Partner partner = new Partner(companyName, loginUsername, loginPassword);
        Set<ConstraintViolation<Partner>> errors = validator.validate(partner);
        if (errors.size() == 0) {
            em.persist(partner);
            em.flush();
            return partner.getId();
        } else return -1L;
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
    
    @Override
    public List<FlightReservation> getFlightReservations(long id) {
        this.findPartner(id).getReservations().size();
        this.findPartner(id).getReservations().stream().forEach(x -> em.detach(x));
        this.findPartner(id).getReservations().stream().forEach(x -> x.getPassengerList().stream().forEach(y -> em.detach(y)));
        this.findPartner(id).getReservations().stream().forEach(x -> x.getFlightBookingList().stream().forEach(z -> em.detach(z)));
        this.findPartner(id).getReservations().stream().forEach(x -> em.detach(x.getPartner()));
        return this.findPartner(id).getReservations();
    }
}
