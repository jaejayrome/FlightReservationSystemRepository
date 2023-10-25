/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
